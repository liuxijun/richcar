package com.fortune.cars.business.conduct.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.cars.business.conduct.dao.daoInterface.ConductItemDaoInterface;
import com.fortune.cars.business.conduct.model.ConductItem;
import org.springframework.stereotype.Repository;

@Repository
public class ConductItemDaoAccess extends BaseDaoAccess<ConductItem, Long>
		implements
			ConductItemDaoInterface {

	public ConductItemDaoAccess() {
		super(ConductItem.class);
	}

}
