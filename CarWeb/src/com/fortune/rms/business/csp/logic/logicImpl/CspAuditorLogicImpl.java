package com.fortune.rms.business.csp.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.csp.dao.daoInterface.CspAuditorDaoInterface;
import com.fortune.rms.business.csp.logic.logicInterface.CspAuditorLogicInterface;
import com.fortune.rms.business.csp.model.CspAuditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("cspAuditorLogicInterface")
public class CspAuditorLogicImpl extends BaseLogicImpl<CspAuditor>
		implements
			CspAuditorLogicInterface {
	private CspAuditorDaoInterface cspAuditorDaoInterface;

	/**
	 * @param cspAuditorDaoInterface the cspAuditorDaoInterface to set
	 */
    @Autowired
	public void setCspAuditorDaoInterface(
			CspAuditorDaoInterface cspAuditorDaoInterface) {
		this.cspAuditorDaoInterface = cspAuditorDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.cspAuditorDaoInterface;
	}

    public List<Admin> getAllAdminOfCsp(long cspId) {

        return cspAuditorDaoInterface.getAllAdminOfCsp(cspId);
    }

    public void saveAuditorToCsp(List<Admin> admins) {
        for(int i=0;i<admins.size();i++){
            CspAuditor ca = admins.get(i).getCspAuditor();

            this.cspAuditorDaoInterface.save(ca);
        }
        
    }

    public List<CspAuditor> getAuditorsByCspId(long cspId) {
        return this.cspAuditorDaoInterface.getAuditorsByCspId(cspId);
    }

    public void saveAuditorToCsp(List<Admin> admins, String adminIds, long cspId) {
        this.cspAuditorDaoInterface.deleteCspAuditorByParam(adminIds,cspId);
        for(int i=0;i<admins.size();i++){
            CspAuditor ca = admins.get(i).getCspAuditor();
            this.cspAuditorDaoInterface.save(ca);
        }
    }
}
