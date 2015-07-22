package com.fortune.vac;

import com.fortune.midware.StartUp;
import com.fortune.util.AppConfigurator;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-10-12
 * Time: 下午2:47
 * 调试用的VAC服务器
 */
public class VacServer extends VacTcp {
    Logger logger = Logger.getLogger(getClass());
    private boolean shouldStop = false;
    private int vacPort ;
    private String bindIpAddress;
    public void stop(){
        shouldStop = true;
    }
    public void run(){

        ServerSocket sockServer;
        Socket sockRequest;
        Thread doThread;

        vacPort = AppConfigurator.getInstance().getIntConfig("vac.hostPort", 9001);
        bindIpAddress = AppConfigurator.getInstance().getConfig("vac.bindIp", "");
        try{
            if( bindIpAddress == null||"".equals(bindIpAddress.trim())){
                sockServer = new ServerSocket(vacPort);
            }else{
                InetAddress bindAddress;
                bindAddress = InetAddress.getByName( bindIpAddress );
                if(bindAddress != null){
                    sockServer = new ServerSocket(vacPort,10240,bindAddress);
                }else{
                    sockServer = new ServerSocket(vacPort);
                }
            }
            this.printAppInfo();
            while(!shouldStop){
                try{
                    sockRequest=sockServer.accept();
                    VacThread thread = new VacThread(sockRequest,this);
                    thread.start();
                }catch(Exception e){
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
        logger.info("||       Welcome to FortuneNet VAC Server!                      ||");
        logger.info("||                 Version: 5.0                                 ||");
        logger.info("||                run time: "+formater.format(new java.util.Date())+"                 ||");
        logger.info("||           Lisening Port: " + vacPort +"                                ||");
        logger.info("||            Bind Address: " + bindIpAddress + "                       ||");
        logger.info("\\*[]==========================================================[]*/");
    }

    public static void main(String[] args) {
        VacServer startUp = new VacServer();
        startUp.run();
        try{
            Thread.sleep(1000);
        }catch(Exception e){
            e.printStackTrace();
        }

    }}
