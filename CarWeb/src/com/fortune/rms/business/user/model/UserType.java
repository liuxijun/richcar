package com.fortune.rms.business.user.model;

import com.fortune.common.business.base.model.BaseModel;

/**
 * Created by xjliu on 14-10-11.
 * 用户类型
 */
public class UserType extends BaseModel {
    long id;
    private String name;
    private String type;
    private Long level;
    private String orgName;
    private Long orgId;

    public UserType() {
    }

    public UserType(long id) {
        this.id = id;
    }

    public UserType(long id, String name, String type, Long level, String orgName, Long orgId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.level = level;
        this.orgName = orgName;
        this.orgId = orgId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
