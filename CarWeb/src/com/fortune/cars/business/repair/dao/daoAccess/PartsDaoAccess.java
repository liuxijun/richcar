package com.fortune.cars.business.repair.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.cars.business.repair.dao.daoInterface.PartsDaoInterface;
import com.fortune.cars.business.repair.model.Parts;
import org.springframework.stereotype.Repository;

@Repository
public class PartsDaoAccess extends BaseDaoAccess<Parts, Long>
		implements
			PartsDaoInterface {

	public PartsDaoAccess() {
		super(Parts.class);
	}

}
