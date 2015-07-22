package com.fortune.rms.business.system.model;

/**
 * Created by 王明路 on 2014/11/27.
 * 系统个性化信息
 */
public class Individual {
    private long id;            // ID
    private String logoPath;    // logo文件的路径，通过页面设置后保存到文件，路径为相对于当前网页根目录起的相对路径

    public String getMobileLogoPath() {
        return mobileLogoPath;
    }

    public void setMobileLogoPath(String mobileLogoPath) {
        this.mobileLogoPath = mobileLogoPath;
    }

    // 如 /images/logo_individual.png
    private String mobileLogoPath;//移动终端logo路径，一般会比logoPath指定的logo图片小一些
    private String name;        // 用户给系统起的别名，如“爱情动作片库”

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Individual() {

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
        return com.fortune.util.JsonUtils.getJsonString(this, "obj.");
    }
}
