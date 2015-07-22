package com.fortune.rms.business.module.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.module.dao.daoInterface.ModuleDaoInterface;
import com.fortune.rms.business.module.dao.daoInterface.ModulePropertyDaoInterface;
import com.fortune.rms.business.module.model.Module;
import com.fortune.rms.business.module.model.ModuleProperty;
import org.springframework.stereotype.Repository;

@Repository
public class ModulePropertyDaoAccess extends BaseDaoAccess<ModuleProperty, Long>
		implements
        ModulePropertyDaoInterface {

	public ModulePropertyDaoAccess() {
		super(ModuleProperty.class);
	}
}
