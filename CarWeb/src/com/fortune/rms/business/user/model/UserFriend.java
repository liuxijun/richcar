package com.fortune.rms.business.user.model;

import com.fortune.util.XmlUtils;


public class UserFriend {
    private long id;
    private String friendName;
    private String friendId;
    private String userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public UserFriend(String friendName, String friendId) {
        this.friendName = friendName;
        this.friendId = friendId;
    }

    public UserFriend() {
    }
    public UserFriend(org.dom4j.Node node){
        friendId  = XmlUtils.getValue(node,"FriendID","-1");
        friendName = XmlUtils.getValue(node,"FriendName","Ä¾ÓÐÃû×Ö");
    }
}
