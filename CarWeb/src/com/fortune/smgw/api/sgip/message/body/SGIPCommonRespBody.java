package com.fortune.smgw.api.sgip.message.body;

import com.fortune.smgw.api.common.ByteHandler;
import com.fortune.smgw.api.common.TypeConvert;
import java.nio.ByteBuffer;

public class SGIPCommonRespBody
{
    private int length = 9;
    private int result;
    private String reserve;

    public int getResult()
    {
        return this.result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getReserve() {
        return this.reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(this.length);
        buffer.put((byte)this.result);
        buffer.put(TypeConvert.string2byte(this.reserve, 8));
        return buffer.array();
    }

    public static SGIPCommonRespBody parse(ByteBuffer buffer) {
        SGIPCommonRespBody body = new SGIPCommonRespBody();
        body.result = buffer.get();

        body.reserve = ByteHandler.readString(buffer, 8);

        return body;
    }

    public int getLength() {
        return this.length;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Result\t\t = " + this.result);
        sb.append("\nReserve\t\t = " + (this.reserve != null ? this.reserve : ""));
        return sb.toString();
    }
}