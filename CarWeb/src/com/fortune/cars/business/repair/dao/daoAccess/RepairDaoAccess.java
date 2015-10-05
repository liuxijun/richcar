package com.fortune.cars.business.repair.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.cars.business.repair.dao.daoInterface.RepairDaoInterface;
import com.fortune.cars.business.repair.model.Repair;
import org.springframework.stereotype.Repository;

@Repository
public class RepairDaoAccess extends BaseDaoAccess<Repair, Long>
		implements
			RepairDaoInterface {

	public RepairDaoAccess() {
		super(Repair.class);
	}

}
