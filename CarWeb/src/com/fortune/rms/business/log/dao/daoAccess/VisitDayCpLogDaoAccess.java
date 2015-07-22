package com.fortune.rms.business.log.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.log.dao.daoInterface.VisitDayCpLogDaoInterface;
import com.fortune.rms.business.log.model.VisitDayCpLog;
import org.springframework.stereotype.Repository;

@Repository
public class VisitDayCpLogDaoAccess extends BaseDaoAccess<VisitDayCpLog, Long>
		implements
			VisitDayCpLogDaoInterface {

	public VisitDayCpLogDaoAccess() {
		super(VisitDayCpLog.class);
	}

}
