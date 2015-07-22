package com.fortune.smgw.socket.client;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-3-16
 * Time: ����8:38
 * �ͻ��˷����߳�
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
                                log.debug("������������...");
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
                                        log.error(" socketClientLink.sendData�޷��Ƴ���һ���б�Ԫ�أ�"+e.getMessage());
                                    }
                                }else{
                                    log.warn("socketClientLink.sendData�б����Ѿ���0���޷������Ƴ�������");
                                }
                                this.log.debug("message sending");
                            } else {
                                this.log.error("�����̣߳���·δ����");
                                errorHappend = true;
                            }
                        } catch (Exception e) {
                            errorHappend = true;
                            log.error("���������쳣��"+e.getMessage(),e);
                        }
                        if(errorHappend){
                            try {
                                socket.finishConnect();
                                socket.close();
                            } catch (Exception ex) {
                                this.log.error("Link close exception", ex);
                            }
                            //��¼�������³���һ��

                            link.setStatus(0);
                            log.debug("���ӳ������⣬���½������ӣ�");
                            link.initiateConnection();
                            link.connectHandler.waitResponse();
                            int status = link.getStatus();
                            if(status==1){
                                log.debug("���ӳɹ���");
                            }
                            link.login();
                            link.connectHandler.waitResponse();
                            status = link.getStatus();
                            if(status!=3){
                                log.debug("���µ�¼�ɹ�������������");
                                continue;
                            }else if(status==-2){
                                log.error("��¼ʧ�ܣ�");
                            }else if(status==1){
                                log.error("�����ѶϿ���");
                            }else if(status==-1){
                                log.error("��ȡ��Ϣ�쳣��");
                            }else{
                                log.error("��¼��״̬δ֪��"+status);
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