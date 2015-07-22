package com.fortune.rms.business.log.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.log.dao.daoInterface.VisitDayAreaLogDaoInterface;
import com.fortune.rms.business.log.model.VisitDayAreaLog;
import org.springframework.stereotype.Repository;

@Repository
public class VisitDayAreaLogDaoAccess
		extends
			BaseDaoAccess<VisitDayAreaLog, Long>
		implements
			VisitDayAreaLogDaoInterface {

	public VisitDayAreaLogDaoAccess() {
		super(VisitDayAreaLog.class);
	}

}
