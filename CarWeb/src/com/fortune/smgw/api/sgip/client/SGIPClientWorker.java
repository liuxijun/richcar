package com.fortune.smgw.api.sgip.client;

import com.fortune.smgw.api.common.ValidityResult;
import com.fortune.smgw.api.sgip.message.*;
import com.fortune.smgw.api.sgip.message.body.SGIPCommonRespBody;
import com.fortune.smgw.api.sgip.message.body.SGIPSequenceNo;
import com.fortune.util.AppConfigurator;
import com.fortune.util.MD5Utils;
import com.fortune.util.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-9-23
 * Time: ����12:23
 * AVC��֤
 */
public class SGIPClientWorker {
    private Socket sockRequest = null;//new java.net.Socket( MiddleServerAddress, MiddleServerPort );
    private BufferedReader sockIn = null;
    private InputStream socketInStream;
    private PrintStream sockOut = null;
    private Logger logger = Logger.getLogger(getClass());
    private boolean connected=false;
    private static SGIPClientWorker instance = new SGIPClientWorker();
    private String sgipIp;
    private int sgipPort;
    private String sgipUser;
    private String sgipPassword;
    private int handsetInterval; //�����ź�ʱ����
    private int timeoutSeconds;//��ʱʱ��
    private int retryTimes;       //��ʱ���Դ���
    private long sequenceId = 0;
    public static SGIPClientWorker getInstance(){
        return instance;
    }
    private SGIPClientWorker(){
        AppConfigurator sysConfig = AppConfigurator.getInstance();
        sgipIp =sysConfig.getConfig("SGIP_IP","221.192.140.33");
        sgipPort = sysConfig.getIntConfig("SGIP_PORT",9001);
        sgipUser = sysConfig.getConfig("SGIP_USER","openhe");
        sgipPassword = sysConfig.getConfig("SGIP_PASSWORD","123456");
        //open(sgipIp,sgipPort);
    }

    public boolean startup(){
        return open(sgipIp,sgipPort);
    }

    public boolean shutdown(){
        SGIPUnbind message = new SGIPUnbind();
        sendToServer(message.toByteArray());
        close();
        return true;
    }

    public int read(byte[] dataBuffer){
        try {
            return socketInStream.read(dataBuffer);
        } catch (IOException e) {
            connected = false;
            bindSuccess =false;
            e.printStackTrace();
        }
        return 0;
    }

    public void write(byte[] dataBuffer){
        try {
            sockOut.write(dataBuffer);
        } catch (IOException e) {
            connected = false;
            bindSuccess =false;
            logger.error("�޷�д���ݣ�"+e.getMessage());
        }
    }
    public void init(Socket socket){
        try {
            socketInStream = socket.getInputStream();
            sockIn = new BufferedReader(new InputStreamReader(socketInStream));
            sockOut = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            connected = false;
            bindSuccess =false;
            logger.error("�޷���ʼ��Socket!"+e.getMessage());
        }
    }
    public boolean open(String hostIp,int hostPort) {
        try {
            sockRequest = new Socket(hostIp,hostPort);
            init(sockRequest);
            connected = true;
            //��ʼ����������豸
        } catch (Exception e) {
            logger.error("�޷���ʼ��"+hostIp+":"+7+","+e.getMessage());
            connected = false;
            return false;
        }
        return true;
    }

    public boolean close() {
        try {
            //�ر����ӣ�ԭ��������������Զ��ر�
            connected = false;
            bindSuccess = false;
            sockRequest.close();
            return true;
        } catch (Exception e) {
            logger.error("�޷��ر�Socket��"+e.getMessage());
            return false;
        }
    }
    public  synchronized byte[] sendToServer(byte[] message){
        if(!connected){
            logger.warn("��δ���ӵ�VAC��֤ƽ̨�����Խ������Ӳ�����");
            open(sgipIp,sgipPort);
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
        write(message);
        byte[] resultBuffer =new byte[512*1024];
        int dataLength = read(resultBuffer);
        //logger.debug("read_dataLength-->"+dataLength);
        if(dataLength>0){
            byte[] buffer = new byte[dataLength];
            System.arraycopy(resultBuffer,0,buffer,0,dataLength);
            return buffer;
        }else{
            return new byte[0];
        }
    }

/*
    public void displayErrorInfo(int errorCode){
        String errorMsg = ErrorCode.getErrorMessage(errorCode);
        logger.error("�����쳣��������룺" + errorCode + " , ������Ϣ��" + errorMsg);

    }
*/
    private boolean bindSuccess = false;
    private com.fortune.smgw.api.sgip.message.SGIPBind getLoginMsg() {
        com.fortune.smgw.api.sgip.message.SGIPBind bind = new com.fortune.smgw.api.sgip.message.SGIPBind();
        bind.getBody().setLoginName(sgipUser);
        bind.getBody().setLoginPassword(sgipPassword);
        bind.getBody().setLoginType((byte)1);
        return bind;
    }

    private SGIPBindResp processBindResp(byte[] msg) {
        SGIPBindResp resp = SGIPBindResp.parse(ByteBuffer.wrap(msg));
        this.logger.debug(resp.toString());
        if (resp.getBody().getResult() == 0) {
            bindSuccess =true;
            this.logger.debug("BindResp process success");
        } else {
            bindSuccess = false;
            this.logger.debug("BindResp process fail");
        }
        return resp;
    }

    public boolean bind(){
        logger.debug("׼��ִ��bind����");
        if(!connected){
            logger.warn("��δ���ӣ���ʼ������"+ sgipIp +":"+sgipPort);
            open(sgipIp, sgipPort);
        }
        if(!connected){
            logger.error("���ӳ�ʼ��ʧ�ܣ�ֱ�ӷ��أ�");
            return false;
        }
        sequenceId = 0;
        com.fortune.smgw.api.sgip.message.SGIPBind message = getLoginMsg();
        byte[] buffer = sendToServer(message.toByteArray());
        if(buffer==null||buffer.length<=0){
            logger.error("bind�������޷���ȡ������ݣ�");
            return false;
        }else{
            String resultByteString = "";
            for(int i=1;i<=buffer.length;i++){
                String b = Integer.toHexString(0xFF & buffer[i-1]);
                if(b.length()==1){
                    b = "0"+b;
                }
                resultByteString+=b;
                if(i%4==0){
                    resultByteString+=" ";
                }
            }
            logger.debug("bind�����У���ȡ�Ľ�����ݣ�"+resultByteString);
        }
        SGIPBindResp resp = processBindResp(buffer);

        logger.debug(resp.toString());
        int resultCode = resp.getBody().getResult();
        if(resultCode==0){
            bindSuccess=true;
            logger.debug("bind�����ɹ������Խ��к�������");
            return true;
        }else{
            String errorMsg = "";
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
    }

    private SGIPSubmitResp getSGIPSubmitResp(SGIPSequenceNo seq, int result) {
        SGIPSubmitResp rsp = new SGIPSubmitResp();
        SGIPCommonRespBody body = new SGIPCommonRespBody();
        body.setResult(result);
        rsp.getHead().setSequenceNo(seq);
        rsp.setBody(body);
        return rsp;
    }


    public synchronized SGIPSubmitResp sendSubmit(SGIPSubmit submit){
        String msgid = submit.getHead().getSequenceNo().toString();
        this.logger.debug("Start send message,msgid:" + msgid);
        this.logger.debug("message data:" + submit.toString());
        ValidityResult vr = submit.checkValidity();

        if (vr.getCode() != 0) {
            return getSGIPSubmitResp(submit.getHead().getSequenceNo(),
                    -1);
        }
        try {
            if(!sockRequest.isConnected()){
                this.close();
                connected = false;
                bindSuccess = false;
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            logger.error("�����쳣��"+e.getMessage());
            this.close();
            connected = false;
            bindSuccess = false;
        }
        if(!bindSuccess){
            bind();
        }
        if (!bindSuccess) {
            return getSGIPSubmitResp(submit.getHead().getSequenceNo(),
                            SGIPErrorCode.ERROR_NETWORK_UNBIND);
        } else {
            logger.debug("׼����������....");
            byte[] result;
            try {
                result = sendToServer(submit.toByteArray());
                logger.debug("���ͽ�����ݣ�"+ MD5Utils.bufferToHex(result));
                SGIPSubmitResp resp = SGIPSubmitResp.parse(ByteBuffer.wrap(result));
                logger.debug("�������ݣ�"+resp);
                return resp;
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                logger.error("��ȡ���ݷ����쳣��"+e.getMessage());
                return getSGIPSubmitResp(submit.getHead().getSequenceNo(),
                        SGIPErrorCode.ERROR_NETWORK_UNCONNECTED);
            }
        }
    }
/*
    public boolean unBind(){
        logger.debug("unBind");
        UnbindMessage message = new UnbindMessage();
        byte[] buffer = sendToServer(message);
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

    public boolean handset(){
        if(bindSuccess){
            logger.debug("׼������handset");
            HandsetMessage message = new HandsetMessage();
            byte[] buffer = sendToServer(message);
            if(buffer!=null&&buffer.length>0){
                //�����źŲ������κ���Ϣ
                MessageHeader header = new MessageHeader(buffer);
                logger.debug(header.toString());
                return true;
            }else{
                //�����źŷ���ʧ�ܣ�
                logger.error("�޷��������֣�");
            }
        }else{
            logger.error("��δbind���޷����������źţ�handset��");
        }
        close();
        return false;
    }

*/
}
