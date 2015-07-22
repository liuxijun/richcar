package com.fortune.rms.business.vote.model;

/**
 * Created by 王明路 on 2014/12/24.
 * 问题选项
 */
public class Option  implements java.io.Serializable{
    private Long id;
    private Long questionId;
    private String title;
    private int sequence;
    private long ticketCount;     // 得票数，在查看投票结果时使用，序列化时不使用
    private boolean selected;       // 是否被选中，查看用户投票详情时使用，序列化不用

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public long getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(long ticketCount) {
        this.ticketCount = ticketCount;
    }

    public Option() {
        selected = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Option(Long id, Long questionId, String title, int sequence) {

        this.id = id;
        this.questionId = questionId;
        this.title = title;
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
