package com.fortune.rms.business.vote.model;

import java.util.Date;

/**
 * Created by 王明路 on 2014/12/24.
 * 投票结果，记录投票的时间和用户名，选项放在OptionResult中
 */
public class VoteResult  implements java.io.Serializable{
    private Long id;
    private Date voteTime;
    private Long voteId;
    private String userId;

    public VoteResult(Long id, Date voteTime, Long voteId, String userId) {
        this.id = id;
        this.voteTime = voteTime;
        this.voteId = voteId;
        this.userId = userId;
    }

    public VoteResult() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getVoteTime() {
        return voteTime;
    }

    public void setVoteTime(Date voteTime) {
        this.voteTime = voteTime;
    }

    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
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
