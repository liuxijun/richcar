package com.fortune.rms.business.system.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.system.dao.daoInterface.IpRangeDaoInterface;
import com.fortune.rms.business.system.logic.logicInterface.IpRangeLogicInterface;
import com.fortune.rms.business.system.model.IpRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ipRangeLogicInterface")
public class IpRangeLogicImpl extends BaseLogicImpl<IpRange>
		implements
			IpRangeLogicInterface {
	private IpRangeDaoInterface ipRangeDaoInterface;

	/**
	 * @param ipRangeDaoInterface the ipRangeDaoInterface to set
	 */
    @Autowired
	public void setIpRangeDaoInterface(IpRangeDaoInterface ipRangeDaoInterface) {
		this.ipRangeDaoInterface = ipRangeDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.ipRangeDaoInterface;
	}

}
