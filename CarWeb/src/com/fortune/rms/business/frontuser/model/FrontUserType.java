package com.fortune.rms.business.frontuser.model;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-24
 * Time: 17:45:33
 * 前台用户类型
 */
public class FrontUserType {
    private Long typeId;
    private String typeName;

    public FrontUserType(Long typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public FrontUserType() {

    }

    public Long getTypeId() {

        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
