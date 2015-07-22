package com.fortune.common.business.security.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.security.model.Permission;
import com.fortune.common.business.security.model.Role;

import java.util.List;

public interface PermissionLogicInterface
		extends
			BaseLogicInterface<Permission> {
    public List<Role> getRolesWitchPermissionCheck(Integer permissionId);
    public void saveRoleToPermission(List<Integer> roleId, Integer permissionId);
    public List<Permission> getPermissionOfOperator(Integer operatorId, Integer cspId);
}
