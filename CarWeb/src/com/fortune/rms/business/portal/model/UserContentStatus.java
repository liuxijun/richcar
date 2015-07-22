package com.fortune.rms.business.portal.model;

import com.fortune.rms.business.user.model.BookMark;

import java.awt.print.Book;
import java.util.Date;

/**
 * Created by ����· on 2015/1/21.
 * �û�������״̬
 */
public class UserContentStatus {
    private String userId;          // �û�Id
    private Long contentId;         // ����Id
    private Integer score;             // ���� <0��ʾ��Ч
    private Date scoreTime;         // �����¼�
    private Boolean faved;          // �Ƿ��ղ�
    private Date favTime;           // �ղ�ʱ��
    private BookMark bookMark;      //��ǩ

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
