package com.fortune.rms.business.user.model;

import com.fortune.util.StringUtils;

import java.util.Date;

/**
 * Created by ����· on 2015/1/27.
 * �ۿ���ʷDTO
 */
public class HistoryDTO {
    private String userId;          // �û�Id
    private Long contentId;         // ����Id
    private Long episodeId;         // �ֶ�Id
    private Date createTime;        // ���ʱ��
    private String contentTitle;    // ��Ƶ����
    private String contentPoster;   // ��Ƶ����

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
