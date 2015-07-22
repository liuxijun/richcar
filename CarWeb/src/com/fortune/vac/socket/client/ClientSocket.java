package com.fortune.vac.socket.client;


import com.fortune.vac.*;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-6-2
 * Time: 下午3:18
 * 一个独立的线程，用以维护和处理发送数据队列
 */
public class ClientSocket {
    private ClientSocketThread clientSocketThread;
    private static ClientSocket ourInstance = new ClientSocket();

    public static ClientSocket getInstance() {
        return ourInstance;
    }

    private ClientSocket() {
        clientSocketThread = new ClientSocketThread();
        clientSocketThread.start();
    }

    public boolean shutdown(){
        return clientSocketThread.shutdown();
    }

    public int checkPriceConfirm(String phoneNumber,String productId){
        return clientSocketThread.checkPriceConfirm(phoneNumber, productId);
    }

    public CheckPriceMessage getBaseCheckPriceMessage(String spId,String phoneNumber,String productId,long operationType){
        return clientSocketThread.getBaseCheckPriceMessage(spId,phoneNumber,productId,operationType);
    }

    public int checkPrice(String phoneNumber,String productId,long operationType,String spId){
        return clientSocketThread.checkPrice(phoneNumber,productId, operationType, spId);
    }

    public byte[] sendToServer(MessageBody message){
        return clientSocketThread.sendAndWait(message);
    }

    public boolean bind(){
        return clientSocketThread.bind();
    }

    public boolean isBindSuccess(){
        return clientSocketThread.isBindSuccess();
    }

    public boolean unBind(){
        return clientSocketThread.unBind();
    }

    public boolean handset(){
        return clientSocketThread.handset(false);
    }

    public CheckPriceResp checkPriceResp(CheckPriceMessage message){
        byte[] resp = clientSocketThread.sendAndWait(message);
        if(resp!=null){
            MessageHeader header = new MessageHeader(resp);
            if(header.getCommandId()== Command.CMDID_CheckPriceResp){
                return new CheckPriceResp(resp);
            }
        }
        return null;
    }
    public boolean startup(){
        return clientSocketThread.startup();
    }
    public static void main(String[] args){

    }
}
