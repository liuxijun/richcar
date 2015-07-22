package com.fortune.rms.business.csp.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.common.business.security.model.Role;
import com.fortune.rms.business.csp.model.AdminCsp;
import com.fortune.util.PageBean;

import java.util.List;

public interface AdminCspLogicInterface extends BaseLogicInterface<AdminCsp> {
    public List<Role> getRolesOfCspWithinAdmin(Integer cspId, Admin admin);
    public AdminCsp save(AdminCsp obj, Integer cspId, List<Integer> roleIds);

    public List<AdminCsp> searchCspName(AdminCsp adminCsp, PageBean pageBean) ;
}
