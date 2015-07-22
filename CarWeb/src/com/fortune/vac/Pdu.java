package com.fortune.vac;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-8-28
 * Time: 下午2:24
 * 数据基本结构
 */
public class Pdu {
    private MessageHeader messageHeader=new MessageHeader();
    private MessageBody messageBody;

    public Pdu(){

    }
    public Pdu(MessageBody body){
        this.messageBody = body;
    }
    public MessageHeader getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(MessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(MessageBody messageBody) {
        if(messageBody!=null)  messageHeader.setCommandId(messageBody.getCommandId());
        this.messageBody = messageBody;
    }
    public String toString(){
        return "Pdu{\r\n"+messageHeader.toString()+"\r\n"+messageBody.toString()+"}\r\n";
    }
    public byte[] buildBuffer(){
        if(messageBody!=null){
            byte[] buffer = messageBody.buildBuffer();
            messageHeader.setTotalLength(buffer.length+12);
            messageHeader.setCommandId(messageBody.getCommandId());
            byte[] result = new byte[messageHeader.getTotalLength()];
            BaseMessage.write_32(result,messageHeader.getTotalLength(),0);
            BaseMessage.write_32(result,messageBody.getCommandId(),4);
            BaseMessage.write_32(result,messageHeader.getSequenceId(),8);
            if(buffer.length>0){
                System.arraycopy(buffer,0,result,12,buffer.length);
            }
            return result;
        }else{
            messageHeader.setTotalLength(12);
            byte[] result = new byte[messageHeader.getTotalLength()];
            BaseMessage.write_32(result,messageHeader.getTotalLength(),0);
            BaseMessage.write_32(result,messageHeader.getCommandId(),4);
            BaseMessage.write_32(result,messageHeader.getSequenceId(),8);
            return result;
        }
    }

}
