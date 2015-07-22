package com.fortune.common.web.security;

import com.fortune.common.business.security.logic.logicInterface.PermissionLogicInterface;
import com.fortune.common.business.security.model.Permission;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Namespace("/security")
@ParentPackage("default")
@Action(value="permission")
public class PermissionAction extends BaseAction<Permission> {
	private static final long serialVersionUID = 3243534534534534l;
	private PermissionLogicInterface permissionLogicInterface;
	@SuppressWarnings("unchecked")
    private List<Integer> roleIds;
	public PermissionAction() {
		super(Permission.class);
	}
	/**
	 * @param permissionLogicInterface the permissionLogicInterface to set
	 */
    @Autowired
	public void setPermissionLogicInterface(
			PermissionLogicInterface permissionLogicInterface) {
		this.permissionLogicInterface = permissionLogicInterface;
		setBaseLogicInterface(permissionLogicInterface);
	}

    public String view(){
        String result = super.view();
        obj.setRoles(permissionLogicInterface.getRolesWitchPermissionCheck(keyId));
        return result;
    }
    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public String save(){
        String result = super.save();
        permissionLogicInterface.saveRoleToPermission(roleIds,obj.getPermissionId());
        return result;
    }
    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }
}
