package com.fortune.rms.business.log.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.log.dao.daoInterface.WebVisitLogDaoInterface;
import com.fortune.rms.business.log.logic.logicInterface.WebVisitLogLogicInterface;
import com.fortune.rms.business.log.model.WebVisitLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("webVisitLogLogicInterface")
public class WebVisitLogLogicImpl extends BaseLogicImpl<WebVisitLog>
		implements
			WebVisitLogLogicInterface {
	private WebVisitLogDaoInterface webVisitLogDaoInterface;

	/**
	 * @param webVisitLogDaoInterface the webVisitLogDaoInterface to set
	 */
    @Autowired
	public void setWebVisitLogDaoInterface(
			WebVisitLogDaoInterface webVisitLogDaoInterface) {
		this.webVisitLogDaoInterface = webVisitLogDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.webVisitLogDaoInterface;
	}

}
