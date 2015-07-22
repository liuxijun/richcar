package com.fortune.rms.business.log.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-20
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */
public class ChannelDemandLog {
    private long id;
    private long channelId;
    private long count;
    private Date createTime;
    private Date dateStatics;
    private long length;
    private long type;
    private long bytesSend;

    public ChannelDemandLog(){

    }

    public ChannelDemandLog(long id,long channelId,long count,Date createTime,Date dateStatics,long length,long type,long bytesSend){
        this.id = id;
        this.channelId = channelId;
        this.count = count;
        this.createTime = createTime;
        this.dateStatics = dateStatics;
        this.length = length;
        this.type = type;
        this.bytesSend = bytesSend;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getBytesSend() {
        return bytesSend;
    }

    public void setBytesSend(long bytesSend) {
        this.bytesSend = bytesSend;
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
