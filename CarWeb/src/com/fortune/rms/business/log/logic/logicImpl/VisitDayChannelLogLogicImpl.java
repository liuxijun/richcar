package com.fortune.rms.business.log.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.log.dao.daoInterface.VisitDayChannelLogDaoInterface;
import com.fortune.rms.business.log.logic.logicInterface.VisitDayChannelLogLogicInterface;
import com.fortune.rms.business.log.model.VisitDayChannelLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("visitDayChannelLogLogicInterface")
public class VisitDayChannelLogLogicImpl
		extends
			BaseLogicImpl<VisitDayChannelLog>
		implements
			VisitDayChannelLogLogicInterface {
	private VisitDayChannelLogDaoInterface visitDayChannelLogDaoInterface;

	/**
	 * @param visitDayChannelLogDaoInterface the visitDayChannelLogDaoInterface to set
	 */
    @Autowired
	public void setVisitDayChannelLogDaoInterface(
			VisitDayChannelLogDaoInterface visitDayChannelLogDaoInterface) {
		this.visitDayChannelLogDaoInterface = visitDayChannelLogDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.visitDayChannelLogDaoInterface;
	}

}
