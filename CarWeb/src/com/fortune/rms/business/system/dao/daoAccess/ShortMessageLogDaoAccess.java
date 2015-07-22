package com.fortune.rms.business.system.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.system.dao.daoInterface.ShortMessageLogDaoInterface;
import com.fortune.rms.business.system.model.ShortMessageLog;
import org.springframework.stereotype.Repository;

@Repository
public class ShortMessageLogDaoAccess extends BaseDaoAccess<ShortMessageLog, Long>
		implements
			ShortMessageLogDaoInterface {

	public ShortMessageLogDaoAccess() {
		super(ShortMessageLog.class);
	}
}
