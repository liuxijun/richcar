package com.fortune.smgw.api.sgip.message.body;

import com.fortune.smgw.api.common.ByteHandler;
import com.fortune.smgw.api.common.TypeConvert;
import com.fortune.smgw.api.sgip.SGIPConfig;
import java.nio.ByteBuffer;

public class SGIPSubmitBody
{
    private static int length = 144;
    private String sPNumber;
    private String chargeNumber;
    private int userCount;
    private String userNumber;
    private String corpId;
    private String serviceType;
    private int feeType;
    private String feeValue;
    private String givenValue;
    private int agentFlag;
    private int morelatetoMTFlag;
    private int priority;
    private String expireTime;
    private String scheduleTime;
    private int reportFlag;
    private int tP_pid;
    private int tP_udhi;
    private int messageCoding;
    private int messageType;
    private int messageLength;
    private byte[] messageContent;
    private String reserve;

    public String getSPNumber()
    {
        return this.sPNumber;
    }

    public void setSPNumber(String number) {
        this.sPNumber = number;
    }

    public String getChargeNumber() {
        return this.chargeNumber;
    }

    public void setChargeNumber(String chargeNumber) {
        this.chargeNumber = chargeNumber;
    }

    public int getUserCount() {
        return this.userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public String getUserNumber() {
        return this.userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getCorpId() {
        return this.corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getServiceType() {
        return this.serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public int getFeeType() {
        return this.feeType;
    }

    public void setFeeType(int feeType) {
        this.feeType = feeType;
    }

    public String getFeeValue() {
        return this.feeValue;
    }

    public void setFeeValue(String feeValue) {
        this.feeValue = feeValue;
    }

    public String getGivenValue() {
        return this.givenValue;
    }

    public void setGivenValue(String givenValue) {
        this.givenValue = givenValue;
    }

    public int getAgentFlag() {
        return this.agentFlag;
    }

    public void setAgentFlag(int agentFlag) {
        this.agentFlag = agentFlag;
    }

    public int getMorelatetoMTFlag() {
        return this.morelatetoMTFlag;
    }

    public void setMorelatetoMTFlag(int morelatetoMTFlag) {
        this.morelatetoMTFlag = morelatetoMTFlag;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getExpireTime() {
        return this.expireTime;
    }

    public void setExpireTime(String expireTime)
    {
        this.expireTime = expireTime;
    }

    public String getScheduleTime() {
        return this.scheduleTime;
    }

    public void setScheduleTime(String scheduleTime)
    {
        this.scheduleTime = scheduleTime;
    }

    public int getReportFlag() {
        return this.reportFlag;
    }

    public void setReportFlag(int reportFlag) {
        this.reportFlag = reportFlag;
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

    public int getMessageType() {
        return this.messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
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
        this.messageLength = messageContent.length;
    }

    public String getReserve() {
        return this.reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getLength());
        buffer.put(TypeConvert.string2byte(this.sPNumber, 21));
        buffer.put(TypeConvert.string2byte(this.chargeNumber, 21));
        buffer.put((byte)this.userCount);
        buffer.put(TypeConvert.string2byte(this.userNumber, 21));
        buffer.put(TypeConvert.string2byte(this.corpId, 5));
        buffer.put(TypeConvert.string2byte(this.serviceType, 10));
        buffer.put((byte)this.feeType);
        buffer.put(TypeConvert.string2byte(this.feeValue, 6));
        buffer.put(TypeConvert.string2byte(this.givenValue, 6));
        buffer.put((byte)this.agentFlag);
        buffer.put((byte)this.morelatetoMTFlag);
        buffer.put((byte)this.priority);
        if ((this.expireTime != null) && (!"".equals(this.expireTime)))
            buffer.put(TypeConvert.string2byte(this.expireTime + SGIPConfig.SGIP_TIME_EXT, 16));
        else {
            buffer.put(TypeConvert.string2byte(this.expireTime, 16));
        }
        if ((this.scheduleTime != null) && (!"".equals(this.scheduleTime)))
            buffer.put(TypeConvert.string2byte(this.scheduleTime + SGIPConfig.SGIP_TIME_EXT, 16));
        else {
            buffer.put(TypeConvert.string2byte(this.scheduleTime, 16));
        }
        buffer.put((byte)this.reportFlag);
        buffer.put((byte)this.tP_pid);
        buffer.put((byte)this.tP_udhi);
        buffer.put((byte)this.messageCoding);
        buffer.put((byte)this.messageType);
        buffer.putInt(this.messageLength);
        buffer.put(this.messageContent);
        buffer.put(TypeConvert.string2byte(this.reserve, 8));
        return buffer.array();
    }

    public static SGIPSubmitBody parse(ByteBuffer buffer) {
        int bufferSize = buffer.capacity();
        SGIPSubmitBody body = new SGIPSubmitBody();

        body.sPNumber = ByteHandler.readString(buffer, 21);

        body.chargeNumber = ByteHandler.readString(buffer, 21);

        body.userCount = ByteHandler.readUIntByOneByte(buffer);

        body.userNumber = ByteHandler.readString(buffer, 21);

        body.corpId = ByteHandler.readString(buffer, 5);

        body.serviceType = ByteHandler.readString(buffer, 10);

        body.feeType = ByteHandler.readUIntByOneByte(buffer);

        body.feeValue = ByteHandler.readString(buffer, 6);

        body.givenValue = ByteHandler.readString(buffer, 6);

        body.agentFlag = ByteHandler.readUIntByOneByte(buffer);
        body.morelatetoMTFlag = ByteHandler.readUIntByOneByte(buffer);
        body.priority = ByteHandler.readUIntByOneByte(buffer);

        body.expireTime = ByteHandler.readString(buffer, 16);

        body.scheduleTime = ByteHandler.readString(buffer, 16);

        body.reportFlag = ByteHandler.readUIntByOneByte(buffer);
        body.tP_pid = ByteHandler.readUIntByOneByte(buffer);
        body.tP_udhi = ByteHandler.readUIntByOneByte(buffer);
        body.messageCoding = ByteHandler.readUIntByOneByte(buffer);
        body.messageType = ByteHandler.readUIntByOneByte(buffer);

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
        StringBuilder sb = new StringBuilder();
        sb.append("SPNumber\t = ").append(this.sPNumber != null ? this.sPNumber : "");
        sb.append("\nChargeNumber\t = ").append(this.chargeNumber != null ? this.chargeNumber : "");
        sb.append("\nUserCount\t = ").append(this.userCount);
        sb.append("\nUserNumber\t = ").append(this.userNumber != null ? this.userNumber : "");
        sb.append("\nCorpId\t = ").append(this.corpId != null ? this.corpId : "");
        sb.append("\nServiceType\t = ").append(this.serviceType != null ? this.serviceType : "");
        sb.append("\nFeeType\t = ").append(this.feeType);
        sb.append("\nFeeValue\t = ").append(this.feeValue);
        sb.append("\nGivenValue\t = ").append(this.givenValue != null ? this.givenValue : "");
        sb.append("\nAgentFlag\t = ").append(this.agentFlag);
        sb.append("\nMorelatetoMTFlag\t = ").append(this.morelatetoMTFlag);
        sb.append("\nPriority\t = ").append(this.priority);
        sb.append("\nExpireTime\t = ").append(this.expireTime != null ? this.expireTime : "");
        sb.append("\nScheduleTime\t = ").append(this.scheduleTime != null ? this.scheduleTime : "");
        sb.append("\nReportFlag\t = ").append(this.reportFlag);
        sb.append("\nTP_pid\t\t = ").append(this.tP_pid);
        sb.append("\nTP_udhi\t\t = ").append(this.tP_udhi);
        sb.append("\nMessageCoding\t = ").append(this.messageCoding);
        sb.append("\nMessageType\t = ").append(this.messageType);
        sb.append("\nMessageLength\t = ").append(this.messageLength);
        sb.append("\nMessageContent\t = ").append(TypeConvert.byte2HexString(this.messageContent));
        sb.append("\nReserve\t\t = ").append(this.reserve != null ? this.reserve : "");
        return sb.toString();
    }
}