package com.fortune.rms.business.log.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.log.dao.daoInterface.VisitDayAreaLogDaoInterface;
import com.fortune.rms.business.log.logic.logicInterface.VisitDayAreaLogLogicInterface;
import com.fortune.rms.business.log.model.VisitDayAreaLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("visitDayAreaLogLogicInterface")
public class VisitDayAreaLogLogicImpl extends BaseLogicImpl<VisitDayAreaLog>
		implements
			VisitDayAreaLogLogicInterface {
	private VisitDayAreaLogDaoInterface visitDayAreaLogDaoInterface;

	/**
	 * @param visitDayAreaLogDaoInterface the visitDayAreaLogDaoInterface to set
	 */
    @Autowired
	public void setVisitDayAreaLogDaoInterface(
			VisitDayAreaLogDaoInterface visitDayAreaLogDaoInterface) {
		this.visitDayAreaLogDaoInterface = visitDayAreaLogDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.visitDayAreaLogDaoInterface;
	}

}
