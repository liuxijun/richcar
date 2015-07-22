package com.fortune.smgw.api.sgip.message.body;

import com.fortune.smgw.api.sgip.SGIPConfig;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SGIPSequenceNo
{
    private int length = 12;
    private int node;
    private int time;
    private int number;

    public int getNode()
    {
        return this.node;
    }

    public void setNode(int node) {
        this.node = node;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public SGIPSequenceNo() {
        this.number = SGIPSequenceNoHandler.getInstance().getNumeber();
        SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");

        String tmp = sdf.format(new Date());
        this.time = Integer.parseInt(tmp);
        this.node = SGIPConfig.getInstance().NODE_ID;
    }

    public byte[] toByteBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(this.length);
        buffer.putInt(this.node);
        buffer.putInt(this.time);
        buffer.putInt(this.number);

        return buffer.array();
    }

    public static SGIPSequenceNo parse(ByteBuffer buffer) {
        SGIPSequenceNo body = new SGIPSequenceNo();
        body.node = buffer.getInt();
        body.time = buffer.getInt();
        body.number = buffer.getInt();

        return body;
    }

    public int getLength() {
        return this.length;
    }

    public String toString() {
        String str = String.valueOf(this.node) + " " + String.valueOf(this.time) + " " + String.valueOf(this.number);
        return str;
    }
}