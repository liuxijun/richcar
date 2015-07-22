package com.fortune.smgw.api.sgip.message.body;

import java.nio.ByteBuffer;

public class SGIPMessageHead
{
    private int length = 8;
    private int messageLength;
    private int commandID;
    private SGIPSequenceNo sequenceNo = new SGIPSequenceNo();

    public int getMessageLength() {
        return this.messageLength;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

    public int getCommandID() {
        return this.commandID;
    }

    public void setCommandID(int commandID) {
        this.commandID = commandID;
    }

    public SGIPSequenceNo getSequenceNo() {
        return this.sequenceNo;
    }

    public void setSequenceNo(SGIPSequenceNo sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(this.length + this.sequenceNo.getLength());
        buffer.putInt(this.messageLength);
        buffer.putInt(this.commandID);
        buffer.put(this.sequenceNo.toByteBuffer());
        return buffer.array();
    }

    public static SGIPMessageHead parse(ByteBuffer buffer) {
        SGIPMessageHead body = new SGIPMessageHead();
        body.messageLength = buffer.getInt();
        body.commandID = buffer.getInt();
        body.sequenceNo = SGIPSequenceNo.parse(buffer);

        return body;
    }

    public int getLength() {
        return this.length + this.sequenceNo.getLength();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Message Length\t = " + this.messageLength);
        sb.append("\nCommand ID\t = " + this.commandID);
        sb.append("\nSequence Number\t = " + this.sequenceNo.toString());
        return sb.toString();
    }
}