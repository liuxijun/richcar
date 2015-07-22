package com.fortune.common.business.security.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.security.model.AdminRole;
import com.fortune.common.business.security.model.Role;

import java.util.List;

public interface AdminRoleDaoInterface
		extends
			BaseDaoInterface<AdminRole, Long> {
      public List<Role> getRolesOfOperator(int operatorId, int cspId);
}