package com.fortune.rms.business.csp.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.csp.model.CspAuditor;

import java.util.List;

public interface CspAuditorLogicInterface
		extends
			BaseLogicInterface<CspAuditor> {
    public List<Admin> getAllAdminOfCsp(long cspId);
    public void saveAuditorToCsp(List<Admin> admins);
    public List<CspAuditor> getAuditorsByCspId(long cspId);
    public void saveAuditorToCsp(List<Admin> admins, String adminIds, long cspId);

}
