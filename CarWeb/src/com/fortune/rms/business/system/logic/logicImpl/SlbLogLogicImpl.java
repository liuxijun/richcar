package com.fortune.rms.business.system.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.security.dao.daoInterface.AdminDaoInterface;
import com.fortune.rms.business.system.dao.daoInterface.SlbLogDaoInterface;
import com.fortune.rms.business.system.logic.logicInterface.SlbLogLogicInterface;
import com.fortune.rms.business.system.model.SlbLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("slbLogLogicInterface")
public class SlbLogLogicImpl extends BaseLogicImpl<SlbLog>
		implements
			SlbLogLogicInterface {
	private SlbLogDaoInterface slbLogDaoInterface;
    private AdminDaoInterface adminDaoInterface;
	/**
	 * @param slbLogDaoInterface the systemLogDaoInterface to set
	 */
    @Autowired
	public void setSlbLogDaoInterface(SlbLogDaoInterface slbLogDaoInterface) {
		this.slbLogDaoInterface = slbLogDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.slbLogDaoInterface;
	}

}
