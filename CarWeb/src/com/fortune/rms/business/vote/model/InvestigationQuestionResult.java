package com.fortune.rms.business.vote.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王明路 on 2015/1/12.
 * 问卷问题结果
 */
public class InvestigationQuestionResult {
    private Long questionId;
    private List<Long> optionList;

    public List<Long> getOptionList() {
        return optionList;
    }

    public InvestigationQuestionResult() {
        optionList = new ArrayList<Long>();
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public InvestigationQuestionResult(Long questionId) {
        this.questionId = questionId;
        optionList = new ArrayList<Long>();

    }

    public void addOption(Long optionId){
        optionList.add(optionId);
    }
}
