package com.fortune.rms.business.ad.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.ad.dao.daoInterface.AdUrlDaoInterface;
import com.fortune.rms.business.ad.logic.logicInterface.AdUrlLogicInterface;
import com.fortune.rms.business.ad.model.AdUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("adUrlLogicInterface")
public class AdUrlLogicImpl extends BaseLogicImpl<AdUrl>
		implements
			AdUrlLogicInterface {
	private AdUrlDaoInterface adUrlDaoInterface;

	/**
	 * @param adUrlDaoInterface the adUrlDaoInterface to set
	 */
    @Autowired
	public void setAdUrlDaoInterface(AdUrlDaoInterface adUrlDaoInterface) {
		this.adUrlDaoInterface = adUrlDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.adUrlDaoInterface;
	}

}
