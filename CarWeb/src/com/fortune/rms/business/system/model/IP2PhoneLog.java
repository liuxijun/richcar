package com.fortune.rms.business.system.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-10-18
 * Time: 下午4:09
 * 通过IP获取用户手机号码的接口调用日志
 */

public class IP2PhoneLog  implements java.io.Serializable {
    private long id;
    private String ip;
    private String token;
    private String uniKey;
    private String phone;
    private int resultCode;
    private String logs;
    private Date startTime;
    private String userAgent;

    public IP2PhoneLog() {
    }

    public IP2PhoneLog(long id) {
        this.id = id;
    }

    public IP2PhoneLog(long id, String ip, String token, String uniKey, String phone, int resultCode, String logs, Date startTime,String userAgent) {
        this.id = id;
        this.ip = ip;
        this.token = token;
        this.uniKey = uniKey;
        this.phone = phone;
        this.resultCode = resultCode;
        this.logs = logs;
        this.startTime = startTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUniKey() {
        return uniKey;
    }

    public void setUniKey(String uniKey) {
        this.uniKey = uniKey;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
