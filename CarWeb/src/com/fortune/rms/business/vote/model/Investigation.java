package com.fortune.rms.business.vote.model;

import java.util.Date;
import java.util.List;

/**
 * Created by ����· on 2014/12/24.
 * �����ʾ�
 */
public class Investigation implements java.io.Serializable{
    public static final long INVESTIGATION_STATUS_ONLINE = 1L;   // ����
    public static final long INVESTIGATION_STATUS_OFFLINE = 2L;  // ����

    private Long id;

    private String title;
    private Date createTime;
    private Date lastModified;
    private Long paperCount;    // �ʾ�Ĵ������ֻ�����ݴ��䣬�����л�
    private Long questionCount; // ��������ֻ�����ݴ��䣬�����к�
    private List<Question> questionList;    // �����б��ڸ��𳡺�ʹ�ã������л�����Ϊ��

    public Long getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Long questionCount) {
        this.questionCount = questionCount;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public Long getPaperCount() {
        return paperCount;
    }

    public void setPaperCount(Long paperCount) {
        this.paperCount = paperCount;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    private Date startTime;
    private Date endTime;
    private Long status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Investigation(Long id, String title, Date createTime, Date startTime, Date endTime, Long status) {
        this.id = id;
        this.title = title;
        this.createTime = createTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Investigation() {

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
