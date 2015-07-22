package com.fortune.rms.business.live.model;

import com.fortune.common.business.base.model.BaseModel;

import java.util.Date;

/**
 * Created by xjliu on 2015/6/14.
 *
 */
public class EPG extends BaseModel{
    private long id;
    private String name;
    private Date beginTime;
    private Date endTime;
    private Long liveId;
    private Long contentId;
    private Long status;
    private String thumbPic;

    public EPG() {
    }

    public EPG(long id, String name,String thumbPic,Date beginTime, Date endTime, Long liveId, Long contentId,Long status) {
        this.id = id;
        this.name = name;
        this.thumbPic = thumbPic;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.liveId = liveId;
        this.contentId = contentId;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getLiveId() {
        return liveId;
    }

    public void setLiveId(Long liveId) {
        this.liveId = liveId;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbPic() {
        return thumbPic;
    }

    public void setThumbPic(String thumbPic) {
        this.thumbPic = thumbPic;
    }
}
