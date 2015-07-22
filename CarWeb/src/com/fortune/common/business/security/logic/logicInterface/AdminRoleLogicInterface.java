package com.fortune.common.business.security.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.security.model.AdminRole;
import com.fortune.common.business.security.model.Role;

import java.util.List;

public interface AdminRoleLogicInterface
		extends
			BaseLogicInterface<AdminRole> {
	
	public void removeByAdminId(Integer opid);
    public void removeByAdminCsp(Integer adminId, int cspId);
    public List<Role> getRolesOfAdmin(int operatorId, int cspId);
    public void saveAdminRoles(List<Integer> roleIds, Integer operatorId, int cspId);
    public  List<AdminRole> getRolesOfAdmin(int operatorId);
}
