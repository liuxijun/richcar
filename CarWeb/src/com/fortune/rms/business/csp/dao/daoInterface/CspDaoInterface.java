package com.fortune.rms.business.csp.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.csp.model.Csp;

import java.util.List;

public interface CspDaoInterface extends BaseDaoInterface<Csp, Long> {

    public List<Csp> getAllSp();
    public List getAllCp();
    public boolean existSpId(String spId);
    public Csp getCspBySpId(String spId);
    public List<Csp> getCspByCpCode(String cpCode);
    public List<Csp> getCpsOfStatus(int status);
    public Csp getCspIdByName(String name);
    public Csp getSpIdByContentId(long contentId);
    public Csp getSpIdByProductId(long productId);
}