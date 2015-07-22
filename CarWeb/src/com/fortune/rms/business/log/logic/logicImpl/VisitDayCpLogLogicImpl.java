package com.fortune.rms.business.log.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.log.dao.daoInterface.VisitDayCpLogDaoInterface;
import com.fortune.rms.business.log.logic.logicInterface.VisitDayCpLogLogicInterface;
import com.fortune.rms.business.log.model.VisitDayCpLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("visitDayCpLogLogicInterface")
public class VisitDayCpLogLogicImpl extends BaseLogicImpl<VisitDayCpLog>
		implements
			VisitDayCpLogLogicInterface {
	private VisitDayCpLogDaoInterface visitDayCpLogDaoInterface;

	/**
	 * @param visitDayCpLogDaoInterface the visitDayCpLogDaoInterface to set
	 */
    @Autowired
	public void setVisitDayCpLogDaoInterface(
			VisitDayCpLogDaoInterface visitDayCpLogDaoInterface) {
		this.visitDayCpLogDaoInterface = visitDayCpLogDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.visitDayCpLogDaoInterface;
	}

}
