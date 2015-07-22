package com.fortune.rms.business.publish.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.publish.dao.daoInterface.RelatedPropertyDaoInterface;
import com.fortune.rms.business.publish.model.RelatedProperty;
import org.springframework.stereotype.Repository;

@Repository
public class RelatedPropertyDaoAccess
		extends
			BaseDaoAccess<RelatedProperty, Long>
		implements
			RelatedPropertyDaoInterface {

	public RelatedPropertyDaoAccess() {
		super(RelatedProperty.class);
	}

}
