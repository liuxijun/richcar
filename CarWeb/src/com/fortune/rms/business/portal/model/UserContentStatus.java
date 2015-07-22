package com.fortune.rms.business.portal.model;

import com.fortune.rms.business.user.model.BookMark;

import java.awt.print.Book;
import java.util.Date;

/**
 * Created by 王明路 on 2015/1/21.
 * 用户，内容状态
 */
public class UserContentStatus {
    private String userId;          // 用户Id
    private Long contentId;         // 内容Id
    private Integer score;             // 评分 <0表示无效
    private Date scoreTime;         // 评分事件
    private Boolean faved;          // 是否被收藏
    private Date favTime;           // 收藏时间
    private BookMark bookMark;      //书签

    public UserContentStatus() {
        faved = false;
        favTime = new Date();
        score = -1;
        scoreTime = new Date();
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getScoreTime() {
        return scoreTime;
    }

    public void setScoreTime(Date scoreTime) {
        this.scoreTime = scoreTime;
    }

    public Boolean getFaved() {
        return faved;
    }

    public void setFaved(Boolean faved) {
        this.faved = faved;
    }

    public Date getFavTime() {
        return favTime;
    }

    public void setFavTime(Date favTime) {
        this.favTime = favTime;
    }

    public BookMark getBookMark() {
        return bookMark;
    }

    public void setBookMark(BookMark bookMark) {
        this.bookMark = bookMark;
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
