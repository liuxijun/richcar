package com.fortune.common.business.security.model;

import com.fortune.rms.business.csp.model.CspAuditor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Admin generated by hbm2java
 */
public class Admin implements java.io.Serializable {
	private Integer id;
	private String login;
	private String password;
	private String realname;
	private String telephone;
	private String datatype;
	private Integer areaId;
	private Integer nodeId;
	private Integer status;
	private String oldpasswordlog;
	private Date modifydate;
	private Date lastlogintime;
    private Integer cspId;
    private Integer isSystem;
    private Integer isRoot;
    private Map<String, Permission> permissions;
    private List<Role> roles;
    private String serializedRole;

    public String getSerializedChannel() {
        return serializedChannel;
    }

    public void setSerializedChannel(String serializedChannel) {
        this.serializedChannel = serializedChannel;
    }

    private String serializedChannel;   // added by mlwang @2014-11-3 序列号的栏目列表，为该管理员可发布内容的栏目列表，用逗号分隔，便于数据传输

    private CspAuditor cspAuditor;

    public Admin() {
	}

    public Integer getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Integer system) {
        isSystem = system;
    }

    public Integer getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(Integer root) {
        isRoot = root;
    }

    @SuppressWarnings({"JpaAttributeTypeInspection"})
    public CspAuditor getCspAuditor() {
        return cspAuditor;
    }


    public void setCspAuditor(CspAuditor cspAuditor) {
        this.cspAuditor = cspAuditor;
    }

    @SuppressWarnings("unchecked")
    public Admin(Integer id, String login, String password) {
		this.id = id;
		this.login = login;
		this.password = password;
        this.serializedRole = "";
	}

    @SuppressWarnings("unchecked")
	public Admin(Integer id, String login, String password,
			String realname, String telephone, Integer status,
            String oldpasswordlog, Date modifydate,
			Date lastlogintime) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.realname = realname;
		this.telephone = telephone;
		this.status = status;
		this.oldpasswordlog = oldpasswordlog;
		this.modifydate = modifydate;
		this.lastlogintime = lastlogintime;
        this.serializedRole = "";
	}

    public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealname() {
		return this.realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getDatatype() {
		return this.datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public Integer getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOldpasswordlog() {
		return this.oldpasswordlog;
	}

	public void setOldpasswordlog(String oldpasswordlog) {
		this.oldpasswordlog = oldpasswordlog;
	}

	public Date getModifydate() {
		return this.modifydate;
	}

	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}

	public Date getLastlogintime() {
		return this.lastlogintime;
	}

	public void setLastlogintime(Date lastlogintime) {
		this.lastlogintime = lastlogintime;
	}

    public String toString(){
        return com.fortune.util.JsonUtils.getJsonString(this);
    }

    public Integer getCspId() {
        return cspId;
    }

    public void setCspId(Integer cspId) {
        this.cspId = cspId;
    }

    @SuppressWarnings({"JpaAttributeTypeInspection"})
    public Map<String, Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Permission> permissions) {
        this.permissions = permissions;
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public String getSimpleJson(){
        return com.fortune.util.JsonUtils.getJsonString(this,null);
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public String getObjJson(){
        return com.fortune.util.JsonUtils.getJsonString(this,"obj.");
    }

    @SuppressWarnings({"JpaAttributeTypeInspection"})
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setSerializedRole(String serializedRole) {
        this.serializedRole = serializedRole;
    }

    public String getSerializedRole() {

        return serializedRole;
    }

    public void addRole(Long roleId){
        if(serializedRole == null) serializedRole = "";
        serializedRole += serializedRole.isEmpty()?  ""+roleId : ","+roleId;
    }

    public void addChannel(Long channelId){
        if(serializedChannel == null) serializedChannel = "";
        serializedChannel += serializedChannel.isEmpty()?  ""+channelId : ","+channelId;
    }
}
