package com.fortune.rms.business.user.model;

import com.fortune.util.StringUtils;

import java.util.Date;

/**
 * Created by 王明路 on 2015/1/27.
 * 观看历史DTO
 */
public class HistoryDTO {
    private String userId;          // 用户Id
    private Long contentId;         // 内容Id
    private Long episodeId;         // 分段Id
    private Date createTime;        // 浏览时间
    private String contentTitle;    // 视频标题
    private String contentPoster;   // 视频海报

    public HistoryDTO() {
    }

    public HistoryDTO(BookMark bookMark){
        userId = bookMark.getUserId();
        contentId = StringUtils.string2long(bookMark.getContentId(), 0);
        episodeId = StringUtils.string2long(bookMark.getSubContentId(), 1);
        createTime = bookMark.getCreateTime();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public Long getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(Long episodeId) {
        this.episodeId = episodeId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentPoster() {
        return contentPoster;
    }

    public void setContentPoster(String contentPoster) {
        this.contentPoster = contentPoster;
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
        return com.fortune.util.JsonUtils.getJsonString(this, "obj.");
    }
}
