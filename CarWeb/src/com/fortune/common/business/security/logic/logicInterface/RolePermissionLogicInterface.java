package com.fortune.common.business.security.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.security.model.Permission;
import com.fortune.common.business.security.model.Role;
import com.fortune.common.business.security.model.RolePermission;

import java.util.List;

public interface RolePermissionLogicInterface
		extends
			BaseLogicInterface<RolePermission> {
	
	public void removeByRoleId(Integer roleid);
    public List<Permission> getPermissionsOfRole(Integer roleId);

    public List<Role> getRolesOfPermission(Integer permissionId);
    public void savePermissionToRole(List<Integer> permissionIds, Integer roleId);
    public void saveRoleToPermission(List<Integer> roleIds, Integer permissionId);
    public void savePermissionToRole(List<Integer> permissionIds, List<Integer> roleIds);
    public void onPermissionDeleted(Integer permissionId);
    public void onRoleDeleted(Integer roleId);
}
