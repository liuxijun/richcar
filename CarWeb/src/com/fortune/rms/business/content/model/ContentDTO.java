package com.fortune.rms.business.content.model;

import java.util.Date;

/**
 * Created by ����· on 2014/12/10.
 * ǰ̨��Ƶý�壬����ΪContent������Ӽ�
 */
public class ContentDTO {
    private Long id;
    private String title;
    private String poster;
    private String bigPoster;
    private String actor;
    private String desc;
    private Long count;
    private Date createTime;
    private Date lastVisit;

    public ContentDTO() {
    }

    public ContentDTO(Content content){
        if( content != null){
            id = content.getId();
            title = content.getName();
            poster = content.getPost1Url();
            bigPoster = content.getPost2Url();
            actor = content.getActors();
            desc = content.getIntro();
            count = content.getAllVisitCount();
            createTime = content.getCreateTime();
            lastVisit = null;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
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
