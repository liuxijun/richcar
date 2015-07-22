package com.fortune.rms.business.log.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.log.dao.daoInterface.VisitDayChannelLogDaoInterface;
import com.fortune.rms.business.log.model.VisitDayChannelLog;
import org.springframework.stereotype.Repository;

@Repository
public class VisitDayChannelLogDaoAccess
		extends
			BaseDaoAccess<VisitDayChannelLog, Long>
		implements
			VisitDayChannelLogDaoInterface {

	public VisitDayChannelLogDaoAccess() {
		super(VisitDayChannelLog.class);
	}

}
