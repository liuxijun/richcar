package com.fortune.smgw.api.sgip.client;

import com.fortune.smgw.api.common.ValidityResult;
import com.fortune.smgw.api.common.timeout.TimeoutEvent;
import com.fortune.smgw.api.common.timeout.TimeoutListener;
import com.fortune.smgw.api.common.timeout.TimeoutManager;
import com.fortune.smgw.api.common.timeout.TimeoutObject;
import com.fortune.smgw.api.sgip.SGIPConfig;
import com.fortune.smgw.api.sgip.message.SGIPSubmit;
import com.fortune.smgw.api.sgip.message.SGIPSubmitResp;
import com.fortune.smgw.api.sgip.message.body.SGIPCommonRespBody;
import com.fortune.smgw.api.sgip.message.body.SGIPSequenceNo;
import com.fortune.smgw.socket.client.SocketClient;
import com.fortune.smgw.socket.client.SocketClientLink;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fortune.util.AppConfigurator;
import com.fortune.util.StringUtils;
import org.apache.log4j.Logger;

public class SGIPClient {
    private Logger log = Logger.getLogger(SGIPClient.class);
    private boolean initFlag = false;
    private static int currentLinkId;
    private SocketClient client;
    public static Map rspHandlers = Collections.synchronizedMap(new HashMap());
    private SGIPClientInitInfo info;
    private static SGIPClient singleton_instance = null;
    private ScheduledExecutorService scheduExec = Executors.newScheduledThreadPool(1);
    private TimeoutManager tm = new TimeoutManager(SGIPConfig.getInstance().SGIP_CLIENT_TIMEOUT);

    public static synchronized SGIPClient getInstance() {
        if (singleton_instance == null) {
            singleton_instance = new SGIPClient();
        }
        return singleton_instance;
    }

    private SGIPClient() {
        AppConfigurator config = AppConfigurator.getInstance();
        info = new SGIPClientInitInfo();
        info.IP = config.getConfig("SGIP_IP", "221.192.140.33");
        info.port = config.getIntConfig("SGIP_PORT", 9001);
        info.userName = config.getConfig("SGIP_USER", "openhe");
        info.passWord = config.getConfig("SGIP_PASSWORD", "123456");
        info.maxLink = config.getIntConfig("SGIP_MAX_LINK", 1);
        init(info);
/*
        AppConfigurator sysConfig = AppConfigurator.getInstance();
        if(sysConfig.getBoolConfig("SGIP_SEND_HANDSET",true)){
            Runnable scheduleTask = new Runnable() {
                public void run(){
                    sendMsgToPhone(StringUtils.date2string(new Date(),"2yyyyMMddHHm"),"Handset");
                }
            };
            //每隔30秒，刷新一下任务列表
            scheduledExecutorService.scheduleWithFixedDelay(scheduleTask, 29, 29, TimeUnit.SECONDS);
        }
*/
    }
    public int sendMsgToPhone(String phoneNumber,String message){
        Date now = new Date();
        AppConfigurator sysConfig = AppConfigurator.getInstance();
        SGIPSubmit submit = new SGIPSubmit();
        Date expireTime = new Date(now.getTime()+5*60*1000L);//过期时间，设定为5分钟
        submit.getBody().setSPNumber(sysConfig.getConfig("SGIP_SP_NUMBER","10655910010"));//接入号
        submit.getBody().setChargeNumber("000000000000000000000");//该费用由SP支付//phoneNumber);
        submit.getBody().setUserCount(1);
        if(phoneNumber.startsWith("+86")||phoneNumber.startsWith("086")){
            phoneNumber = phoneNumber.substring(1);
        }
        if(phoneNumber.startsWith("0086")){
            phoneNumber = phoneNumber.substring(2);
        }
        if(!phoneNumber.startsWith("86")){
            phoneNumber= "86"+phoneNumber;
        }
        submit.getBody().setUserNumber(phoneNumber);
        submit.getBody().setCorpId(sysConfig.getConfig("SGIP_CORP_ID","14294"));//企业代码
        submit.getBody().setFeeType(1);
        submit.getBody().setFeeValue("0");
        submit.getBody().setGivenValue("0");//赠送的话费
        submit.getBody().setAgentFlag(0);
        submit.getBody().setMorelatetoMTFlag(0);
        submit.getBody().setPriority(0);
        submit.getBody().setExpireTime(StringUtils.date2string(expireTime, "yyMMddHHmmss"));
        //submit.getBody().setScheduleTime(date2string(now,"yymmddhhmmss")+"032+");//"090621010101"
        submit.getBody().setScheduleTime(null);//立即发送。\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0");//"090621010101"
        submit.getBody().setReportFlag(2); /*无需状态回报
                    状态报告标记
                            0-该条消息只有最后出错时要返回状态报告
                            1-该条消息无论最后是否成功都要返回状态报告
                            2-该条消息不需要返回状态报告
                            3-该条消息仅携带包月计费信息，不下发给用户，要返回状态报告

                    */
        submit.getBody().setTP_pid(0);
        submit.getBody().setTP_udhi(0);
        submit.getBody().setMessageCoding(15);//GBK编码
        /*
       短消息的编码格式。
               0：纯ASCII字符串
               3：写卡操作
               4：二进制编码
               8：UCS2编码
               15: GBK编码
               其它参见GSM3.38第4节：SMS Data Coding Scheme

       * */
        try {
            submit.getBody().setMessageContent(message.getBytes("GBK"));
        } catch (UnsupportedEncodingException e) {
            log.error("转码出现错误，不支持到GBK的转码："+message,e);
        }
        submit.getBody().setReserve("");
        submit.getBody().setMessageType(0);
        SGIPRsp rsphandler6;
        rsphandler6 = new SGIPRsp();
        submit.getHead().setSequenceNo(new SGIPSequenceNo());
        log.debug("准备发送.....");
        try {
            sendSubmit(submit, rsphandler6);
        } catch (IOException e) {
            //result = -100;
        }
        SGIPSubmitResp rsp6 = rsphandler6.waitForSGIPSubmitResp();
        //client.close();
        return rsp6.getBody().getResult();
    }
    public void reInit(SGIPClientInitInfo reInitInfo){
        if(reInitInfo !=null){
            //配置不一样的话，才会重新初始化
            if(!(reInitInfo.IP.equals(info.IP)&& reInitInfo.port==info.port)
                    && reInitInfo.passWord.equals(info.passWord)&& reInitInfo.userName.equals(info.userName)){
                init(reInitInfo);
            }else{
                log.warn("参数没有发生变化，不重新初始化配置！");
            }
        }
    }
    public void init(SGIPClientInitInfo initInfo) {
        try {
            log.debug("正在进行初始化操作....");
            if (!this.initFlag) {
                this.info.IP = initInfo.IP;
                this.info.port = initInfo.port;
                if (initInfo.maxLink > 20)
                    this.info.maxLink = 20;
                else {
                    this.info.maxLink = initInfo.maxLink;
                }
                this.info.userName = initInfo.userName;
                this.info.passWord = initInfo.passWord;
                this.initFlag = true;
            }
            this.client = new SocketClient(InetAddress.getByName(info.IP), info.port, info.maxLink,
                    "com.fortune.smgw.api.sgip.client.SGIPRspHandler",
                    "com.fortune.smgw.api.sgip.client.SGIPLoginHandler");
            this.log.info("Init SocketClient success");

            this.tm.addTimeoutListener(new TimeoutListener() {
                public void timeoutPerformed(TimeoutEvent event) {
                    List list = event.getKeys();
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        SGIPSequenceNo key = (SGIPSequenceNo) it.next();
                        SGIPRsp handler = (SGIPRsp) SGIPClient.rspHandlers.remove(key.toString());
                        if (handler != null) {
                            handler.handleResponse(SGIPClient.this.getSGIPSubmitResp(key, -2));
                            SGIPClient.this.log.error("message send fail,msgid: " + key + ",send timeout");
                        }
                    }
                }
            });
            this.tm.start();
        } catch (Exception e) {
            this.log.error("Init SocketClient Exception,methed:init", e);
        }
    }
    public void close(){
        try {
            SocketClientLink link = getLink();
            link.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("尝试关闭是发生异常："+e.getMessage());
        }
    }
    public void sendSubmit(SGIPSubmit submit, SGIPRsp handler)
            throws IOException {
        String msgid = submit.getHead().getSequenceNo().toString();
        this.log.debug("Start send message,msgid:" + msgid);
        this.log.debug("message data:" + submit.toString());
        ValidityResult vr = submit.checkValidity();

        if (vr.getCode() != 0) {
            handler.handleResponse(
                    getSGIPSubmitResp(submit.getHead().getSequenceNo(),
                            -1));
            this.log.error("message check fail,send terminate,resean:" + vr.getDescribe());
            return;
        }
        log.debug("尝试获取连接....");
        SocketClientLink link = getLink();
        log.debug("尝试获取连接完毕！");
        if(!link.getSocket().isConnected()){
            log.error("发现网络未连接！");
        }else{

        }
        log.debug("link.status="+link.getStatus());
        if (link.getStatus() == -2) {
            log.debug("直接返回发送状态：-4");
            handler.handleResponse(
                    getSGIPSubmitResp(submit.getHead().getSequenceNo(),
                            -4));
            link.setStatus(0);
        } else if (link.getStatus() == -1) {
            log.debug("直接返回发送状态：-3");
            handler.handleResponse(
                    getSGIPSubmitResp(submit.getHead().getSequenceNo(),
                            -3));
            link.setStatus(0);
        } else {
            log.debug("准备发送数据....");
            this.tm.addTimeoutObject(new TimeoutObject(submit.getHead().getSequenceNo()));
            addHandlerMap(msgid, handler);
            link.send(submit.toByteArray());
            log.debug("准备发送数据....");
        }

    }

    private SGIPSubmitResp getSGIPSubmitResp(SGIPSequenceNo seq, int result) {
        SGIPSubmitResp rsp = new SGIPSubmitResp();
        SGIPCommonRespBody body = new SGIPCommonRespBody();
        body.setResult(result);
        rsp.getHead().setSequenceNo(seq);
        rsp.setBody(body);
        return rsp;
    }

    private void addHandlerMap(String msgid, SGIPRsp handler) {
        rspHandlers.put(msgid, handler);
    }

    private SocketClientLink getLink() throws IOException {
        return client.getLink();
    }

    public String getUserName() {
        return info.userName;
    }

    public String getPassWord() {
        return info.passWord;
    }

    public String getIP() {
        return info.IP;
    }

    public int getPort() {
        return info.port;
    }

    public boolean isInitFlag() {
        return initFlag;
    }

    public int getMaxLink() {
        return info.maxLink;
    }

    public SGIPClientInitInfo getInitInfo(){
        return info;
    }
}