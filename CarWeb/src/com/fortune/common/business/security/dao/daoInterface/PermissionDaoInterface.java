package com.fortune.common.business.security.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.security.model.Permission;

import java.util.List;

public interface PermissionDaoInterface
		extends
			BaseDaoInterface<Permission, Integer> {
    public List<Permission> getPermissionOfOperator(Integer operatorId, Integer cspId);
}