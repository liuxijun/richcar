package com.fortune.rms.business.user.model;

/**
 * Created with IntelliJ IDEA.
 * User: long
 * Date: 12-10-30
 * Time: ÉÏÎç9:46
 * To change this template use File | Settings | File Templates.
 */
public class Stb implements java.io.Serializable{
    private long id;
    private  Integer userId;
    private String serialNo;
    private Integer stbType;
    private String location;
    private Integer status;
    public Stb(){

    }

    public Stb(long id, Integer userId, String serialNo, Integer stbType, String location, Integer status) {
        this.id = id;
        this.userId = userId;
        this.serialNo = serialNo;
        this.stbType = stbType;
        this.location = location;
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Integer getStbType() {
        return stbType;
    }

    public void setStbType(Integer stbType) {
        this.stbType = stbType;
    }

    /**
     * toString return json format string of this bean
     * @return String
     */
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
