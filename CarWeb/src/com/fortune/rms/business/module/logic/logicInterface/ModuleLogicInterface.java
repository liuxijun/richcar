package com.fortune.rms.business.module.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.module.model.Module;
import com.fortune.util.PageBean;

import java.util.List;

public interface ModuleLogicInterface extends BaseLogicInterface<Module> {
    public List<Module> getModulesOfStatus(Module module, PageBean pageBean);
    public Module saveModule(Module module, List<String> propertyIds);
}
