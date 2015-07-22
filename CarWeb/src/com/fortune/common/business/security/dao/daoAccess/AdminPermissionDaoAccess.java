package com.fortune.common.business.security.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.common.business.security.dao.daoInterface.AdminPermissionDaoInterface;
import com.fortune.common.business.security.model.AdminPermission;
import org.springframework.stereotype.Repository;

@Repository
public class AdminPermissionDaoAccess
		extends
			BaseDaoAccess<AdminPermission, Long>
		implements
        AdminPermissionDaoInterface {

	public AdminPermissionDaoAccess() {
		super(AdminPermission.class);
	}

}
