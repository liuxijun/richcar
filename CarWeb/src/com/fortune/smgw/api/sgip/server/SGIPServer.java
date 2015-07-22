package com.fortune.smgw.api.sgip.server;

import com.fortune.smgw.api.sgip.client.SGIPClient;
import com.fortune.smgw.api.sgip.client.SGIPClientInitInfo;
import com.fortune.smgw.api.sgip.message.*;
import com.fortune.smgw.api.sgip.message.body.SgipCommandId;
import com.fortune.smgw.socket.server.ReceiveObj;
import com.fortune.smgw.socket.server.SocketServer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fortune.util.AppConfigurator;
import org.apache.log4j.Logger;

public class SGIPServer
        implements Runnable
{
    private Logger log = Logger.getLogger(SGIPServer.class);
    private String IP;
    private int port;
    private String userName;
    private String passWord;
    private boolean initFlag = false;

    public static List MSGID_QUEUE = Collections.synchronizedList(new LinkedList());
    public static Map LoginedChannel = Collections.synchronizedMap(new HashMap());
    private static SGIPServer singleton_instance = null;
    private SocketServer server;
    public static Map MSG_QUEUE = Collections.synchronizedMap(new HashMap());

    public static synchronized SGIPServer getInstance()
    {
        if (singleton_instance == null) {
            singleton_instance = new SGIPServer();
        }
        return singleton_instance;
    }

    public void init(SGIPServerInitInfo info) {
        try {
            if (!this.initFlag) {
                this.IP = info.IP;
                this.port = info.port;
                this.userName = info.userName;
                this.passWord = info.passWord;
                this.initFlag = true;
                this.log.info("SGIP server is init success!");
            } else {
                this.log.warn("SGIP server is aleady init,this init is not take effect");
            }
        }
        catch (Exception e) {
            this.log.error("SGIP server init  exception", e);
        }
    }

    public void start() throws UnknownHostException, IOException, ClassNotFoundException {
        if (this.initFlag) {
            this.server = new SocketServer(InetAddress.getByName(this.IP), this.port,
                    Class.forName("com.fortune.smgw.api.sgip.server.SGIPReqHandler"));
            this.log.info("SGIP server is start success!");
        } else {
            this.log.error("SGIP server is not init ,can`t start!");
        }
    }

    public String getUserName()
    {
        return this.userName;
    }

    public String getPassWord()
    {
        return this.passWord;
    }

    public void run()
    {
        while (true)
            try
            {
                Thread.currentThread(); Thread.sleep(50L);
            }
            catch (Exception localException)
            {
            }
    }

    public int MessageLength() {
        try {
            Thread.currentThread(); Thread.sleep(50L);
        }
        catch (Exception localException) {
        }
        return MSGID_QUEUE.size();
    }

    public SGIPRecieveMsg ReceiveMessage()
    {
        if (MSGID_QUEUE.size() > 0) {
            String msgid = (String)MSGID_QUEUE.remove(0);
            ReceiveObj obj = (ReceiveObj)MSG_QUEUE.remove(msgid);

            SGIPRecieveMsg message = new SGIPRecieveMsg();
            message.obj = obj.obj;
            if (obj.msgType == SgipCommandId.SGIP_DELIVER) {
                message.messageType = 1;
                SGIPDeliverResp resp = new SGIPDeliverResp();
                resp.getHead().setSequenceNo(((SGIPDeliver)message.obj).getHead().getSequenceNo());
                resp.getBody().setResult(0);
                this.server.send(obj.channel, resp.toByteArray());
                this.log.debug("send DeliverResp message:");
                this.log.debug(resp.toString());
            } else if (obj.msgType == SgipCommandId.SGIP_REPORT) {
                message.messageType = 2;
                SGIPReportResp resp = new SGIPReportResp();
                resp.getHead().setSequenceNo(((SGIPReport)message.obj).getHead().getSequenceNo());
                resp.getBody().setResult(0);
                this.server.send(obj.channel, resp.toByteArray());
                this.log.debug("send DeliverResp message:");
                this.log.debug(resp.toString());
            } else if (obj.msgType == SgipCommandId.SGIP_SUBMIT) {
                message.messageType = 3;
                SGIPSubmitResp resp = new SGIPSubmitResp();
                resp.getHead().setSequenceNo(((SGIPSubmit)message.obj).getHead().getSequenceNo());
                resp.getBody().setResult(0);
                this.server.send(obj.channel, resp.toByteArray());
                this.log.debug("send SubmitResp message:");
                this.log.debug(resp.toString());
            }
            return message;
        }
        return null;
    }
/*
    static{
        AppConfigurator sysConfig = AppConfigurator.getInstance();
        String sgipIp =sysConfig.getConfig("SGIP_IP","221.192.140.33");
        int sgipPort = sysConfig.getIntConfig("SGIP_PORT",9001);
        String sgipUser = sysConfig.getConfig("SGIP_USER","openhe");
        String sgipPassword = sysConfig.getConfig("SGIP_PASSWORD","123456");
        SGIPClientInitInfo info = new SGIPClientInitInfo();
        info.IP = sgipIp;
        info.port = sgipPort;
        info.userName = sgipUser;
        info.passWord = sgipPassword;
        info.maxLink =1;
//            info.IP = "10.130.83.207";
//            info.port = 5577;
//            info.userName = "zhjec";
//            info.passWord = "zhjec";
//            info.maxLink =1;
        SGIPClient client = SGIPClient.getInstance();
        client.init(info);
    }
*/
}