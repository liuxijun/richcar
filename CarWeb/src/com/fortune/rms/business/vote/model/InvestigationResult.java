package com.fortune.rms.business.vote.model;

import java.util.Date;

/**
 * Created by 王明路 on 2014/12/24
 * 问卷结果.
 */
public class InvestigationResult implements java.io.Serializable {
    private Long id;
    private Date investigateTime;
    private Long duration;
    private Long investigationId;
    private String userId;

    public InvestigationResult() {
    }

    public InvestigationResult(Long id, Date investigateTime, Long duration, Long investigationId, String userId) {
        this.id = id;
        this.investigateTime = investigateTime;
        this.duration = duration;
        this.investigationId = investigationId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getInvestigateTime() {
        return investigateTime;
    }

    public void setInvestigateTime(Date investigateTime) {
        this.investigateTime = investigateTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getInvestigationId() {
        return investigationId;
    }

    public void setInvestigationId(Long investigationId) {
        this.investigationId = investigationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
        return com.fortune.util.JsonUtils.getJsonString(this, "");
    }
}
