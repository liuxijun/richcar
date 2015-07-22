package com.fortune.vac;

import com.fortune.util.AppConfigurator;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-10-12
 * Time: 下午2:48
 * TCP基本通讯模块
 */
public class VacTcp {
    protected Socket sockRequest = null;//new java.net.Socket( MiddleServerAddress, MiddleServerPort );
    protected BufferedReader sockIn = null;
    protected InputStream socketInStream;
    protected PrintStream sockOut = null;
    protected Logger logger = Logger.getLogger(getClass());
    protected boolean connected=false;
    protected String vacHost;
    protected int vacPort;
    protected int handsetInterval; //握手信号时间间隔
    protected int timeoutSeconds;//超时时间
    protected int retryTimes;       //超时重试次数
    public VacTcp(){
        AppConfigurator config = AppConfigurator.getInstance();
        vacHost = config.getConfig("vac.hostIp","127.0.0.1");
        vacPort = config.getIntConfig("vac.hostPort",8200);
        handsetInterval = config.getIntConfig("vac.handsetInterval",60);
        retryTimes = config.getIntConfig("vac.retryTimes",2);
        timeoutSeconds = config.getIntConfig("vac.timeoutSeconds",10);
    }
    public int read(byte[] dataBuffer){
        try {
            return socketInStream.read(dataBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void write(byte[] dataBuffer){
        try {
            sockOut.write(dataBuffer);
        } catch (IOException e) {
            logger.error("无法写数据："+e.getMessage());
        }
    }
    public void init(Socket socket){
        try {
            socketInStream = socket.getInputStream();
            sockIn = new BufferedReader(new InputStreamReader(socketInStream));
            sockOut = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
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
            logger.error("无法初始化"+hostIp+":"+hostPort+","+e.getMessage());
            return false;
        }
        return true;
    }

    public boolean close() {
        try {
            //关闭连接，原来的输入输出会自动关闭
            sockRequest.close();
            return true;
        } catch (Exception e) {
            logger.error("无法关闭Socket："+e.getMessage());
            return false;
        }
    }
    public byte[] sendToServer(MessageBody messageBody){
        if(!connected){
            logger.error("尚未连接到VAC认证平台！");
        }
        Pdu pdu = new Pdu();
        pdu.setMessageBody(messageBody);
        byte[] message = pdu.buildBuffer();
        write(message);
        byte[] resultBuffer =new byte[512*1024];
        int dataLength = read(resultBuffer);
        byte[] buffer = new byte[dataLength];
        System.arraycopy(resultBuffer,0,buffer,0,dataLength);
        return buffer;
    }

}
