package com.fortune.rms.business.log.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: xy
 * Date: 13-8-1
 * Time: 下午1:45
 * To change this template use File | Settings | File Templates.
 */
public class AreaDemandLog {
    private long id;
    private Date createTime;
    private long areaId;
    private long count;
    private Date dateStatics;
    private long length;
    private long type;
    private long grade;
    private long padCount;
    private long padLength;
    private long phoneCount;
    private long phoneLength;
    private long bytesSend;
    private long bytesSendPad;
    private long bytesSendPhone;
    private long mobileBytesSend;
    private long elseBytesSend;
    private long mobileCount;
    private long elseCount;
    private long mobileLength;
    private long elseLength;
    private long userOnLineCount;

    public AreaDemandLog(){

    }

    public AreaDemandLog(long id, Date createTime, long areaId, long count, Date dateStatics, long length, long type,long grade
            ,long padCount,long padLength,long phoneCount,long phoneLength,long bytesSend,long bytesSendPad,long bytesSendPhone
            ,long mobileBytesSend,long elseBytesSend,long mobileCount,long  elseCount,long mobileLength,long elseLength,long userOnLineCount){
        this.id = id;
        this.createTime = createTime;
        this.areaId = areaId;
        this.count = count;
        this.dateStatics = dateStatics;
        this.length = length;
        this.type = type;
        this.grade = grade;
        this.padCount = padCount;
        this.padLength = padLength;
        this.phoneCount = phoneCount;
        this.phoneLength = phoneLength;
        this.bytesSend = bytesSend;
        this.bytesSendPad = bytesSendPad;
        this.bytesSendPhone = bytesSendPhone;
        this.mobileBytesSend = mobileBytesSend;
        this.elseBytesSend = elseBytesSend;
        this.mobileCount = mobileCount;
        this.elseCount  = elseCount;
        this.mobileLength = mobileLength;
        this.elseLength = elseLength;
        this.userOnLineCount = userOnLineCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public Date getDateStatics() {
        return dateStatics;
    }

    public void setDateStatics(Date dateStatics) {
        this.dateStatics = dateStatics;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
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

    public long getGrade() {
        return grade;
    }

    public void setGrade(long grade) {
        this.grade = grade;
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

    public long getMobileBytesSend() {
        return mobileBytesSend;
    }

    public void setMobileBytesSend(long mobileBytesSend) {
        this.mobileBytesSend = mobileBytesSend;
    }

    public long getElseBytesSend() {
        return elseBytesSend;
    }

    public void setElseBytesSend(long elseBytesSend) {
        this.elseBytesSend = elseBytesSend;
    }

    public long getMobileCount() {
        return mobileCount;
    }

    public void setMobileCount(long mobileCount) {
        this.mobileCount = mobileCount;
    }

    public long getElseCount() {
        return elseCount;
    }

    public void setElseCount(long elseCount) {
        this.elseCount = elseCount;
    }

    public long getMobileLength() {
        return mobileLength;
    }

    public void setMobileLength(long mobileLength) {
        this.mobileLength = mobileLength;
    }

    public long getElseLength() {
        return elseLength;
    }

    public void setElseLength(long elseLength) {
        this.elseLength = elseLength;
    }

    public long getUserOnLineCount() {
        return userOnLineCount;
    }

    public void setUserOnLineCount(long userOnLineCount) {
        this.userOnLineCount = userOnLineCount;
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
