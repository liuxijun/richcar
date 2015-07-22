package com.fortune.rms.business.ad.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.ad.dao.daoInterface.AdUrlDaoInterface;
import com.fortune.rms.business.ad.model.AdUrl;
import org.springframework.stereotype.Repository;

@Repository
public class AdUrlDaoAccess extends BaseDaoAccess<AdUrl, Long>
		implements
			AdUrlDaoInterface {

	public AdUrlDaoAccess() {
		super(AdUrl.class);
	}

}
