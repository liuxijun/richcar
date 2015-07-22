package com.fortune.rms.business.module.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.csp.logic.logicInterface.CspModuleLogicInterface;
import com.fortune.rms.business.csp.model.CspModule;
import com.fortune.rms.business.module.dao.daoInterface.ModuleDaoInterface;
import com.fortune.rms.business.module.logic.logicInterface.ModuleLogicInterface;
import com.fortune.rms.business.module.logic.logicInterface.ModulePropertyLogicInterface;
import com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface;
import com.fortune.rms.business.module.model.Module;
import com.fortune.rms.business.module.model.ModuleProperty;
import com.fortune.rms.business.module.model.Property;
import com.fortune.util.AppConfigurator;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("moduleLogicInterface")
public class ModuleLogicImpl extends BaseLogicImpl<Module>
		implements
			ModuleLogicInterface {
	private ModuleDaoInterface moduleDaoInterface;
    private ModulePropertyLogicInterface modulePropertyLogicInterface;
    private CspModuleLogicInterface cspModuleLogicInterface;

	/**
	 * @param moduleDaoInterface the moduleDaoInterface to set
	 */
    @Autowired
	public void setModuleDaoInterface(ModuleDaoInterface moduleDaoInterface) {
		this.moduleDaoInterface = moduleDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.moduleDaoInterface;
	}

    @Autowired
    public void setCspModuleLogicInterface(CspModuleLogicInterface cspModuleLogicInterface) {
        this.cspModuleLogicInterface = cspModuleLogicInterface;
    }

    @Autowired
    public void setModulePropertyLogicInterface(ModulePropertyLogicInterface modulePropertyLogicInterface) {
        this.modulePropertyLogicInterface = modulePropertyLogicInterface;
    }

    public List<Module> getModulesOfStatus(Module module, PageBean pageBean) {
        String hql = "status=1";
        List<Module> modules = super.search(module, pageBean, hql);
        return modules;
    }

    public Module saveModule(Module module, List<String> propertyIds) {
        module = save(module);
        if(AppConfigurator.getInstance().getBoolConfig("system.compactMode",false)){
            //如果是简洁模式，则需要同步保存模版和csp的对应关系
            CspModule cspModule = new CspModule();
            cspModule.setCspId(2L);
            cspModule.setModuleId(module.getId());
            List<CspModule> cspModules = cspModuleLogicInterface.search(cspModule);
            if(cspModules==null||cspModules.size()==0){
                cspModuleLogicInterface.save(cspModule);
            }
        }
        long moduleId = module.getId();
        ModuleProperty mp = new ModuleProperty();
        mp.setModuleId(moduleId);
        List<ModuleProperty> oldData = modulePropertyLogicInterface.search(mp);
        long index = 0;
        for(String key:propertyIds){
            long propertyId = StringUtils.string2long(key, -1);
            if(propertyId>0){
                boolean dataSaved = false;
                for(int j=oldData.size()-1;j>=0;j--){
                    ModuleProperty data = oldData.get(j);
                    if(data.getPropertyId()==propertyId){
                        data.setDisplayOrder(index);
                        data.setStatus(PropertyLogicInterface.STATUS_ON);
                        modulePropertyLogicInterface.save(data);
                        oldData.remove(j);
                        dataSaved = true;
                    }
                }
                if(!dataSaved){
                    ModuleProperty data = new ModuleProperty();
                    data.setModuleId(moduleId);
                    data.setDisplayOrder(index);
                    data.setStatus(PropertyLogicInterface.STATUS_ON);
                    data.setPropertyId(propertyId);
                    modulePropertyLogicInterface.save(data);
                }
                index++;
            }
        }
        for(ModuleProperty data:oldData){
            modulePropertyLogicInterface.remove(data);
        }
        return module;
    }
}
