package com.fortune.util.net;

import com.fortune.util.AppConfigurator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-2-21
 * Time: 9:57:42
 * TCP的一个服务器
 */
public class TcpServer {
    private String bindAddress;
    private int port;
    private int threadPoolNumber;
    private boolean shouldStop;
//    private ServerHandler handler;
    public TcpServer(String bindAddress, int port, int threadPoolNumber) {
        this.bindAddress = bindAddress;
        this.port = port;
//        this.handler = handler;
        if (threadPoolNumber > 0) {
            this.threadPoolNumber = threadPoolNumber;
        } else {
            this.threadPoolNumber = AppConfigurator.getInstance().getIntConfig("tcpserver.poolSize", 20);
        }
    }

    public String getBindAddress() {
        return bindAddress;
    }

    public void setBindAddress(String bindAddress) {
        this.bindAddress = bindAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getThreadPoolNumber() {
        return threadPoolNumber;
    }

    public void setThreadPoolNumber(int threadPoolNumber) {
        this.threadPoolNumber = threadPoolNumber;
    }

    public void startup() {
        ServerSocket serverSocket = null;
        try {
            if (bindAddress != null) {
                InetAddress bindAddr = InetAddress.getByName(bindAddress);
                serverSocket = new ServerSocket(port, 0, bindAddr);
            } else {
                serverSocket = new ServerSocket(port);
            }
            Socket clientRequest = null;
            ExecutorService es = Executors.newFixedThreadPool(threadPoolNumber);
            shouldStop = false;
            while(!shouldStop){
                Runnable task = new SocketReader(serverSocket.accept());
                es.execute(task);
            }
            es.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        shouldStop = true;
    }
    public void execute(BaseProtocolContext context) {
        String result = "500 Internal Error!";
    }
    public void onConnected(InputStream inputStream, OutputStream outputStream){

    }
    public class SocketReader implements Runnable{
        private Socket socket;
        public SocketReader(Socket socket){
            this.socket = socket;
        }
        public void run(){
            InputStream is = null;
            OutputStream os = null;
            BaseProtocolContext context=new BaseProtocolContext();
            try {
                is = socket.getInputStream();
                os = socket.getOutputStream();
                context.onConnected(is,os);
                execute(context);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(is!=null){

                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if(os!=null){
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace(); 
                }
            }

        }
    }
}
