package com.fortune.cars.business.conduct.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.cars.business.conduct.dao.daoInterface.ConductValueDaoInterface;
import com.fortune.cars.business.conduct.model.ConductValue;
import org.springframework.stereotype.Repository;

@Repository
public class ConductValueDaoAccess extends BaseDaoAccess<ConductValue, Long>
		implements
			ConductValueDaoInterface {

	public ConductValueDaoAccess() {
		super(ConductValue.class);
	}

}
