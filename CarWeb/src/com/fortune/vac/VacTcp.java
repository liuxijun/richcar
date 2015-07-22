package com.fortune.vac;

import com.fortune.util.AppConfigurator;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-10-12
 * Time: ����2:48
 * TCP����ͨѶģ��
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
    protected int handsetInterval; //�����ź�ʱ����
    protected int timeoutSeconds;//��ʱʱ��
    protected int retryTimes;       //��ʱ���Դ���
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
            logger.error("�޷�д���ݣ�"+e.getMessage());
        }
    }
    public void init(Socket socket){
        try {
            socketInStream = socket.getInputStream();
            sockIn = new BufferedReader(new InputStreamReader(socketInStream));
            sockOut = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
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
            logger.error("�޷���ʼ��"+hostIp+":"+hostPort+","+e.getMessage());
            return false;
        }
        return true;
    }

    public boolean close() {
        try {
            //�ر����ӣ�ԭ��������������Զ��ر�
            sockRequest.close();
            return true;
        } catch (Exception e) {
            logger.error("�޷��ر�Socket��"+e.getMessage());
            return false;
        }
    }
    public byte[] sendToServer(MessageBody messageBody){
        if(!connected){
            logger.error("��δ���ӵ�VAC��֤ƽ̨��");
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
