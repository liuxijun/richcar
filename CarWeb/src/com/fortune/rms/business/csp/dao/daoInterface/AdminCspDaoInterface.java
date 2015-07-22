package com.fortune.rms.business.csp.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.csp.model.AdminCsp;
import com.fortune.util.PageBean;

import java.util.List;

public interface AdminCspDaoInterface extends BaseDaoInterface<AdminCsp, Long> {
     public int setDefaultCsp(int cspId, int adminId);

     public List<Object[]> searchCspName(AdminCsp adminCsp, PageBean pageBean);
}