package com.fortune.rms.business.system.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.system.dao.daoInterface.AreaDaoInterface;
import com.fortune.rms.business.system.model.Area;
import org.springframework.stereotype.Repository;

@Repository
public class AreaDaoAccess extends BaseDaoAccess<Area, Long>
		implements
			AreaDaoInterface {

	public AreaDaoAccess() {
		super(Area.class);
	}

}
