package com.fortune.smgw.api.sgip.server;

import com.fortune.util.AppConfigurator;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-3-16
 * Time: 下午6:21
 * SGIP服务器
 */
public class Startup {
    private Logger logger = Logger.getLogger(getClass());
    private String bindAddr;
    private int port;
    private String userId ;
    private String userPassword;
    private boolean shouldStop = false;
    public Startup(String ip,int port,String uid,String pwd){
         this.bindAddr = ip;
        this.port = port;
        this.userId = uid;
        this.userPassword = pwd;
    }
/*
    public Startup(){
        AppConfigurator conf = AppConfigurator.getInstance();
        bindAddr = null;
        port = conf.getIntConfig("cdn.gslb.port", -1);
    }
*/
    public void start(){
        SGIPServerInitInfo info = new SGIPServerInitInfo();
        info.IP = bindAddr;
        info.port = port;
        info.userName = userId;
        info.passWord = userPassword;

        SGIPServer server = SGIPServer.getInstance();
        server.init(info);
        try {
            logger.info("准备开始监听"+bindAddr+":"+port);
            server.start();
        } catch (IOException e) {
            logger.error("发生IOException异常：" + e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.error("发生ClassNotFoundException异常："+e.getMessage());
        }
    }
    public void stop(){
        shouldStop = true;
        //SGIPServer server = SGIPServer.getInstance();

    }

    public static void main(String[] args){
        AppConfigurator config = AppConfigurator.getInstance();
        String bindIp = config.getConfig("SGIP_SERVER_BIND_IP","61.55.144.87");
        int bindPort = config.getIntConfig("SGIP_SERVER_BIND_PORT",6888);
        String sgipUser = config.getConfig("SGIP_SERVER_USER","openhe");
        String sgipPwd = config.getConfig("SGIP_SERVER_PWD","1qazQ2WE");
        Startup startup = new Startup(bindIp,bindPort,sgipUser,sgipPwd);
        startup.start();
    }
}
