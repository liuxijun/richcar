package com.fortune.rms.business.ad.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.ad.dao.daoInterface.AdDaoInterface;
import com.fortune.rms.business.ad.model.Ad;
import org.springframework.stereotype.Repository;

@Repository
public class AdDaoAccess extends BaseDaoAccess<Ad, Long>
		implements
			AdDaoInterface {

	public AdDaoAccess() {
		super(Ad.class);
	}

}
