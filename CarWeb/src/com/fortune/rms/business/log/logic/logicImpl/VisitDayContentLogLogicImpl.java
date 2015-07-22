package com.fortune.rms.business.log.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.log.dao.daoInterface.VisitDayContentLogDaoInterface;
import com.fortune.rms.business.log.logic.logicInterface.VisitDayContentLogLogicInterface;
import com.fortune.rms.business.log.model.VisitDayContentLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("visitDayContentLogLogicInterface")
public class VisitDayContentLogLogicImpl
		extends
			BaseLogicImpl<VisitDayContentLog>
		implements
			VisitDayContentLogLogicInterface {
	private VisitDayContentLogDaoInterface visitDayContentLogDaoInterface;

	/**
	 * @param visitDayContentLogDaoInterface the visitDayContentLogDaoInterface to set
	 */
    @Autowired
	public void setVisitDayContentLogDaoInterface(
			VisitDayContentLogDaoInterface visitDayContentLogDaoInterface) {
		this.visitDayContentLogDaoInterface = visitDayContentLogDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.visitDayContentLogDaoInterface;
	}

}
