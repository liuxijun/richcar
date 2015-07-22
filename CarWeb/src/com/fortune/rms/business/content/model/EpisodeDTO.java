package com.fortune.rms.business.content.model;

/**
 * Created by 王明路 on 2014/12/22.
 * 视频集
 */
public class EpisodeDTO {
    private long id;
    private int serial;     // 序号
    private String title;    // 名称
    private String url;      // url

    public EpisodeDTO() {
    }

    public EpisodeDTO(ContentProperty property){
        if(property != null){
            id = property.getId();
            serial = property.getIntValue().intValue();
            title = property.getName();
            url = property.getStringValue();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
