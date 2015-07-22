package com.fortune.rms.business.csp.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.csp.dao.daoInterface.CspAuditorDaoInterface;
import com.fortune.rms.business.csp.model.CspAuditor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class CspAuditorDaoAccess extends BaseDaoAccess<CspAuditor, Long>
		implements
			CspAuditorDaoInterface {

	public CspAuditorDaoAccess() {
		super(CspAuditor.class);
	}

    public List<Admin> getAllAdminOfCsp(long cspId) {
        String hqlStr = "from Admin a";
        if(cspId>0){
            hqlStr+= " where a.id in(select ca.adminId from CspAuditor ca where ca.cspId="+cspId+").";
        }
        return this.getHibernateTemplate().find(hqlStr);
    }

    public List<CspAuditor> getAuditorsByCspId(long cspId) {
       String hqlStr ="from CspAuditor ca where ca.cspId="+cspId+"";
        return this.getHibernateTemplate().find(hqlStr);
    }

    public void deleteCspAuditorByParam(String adminIds, long cspId) {
        String hqlStr ="delete from CspAuditor ca where ca.cspId="+cspId+" and ca.adminId in("+adminIds+")";
        executeUpdate(hqlStr);

        //this.get
    }
}
