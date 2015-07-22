package com.fortune.rms.business.content.model;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-16
 * Time: 9:35:43
 * 视频媒体的详细信息，包括基本信息、所属频道，用户组和媒体文件信息
 */
public class RedexContentDetail {
    private Long id;
    private String name;
    private String poster;
    private String bigPoster;
    private String actor;
    private String intro;
    private String activityTime;    // 活动发生时间
    private Long deviceId;
    private boolean success;    // for client json parse

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    private Long moduleId;
    private Date createTime;
    private String channels;        // 频道Id拼接的串
    private String userType;         // 可以访问用户类型串，同样是id拼接的串
    private List<RedexContentFile> fileList;       // 文件列表

    @SuppressWarnings("unchecked")
    public RedexContentDetail() {
        success = true;
        fileList = new ArrayList();
    }

    public void clearFileList(){
        fileList.clear();
    }

    public void addContentFile(RedexContentFile file){
        if(file != null){
            fileList.add(file);
        }
    }

    public List<RedexContentFile> getFileList() {
        return fileList;
    }

    public void sortFiles(){
        Collections.sort(fileList, new Comparator<RedexContentFile>(){
            public int compare(RedexContentFile b1, RedexContentFile b2) {
                return b1.getIndex().compareTo(b2.getIndex());
            }
        });
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getBigPoster() {
        return bigPoster;
    }

    public void setBigPoster(String bigPoster) {
        this.bigPoster = bigPoster;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

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
