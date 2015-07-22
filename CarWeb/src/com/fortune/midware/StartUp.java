package com.fortune.midware;

import com.fortune.util.config.Config;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;

public class StartUp {
    Logger logger = Logger.getLogger(getClass());
    private boolean shouldStop = false;
    public void stop(){
        shouldStop = true;
    }
    public void run(){

        ServerSocket sockServer;
        Socket sockRequest;
        Thread doThread;

        int ServerPort = Config.getIntValue("midware.port",8200);
        String sBindAddress = Config.getStrValue("midware.ip","");
        try{
            if( sBindAddress == null||"".equals(sBindAddress.trim())){
                sockServer = new ServerSocket(ServerPort);
            }else{
                InetAddress bindAddress;
                bindAddress = InetAddress.getByName( sBindAddress );
                if(bindAddress != null){
                    sockServer = new ServerSocket(ServerPort,10240,bindAddress);
                }else{
                    sockServer = new ServerSocket(ServerPort);
                }
            }
            this.printAppInfo();
            while(!shouldStop){
                try{
                    if (DoThread.lThreadNumber < 2000){
                        sockRequest=sockServer.accept();
                        doThread = new DoThread(sockRequest,this);
                        doThread.start();
                    } else {
                        Thread.sleep(10);
                    }
                }catch(Exception e){
                    logger.error("线程超过2000,总:"+DoThread.lThreadNumber);
                    e.printStackTrace();
                }
                if(shouldStop){
                    break;
                }
            }
        }catch(IOException e){
            logger.error("Start service error:"+e.getMessage());
        }

    }
    private void printAppInfo(){
        java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("/*[]==========================================================[]*\\");
        logger.info("||       Welcome to FortuneNet Middle Layer Server!             ||");
        logger.info("||                 Version: 5.0                                 ||");
        logger.info("||                run time: "+formater.format(new java.util.Date())+"                 ||");
        logger.info("||           Lisening Port: " + Config.getIntValue("midware.port",0) +"                                ||");
        logger.info("||            Bind Address: " + Config.getStrValue("midware.ip","") + "                       ||");
        logger.info("\\*[]==========================================================[]*/");
    }

    public static void main(String[] args) {
        StartUp startUp = new StartUp();
        startUp.run();
        try{
            Thread.sleep(1000);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}