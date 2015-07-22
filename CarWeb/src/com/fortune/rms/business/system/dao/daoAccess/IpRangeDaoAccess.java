package com.fortune.rms.business.system.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.system.dao.daoInterface.IpRangeDaoInterface;
import com.fortune.rms.business.system.model.IpRange;
import org.springframework.stereotype.Repository;

@Repository
public class IpRangeDaoAccess extends BaseDaoAccess<IpRange, Long>
		implements
			IpRangeDaoInterface {

	public IpRangeDaoAccess() {
		super(IpRange.class);
	}

}
