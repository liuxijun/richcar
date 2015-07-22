package com.fortune.rms.business.csp.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.csp.model.CspCsp;

import java.util.List;

public interface CspCspLogicInterface extends BaseLogicInterface<CspCsp> {
    public void saveCspToCsp(List<Long> cspIds, long cspId);
    public List<Csp> getCpsOfStatus(long spId);
    public List<Csp> getCpsBySpId(long spId);
}
