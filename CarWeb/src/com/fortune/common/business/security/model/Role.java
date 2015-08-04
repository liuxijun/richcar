package com.fortune.common.business.security.model;

import com.fortune.common.business.base.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;

/**
 * Role generated by hbm2java
 */
public class Role extends BaseModel{

	private Integer roleid;
	private String name;
	private String memo;
    private boolean selected;
    private Integer type;
    private List<Permission> permissions;
    private List<Menu> menus;
	public Role() {
	}

	public Role(Integer roleid, String name) {
		this.roleid = roleid;
		this.name = name;
	}
	public Role(Integer roleid, String name, String memo) {
		this.roleid = roleid;
		this.name = name;
		this.memo = memo;
	}


	public Integer getRoleid() {
		return this.roleid;
	}

	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection", "JpaAttributeTypeInspection"})
    public List<Permission> getPermissions() {
        return permissions;
    }
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection", "JpaAttributeTypeInspection"})
    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }
}