package com.fortune.rms.business.publish.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.publish.dao.daoInterface.RelatedDaoInterface;
import com.fortune.rms.business.publish.model.Related;
import org.springframework.stereotype.Repository;

@Repository
public class RelatedDaoAccess extends BaseDaoAccess<Related, Long>
		implements
			RelatedDaoInterface {

	public RelatedDaoAccess() {
		super(Related.class);
	}

}
