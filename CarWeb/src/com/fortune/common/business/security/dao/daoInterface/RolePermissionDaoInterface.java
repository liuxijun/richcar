package com.fortune.common.business.security.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.security.model.Permission;
import com.fortune.common.business.security.model.Role;
import com.fortune.common.business.security.model.RolePermission;

import java.util.List;

public interface RolePermissionDaoInterface
		extends
			BaseDaoInterface<RolePermission, Long> {
    public List<Permission> getPermissionsOfRole(Integer roleId);
    public List<Role> getRolesOfPermission(Integer permissionId);

}