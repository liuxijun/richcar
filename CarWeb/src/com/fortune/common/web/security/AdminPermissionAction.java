package com.fortune.common.web.security;

import java.util.List;

import com.fortune.common.business.security.logic.logicInterface.AdminPermissionLogicInterface;
import com.fortune.common.business.security.model.AdminPermission;
import com.fortune.util.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.fortune.common.business.security.logic.logicInterface.PermissionLogicInterface;
import com.fortune.common.business.security.model.Permission;
import com.fortune.common.Constants;
import com.fortune.common.web.base.BaseAction;
import com.fortune.util.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/security")
@ParentPackage("default")
@Action(value="adminPermission")
public class AdminPermissionAction extends BaseAction<AdminPermission> {
	private static final long serialVersionUID = 3243534534534534l;
	private AdminPermissionLogicInterface adminPermissionLogicInterface;
	private PermissionLogicInterface permissionLogicInterface;

	private List<Permission> permissionList;
	private String[] operatorPermission;

    public String[] getOperatorPermission() {
		return operatorPermission;
	}
    @Autowired
	public void setOperatorPermission(String[] operatorPermission) {
		this.operatorPermission = operatorPermission;
	}

	@SuppressWarnings("unchecked")
	public AdminPermissionAction() {
		super(AdminPermission.class);
	}

	/**
	 * @param adminPermissionLogicInterface
	 *            the adminPermissionLogicInterface to set
	 */
    @Autowired
	public void setAdminPermissionLogicInterface(
			AdminPermissionLogicInterface adminPermissionLogicInterface) {
		this.adminPermissionLogicInterface = adminPermissionLogicInterface;
		setBaseLogicInterface(adminPermissionLogicInterface);
	}
	@Override
	@SkipValidation
	public void prepare() {

    }
	@Override
	@SkipValidation
	public String search() {
		log.debug("in search");
		AdminPermission searchBean = new AdminPermission();
		Integer keyId= StringUtils.string2int(getKeyId(), -1);
		if (keyId >0) {
			log.debug("keyId is :" + keyId);
			searchBean.setAdminId(keyId);
		} else {
			log.debug("keyId null.");
		}
		objs = adminPermissionLogicInterface
				.search(searchBean, pageBean);
		return Constants.ACTION_LIST;
	}

	@Override
	@SkipValidation
	public String view() {
		log.debug("in view");
		AdminPermission searchBean = new AdminPermission();
		Integer keyId = StringUtils.string2int(getKeyId(),-1);
		if (keyId >0) {
			log.debug("keyId is :" + keyId);
			searchBean.setAdminId(keyId);
		} else {
			log.debug("keyId null.");
		}
		objs = adminPermissionLogicInterface
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
				adminPermissionLogicInterface.removeByOperatorId(obj
						.getAdminId());
				if (operatorPermission != null && operatorPermission.length > 0)
					for (int i = 0; i < operatorPermission.length; i++) {
						if (operatorPermission[i] != null) {
							AdminPermission opm = new AdminPermission();
							opm.setAdminId(obj.getAdminId());
							opm.setPermissionId(Integer
									.parseInt(operatorPermission[i]));
							adminPermissionLogicInterface.save(opm);
						}
					}
			}
            writeSysLog("保存数据： adminId="+obj.getAdminId()+",permissionId="+obj.getPermissionId());
			super.addActionMessage("成功保存数据！");
		} catch (Exception e) {
			super.addActionError("保存数据发生异常：" + e.getMessage());
			e.printStackTrace(); // To change body of catch statement use
			// File | Settings | File Templates.
		}
		return Constants.ACTION_SAVE;
	}

	public void setPermissionLogicInterface(
			PermissionLogicInterface permissionLogicInterface) {
		this.permissionLogicInterface = permissionLogicInterface;
	}

	public List<Permission> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
	}
}
