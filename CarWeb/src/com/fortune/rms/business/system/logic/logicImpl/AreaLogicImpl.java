package com.fortune.rms.business.system.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.system.dao.daoInterface.AreaDaoInterface;
import com.fortune.rms.business.system.logic.logicInterface.AreaLogicInterface;
import com.fortune.rms.business.system.model.Area;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("areaLogicInterface")
public class AreaLogicImpl extends BaseLogicImpl<Area>
		implements
			AreaLogicInterface {
	private AreaDaoInterface areaDaoInterface;

	/**
	 * @param areaDaoInterface the areaDaoInterface to set
	 */
    @Autowired
	public void setAreaDaoInterface(AreaDaoInterface areaDaoInterface) {
		this.areaDaoInterface = areaDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.areaDaoInterface;
	}



}
