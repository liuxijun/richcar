package com.fortune.smgw.socket.client;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-3-16
 * Time: 上午8:38
 * 客户端发送线程
 */

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

public class ClientSendThread
        implements Runnable {
    private Logger log = Logger.getLogger(ClientSendThread.class);
    private final Thread m_SendThread;
    private int linkId;

    public ClientSendThread(int linkId) {
        this.linkId = linkId;
        this.m_SendThread = new Thread(this);
        this.m_SendThread.start();
    }

    public void run() {
        while (true) {
            SocketClientLink link = SocketClient.Links[this.linkId];
            if (link != null)
                while (link.sendData.size() > 0) {
                    ByteBuffer buffer = (ByteBuffer) link.sendData.get(0);
                    this.log.debug("get 1 message to send");
                    if (buffer != null){
                        boolean errorHappend = false;
                        SocketClientLink socketClientLink = SocketClient.Links[linkId];
                        SocketChannel socket = socketClientLink.getSocket();
                        try {
/*
                            if(!socket.isConnected()){
                                log.debug("尝试重新连接...");
                                socketClientLink.initiateConnection();
                                socketClientLink.connectHandler.waitResponse();
                            }
*/
                            if (socket.isConnected()) {
                                socket.write(buffer);
                                if(socketClientLink.sendData.size()>0){
                                    try {
                                        socketClientLink.sendData.remove(0);
                                    } catch (Exception e) {
                                        log.error(" socketClientLink.sendData无法移除第一个列表元素："+e.getMessage());
                                    }
                                }else{
                                    log.warn("socketClientLink.sendData列表长度已经是0，无法进行移除操作！");
                                }
                                this.log.debug("message sending");
                            } else {
                                this.log.error("发送线程：链路未连接");
                                errorHappend = true;
                            }
                        } catch (Exception e) {
                            errorHappend = true;
                            log.error("发生网络异常："+e.getMessage(),e);
                        }
                        if(errorHappend){
                            try {
                                socket.finishConnect();
                                socket.close();
                            } catch (Exception ex) {
                                this.log.error("Link close exception", ex);
                            }
                            //登录出错，重新尝试一下

                            link.setStatus(0);
                            log.debug("连接出现问题，重新建立连接！");
                            link.initiateConnection();
                            link.connectHandler.waitResponse();
                            int status = link.getStatus();
                            if(status==1){
                                log.debug("连接成功！");
                            }
                            link.login();
                            link.connectHandler.waitResponse();
                            status = link.getStatus();
                            if(status!=3){
                                log.debug("重新登录成功，继续操作！");
                                continue;
                            }else if(status==-2){
                                log.error("登录失败！");
                            }else if(status==1){
                                log.error("连接已断开！");
                            }else if(status==-1){
                                log.error("读取消息异常！");
                            }else{
                                log.error("登录后，状态未知："+status);
                            }
                            try {
                                Thread.sleep(5L);
                            } catch (InterruptedException e) {
                                log.error(e);
                            }
                            //buffer.clear();
                            //SocketClient.Links[this.linkId].sendData.clear();
                            //break;
                        }
                    }
                }
            try {
                Thread.sleep(1L);
            } catch (Exception e) {
                this.log.error("m_SendThread sleep Exceiption", e);
            }
        }
    }
}