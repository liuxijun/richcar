package com.fortune.smgw.api.sgip.message.body;

import com.fortune.smgw.api.common.ByteHandler;
import com.fortune.smgw.api.common.TypeConvert;
import java.nio.ByteBuffer;

public class SGIPDeliverBody
{
    private static int length = 57;
    private String userNumber;
    private String sPNumber;
    private int tP_pid;
    private int tP_udhi;
    private int messageCoding;
    private int messageLength;
    private byte[] messageContent;
    private String reserve;

    public String getUserNumber()
    {
        return this.userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getSPNumber() {
        return this.sPNumber;
    }

    public void setSPNumber(String number) {
        this.sPNumber = number;
    }

    public int getTP_pid() {
        return this.tP_pid;
    }

    public void setTP_pid(int tp_pid) {
        this.tP_pid = tp_pid;
    }

    public int getTP_udhi() {
        return this.tP_udhi;
    }

    public void setTP_udhi(int tp_udhi) {
        this.tP_udhi = tp_udhi;
    }

    public int getMessageCoding() {
        return this.messageCoding;
    }

    public void setMessageCoding(int messageCoding) {
        this.messageCoding = messageCoding;
    }

    public int getMessageLength() {
        return this.messageLength;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

    public byte[] getMessageContent() {
        return this.messageContent;
    }

    public void setMessageContent(byte[] messageContent) {
        this.messageContent = messageContent;
    }

    public String getReserve() {
        return this.reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getLength());
        buffer.put(TypeConvert.string2byte(this.userNumber, 21));
        buffer.put(TypeConvert.string2byte(this.sPNumber, 21));
        buffer.put((byte)this.tP_pid);
        buffer.put((byte)this.tP_udhi);
        buffer.put((byte)this.messageCoding);
        buffer.put((byte)this.messageLength);
        buffer.put(this.messageContent);
        buffer.put(TypeConvert.string2byte(this.reserve, 8));
        return buffer.array();
    }

    public static SGIPDeliverBody parse(ByteBuffer buffer) {
        int bufferSize = buffer.capacity();
        SGIPDeliverBody body = new SGIPDeliverBody();
        body.userNumber = ByteHandler.readString(buffer, 21);
        body.sPNumber = ByteHandler.readString(buffer, 21);

        body.tP_pid = ByteHandler.readUIntByOneByte(buffer);
        body.tP_udhi = ByteHandler.readUIntByOneByte(buffer);
        body.messageCoding = ByteHandler.readUIntByOneByte(buffer);
        body.messageLength = buffer.getInt();

        body.messageContent = new byte[bufferSize - length - 20];
        buffer.get(body.messageContent);

        body.reserve = ByteHandler.readString(buffer, 8);

        return body;
    }

    public int getLength() {
        return length + this.messageContent.length;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("UserNumber\t = " + (this.userNumber != null ? this.userNumber : ""));
        sb.append("\nSPNumber\t = " + (this.sPNumber != null ? this.sPNumber : ""));
        sb.append("\nTP_pid\t\t = " + this.tP_pid);
        sb.append("\nTP_udhi\t\t = " + this.tP_udhi);
        sb.append("\nMessageCoding\t = " + this.messageCoding);
        sb.append("\nMessageLength\t = " + this.messageLength);
        sb.append("\nMessageContent\t = " + TypeConvert.byte2HexString(this.messageContent));
        sb.append("\nReserve\t\t = " + (this.reserve != null ? this.reserve : ""));
        return sb.toString();
    }
}