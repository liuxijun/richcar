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
 * Time: ����3:19
 * һ���������̣߳�����ά���ʹ��������ݶ���
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
    private int handsetInterval; //�����ź�ʱ����
    private int timeoutSeconds;//��ʱʱ��
    private int retryTimes;       //��ʱ���Դ���
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
        //��ȡ�����̺߳ͷ��������̷ֿ߳��������ȡҲ��һ�������Ľ���
        Thread readThread = new Thread() {
            @Override
            public void run() {
                byte[] responseHeader = new byte[12];
                while(!shouldStop){
                    try {
                        if(connected){
                            logger.debug("׼����ȡ��Ϣͷ������...");
                            int headerReadLength = read(responseHeader);
                            logger.debug("ͷ��Ϣ��ȡ��ϣ�");
                            if(headerReadLength<0){
                                logger.error("���ݶ�ȡ���󣬶�ȡ�����ݳ���С��0�����ȷ���ֵ:"+headerReadLength);
                            }else if(headerReadLength==0){
                                logger.error("���ݶ�ȡ���󣬶�ȡ�����ݳ��ȵ���0�����ȷ���ֵ:"+headerReadLength);
                            }else if(headerReadLength!=12){
                                logger.error("�������ݳ��֣����Ȳ���12��"+MD5Utils.bufferToHex(responseHeader));
                            }else {
                                MessageHeader header = new MessageHeader(responseHeader);
                                try {
                                    //������������ͷ���
                                    if(header.getCommandId()!=Command.CMDID_HandsetResp){
                                        int willReadLength = header.getTotalLength()-12;
                                        if(willReadLength>0){
                                            byte[] responseBody = new byte[header.getTotalLength()];
                                            Arrays.fill(responseBody,(byte)0);
                                            int dataReadLength = socketInStream.read(responseBody,12,willReadLength);
                                            if(dataReadLength!=willReadLength){
                                                logger.error("��ȡ���ݵĳ�����Ӧ��ȡ�����ݳ��Ȳ�һ�£�Ӧ����"+willReadLength+",ʵ����"+dataReadLength);
                                            }
                                            System.arraycopy(responseHeader,0,responseBody,0,12);
                                            logger.debug("�յ�����Ϣ���ܳ�" +header.getTotalLength()+
                                                    "�ֽڣ����ݣ�"+ MD5Utils.bufferToHex(responseBody));
                                            synchronized (readLock){
                                                responseList.put(header.getSequenceId(),responseBody);
                                            }
                                        }else{
                                            logger.debug("���������ͷ��Ϣ��û�з������ݣ�"+Command.commandNames.get(header.getCommandId()));
                                        }
                                    }
                                } catch (IOException e) {
                                    logger.error("��ȡ����ʱ�����쳣��"+e.getMessage());
                                    close();
                                    break;
                                }
                            }
                        }else{
                            sleep(100);
                        }
                    } catch (Exception e) {
                        logger.error("��ȡ����ʱ�����쳣��"+e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        };
        //�������������߳�
        readThread.start();
        //�����߳��ڽ������ݵķ���
        while(!shouldStop){
            //���зǿգ���������ݷ���
           while(!sendList.isEmpty()){
               Pdu pdu;
               synchronized (sendQueueLock){
                   pdu = sendList.poll();
               }
               if(pdu!=null){
                   byte[] sendArray = pdu.buildBuffer();
                   logger.debug("׼���������ݣ�"+pdu.toString());
                   int sendResult = sendToServer(sendArray);
                   long sn = pdu.getMessageHeader().getSequenceId();
                   //����0�ǳɹ���-1��ʧ��
                   if(sendResult <0){
                       //����ʧ��
                       logger.error("��������ʱ�����쳣���޷��������У�\n"+pdu);
                       synchronized (readLock){
                           responseList.put(sn,null);
                       }
                       close();
                   }else{

                   }
               }else{
                   logger.warn("�ӷ��Ͷ������ó����������ǿյģ�");
               }
               duration = 0;
               lastCheckTime = System.currentTimeMillis();
           }
           try {
               if(bindSuccess){
                   duration++;
                   if(duration>(handsetInterval-5)*10){
                       synchronized (sendQueueLock){
                           logger.debug("���г�����ʱ�����������������Ϣ��");
                           sendList.offer(new Pdu(new HandsetMessage()));
                       }
                       duration = 0;
                   }
               }
               long now = System.currentTimeMillis();
               if((now-lastCheckTime)>30000){//������ʮ�����ʾһ��
                   logger.debug("���г�������30��....");
                   lastCheckTime = now;
               }
               sleep(100);
           } catch (InterruptedException e) {
               e.printStackTrace();
               logger.warn("�������ˣ�");
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
            logger.error("�޷�д���ݣ�"+e.getMessage());
            e.printStackTrace();
            return -1;
        } catch (Exception e) {
            connected = false;
            bindSuccess =false;
            logger.error("�޷�д���ݣ�"+e.getMessage());
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
            logger.error("�޷���ʼ��Socket!"+e.getMessage());
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
                    //��ʼ����������豸
                } catch (Exception e) {
                    logger.error("�޷���ʼ��"+hostIp+":"+7+","+e.getMessage());
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
            //�ر����ӣ�ԭ��������������Զ��ر�
            logger.debug("�ر����ӡ�������");
            connected = false;
            bindSuccess = false;
            sockRequest.close();
            return true;
        } catch (Exception e) {
            logger.error("�޷��ر�Socket��"+e.getMessage());
            return false;
        }
    }
    public  int sendToServer(byte[] message){
        if(!connected){
            logger.warn("��δ���ӵ�VAC��֤ƽ̨�����Խ������Ӳ�����");
            open(vacHost,vacPort);
        }
        if(!connected){
            logger.error("���ӵ�VAC��֤ƽ̨ʧ�ܣ����ܷ������ݣ�");
        }

/*
        if(!bindSuccess){
            logger.error("�޷����ӵ�VAC��֤����");
            return new byte[0];
        }
*/
        logger.debug("�������͵�byteArray:"+ MD5Utils.bufferToHex(message));
        return write(message);
    }

    public void displayErrorInfo(int errorCode){
        String errorMsg = ErrorCode.getErrorMessage(errorCode);
        logger.error("�����쳣��������룺" + errorCode + " , ������Ϣ��" + errorMsg);

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
                logger.warn("�����ˣ�" + e.getMessage());
                break;
            }
        }
        return null;
    }
    public boolean bind(){
        logger.debug("׼��ִ��bind����");
        String keyName = "vacBindKey";
        ThreadUtils tu = ThreadUtils.getInstance();
        try {
            bindSuccess =false;
            tu.acquire(keyName);
            if(!bindSuccess){
                if(!connected){
                    logger.warn("��δ���ӣ���ʼ������"+vacHost+":"+vacPort);
                    open(vacHost,vacPort);
                }
                if(!connected){
                    logger.error("���ӳ�ʼ��ʧ�ܣ�ֱ�ӷ��أ�");
                    return false;
                }else{
                    logger.debug("���ӳɹ���׼������������"+vacHost+":"+vacPort);
                }
                sequenceId = 0;
                BindMessage message = new BindMessage();
                byte[] buffer = sendAndWait(message);
                if(buffer==null||buffer.length<=0){
                    logger.error("bind�������޷���ȡ������ݣ�");
                    return false;
                }else{
                    logger.debug("bind�����У���ȡ�Ľ�����ݣ�"+MD5Utils.bufferToHex(buffer));
                }
                BindMessageResp resp = new BindMessageResp(buffer);
                logger.debug(resp.toString());
                int resultCode = (int) resp.getResultCode();
                if(resultCode==0){
                    bindSuccess=true;
                    logger.debug("bind�����ɹ������Խ��к�������");
                    return true;
                }else{
                    String errorMsg;
                    bindSuccess = false;
                    close();
                    switch(resultCode){
/*
               case 0:
                   errorMsg = "�ɹ�";
                   break;
*/
                        case 1:
                            errorMsg = "�ʻ�����";
                            break;
                        case 2:
                            errorMsg = "�������";
                            break;
                        case 3:
                            errorMsg = "ԭʼ���豸���ͷǷ�";
                            break;
                        case 4:
                            errorMsg = "ԭʼ���豸ID�ŷǷ�";
                            break;
                        case 5:
                            errorMsg = "Ŀ����豸���ͷǷ�";
                            break;
                        case 6:
                            errorMsg = "Ŀ���豸ID �Ƿ�";
                            break;
                        case 7:
                            errorMsg = "�ظ�����������";
                            break;
                        default:
                            errorMsg = "δ֪�Ĵ�����Ϣ��"+resultCode;
                            break;

                    }
                    logger.error("bind��Ϣʱ�����쳣��"+errorMsg);
                }
                return false;
            }else{
                logger.debug("�ȴ��ڼ䣬���������Ѿ�������bind�������������к���������");
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
        //bind��unbind������Ϣ��һ�µĽṹ
        BindMessageResp resp = new BindMessageResp(buffer);
        logger.debug(resp.toString());
        long resultCode = resp.getResultCode();
        if(resultCode==0){
            bindSuccess = false;
            connected = false;
            close();
            return true;
        }else{
            String errorMsg = "û������";
            if(resultCode!=8){
                errorMsg = "δ֪�Ĵ���:"+resultCode;
            }
            logger.error("unbind�Ƴ�����ʱ�����쳣��"+errorMsg);
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
                            logger.error("�û��ǹ�������ʧ�ܺ��ظ�����:"+pdu);
                            return null;
                        }
                    }
                    logger.error("���ͺ�ȴ�VAC��������������̷����쳣���п����Ƿ���ʧ�ܣ���ǰ���г��ȣ�" +
                            sendList.size()+","+
                            "����һ�Σ����ǵ�" +tryTimes+"�Σ�Ҫ���͵������ǣ�\n"+pdu);
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
     * ������Ϣ����
     * @param shouldWait ���ͺ����ݺ��Ƿ�ȴ������������ȴ���ֱ�ӷ���
     * @return ���ͺ����ݽ��
     */
    public boolean handset(boolean shouldWait){
        if(bindSuccess){
           // logger.debug("׼������handset");
            HandsetMessage message = new HandsetMessage();
            byte[] result = sendAndWait(message,shouldWait);
            return result!=null&&result.length==12;
        }else{
            logger.error("��δbind���޷����������źţ�handset��");
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
        messageBody.setProductId("        ");//CRM��ƷID, ServiceIDTypeΪ5ʱ��Ч��������8���ո�
        messageBody.setSpId(spId);//��ҵ����
        messageBody.setOA(phoneNumber); //����:��8613312345678
        messageBody.setOAType(1L);   //ҵ����˵�ַ���ͣ��ο���ǰVAC�Ĳ����ֲᣬ��д��1
        messageBody.setOANetworkID("WCDMA"); //ҵ������û����������ʶ,�ο���ǰVAC�Ĳ����ֲ�WCDMA
        messageBody.setDA(phoneNumber);
        messageBody.setDAType(1L);   //Ŀ���ַ����,�ο���ǰVAC�Ĳ����ֲᣬ��д��1
        messageBody.setDANetworkID("WCDMA");//Ŀ����û����������ʶ,�ο���ǰVAC�Ĳ����ֲᣬ��д��WCDMA
        messageBody.setFA(phoneNumber);
        messageBody.setFAType(1L);   //Ŀ���ַ����,�ο���ǰVAC�Ĳ����ֲᣬ��д��1
        messageBody.setFANetworkID("WCDMA");//Ŀ����û����������ʶ,�ο���ǰVAC�Ĳ����ֲᣬ��д��WCDMA
        messageBody.setOperationType(operationType);  //����  �ο���ǰVAC�����ֲᣬ��д��1
        messageBody.setServiceUpDownType(9L);//ҵ�����������ͣ��ο���ǰVA�����ֲᣬ��д��1,���޸ĳ�9
        messageBody.setServiceIDType(3L);  //�ο���ǰ��VAC�ֲ���д��3
        messageBody.setServiceType("80"); //ԭ��VAC�ֲ���д����93����ͨ����ÿ�����80
        messageBody.setServiceId(productId);// ServiceIDTypeΪ1ʱ��дServiceID��Ϊ3ʱ��дSP_ProductID��Ϊ5ʱ��дSPEC_ProductID(ҵ�񲿼���֪���û�������㲥CRM�Ĳ�Ʒ����)
        return messageBody;
    }
    public int  checkPrice(String phoneNumber,String productId,long operationType,String spId){
        logger.debug("����checkPrice��ʼ��");
        if(!bindSuccess){
            logger.error("����checkPriceʱ������δbind������bind������");
            bind();
        }
        if(!bindSuccess){
            logger.error("����checkPriceʱbindʧ�ܣ��޷����к���������");
            return ErrorCode.ERROR_NOT_BINDED;
        }
        boolean result=false;
        int errorCode=ErrorCode.ERROR_SEND_ERROR;
//        String spId = config.getConfig("vac.spId","0101");
        CheckPriceMessage checkPriceMessage = getBaseCheckPriceMessage(spId, phoneNumber, productId, operationType);

        //����VAC�����뷴����Ϣ��־
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
            vacLog.setCheckPriceResp("���ݳ��Է��ͣ��ȴ������");
            //vacLog = vacLogLogicInterface.save(vacLog);
            byte[] buffer = sendAndWait(checkPriceMessage);
            vacLog.setCheckPriceMessage(checkPriceMessage.toString());
            if(buffer!=null&&buffer.length>0){
                MessageHeader header = new MessageHeader(buffer);
                CheckPriceResp checkPriceResp = new CheckPriceResp(buffer);
                errorCode = (int)checkPriceResp.getResultCode();
                logger.debug("checkPrice���ú�����ݷ��أ�\r\n"+header.toString()+"\r\n"+checkPriceResp);
                result = errorCode == 0;
                if(!result){
                    displayErrorInfo(errorCode);
                }
                vacLog.setCheckPriceResp(checkPriceResp.toString());
            }else{
                vacLog.setCheckPriceResp("���������쳣���޷�����������");
            }
            vacLog.setResultCode(errorCode);
            Date stopTime = new Date();
            vacLog.setFinishTime(stopTime);
            vacLog.setDuration((int)(stopTime.getTime()-startTime.getTime()));
            vacLogLogicInterface.save(vacLog);
            if(result&&Command.OPERATE_TYPE_VOD.equals(checkPriceMessage.getOperationType())&&
                    AppConfigurator.getInstance().getBoolConfig("vac.checkPriceForVodNeedConfirm",false)){
                //����ǰ��Σ������Ҫ�Ʒ�ȷ��
                String sequenceNumber = checkPriceMessage.getSequenceNumber();
                CheckPriceConfirmMessage checkPriceConfirmMessage = new CheckPriceConfirmMessage();
                checkPriceConfirmMessage.setSequenceNumber(sequenceNumber);
                checkPriceConfirmMessage.setErrCode(0L);
                checkPriceConfirmMessage.setServiceType("80");
                checkPriceConfirmMessage.setEndTime(StringUtils.date2string(new Date(new Date().getTime() + 24 * 3600 * 1000L), "yyyyMMddHHmmss"));
                logger.debug("���ε㲥��Ҫ����ȷ�ϣ�׼������ȷ����Ϣ��"+checkPriceConfirmMessage.toString());
                buffer= sendAndWait(checkPriceConfirmMessage);
                vacLog.setId(-1);
                vacLog.setResultCode(-9999);
                vacLog.setCheckPriceMessage(checkPriceConfirmMessage.toString());
                if(buffer!=null&&buffer.length>0){
                    MessageHeader header = new MessageHeader(buffer);
                    CheckPriceConfirmResp resp = new CheckPriceConfirmResp(buffer);
                    vacLog.setResultCode((int)resp.getResultCode());
                    vacLog.setCheckPriceResp(resp.toString());
                    logger.debug("���ε㲥��Ҫ����ȷ�ϣ�����ȷ����Ϣ�󷵻أ�" + checkPriceConfirmMessage.toString());
                }else{
                    logger.error("���η���ȷ����Ϣʱʧ�ܣ�"+checkPriceConfirmMessage.toString());
                }
                vacLogLogicInterface.save(vacLog);
            }
        } catch (Exception e) {
            logger.debug("�����쳣���޷�����VAC��־��"+e.getMessage());
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
