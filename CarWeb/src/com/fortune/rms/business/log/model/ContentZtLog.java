package com.fortune.rms.business.log.model;


import java.util.Date;

public class ContentZtLog {
    private long id;
    private long contentId;
    private Date createTime;
    private long isSpecial;
    private long type;
    private long cspId;

    public ContentZtLog(){
    }

    public ContentZtLog(long id,long contentId,Date createTime,long isSpecial,long type,long cspId){
        this.id = id;
        this.contentId = contentId;
        this.createTime = createTime;
        this.isSpecial = isSpecial;
        this.type = type;
        this.cspId = cspId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getIsSpecial() {
        return isSpecial;
    }

    public void setIsSpecial(long isSpecial) {
        this.isSpecial = isSpecial;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getCspId() {
        return cspId;
    }

    public void setCspId(long cspId) {
        this.cspId = cspId;
    }

    //转换成json  方便页面显示
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
