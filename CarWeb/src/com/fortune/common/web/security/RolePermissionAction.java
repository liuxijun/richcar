package com.fortune.common.web.security;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.fortune.common.business.security.logic.logicInterface.PermissionLogicInterface;
import com.fortune.common.business.security.logic.logicInterface.RoleLogicInterface;
import com.fortune.common.business.security.logic.logicInterface.RolePermissionLogicInterface;
import com.fortune.common.business.security.model.Permission;
import com.fortune.common.business.security.model.Role;
import com.fortune.common.business.security.model.RolePermission;
import com.fortune.common.Constants;
import com.fortune.common.web.base.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/security")
@ParentPackage("default")
@Action(value="rolePermission")
public class RolePermissionAction extends BaseAction<RolePermission> {
	private static final long serialVersionUID = 3243534534534534l;
	private RolePermissionLogicInterface rolePermissionLogicInterface;
	private RoleLogicInterface roleLogicInterface;
	private List<Permission> permissionList;
	private PermissionLogicInterface permissionLogicInterface;
	private String[] rolePermission;
	private Role therole;
    @Autowired
	public void setPermissionLogicInterface(
			PermissionLogicInterface permissionLogicInterface) {
		this.permissionLogicInterface = permissionLogicInterface;
	}
	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
	}
	@SuppressWarnings("unchecked")
	public RolePermissionAction() {
		super(RolePermission.class);
	}
	/**
	 * @param rolePermissionLogicInterface the rolePermissionLogicInterface to set
	 */
    @Autowired
	public void setRolePermissionLogicInterface(
			RolePermissionLogicInterface rolePermissionLogicInterface) {
		this.rolePermissionLogicInterface = rolePermissionLogicInterface;
		setBaseLogicInterface(rolePermissionLogicInterface);
	}
	
	@Override
	@SkipValidation
	public String view() {
		log.debug("in view");
		RolePermission searchBean = new RolePermission();
		if (keyId >0) {
			log.debug("keyId is :" + keyId);
			searchBean.setRoleid(keyId);
			therole = roleLogicInterface.get(keyId);
		} else {
			log.debug("keyId null.");
		}
		objs = rolePermissionLogicInterface
			.search(searchBean);

		permissionList = permissionLogicInterface.getAll();
		return Constants.ACTION_VIEW;
	}
	@Override
	public String update() {
		return this.view();
	}
	@Override
	public String save() {
		log.debug("in save.");
		try {
			if (obj != null) {
				rolePermissionLogicInterface.removeByRoleId(obj
						.getRoleid());
				if (rolePermission != null && rolePermission.length > 0)
					for (int i = 0; i < rolePermission.length; i++) {
						if (rolePermission[i] != null) {
							RolePermission rp = new RolePermission();
							rp.setRoleid(obj.getRoleid());
							rp.setPermissionid(Integer
									.parseInt(rolePermission[i]));
							rolePermissionLogicInterface.save(rp);
						}
					}
			}
           writeSysLog("成功保存数据： role.name="+obj.getRole().getName()+"permission.name="+obj.getPermission().getName());
			super.addActionMessage("成功保存数据！");
		} catch (Exception e) {
			super.addActionError("保存数据发生异常：" + e.getMessage());
			e.printStackTrace(); 
		}
		return Constants.ACTION_SAVE;
	}
	public List<Permission> getPermissionList() {
		return permissionList;
	}
	public String[] getRolePermission() {
		return rolePermission;
	}
	public void setRolePermission(String[] rolePermission) {
		this.rolePermission = rolePermission;
	}
    @Autowired
	public void setRoleLogicInterface(RoleLogicInterface roleLogicInterface) {
		this.roleLogicInterface = roleLogicInterface;
	}

	public Role getTherole() {
		return therole;
	}
}
