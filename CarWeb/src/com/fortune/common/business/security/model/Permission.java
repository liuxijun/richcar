package com.fortune.common.business.security.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * Permission generated by hbm2java
 */
@Entity
@Table(name = "PERMISSION")
@SequenceGenerator(name = "FORTUNE_GLOBAL_SEQ", sequenceName = "FORTUNE_GLOBAL_SEQ")
public class Permission implements java.io.Serializable {

	private Integer permissionId;
	private String name;
	private String target;
	private String classname;
	private String methodName;
	private String permissionDesc;
    private boolean selected;
    private List<Role> roles;

    public Permission() {
	}

	public Permission(Integer permissionId, String name, String target,
			String classname, String methodName) {
		this.permissionId = permissionId;
		this.name = name;
		this.target = target;
		this.classname = classname;
		this.methodName = methodName;
	}
	public Permission(Integer permissionid, String name, String target,
			String classname, String methodName, String permissionDesc) {
		this.permissionId = permissionid;
		this.name = name;
		this.target = target;
		this.classname = classname;
		this.methodName = methodName;
		this.permissionDesc = permissionDesc;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FORTUNE_GLOBAL_SEQ")
	@Column(name = "ID", unique = true, nullable = false, precision = 16, scale = 0)
	public Integer getPermissionId() {
		return this.permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	@Column(name = "NAME", nullable = false, length = 128)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TARGET", nullable = false, length = 128)
	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Column(name = "CLASSNAME", nullable = false, length = 255)
	public String getClassname() {
		return this.classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	@Column(name = "METHODNAME", nullable = false, length = 255)
	public String getMethodName() {
		return this.methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Column(name = "PERMISSIONDESC", length = 1024)
	public String getPermissionDesc() {
		return this.permissionDesc;
	}

	public void setPermissionDesc(String permissionDesc) {
		this.permissionDesc = permissionDesc;
	}
    public String toString(){
        return com.fortune.util.JsonUtils.getJsonString(this);
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public String getSimpleJson(){
        return com.fortune.util.JsonUtils.getJsonString(this,null);
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public String getObjJson(){
        return com.fortune.util.JsonUtils.getJsonString(this,"obj.");
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @SuppressWarnings({"JpaAttributeTypeInspection"})
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}