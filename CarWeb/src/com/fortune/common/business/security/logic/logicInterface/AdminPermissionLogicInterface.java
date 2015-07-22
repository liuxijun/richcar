package com.fortune.common.business.security.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.security.model.AdminPermission;

public interface AdminPermissionLogicInterface
		extends
			BaseLogicInterface<AdminPermission> {
	
	public void removeByOperatorId(Long opid);
}
