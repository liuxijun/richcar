package com.fortune.rms.business.csp.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.csp.model.CspAuditor;

import java.util.List;

public interface CspAuditorDaoInterface
		extends
			BaseDaoInterface<CspAuditor, Long> {
     public List<Admin> getAllAdminOfCsp(long cspId);
     public List<CspAuditor> getAuditorsByCspId(long cspId);
     public void deleteCspAuditorByParam(String adminIds, long cspId);

}