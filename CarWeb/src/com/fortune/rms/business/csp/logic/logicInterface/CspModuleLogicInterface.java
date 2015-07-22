package com.fortune.rms.business.csp.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.csp.model.CspModule;
import com.fortune.rms.business.module.model.Module;

import java.util.List;

public interface CspModuleLogicInterface extends BaseLogicInterface<CspModule> {
    public List<Module> getModuleOfCsp(long cspId);
    public Long getDefaultModuleId(long cspId);
    public void saveModuleToCsp(List<CspModule> cspModules, String moduleIds, long cspId, long defaultModuleId);
    public List<CspModule> getModulesByCspId(long cspId);
    public CspModule getDefaultModule(long cspId);
}
