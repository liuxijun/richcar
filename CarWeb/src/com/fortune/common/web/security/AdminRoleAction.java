package com.fortune.common.web.security;

import java.util.List;

import com.fortune.common.business.security.logic.logicInterface.AdminLogicInterface;
import com.fortune.common.business.security.logic.logicInterface.AdminRoleLogicInterface;
import com.fortune.common.business.security.model.AdminRole;
import com.fortune.util.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.fortune.common.business.security.logic.logicInterface.RoleLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.common.business.security.model.Role;
import com.fortune.common.Constants;
import com.fortune.common.web.base.BaseAction;
import com.fortune.util.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/security")
@ParentPackage("default")
@Action(value="adminRoleAction")
public class AdminRoleAction extends BaseAction<AdminRole> {
	private static final long serialVersionUID = 3243534534534534l;
	private AdminRoleLogicInterface adminRoleLogicInterface;
	private AdminLogicInterface adminLogicInterface;
	private RoleLogicInterface roleLogicInterface;
	private List<Role> roleList;
	private String[] operatorRole;
	private Admin theoperator;

    @SuppressWarnings("unchecked")
	public AdminRoleAction() {
		super(AdminRole.class);
	}

    /**
	 * @param adminRoleLogicInterface the adminRoleLogicInterface to set
	 */
    @Autowired
	public void setAdminRoleLogicInterface(
			AdminRoleLogicInterface adminRoleLogicInterface) {
		this.adminRoleLogicInterface = adminRoleLogicInterface;
		setBaseLogicInterface(adminRoleLogicInterface);
	}
	@Override
	@SkipValidation
	public String search() {
		log.debug("in search");
		AdminRole searchBean = new AdminRole();
		Integer keyId = StringUtils.string2int(getKeyId(), -1);
		if (keyId >0) {
			log.debug("keyId is :" + keyId);
			searchBean.setAdminId(keyId);
		} else {
			log.debug("keyId null.");
		}
		objs = adminRoleLogicInterface
				.search(searchBean, pageBean);
		return Constants.ACTION_LIST;
	}
	
	@Override
	@SkipValidation
	public String view() {
		log.debug("in view");
		AdminRole searchBean = new AdminRole();
		Integer keyId = StringUtils.string2int(getKeyId(), -1);
		if (keyId >0) {
			log.debug("keyId is :" + keyId);
			searchBean.setAdminId(keyId);
			theoperator = adminLogicInterface.get(keyId);
		} else {
			log.debug("keyId null.");
		}
		objs = adminRoleLogicInterface
			.search(searchBean);

		roleList = roleLogicInterface.getAll();
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
				adminRoleLogicInterface.removeByAdminId(obj
						.getAdminId());
				if (operatorRole != null && operatorRole.length > 0)
					for (int i = 0; i < operatorRole.length; i++) {
						if (operatorRole[i] != null) {
							AdminRole or = new AdminRole();
							or.setAdminId(obj.getAdminId());
							or.setRoleId(Integer
									.parseInt(operatorRole[i]));
							adminRoleLogicInterface.save(or);
						}
					}
			}
            writeSysLog("保存数据 adminId="+obj.getAdminId()+",roleId="+obj.getRoleId());
			super.addActionMessage("成功保存数据！");
		} catch (Exception e) {
			super.addActionError("保存数据发生异常：" + e.getMessage());
			e.printStackTrace(); // To change body of catch statement use
			// File | Settings | File Templates.
		}
		return Constants.ACTION_SAVE;
	}
	public List<Role> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
    @Autowired
	public void setRoleLogicInterface(RoleLogicInterface roleLogicInterface) {
		this.roleLogicInterface = roleLogicInterface;
	}
	public String[] getOperatorRole() {
		return operatorRole;
	}
	public void setOperatorRole(String[] operatorRole) {
		this.operatorRole = operatorRole;
	}
	public Admin getTheoperator() {
		return theoperator;
	}
    @Autowired
	public void setOperatorLogicInterface(
			AdminLogicInterface adminLogicInterface) {
		this.adminLogicInterface = adminLogicInterface;
	}
}
