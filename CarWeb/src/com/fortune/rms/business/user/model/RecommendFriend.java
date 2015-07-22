package com.fortune.rms.business.user.model;

/**
 * Created with IntelliJ IDEA.
 * User: liupeng
 * Date: 12-7-24
 * Time: ÏÂÎç2:58
 */
public class RecommendFriend {
    private long id;
    private String recommendId;
    private String friendId;
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(String recommendId) {
        this.recommendId = recommendId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
