package com.fortune.rms.business.log.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.log.dao.daoInterface.VisitDaySpLogDaoInterface;
import com.fortune.rms.business.log.model.VisitDaySpLog;
import org.springframework.stereotype.Repository;

@Repository
public class VisitDaySpLogDaoAccess extends BaseDaoAccess<VisitDaySpLog, Long>
		implements
			VisitDaySpLogDaoInterface {

	public VisitDaySpLogDaoAccess() {
		super(VisitDaySpLog.class);
	}

}
