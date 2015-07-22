package com.fortune.rms.business.vote.model;

import com.fortune.rms.business.frontuser.model.FrontUser;

import java.util.Date;

/**
 * Created by 王明路 on 2015/1/13.
 * 问卷参与用户信息
 */
public class InvestUser {
    private String userId;
    private String name;
    private Date investTime;
    private Long duration;

    public InvestUser() {
    }

    public InvestUser(FrontUser user) {
        userId = user.getUserId();
        name = user.getName();
    }

    public InvestUser(InvestigationResult investigationResult) {
        userId = investigationResult.getUserId();
        investTime = investigationResult.getInvestigateTime();
        duration = investigationResult.getDuration();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
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
