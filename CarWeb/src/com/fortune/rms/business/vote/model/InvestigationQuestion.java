package com.fortune.rms.business.vote.model;

/**
 * Created by 王明路 on 2014/12/24.
 * 问卷问题
 */
public class InvestigationQuestion implements java.io.Serializable {
    private Long id;
    private Long investigationId;
    private Long questionId;
    private Long sequence;

    public InvestigationQuestion() {
    }

    public InvestigationQuestion(Long id, Long investigationId, Long questionId, Long sequence) {
        this.id = id;
        this.investigationId = investigationId;
        this.questionId = questionId;
        this.sequence = sequence;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInvestigationId() {
        return investigationId;
    }

    public void setInvestigationId(Long investigationId) {
        this.investigationId = investigationId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
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
