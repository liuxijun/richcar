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
 * Time: 下午12:23
 * AVC认证
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
    private int handsetInterval; //握手信号时间间隔
    private int timeoutSeconds;//超时时间
    private int retryTimes;       //超时重试次数
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
            logger.error("无法写数据："+e.getMessage());
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
            logger.error("无法初始化Socket!"+e.getMessage());
        }
    }
    public boolean open(String hostIp,int hostPort) {
        try {
            sockRequest = new Socket(hostIp,hostPort);
            init(sockRequest);
            connected = true;
            //初始化输入输出设备
        } catch (Exception e) {
            logger.error("无法初始化"+hostIp+":"+7+","+e.getMessage());
            connected = false;
            return false;
        }
        return true;
    }

    public boolean close() {
        try {
            //关闭连接，原来的输入输出会自动关闭
            connected = false;
            bindSuccess = false;
            sockRequest.close();
            return true;
        } catch (Exception e) {
            logger.error("无法关闭Socket："+e.getMessage());
            return false;
        }
    }
    public  synchronized byte[] sendToServer(byte[] message){
        if(!connected){
            logger.warn("尚未连接到VAC认证平台！尝试进行连接操作：");
            open(sgipIp,sgipPort);
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
        logger.error("发生异常，错误代码：" + errorCode + " , 错误信息：" + errorMsg);

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
        logger.debug("准备执行bind操作");
        if(!connected){
            logger.warn("尚未连接！初始化连接"+ sgipIp +":"+sgipPort);
            open(sgipIp, sgipPort);
        }
        if(!connected){
            logger.error("连接初始化失败，直接返回！");
            return false;
        }
        sequenceId = 0;
        com.fortune.smgw.api.sgip.message.SGIPBind message = getLoginMsg();
        byte[] buffer = sendToServer(message.toByteArray());
        if(buffer==null||buffer.length<=0){
            logger.error("bind过程中无法获取结果数据！");
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
            logger.debug("bind过程中，获取的结果数据："+resultByteString);
        }
        SGIPBindResp resp = processBindResp(buffer);

        logger.debug(resp.toString());
        int resultCode = resp.getBody().getResult();
        if(resultCode==0){
            bindSuccess=true;
            logger.debug("bind操作成功，可以进行后续操作");
            return true;
        }else{
            String errorMsg = "";
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
            logger.error("发生异常："+e.getMessage());
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
            logger.debug("准备发送数据....");
            byte[] result;
            try {
                result = sendToServer(submit.toByteArray());
                logger.debug("发送结果数据："+ MD5Utils.bufferToHex(result));
                SGIPSubmitResp resp = SGIPSubmitResp.parse(ByteBuffer.wrap(result));
                logger.debug("返回数据："+resp);
                return resp;
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                logger.error("读取数据发生异常："+e.getMessage());
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

    public boolean handset(){
        if(bindSuccess){
            logger.debug("准备发送handset");
            HandsetMessage message = new HandsetMessage();
            byte[] buffer = sendToServer(message);
            if(buffer!=null&&buffer.length>0){
                //握手信号不返回任何消息
                MessageHeader header = new MessageHeader(buffer);
                logger.debug(header.toString());
                return true;
            }else{
                //握手信号发送失败！
                logger.error("无法进行握手！");
            }
        }else{
            logger.error("尚未bind，无法发送握手信号：handset！");
        }
        close();
        return false;
    }

*/
}
