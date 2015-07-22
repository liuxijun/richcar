package com.fortune.rms.business.csp.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.csp.model.CspCsp;

import java.util.List;

public interface CspCspDaoInterface extends BaseDaoInterface<CspCsp, Long> {
    public List<Csp> getCpOfSp(long spId);
    public List<Csp> getCpsBySpId(long spId);
}