package com.fortune.rms.business.system.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.system.dao.daoInterface.AreaIpRangeDaoInterface;
import com.fortune.rms.business.system.model.AreaIpRange;
import org.springframework.stereotype.Repository;
@Repository
public class AreaIpRangeDaoAccess extends BaseDaoAccess<AreaIpRange, Long>
		implements
			AreaIpRangeDaoInterface {

	public AreaIpRangeDaoAccess() {
		super(AreaIpRange.class);
	}




}
