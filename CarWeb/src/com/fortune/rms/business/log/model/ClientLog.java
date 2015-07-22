package com.fortune.rms.business.log.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: xy
 * Date: 13-12-6
 * Time: 下午2:22
 * To change this template use File | Settings | File Templates.
 */
public class ClientLog {
    private long id;
    private Date time;
    private String userAgent;
    private String phoneCode;
    private String uuid;//设备的唯一编号
    private String ip;
    private String clientVersion;
    private Integer type;
    private Long areaId;
    private Long status;

    public ClientLog(){
    }

    public ClientLog(long id, Date time, String userAgent, String phoneCode, String uuid, String ip, String clientVersion, Integer type, Long areaId, Long status) {
        this.id = id;
        this.time = time;
        this.userAgent = userAgent;
        this.phoneCode = phoneCode;
        this.uuid = uuid;
        this.ip = ip;
        this.clientVersion = clientVersion;
        this.type = type;
        this.areaId = areaId;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
