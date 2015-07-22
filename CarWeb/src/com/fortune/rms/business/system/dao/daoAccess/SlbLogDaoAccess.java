package com.fortune.rms.business.system.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.system.dao.daoInterface.SlbLogDaoInterface;
import com.fortune.rms.business.system.model.SlbLog;
import org.springframework.stereotype.Repository;

@Repository
public class SlbLogDaoAccess extends BaseDaoAccess<SlbLog, Long>
		implements
			SlbLogDaoInterface {

	public SlbLogDaoAccess() {
		super(SlbLog.class);
	}
}
