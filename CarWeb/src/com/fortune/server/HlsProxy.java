package com.fortune.server;

import com.fortune.common.Constants;
import com.fortune.server.message.ServerMessager;
import com.fortune.server.protocol.HttpProcessor;
import com.fortune.util.AppConfigurator;
import com.fortune.util.ParamUtils;
import com.fortune.util.StringUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 14-1-22
 * Time: 上午9:29
 * LiveHttp的测试代理，用来检查是否是Chuck模式问题
 */
public class HlsProxy  implements HttpHandler {
    private boolean stopNow = false;
    private Logger logger = Logger.getLogger(getClass());

    private int serverPort;
    private String serverIp;
    private String baseDir;
    private boolean isThisServerTracker;
    private boolean running = false;

    @SuppressWarnings("unused")
    public void setTracker(boolean isThisServerTracker) {
        this.isThisServerTracker = isThisServerTracker;
    }

    public void start(){
        logger.debug("执行启动命令!当前状态：running="+running);
        run();
    }

    public void stop(){
        ServerMessager messager= new ServerMessager();
        AppConfigurator config = AppConfigurator.getInstance();
        logger.debug("执行停止命令：\n"+messager.getMessage("127.0.0.1",
                config.getIntConfig("system.proxy.hlsPort",8080),"ep-get/manage?command=stop",null));
        this.stopNow = true;
    }

    public HlsProxy(String serverIp, int serverPort, String baseDir,boolean isTracker) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.baseDir = baseDir;
        this.isThisServerTracker = isTracker;
    }

    public void setStop() {
        stopNow = true;
    }

    public void handle(HttpExchange hx) {
        HttpProcessor hp = new HttpProcessor(this,hx);
        hp.start();
    }
    public String getDisplayMessage(String[] messages){
        String result = "";
        int lineLength = 50;
        int start = 10;
        String leftTop = "*";
        String leftBottom = "*";
        String rightTop = "*";
        String rightBottom = "*";
        String top = "=";
        String left = "|";
        String right = "|";
        String bottom = "=";
        String space = " ";
        result +=getLine(leftTop,top,"",rightTop,lineLength,start);
        result +=getLine(left,space,"",right,lineLength,start);
        for(String msg:messages){
           result += getLine(left,space,msg,right,lineLength,start);
        }
        result +=getLine(left,space,"",right,lineLength,start);
        result +=getLine(leftBottom,bottom,"",rightBottom,lineLength,start);
        return result;
    }

    public String getLine(String head,String space,String msg,String tail,int maxLength,int start){
        String result = head;
        while(result.length()<start){
            result+=space;
        }
        result +=msg;
        while(result.length()<maxLength){
            result += space;
        }
        return result + tail+"\n";
    }
    public void run() {
        try {
            running = true;
            AppConfigurator appConfig = AppConfigurator.getInstance();
            if (baseDir == null) {
                baseDir = appConfig.getConfig("p2p.local.dir", "");
            }
            String bindIp = serverIp;
            if (bindIp == null || "".equals(bindIp)) {
                bindIp = appConfig.getConfig("p2p.peer.local.ip", null);
            }
            //logger.info("/**       本地根目录:" + baseDir +"              **\\");

            int bindPort = serverPort;
            if (bindPort <= 0) {
                bindPort = appConfig.getIntConfig("system.proxy.hlsPort", 8080);
            }
            InetSocketAddress bindAddress;
            //bindAddress = InetAddress.getByName(bindIp);
            try {
                if (bindIp == null) {
                    bindAddress = new InetSocketAddress(bindPort);
                    bindIp = "0.0.0.0";
                } else {
                    bindAddress = new InetSocketAddress(bindIp, bindPort);
                }
                HttpServerProvider httpServerProvider = HttpServerProvider.provider();
                HttpServer httpServer = httpServerProvider.createHttpServer(bindAddress,
                        appConfig.getIntConfig("system.hls.proxy.webServerThreadCount", 128));
                httpServer.setExecutor(null);
                stopNow = false;
                httpServer.createContext("/", this);
                httpServer.start();
                logger.info("\n"+getDisplayMessage(new String[]{
                        "HLS Proxy Server Version 0.5",
                        "Listen    "+bindIp+":"+bindPort,
                        "Startup   "+StringUtils.date2string(new Date())}));
                //启动Spring的定时
                /*Constants.SPRING_APPLICATION_CONTEXT = new ClassPathXmlApplicationContext(
                        new String[]{"springCrontab.xml"});
                */

                while (!stopNow) {
                    Thread.sleep(1000);
                }
                logger.info("准备停止服务.......");
                Thread.sleep(500);
                httpServer.stop(0);
                logger.info("服务停止");
            } catch (IOException e) {
                logger.error("Start service error:" + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Start service error:" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String serverIp;
        String baseDir;
        int serverPort;
        Map<String, String> parameters = new HashMap<String, String>();
        String command = null;
        if (args != null) {
            parameters = ParamUtils.getCmdParameters(args);
            if(args.length>0){
                command = args[0];
            }
        }
        serverPort = StringUtils.string2int(parameters.get("port"), -1);
        baseDir = parameters.get("baseDir");
        serverIp = parameters.get("bindIp");

        HlsProxy proxyServer = new HlsProxy(serverIp, serverPort, baseDir,false);
        //PeerServer ps = new PeerServer();
        if("stop".equals(command)){
            proxyServer.stop();
        }else{
            proxyServer.start();
        }
    }
}
