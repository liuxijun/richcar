package com.fortune.rms.business.csp.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.csp.model.CspModule;
import com.fortune.rms.business.module.model.Module;

import java.util.List;

public interface CspModuleDaoInterface
		extends
			BaseDaoInterface<CspModule, Long> {
   public List<Module> getModuleOfCsp(long cspId);
   public Long getDefaultModule(long cspId);
   public List<CspModule> getModulesByCspId(long cspId);
   public void deleteCspModuleByParam(String moduleIds, long cspId);
   public void deleteCspModuleIsDefault(long defaultModuleId, long cspId);
}