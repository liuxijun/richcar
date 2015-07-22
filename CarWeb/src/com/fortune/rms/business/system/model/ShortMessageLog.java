package com.fortune.rms.business.system.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-5-14
 * Time: ÉÏÎç9:26
 * ¶ÌÐÅ
 */
public class ShortMessageLog  implements java.io.Serializable {
    private long id;
    private String sn;
    private String phoneNumber;
    private String message;
    private Date startTime;
    private Long status;
    private String smsIp;
    private String log;
    private Date responseTime;
    private Long responseCode;
    private String responseIp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getSmsIp() {
        return smsIp;
    }

    public void setSmsIp(String smsIp) {
        this.smsIp = smsIp;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public Long getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Long responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseIp() {
        return responseIp;
    }

    public void setResponseIp(String responseIp) {
        this.responseIp = responseIp;
    }
}
