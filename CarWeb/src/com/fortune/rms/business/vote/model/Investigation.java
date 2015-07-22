package com.fortune.rms.business.vote.model;

import java.util.Date;
import java.util.List;

/**
 * Created by 王明路 on 2014/12/24.
 * 调查问卷
 */
public class Investigation implements java.io.Serializable{
    public static final long INVESTIGATION_STATUS_ONLINE = 1L;   // 上线
    public static final long INVESTIGATION_STATUS_OFFLINE = 2L;  // 下线

    private Long id;

    private String title;
    private Date createTime;
    private Date lastModified;
    private Long paperCount;    // 问卷的答卷数，只做数据传输，不序列化
    private Long questionCount; // 问题数，只做数据传输，不序列号
    private List<Question> questionList;    // 问题列表，在个别场合使用，不序列化，可为空

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
