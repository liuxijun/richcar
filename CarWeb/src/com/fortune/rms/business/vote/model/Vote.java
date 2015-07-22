package com.fortune.rms.business.vote.model;

import java.util.Date;
import java.util.List;

/**
 * Created by 王明路 on 2014/12/24.
 * 投票
 */
public class Vote implements java.io.Serializable{
    public static final long VOTE_STATUS_ONLINE = 1L;   // 上线
    public static final long VOTE_STATUS_OFFLINE = 2L;  // 下线

    public static final long VOTE_TIME_STATUS_ACTIVE = 1L;  // 激活
    public static final long VOTE_TIME_STATUS_EXPIRED = 2L;  // 过期
    public static final long VOTE_TIME_STATUS_NOT_OPEN = 3L;  // 未开启

    private Long id;
    private String title;
    private Date createTime;
    private Date lastModified;

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    private Date startTime;
    private Date endTime;
    private Long status;
    private Long questionId;
    private Long ticketCount;   // 投票数（DTO only not for serialize）
    private Long timeSpanStatus; // 时间范围状态（DTO only not for serialize）
    private int maxOption;      // 最大选项数（DTO only not for serialize），值来自于vote对应Question中的maxOption
    private List<Option> optionList;    // 选项（DTO only not for serialize）

    public List<Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<Option> optionList) {
        this.optionList = optionList;
    }

    public int getMaxOption() {
        return maxOption;
    }

    public void setMaxOption(int maxOption) {
        this.maxOption = maxOption;
    }

    public Long getTimeSpanStatus() {
        return timeSpanStatus;
    }

    public void setTimeSpanStatus(Long timeSpanStatus) {
        this.timeSpanStatus = timeSpanStatus;
    }

    public Long getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(Long ticketCount) {
        this.ticketCount = ticketCount;
    }

    public Vote(Long id, String title, Date createTime, Date startTime, Date endTime, Long status, Long questionId) {
        this.id = id;
        this.title = title;
        this.createTime = createTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.questionId = questionId;
    }

    public Vote() {
    }

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

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
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
