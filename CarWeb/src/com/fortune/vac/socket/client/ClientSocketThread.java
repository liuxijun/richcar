package com.fortune.vac.socket.client;

import com.fortune.rms.business.log.logic.logicInterface.VacLogLogicInterface;
import com.fortune.rms.business.log.model.VacLog;
import com.fortune.util.*;
import com.fortune.vac.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-6-2
 * Time: 下午3:19
 * 一个独立的线程，用以维护和处理发送数据队列
 */
@SuppressWarnings("unused")
public class ClientSocketThread extends Thread{
    private Socket sockRequest = null;//new java.net.Socket( MiddleServerAddress, MiddleServerPort );
    //private BufferedReader sockIn = null;
    private InputStream socketInStream;
    private PrintStream sockOut = null;
    private Logger logger = Logger.getLogger(getClass());
    private boolean connected=false;
    private String vacHost;
    private boolean shouldStop = false;
    private int vacPort;
    private int handsetInterval; //握手信号时间间隔
    private int timeoutSeconds;//超时时间
    private int retryTimes;       //超时重试次数
    private long sequenceId = 0;
    final byte[] lock = new byte[0];
    final byte[] readLock = new byte[0];
    final byte[] sendQueueLock = new byte[0];
    private int checkPriceTimes = 0;
    private Date startTime = new Date();
    private long duration = 0;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getCheckPriceTimes() {
        return checkPriceTimes;
    }

    public void setCheckPriceTimes(int checkPriceTimes) {
        this.checkPriceTimes = checkPriceTimes;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public ClientSocketThread(){
        AppConfigurator config = AppConfigurator.getInstance();
        vacHost = config.getConfig("vac.hostIp","10.17.170.200");
        vacPort = config.getIntConfig("vac.hostPort",9999);
        handsetInterval = config.getIntConfig("vac.handsetInterval",60);
        retryTimes = config.getIntConfig("vac.retryTimes",5);
        timeoutSeconds = config.getIntConfig("vac.timeoutSeconds",30);
        //open(vacHost,vacPort);
    }

    private Queue<Pdu> sendList = new LinkedList<Pdu>();
    private Map<Long,byte[]> responseList = new HashMap<Long,byte[]>();
    public void run(){
        int duration = 0;
        long lastCheckTime = System.currentTimeMillis();
        //startup();
        //读取数据线程和发送数据线程分开，否则读取也是一个阻塞的进程
        Thread readThread = new Thread() {
            @Override
            public void run() {
                byte[] responseHeader = new byte[12];
                while(!shouldStop){
                    try {
                        if(connected){
                            logger.debug("准备读取信息头部数据...");
                            int headerReadLength = read(responseHeader);
                            logger.debug("头信息读取完毕！");
                            if(headerReadLength<0){
                                logger.error("数据读取错误，读取的数据长度小于0，长度返回值:"+headerReadLength);
                            }else if(headerReadLength==0){
                                logger.error("数据读取错误，读取的数据长度等于0，长度返回值:"+headerReadLength);
                            }else if(headerReadLength!=12){
                                logger.error("垃圾数据出现，长度不是12："+MD5Utils.bufferToHex(responseHeader));
                            }else {
                                MessageHeader header = new MessageHeader(responseHeader);
                                try {
                                    //如果是心跳，就放弃
                                    if(header.getCommandId()!=Command.CMDID_HandsetResp){
                                        int willReadLength = header.getTotalLength()-12;
                                        if(willReadLength>0){
                                            byte[] responseBody = new byte[header.getTotalLength()];
                                            Arrays.fill(responseBody,(byte)0);
                                            int dataReadLength = socketInStream.read(responseBody,12,willReadLength);
                                            if(dataReadLength!=willReadLength){
                                                logger.error("读取数据的长度与应读取的数据长度不一致：应该是"+willReadLength+",实际上"+dataReadLength);
                                            }
                                            System.arraycopy(responseHeader,0,responseBody,0,12);
                                            logger.debug("收到的消息，总长" +header.getTotalLength()+
                                                    "字节，内容："+ MD5Utils.bufferToHex(responseBody));
                                            synchronized (readLock){
                                                responseList.put(header.getSequenceId(),responseBody);
                                            }
                                        }else{
                                            logger.debug("该命令除了头信息，没有返回数据："+Command.commandNames.get(header.getCommandId()));
                                        }
                                    }
                                } catch (IOException e) {
                                    logger.error("读取数据时发生异常："+e.getMessage());
                                    close();
                                    break;
                                }
                            }
                        }else{
                            sleep(100);
                        }
                    } catch (Exception e) {
                        logger.error("读取数据时发生异常："+e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        };
        //启动接收数据线程
        readThread.start();
        //在主线程内进行数据的发送
        while(!shouldStop){
            //队列非空，则进行数据发送
           while(!sendList.isEmpty()){
               Pdu pdu;
               synchronized (sendQueueLock){
                   pdu = sendList.poll();
               }
               if(pdu!=null){
                   byte[] sendArray = pdu.buildBuffer();
                   logger.debug("准备发送数据："+pdu.toString());
                   int sendResult = sendToServer(sendArray);
                   long sn = pdu.getMessageHeader().getSequenceId();
                   //等于0是成功，-1是失败
                   if(sendResult <0){
                       //发送失败
                       logger.error("发送数据时发生异常，无法继续进行：\n"+pdu);
                       synchronized (readLock){
                           responseList.put(sn,null);
                       }
                       close();
                   }else{

                   }
               }else{
                   logger.warn("从发送队列里拿出来的数据是空的！");
               }
               duration = 0;
               lastCheckTime = System.currentTimeMillis();
           }
           try {
               if(bindSuccess){
                   duration++;
                   if(duration>(handsetInterval-5)*10){
                       synchronized (sendQueueLock){
                           logger.debug("队列持续空时间过长，发送心跳信息。");
                           sendList.offer(new Pdu(new HandsetMessage()));
                       }
                       duration = 0;
                   }
               }
               long now = System.currentTimeMillis();
               if((now-lastCheckTime)>30000){//超过三十秒就提示一下
                   logger.debug("队列持续空了30秒....");
                   lastCheckTime = now;
               }
               sleep(100);
           } catch (InterruptedException e) {
               e.printStackTrace();
               logger.warn("被唤醒了！");
           }
       }
    }
    public boolean startup(){
        return open(vacHost,vacPort);
    }

    public boolean shutdown(){
        UnbindMessage message = new UnbindMessage();
        sendAndWait(message);
        shouldStop = true;
        close();
        return true;
    }

    public int read(byte[] dataBuffer){
        try {
            if(socketInStream!=null&&dataBuffer!=null){
                int result = socketInStream.read(dataBuffer);
                if(result<0){
                    connected = false;
                    bindSuccess = false;
                }
                return result;
            }
        } catch (IOException e) {
            connected = false;
            bindSuccess =false;
            close();
            e.printStackTrace();
        }
        return -1;
    }

    public int write(byte[] dataBuffer){
        try {
            sockOut.write(dataBuffer);
        } catch (IOException e) {
            connected = false;
            bindSuccess =false;
            logger.error("无法写数据："+e.getMessage());
            e.printStackTrace();
            return -1;
        } catch (Exception e) {
            connected = false;
            bindSuccess =false;
            logger.error("无法写数据："+e.getMessage());
            e.printStackTrace();
            return -1;
        }
        return 0;
    }
    public void init(Socket socket){
        try {
            socketInStream = socket.getInputStream();
            //sockIn = new BufferedReader(new InputStreamReader(socketInStream));
            sockOut = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            connected = false;
            bindSuccess =false;
            logger.error("无法初始化Socket!"+e.getMessage());
            e.printStackTrace();
        }
    }
    public boolean open(String hostIp,int hostPort) {
        String keyName = "vacOpenSocket";
        try {
            ThreadUtils.getInstance().acquire(keyName);
            if(!connected){
                try {
                    sockRequest = new Socket(hostIp,hostPort);
                    //sockRequest.setSoTimeout(1000);
                    init(sockRequest);
                    connected = true;
                    //初始化输入输出设备
                } catch (Exception e) {
                    logger.error("无法初始化"+hostIp+":"+7+","+e.getMessage());
                    connected = false;
                    e.printStackTrace();
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ThreadUtils.getInstance().release(keyName);
        }
        return true;
    }

    public boolean close() {
        try {
            //关闭连接，原来的输入输出会自动关闭
            logger.debug("关闭连接。。。。");
            connected = false;
            bindSuccess = false;
            sockRequest.close();
            return true;
        } catch (Exception e) {
            logger.error("无法关闭Socket："+e.getMessage());
            return false;
        }
    }
    public  int sendToServer(byte[] message){
        if(!connected){
            logger.warn("尚未连接到VAC认证平台！尝试进行连接操作：");
            open(vacHost,vacPort);
        }
        if(!connected){
            logger.error("连接到VAC认证平台失败！不能发送数据！");
        }

/*
        if(!bindSuccess){
            logger.error("无法连接到VAC认证中心");
            return new byte[0];
        }
*/
        logger.debug("即将发送的byteArray:"+ MD5Utils.bufferToHex(message));
        return write(message);
    }

    public void displayErrorInfo(int errorCode){
        String errorMsg = ErrorCode.getErrorMessage(errorCode);
        logger.error("发生异常，错误代码：" + errorCode + " , 错误信息：" + errorMsg);

    }
    private boolean bindSuccess = false;
    public byte[] waitResponse(Long sn){
        int timeout = timeoutSeconds*100;
        while(timeout>0){
            timeout--;
            if(responseList.containsKey(sn)){
                byte[] result;
                synchronized (readLock){
                     result = responseList.remove(sn);
                }
                return result;
            }
            try {
                sleep(10);
            } catch (InterruptedException e) {
                logger.warn("唤醒了：" + e.getMessage());
                break;
            }
        }
        return null;
    }
    public boolean bind(){
        logger.debug("准备执行bind操作");
        String keyName = "vacBindKey";
        ThreadUtils tu = ThreadUtils.getInstance();
        try {
            bindSuccess =false;
            tu.acquire(keyName);
            if(!bindSuccess){
                if(!connected){
                    logger.warn("尚未连接！初始化连接"+vacHost+":"+vacPort);
                    open(vacHost,vacPort);
                }
                if(!connected){
                    logger.error("连接初始化失败，直接返回！");
                    return false;
                }else{
                    logger.debug("连接成功，准备后续操作："+vacHost+":"+vacPort);
                }
                sequenceId = 0;
                BindMessage message = new BindMessage();
                byte[] buffer = sendAndWait(message);
                if(buffer==null||buffer.length<=0){
                    logger.error("bind过程中无法获取结果数据！");
                    return false;
                }else{
                    logger.debug("bind过程中，获取的结果数据："+MD5Utils.bufferToHex(buffer));
                }
                BindMessageResp resp = new BindMessageResp(buffer);
                logger.debug(resp.toString());
                int resultCode = (int) resp.getResultCode();
                if(resultCode==0){
                    bindSuccess=true;
                    logger.debug("bind操作成功，可以进行后续操作");
                    return true;
                }else{
                    String errorMsg;
                    bindSuccess = false;
                    close();
                    switch(resultCode){
/*
               case 0:
                   errorMsg = "成功";
                   break;
*/
                        case 1:
                            errorMsg = "帐户错误";
                            break;
                        case 2:
                            errorMsg = "密码错误";
                            break;
                        case 3:
                            errorMsg = "原始端设备类型非法";
                            break;
                        case 4:
                            errorMsg = "原始端设备ID号非法";
                            break;
                        case 5:
                            errorMsg = "目标端设备类型非法";
                            break;
                        case 6:
                            errorMsg = "目标设备ID 非法";
                            break;
                        case 7:
                            errorMsg = "重复的连接请求";
                            break;
                        default:
                            errorMsg = "未知的错误信息："+resultCode;
                            break;

                    }
                    logger.error("bind消息时发生异常："+errorMsg);
                }
                return false;
            }else{
                logger.debug("等待期间，其他请求已经按成了bind操作！继续进行后续操作！");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tu.release(keyName);
        }
        return false;
    }

    public boolean unBind(){
        logger.debug("unBind");
        UnbindMessage message = new UnbindMessage();
        byte[] buffer = sendAndWait(message);
        //bind和unbind返回消息是一致的结构
        BindMessageResp resp = new BindMessageResp(buffer);
        logger.debug(resp.toString());
        long resultCode = resp.getResultCode();
        if(resultCode==0){
            bindSuccess = false;
            connected = false;
            close();
            return true;
        }else{
            String errorMsg = "没有连接";
            if(resultCode!=8){
                errorMsg = "未知的错误:"+resultCode;
            }
            logger.error("unbind移除链接时发生异常："+errorMsg);
        }
        return false;
    }
    public byte[] sendAndWait(MessageBody messageBody){
        return sendAndWait(messageBody, true);
    }
    public byte[] sendAndWait(MessageBody messageBody,boolean shouldWait){
        Pdu pdu = new Pdu();
        int tryTimes = 0;
        pdu.setMessageBody(messageBody);
        while(tryTimes<retryTimes){
            synchronized (lock){
                pdu.getMessageHeader().setSequenceId(sequenceId);
                sequenceId++;
            }
            Long sn = pdu.getMessageHeader().getSequenceId();
            if(messageBody instanceof CheckPriceMessage){
                CheckPriceMessage msg = (CheckPriceMessage) messageBody;
                msg.setSequenceNumber(generateSerialNumber(sn));
            }
            synchronized (sendQueueLock){
                sendList.offer(pdu);
            }
            tryTimes++;
            if(shouldWait){
                byte[] result = waitResponse(sn);
                if(result==null||result.length==0){
                    if(pdu.getMessageHeader().getCommandId()==Command.CMDID_CheckPrice){
                        CheckPriceMessage message = (CheckPriceMessage) pdu.getMessageBody();
                        if(Command.OPERATE_TYPE_SUBSCRIBE.equals(message.getOperationType())){
                            logger.error("用户是购买请求，失败后不重复发送:"+pdu);
                            return null;
                        }
                    }
                    logger.error("发送后等待VAC返回数据这个过程发生异常，有可能是发送失败！当前队列长度：" +
                            sendList.size()+","+
                            "再来一次，这是第" +tryTimes+"次，要发送的数据是：\n"+pdu);
                }else{
                    return result;
                }
            }else{
                return null;
            }
        }
        return null;
    }

    /**
     * 心跳信息发送
     * @param shouldWait 发送好数据后，是否等待结果。如果不等待，直接返回
     * @return 发送后数据结果
     */
    public boolean handset(boolean shouldWait){
        if(bindSuccess){
           // logger.debug("准备发送handset");
            HandsetMessage message = new HandsetMessage();
            byte[] result = sendAndWait(message,shouldWait);
            return result!=null&&result.length==12;
        }else{
            logger.error("尚未bind，无法发送握手信号：handset！");
        }
        return false;
    }

    public int checkPriceConfirm(String phoneNumber,String productId){
        logger.debug("checkPriceConfirm");
        boolean result;
//        AppConfigurator config = AppConfigurator.getInstance();
        CheckPriceConfirmMessage messageBody = new CheckPriceConfirmMessage();
        messageBody.setDestinationDeviceID(phoneNumber);
        messageBody.setSourceDeviceID(productId);
        byte[] buffer = sendAndWait(messageBody);
        CheckPriceResp checkPriceResp = new CheckPriceResp(buffer);
        int errorCode = (int)checkPriceResp.getResultCode();
        result = errorCode == 0;
        if(!result){
            displayErrorInfo(errorCode);
        }
        return errorCode;
    }

    public String generateSerialNumber(long sn){
        String sequenceIdStr = ""+sn%10000;
        while(sequenceIdStr.length()<4){
            sequenceIdStr = "0"+sequenceIdStr;
        }
        AppConfigurator config = AppConfigurator.getInstance();
        String proviceCode = config.getConfig("vac.proviceCode","311");
        String deviceCode = config.getConfig("vac.deviceCode","1");
        String deviceType = config.getConfig("vac.deviceType","22");
        sequenceIdStr = StringUtils.date2string(new Date(), "MMddHHmmss")+deviceType+proviceCode+deviceCode+sequenceIdStr;
        return sequenceIdStr;
    }
    public CheckPriceMessage getBaseCheckPriceMessage(String spId,String phoneNumber,String productId,long operationType){
        CheckPriceMessage messageBody = new CheckPriceMessage();
        messageBody.setProductId("        ");//CRM产品ID, ServiceIDType为5时有效，其他填8个空格
        messageBody.setSpId(spId);//企业代码
        messageBody.setOA(phoneNumber); //发起方:如8613312345678
        messageBody.setOAType(1L);   //业务发起端地址类型，参考以前VAC的测试手册，填写了1
        messageBody.setOANetworkID("WCDMA"); //业务发起端用户归属网络标识,参考以前VAC的测试手册WCDMA
        messageBody.setDA(phoneNumber);
        messageBody.setDAType(1L);   //目标地址类型,参考以前VAC的测试手册，填写了1
        messageBody.setDANetworkID("WCDMA");//目标端用户归属网络标识,参考以前VAC的测试手册，填写了WCDMA
        messageBody.setFA(phoneNumber);
        messageBody.setFAType(1L);   //目标地址类型,参考以前VAC的测试手册，填写了1
        messageBody.setFANetworkID("WCDMA");//目标端用户归属网络标识,参考以前VAC的测试手册，填写了WCDMA
        messageBody.setOperationType(operationType);  //订购  参考以前VAC测试手册，填写了1
        messageBody.setServiceUpDownType(9L);//业务上下行类型，参考以前VA测试手册，填写了1,后修改成9
        messageBody.setServiceIDType(3L);  //参考以前的VAC手册填写的3
        messageBody.setServiceType("80"); //原来VAC手册上写的是93，沟通后觉得可能是80
        messageBody.setServiceId(productId);// ServiceIDType为1时填写ServiceID，为3时填写SP_ProductID，为5时填写SPEC_ProductID(业务部件能知晓用户定购或点播CRM的产品构成)
        return messageBody;
    }
    public int  checkPrice(String phoneNumber,String productId,long operationType,String spId){
        logger.debug("调用checkPrice开始！");
        if(!bindSuccess){
            logger.error("在做checkPrice时发现尚未bind，尝试bind操作！");
            bind();
        }
        if(!bindSuccess){
            logger.error("在做checkPrice时bind失败，无法进行后续操作！");
            return ErrorCode.ERROR_NOT_BINDED;
        }
        boolean result=false;
        int errorCode=ErrorCode.ERROR_SEND_ERROR;
//        String spId = config.getConfig("vac.spId","0101");
        CheckPriceMessage checkPriceMessage = getBaseCheckPriceMessage(spId, phoneNumber, productId, operationType);

        //保存VAC请求与反馈信息日志
        VacLog vacLog = new VacLog();
        try {
            VacLogLogicInterface vacLogLogicInterface = (VacLogLogicInterface)SpringUtils.getBean("vacLogLogicInterface");
            vacLog.setUserId(phoneNumber);
            vacLog.setProductId(productId);
            Date startTime = new Date();
            vacLog.setCreateTime(startTime);
            vacLog.setOperationType(operationType);
            vacLog.setSpId(spId);
            vacLog.setResultCode(-9999);
            vacLog.setCheckPriceResp("数据尝试发送，等待结果！");
            //vacLog = vacLogLogicInterface.save(vacLog);
            byte[] buffer = sendAndWait(checkPriceMessage);
            vacLog.setCheckPriceMessage(checkPriceMessage.toString());
            if(buffer!=null&&buffer.length>0){
                MessageHeader header = new MessageHeader(buffer);
                CheckPriceResp checkPriceResp = new CheckPriceResp(buffer);
                errorCode = (int)checkPriceResp.getResultCode();
                logger.debug("checkPrice调用后的数据返回：\r\n"+header.toString()+"\r\n"+checkPriceResp);
                result = errorCode == 0;
                if(!result){
                    displayErrorInfo(errorCode);
                }
                vacLog.setCheckPriceResp(checkPriceResp.toString());
            }else{
                vacLog.setCheckPriceResp("返回数据异常，无法继续操作！");
            }
            vacLog.setResultCode(errorCode);
            Date stopTime = new Date();
            vacLog.setFinishTime(stopTime);
            vacLog.setDuration((int)(stopTime.getTime()-startTime.getTime()));
            vacLogLogicInterface.save(vacLog);
            if(result&&Command.OPERATE_TYPE_VOD.equals(checkPriceMessage.getOperationType())&&
                    AppConfigurator.getInstance().getBoolConfig("vac.checkPriceForVodNeedConfirm",false)){
                //如果是按次，如果需要计费确认
                String sequenceNumber = checkPriceMessage.getSequenceNumber();
                CheckPriceConfirmMessage checkPriceConfirmMessage = new CheckPriceConfirmMessage();
                checkPriceConfirmMessage.setSequenceNumber(sequenceNumber);
                checkPriceConfirmMessage.setErrCode(0L);
                checkPriceConfirmMessage.setServiceType("80");
                checkPriceConfirmMessage.setEndTime(StringUtils.date2string(new Date(new Date().getTime() + 24 * 3600 * 1000L), "yyyyMMddHHmmss"));
                logger.debug("按次点播需要进行确认，准备发送确认消息："+checkPriceConfirmMessage.toString());
                buffer= sendAndWait(checkPriceConfirmMessage);
                vacLog.setId(-1);
                vacLog.setResultCode(-9999);
                vacLog.setCheckPriceMessage(checkPriceConfirmMessage.toString());
                if(buffer!=null&&buffer.length>0){
                    MessageHeader header = new MessageHeader(buffer);
                    CheckPriceConfirmResp resp = new CheckPriceConfirmResp(buffer);
                    vacLog.setResultCode((int)resp.getResultCode());
                    vacLog.setCheckPriceResp(resp.toString());
                    logger.debug("按次点播需要进行确认，发送确认消息后返回：" + checkPriceConfirmMessage.toString());
                }else{
                    logger.error("按次发送确认消息时失败："+checkPriceConfirmMessage.toString());
                }
                vacLogLogicInterface.save(vacLog);
            }
        } catch (Exception e) {
            logger.debug("发生异常，无法保存VAC日志："+e.getMessage());
        }
        return errorCode;
    }

    public boolean isBindSuccess() {
        return bindSuccess;
    }

/*
    public boolean buy(String phoneNumber,String productId,Long operationType,long cspId) {
        int result = checkPrice(phoneNumber,productId,operationType,cspId);
        return result==0;
    }

    public boolean checkBuy(String phoneNumber,List<String> productIds,Long operationType,long cspId) {
        if(productIds != null && productIds.size() > 0)  {
            for (String productId : productIds) {
                int result = checkPrice(phoneNumber,productId,operationType,cspId);
                if(result == 0) {
                    return true;
                }
            }
        }
        return false;
    }


*/
}
