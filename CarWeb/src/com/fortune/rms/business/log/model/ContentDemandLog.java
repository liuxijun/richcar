package com.fortune.rms.business.log.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-6
 * Time: 上午9:19
 * To change this template use File | Settings | File Templates.
 */
public class ContentDemandLog {
    private long id;
    private Date createTime;
    private long contentId;
    private long count;
    private Date dateStatics;
    private long length;
    private long spId;
    private long channelId;
    private long type;
    private long padCount;
    private long padLength;
    private long phoneCount;
    private long phoneLength;
    private long bytesSend;
    private long bytesSendPad;
    private long bytesSendPhone;

    public ContentDemandLog(){

    }

    public ContentDemandLog(long id,Date createTime,long contentId,long count,Date dateStatics,long length,long spId,long type,long channelId,
                            long padCount,long padLength,long phoneCount,long phoneLength,long bytesSend,long bytesSendPad,long bytesSendPhone){
        this.id = id;
        this.createTime = createTime;
        this.contentId = contentId;
        this.count = count;
        this.dateStatics = dateStatics;
        this.length = length;
        this.spId = spId;
        this.type = type;
        this.channelId = channelId;
        this.padCount = padCount;
        this.padLength = padLength;
        this.phoneCount = phoneCount;
        this.phoneLength = phoneLength;
        this.bytesSend = bytesSend;
        this.bytesSendPad = bytesSendPad;
        this.bytesSendPhone = bytesSendPhone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getContentId() {
        return contentId;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Date getDateStatics() {
        return dateStatics;
    }

    public void setDateStatics(Date dateStatics) {
        this.dateStatics = dateStatics;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getSpId() {
        return spId;
    }

    public void setSpId(long spId) {
        this.spId = spId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getPadCount() {
        return padCount;
    }

    public void setPadCount(long padCount) {
        this.padCount = padCount;
    }

    public long getPadLength() {
        return padLength;
    }

    public void setPadLength(long padLength) {
        this.padLength = padLength;
    }

    public long getPhoneCount() {
        return phoneCount;
    }

    public void setPhoneCount(long phoneCount) {
        this.phoneCount = phoneCount;
    }

    public long getPhoneLength() {
        return phoneLength;
    }

    public void setPhoneLength(long phoneLength) {
        this.phoneLength = phoneLength;
    }

    public long getBytesSend() {
        return bytesSend;
    }

    public void setBytesSend(long bytesSend) {
        this.bytesSend = bytesSend;
    }

    public long getBytesSendPad() {
        return bytesSendPad;
    }

    public void setBytesSendPad(long bytesSendPad) {
        this.bytesSendPad = bytesSendPad;
    }

    public long getBytesSendPhone() {
        return bytesSendPhone;
    }

    public void setBytesSendPhone(long bytesSendPhone) {
        this.bytesSendPhone = bytesSendPhone;
    }

    //转换成json  方便页面显示
    public String toString() {
        return com.fortune.util.JsonUtils.getJsonString(this);
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public String getSimpleJson() {
        return com.fortune.util.JsonUtils.getJsonString(this, null);
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public String getObjJson() {
        return com.fortune.util.JsonUtils.getJsonString(this, "obj.");
    }
}
