package com.fortune.rms.business.log.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.log.dao.daoInterface.WebVisitLogDaoInterface;
import com.fortune.rms.business.log.model.WebVisitLog;
import org.springframework.stereotype.Repository;

@Repository
public class WebVisitLogDaoAccess extends BaseDaoAccess<WebVisitLog, Long>
		implements
			WebVisitLogDaoInterface {

	public WebVisitLogDaoAccess() {
		super(WebVisitLog.class);
	}

}
