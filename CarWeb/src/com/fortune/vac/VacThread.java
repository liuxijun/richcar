package com.fortune.vac;

import com.fortune.rms.business.user.logic.logicInterface.UserLogicInterface;
import com.fortune.util.AppConfigurator;
import com.fortune.util.MD5Utils;
import com.fortune.util.cache.Cache;
import com.fortune.util.config.Config;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class VacThread extends Thread {
    private Socket sockRequest;
    protected BufferedReader sockIn = null;
    private PrintStream sockOut;
    protected InputStream socketInStream;
    public  static long lThreadNumber;
    private String clientServerIp;
    //ͬ����
    private static Object oLock = new Object();
    private static long lKey = 0;
    private VacServer startup;
    private boolean hasBeenBind = false;
    //cache server list
    private static Cache cache = new Cache(50000000,Config.getIntValue("midware.validtime",2)*1000);
    private int timer = 600;//60����û�������˳�
    private boolean  shouldStop = false;
    public class CloseSocketTask extends TimerTask{
        private Timer timer;
        public CloseSocketTask(Timer timer){
            this.timer = timer;
        }
        public void run(){
            logger.warn("��ʱ�ˣ����رմ����ӣ�");
            sockOut.println("yes timeOut");
            closeSocket();
            timer.cancel();
        }
    }

    public VacThread(Socket socket, VacServer startup) {
        this.sockRequest = socket;
        this.startup = startup;
        synchronized(oLock){
            //logger.error("count:"+lThreadNumber);
            lThreadNumber++;
        }

        try {
            socketInStream = socket.getInputStream();
            sockIn = new BufferedReader(new InputStreamReader(socketInStream));
            sockOut = new PrintStream(socket.getOutputStream());
            clientServerIp = sockRequest.getInetAddress().getHostAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    Logger logger = Logger.getLogger(getClass());

    public int read(byte[] dataBuffer,int startPos,int len){
        try {
            return socketInStream.read(dataBuffer,startPos,len);
        } catch (IOException e) {
            e.printStackTrace();
            closeSocket();
        }
        return 0;
    }
    public int read(byte[] dataBuffer){
        try {
            return socketInStream.read(dataBuffer);
        } catch (IOException e) {
            e.printStackTrace();
            closeSocket();
        }
        return 0;
    }

    public void write(byte[] dataBuffer){
        try {
            logger.debug("����Ϣ���ȣ�"+dataBuffer.length);
            sockOut.write(dataBuffer);
        } catch (IOException e) {
            logger.error("�޷�д���ݣ�"+e.getMessage());
            closeSocket();
        }
    }

    /**
     * �����߳�
     */
    public void run() {
        AppConfigurator config = AppConfigurator.getInstance();
        String clientIp = sockRequest.getInetAddress().getHostAddress();
        String sourceDeviceID = config.getConfig("vac.default.sourceDeviceID","421802");
        String pwd = config.getConfig("vac.default.pwd","123456");
        byte[] dataBuffer = new byte[64*1024];
        byte[] messageHeader = new byte[12];
        Map<String,Integer> buyProducts = new HashMap<String, Integer>();
        while((!shouldStop)&&timer>0){
            timer--;
            int dataLength = read(messageHeader);
            if(dataLength>0){
                timer = 600;
                MessageHeader header = new MessageHeader(messageHeader);
                if(header.getTotalLength()>12){
                    read(dataBuffer,12,(int)header.getTotalLength()-12);
                }
                logger.debug("�пͻ������ӹ�����"+clientIp+"�������ֽ�����"+dataLength+",\r\n"+header.toString());
                if(header.getCommandId()==Command.CMDID_Bind){
                    BindMessage bindMessage = new BindMessage(dataBuffer);
                    String timeStamp = bindMessage.getTimeStamp();
                    long resultCode;
                    if(sourceDeviceID.equals(bindMessage.getSourceDeviceID())){
                        try {
                            String myPwd = MD5Utils.getMD5String(sourceDeviceID+pwd+timeStamp).toUpperCase();
                            String checkSource= MD5Utils.bufferToHex(bindMessage.getCheckSource());
                            if(checkSource!=null&&checkSource.length()>=16&&myPwd.equalsIgnoreCase(checkSource)){
                                resultCode = 0;
                            }else{
                                logger.error("�ͻ��������쳣��У����������ҵ���Կ��"+myPwd+")!=������Կ��"+checkSource+"��");
                                resultCode = 2;
                            }
                        } catch (NoSuchAlgorithmException e) {
                            resultCode = 105;
                        }
                    }else{
                        resultCode = 4;
                        logger.error("�ͻ��˷�����ԭʼ�豸�Ų��ԣ��ҵģ�"+sourceDeviceID+"��!=�Է��ģ�"+bindMessage.getSourceDeviceID()+")");
                    }
                    logger.debug("bindMessage="+bindMessage.toString());
                    Pdu pdu = new Pdu();
                    pdu.setMessageHeader(new MessageHeader(16,Command.CMDID_BindResp,header.getSequenceId()));
                    pdu.setMessageBody(new BindMessageResp(resultCode));
                    write(pdu.buildBuffer());
                    hasBeenBind = true;
                }else if(header.getCommandId() == Command.CMDID_CheckPriceConfirm){
                    CheckPriceConfirmMessage confirmMessage = new CheckPriceConfirmMessage(dataBuffer);
                    CheckPriceConfirmResp resp = new CheckPriceConfirmResp(0);
                    logger.debug("����ȷ����Ϣ��"+confirmMessage.toString());
                    logger.debug("����ȷ�Ϸ��أ�"+resp.toString());
                    header.setCommandId(Command.CMDID_CheckPriceConfirmResp);
                    Pdu pdu = new Pdu();
                    pdu.setMessageBody(resp);
                    pdu.setMessageHeader(header);
                    write(pdu.buildBuffer());
                }else if(header.getCommandId() == Command.CMDID_CheckPrice){
                    long resultCode = 0;
                    if(!hasBeenBind){
                        resultCode = 6;
                    }
                    CheckPriceMessage checkPriceMessage = new CheckPriceMessage(dataBuffer);
                    logger.debug(checkPriceMessage.toString());
                    CheckPriceResp resp = new CheckPriceResp(resultCode,0,0,checkPriceMessage.getDA());
                    String key = checkPriceMessage.getDA()+"_"+checkPriceMessage.getServiceId();
                    if(UserLogicInterface.VAC_CHECK_PRODUCT_ORDER.equals(checkPriceMessage.getOperationType())){
                        Integer times =buyProducts.get(key);
                        if(times!=null){
                            times++;
                            buyProducts.put(key,times);
                            logger.debug("׼������Ƿ��Ѿ�����"+checkPriceMessage.getServiceId()+",���ֹ����¼��");
                            resultCode = 0;
                        }else{
                            logger.debug("׼������Ƿ��Ѿ�����"+checkPriceMessage.getServiceId()+",Ĭ��Ҫ����");
                            //buyProducts.put(key,1);
                            resultCode = 6;
                        }
                    }else if(UserLogicInterface.VAC_ORDER_PRODUCT_FOR_ONCE_TIME.equals(checkPriceMessage.getOperationType())){
                        logger.debug("���Թ��򰴴Σ�"+checkPriceMessage.getServiceId()+",Ĭ��ͨ��!");
                        buyProducts.put(key,1);
                        resp.setLinkId(new Tlv(0x0001,20,checkPriceMessage.getSequenceNumber()));
                        resultCode = 0;
                    }else if(UserLogicInterface.VAC_ORDER_PRODUCT.equals(checkPriceMessage.getOperationType())){
                        logger.debug("���Թ�����£�"+checkPriceMessage.getServiceId()+",Ĭ��ͨ��!");
                        buyProducts.put(key,1);
                        resultCode = 0;
                    }
                    header.setCommandId(Command.CMDID_CheckPriceResp);
                    Tlv feeType = new Tlv(1,"3");
                    Tlv needToNextNode = new Tlv(1,"1");

                    resp.setResultCode(resultCode);
                    resp.setFeeType(feeType);
                    resp.setNeedToNextNode(needToNextNode);
                    Pdu pdu = new Pdu();
                    pdu.setMessageBody(resp);
                    pdu.setMessageHeader(header);
                    logger.debug(resp.toString());
                    write(pdu.buildBuffer());
                }else if(header.getCommandId() == Command.CMDID_UnBind){
                    Pdu pdu = new Pdu();
                    //ȥ���Ӻ���������һ����
                    pdu.setMessageHeader(new MessageHeader(16, Command.CMDID_UnBindResp, header.getSequenceId()));
                    pdu.setMessageBody(new BindMessageResp(0));
                    write(pdu.buildBuffer());
                }else if(header.getCommandId() == Command.CMDID_Handset){
                    Pdu pdu = new Pdu();
                    //ȥ���Ӻ���������һ����
                    HandsetMessage handsetMessage = new HandsetMessage();
                    pdu.setMessageHeader(new MessageHeader(16,Command.CMDID_HandsetResp,header.getSequenceId()));
                    pdu.setMessageBody(null);
                    write(pdu.buildBuffer());
                    timer = 600;
                }else{
                    logger.error("������ʶ�����"+header.toJson());
                }
            }
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        closeSocket();
    }


    /**
     * �رճ�Աsocket
     */
    private void closeSocket() {
        //logger.error(sockRequest.isConnected()+":"+sockRequest.isClosed());

        synchronized(oLock){
            lThreadNumber--;
        }
        
        try{
            sockRequest.shutdownInput();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            sockRequest.shutdownOutput();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            if(sockIn != null){
                sockIn.close();
                sockIn = null;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            if(sockOut!=null){
                sockOut.close();
                sockOut = null;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            if(sockRequest!=null){
                sockRequest.close();
                sockRequest = null;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}