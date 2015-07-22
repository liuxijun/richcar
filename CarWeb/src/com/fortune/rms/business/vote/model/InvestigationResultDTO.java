package com.fortune.rms.business.vote.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 王明路 on 2015/1/12.
 * 问卷结果DTO
 */
public class InvestigationResultDTO {
    private Date investigateTime;
    private Long duration;
    private Long investigationId;
    private String userId;
    private List<InvestigationQuestionResult> questionList;

    public InvestigationResultDTO() {
        questionList = new ArrayList<InvestigationQuestionResult>();
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

    public List<InvestigationQuestionResult> getQuestionList() {
        return questionList;
    }

    public void addQuestionResult(InvestigationQuestionResult r){
        questionList.add(r);

    }
}
