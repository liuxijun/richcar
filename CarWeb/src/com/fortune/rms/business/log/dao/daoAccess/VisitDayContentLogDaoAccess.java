package com.fortune.rms.business.log.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.log.dao.daoInterface.VisitDayContentLogDaoInterface;
import com.fortune.rms.business.log.model.VisitDayContentLog;
import org.springframework.stereotype.Repository;

@Repository
public class VisitDayContentLogDaoAccess
		extends
			BaseDaoAccess<VisitDayContentLog, Long>
		implements
			VisitDayContentLogDaoInterface {

	public VisitDayContentLogDaoAccess() {
		super(VisitDayContentLog.class);
	}

}
