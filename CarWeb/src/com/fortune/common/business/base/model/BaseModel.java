package com.fortune.common.business.base.model;

/**
 * Created by xjliu on 14-6-12.
 * ������Bean
 */
public class BaseModel implements java.io.Serializable {
    private Object extraObj;

    public Object getExtraObj() {
        return extraObj;
    }

    public void setExtraObj(Object extraObj) {
        this.extraObj = extraObj;
    }

    //ת����json  ����ҳ����ʾ
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
