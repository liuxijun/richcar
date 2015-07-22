package com.fortune.rms.business.csp.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.csp.dao.daoInterface.CspModuleDaoInterface;
import com.fortune.rms.business.csp.logic.logicInterface.CspModuleLogicInterface;
import com.fortune.rms.business.csp.model.CspModule;
import com.fortune.rms.business.module.model.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("cspModuleLogicInterface")
public class CspModuleLogicImpl extends BaseLogicImpl<CspModule>
		implements
			CspModuleLogicInterface {
	private CspModuleDaoInterface cspModuleDaoInterface;

	/**
	 * @param cspModuleDaoInterface the cspModuleDaoInterface to set
	 */
    @Autowired
	public void setCspModuleDaoInterface(
			CspModuleDaoInterface cspModuleDaoInterface) {
		this.cspModuleDaoInterface = cspModuleDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.cspModuleDaoInterface;
	}

    public List<Module> getModuleOfCsp(long cspId) {
        return cspModuleDaoInterface.getModuleOfCsp(cspId);
    }

    public Long getDefaultModuleId(long cspId) {
        return cspModuleDaoInterface.getDefaultModule(cspId);
    }


    public void saveModuleToCsp(List<CspModule> cspModules, String moduleIds, long cspId,long defaultModuleId) {
         if(defaultModuleId!=0){
             this.cspModuleDaoInterface.deleteCspModuleIsDefault(defaultModuleId,cspId);
         }
         this.cspModuleDaoInterface.deleteCspModuleByParam(moduleIds,cspId);
        for(int i=0;i<cspModules.size();i++){
            CspModule cm = cspModules.get(i);
            this.cspModuleDaoInterface.save(cm);
        }
    }



    public List<CspModule> getModulesByCspId(long cspId) {
        return this.cspModuleDaoInterface.getModulesByCspId(cspId);
    }

    public CspModule getDefaultModule(long cspId){
        CspModule cm = new CspModule();
        cm.setCspId(cspId);
        cm.setIsDefault(1l);
        try{
            List list1 = cspModuleDaoInterface.getObjects(cm) ;
            if (list1!=null && list1.size()>0){
                CspModule cspModule = (CspModule)list1.get(0);
                return cspModule;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
