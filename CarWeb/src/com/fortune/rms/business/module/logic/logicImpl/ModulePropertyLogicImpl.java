package com.fortune.rms.business.module.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.module.dao.daoInterface.ModulePropertyDaoInterface;
import com.fortune.rms.business.module.logic.logicInterface.ModulePropertyLogicInterface;
import com.fortune.rms.business.module.model.ModuleProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("modulePropertyLogicInterface")
public class ModulePropertyLogicImpl extends BaseLogicImpl<ModuleProperty>
		implements
			ModulePropertyLogicInterface {
	private ModulePropertyDaoInterface modulePropertyDaoInterface;

	/**
	 * @param modulePropertyDaoInterface the moduleDaoInterface to set
	 */
    @Autowired
	public void setModuleDaoInterface(ModulePropertyDaoInterface modulePropertyDaoInterface) {
		this.modulePropertyDaoInterface = modulePropertyDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.modulePropertyDaoInterface;
	}

}
