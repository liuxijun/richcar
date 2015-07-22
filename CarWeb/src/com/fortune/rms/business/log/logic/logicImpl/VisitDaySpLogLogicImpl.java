package com.fortune.rms.business.log.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.log.dao.daoInterface.VisitDaySpLogDaoInterface;
import com.fortune.rms.business.log.logic.logicInterface.VisitDaySpLogLogicInterface;
import com.fortune.rms.business.log.model.VisitDaySpLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("visitDaySpLogLogicInterface")
public class VisitDaySpLogLogicImpl extends BaseLogicImpl<VisitDaySpLog>
		implements
			VisitDaySpLogLogicInterface {
	private VisitDaySpLogDaoInterface visitDaySpLogDaoInterface;

	/**
	 * @param visitDaySpLogDaoInterface the visitDaySpLogDaoInterface to set
	 */
    @Autowired
	public void setVisitDaySpLogDaoInterface(
			VisitDaySpLogDaoInterface visitDaySpLogDaoInterface) {
		this.visitDaySpLogDaoInterface = visitDaySpLogDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.visitDaySpLogDaoInterface;
	}

}
