package com.fortune.car.app.bean;

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
}
