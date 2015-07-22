package com.fortune.common.business.security.model;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-11-3
 * Time: 16:11:30
 * 发布管理员管理的频道信息
 */
public class AdminChannel {
    private Long id;            // id
    private Long adminId;       // 管理员Id，不是login
    private Long channelId;   //  栏目Id

    public AdminChannel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String toString(){
        return com.fortune.util.JsonUtils.getJsonString(this);
    }
    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public String getSimpleJson(){
        return com.fortune.util.JsonUtils.getJsonString(this,null);
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public String getObjJson(){
        return com.fortune.util.JsonUtils.getJsonString(this,"obj.");
    }
    
}
