package com.fortune.rms.business.publish.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.publish.dao.daoInterface.RelatedPropertyContentDaoInterface;
import com.fortune.rms.business.publish.model.RelatedPropertyContent;
import org.springframework.stereotype.Repository;

@Repository
public class RelatedPropertyContentDaoAccess
		extends
			BaseDaoAccess<RelatedPropertyContent, Long>
		implements
			RelatedPropertyContentDaoInterface {

	public RelatedPropertyContentDaoAccess() {
		super(RelatedPropertyContent.class);
	}

}
