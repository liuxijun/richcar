package com.fortune.rms.business.log.model;

import java.util.Date;

public class VacLog {
    private long id;
    private String userId;
    private String productId;
    private String checkPriceMessage;
    private String checkPriceResp;
    private Date createTime;
    private Date finishTime;
    private int duration;
    private String spId;
    private long operationType;
    private int resultCode;

    public VacLog() {
    }

    public VacLog(long id, String userId, String productId, String checkPriceMessage, String checkPriceResp,
                  Date createTime, Date finishTime, int duration, String spId, long operationType, int resultCode) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.checkPriceMessage = checkPriceMessage;
        this.checkPriceResp = checkPriceResp;
        this.createTime = createTime;
        this.finishTime = finishTime;
        this.duration = duration;
        this.spId = spId;
        this.operationType = operationType;
        this.resultCode = resultCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCheckPriceMessage() {
        return checkPriceMessage;
    }

    public void setCheckPriceMessage(String checkPriceMessage) {
        this.checkPriceMessage = checkPriceMessage;
    }

    public String getCheckPriceResp() {
        return checkPriceResp;
    }

    public void setCheckPriceResp(String checkPriceResp) {
        this.checkPriceResp = checkPriceResp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public long getOperationType() {
        return operationType;
    }

    public void setOperationType(long operationType) {
        this.operationType = operationType;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
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
