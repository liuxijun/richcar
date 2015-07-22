package com.fortune.rms.business.csp.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.csp.dao.daoInterface.CspModuleDaoInterface;
import com.fortune.rms.business.csp.model.CspModule;
import com.fortune.rms.business.module.model.Module;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class CspModuleDaoAccess extends BaseDaoAccess<CspModule, Long>
		implements
			CspModuleDaoInterface {

	public CspModuleDaoAccess() {
		super(CspModule.class);
	}

    public List<Module> getModuleOfCsp(long cspId) {
        String hqlStr="from Module m where m.status=1";
        if(cspId>0){
               hqlStr +=" and m.id in (select cm.moduleId from CspModule cm where cm.cspId="+cspId+")";
        }
        return this.getHibernateTemplate().find(hqlStr);
    }

    public Long getDefaultModule(long cspId) {
        String hqlStr="from CspModule cm where cm.cspId="+cspId+" and cm.isDefault=1";
        return ((CspModule)this.getHibernateTemplate().find(hqlStr).get(0)).getCspId();
    }

    public List<CspModule> getModulesByCspId(long cspId) {
            String hqlStr="from CspModule cm where cm.cspId="+cspId+"";
        return this.getHibernateTemplate().find(hqlStr); 
    }

    public void deleteCspModuleByParam(String moduleIds, long cspId) {
        String hqlStr ="delete from CspModule cm where cm.cspId="+cspId+" and cm.moduleId in("+moduleIds+")";
        executeUpdate(hqlStr);
    }

    public void deleteCspModuleIsDefault(long defaultModuleId) {
       String hqlStr ="delete from CspModule cm where cm.isDefault=1";
       executeUpdate(hqlStr);
    }

    public void deleteCspModuleIsDefault(long defaultModuleId, long cspId) {
       String hqlStr ="delete from CspModule cm where cm.isDefault=1 and cm.cspId="+cspId+"";
       executeUpdate(hqlStr);
    }
}
