package com.fortune.smgw.api.sgip.message.body;

import com.fortune.smgw.api.common.ByteHandler;
import com.fortune.smgw.api.common.TypeConvert;
import java.nio.ByteBuffer;

public class SGIPReportBody
{
    private int length = 32;
    private SGIPSequenceNo submitSequenceNumber = new SGIPSequenceNo();
    private int reportType;
    private String userNumber;
    private int state;
    private int errorCode;
    private String reserve;

    public SGIPSequenceNo getSubmitSequenceNumber()
    {
        return this.submitSequenceNumber;
    }

    public void setSubmitSequenceNumber(SGIPSequenceNo submitSequenceNumber) {
        this.submitSequenceNumber = submitSequenceNumber;
    }

    public int getReportType() {
        return this.reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

    public String getUserNumber() {
        return this.userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getReserve() {
        return this.reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getLength());
        buffer.put(this.submitSequenceNumber.toByteBuffer());
        buffer.put((byte)this.reportType);
        buffer.put(TypeConvert.string2byte(this.userNumber, 21));
        buffer.put((byte)this.state);
        buffer.put((byte)this.errorCode);
        buffer.put(TypeConvert.string2byte(this.reserve, 8));
        return buffer.array();
    }

    public static SGIPReportBody parse(ByteBuffer buffer) {
        SGIPReportBody body = new SGIPReportBody();
        body.submitSequenceNumber = SGIPSequenceNo.parse(buffer);
        body.reportType = ByteHandler.readUIntByOneByte(buffer);
        body.userNumber = ByteHandler.readString(buffer, 21);
        body.state = ByteHandler.readUIntByOneByte(buffer);
        body.errorCode = ByteHandler.readUIntByOneByte(buffer);
        body.reserve = ByteHandler.readString(buffer, 8);

        return body;
    }

    public int getLength() {
        return this.length + this.submitSequenceNumber.getLength();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SubmitSeqNumber\t = " + this.submitSequenceNumber);
        sb.append("\nReportType\t = " + this.reportType);
        sb.append("\nUserNumber\t = " + (this.userNumber != null ? this.userNumber : ""));
        sb.append("\nState\t = " + this.state);
        sb.append("\nErrorCode\t = " + this.errorCode);
        sb.append("\nReserve\t\t = " + (this.reserve != null ? this.reserve : ""));
        return sb.toString();
    }
}