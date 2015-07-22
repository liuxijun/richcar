package com.fortune.rms.business.vote.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.vote.model.*;
import com.fortune.util.PageBean;

import java.util.List;

/**
 * Created by ÍõÃ÷Â· on 2015/1/7.
 */
public interface InvestigationLogicInterface extends BaseLogicInterface<Investigation> {
    public List<Investigation> searchInvestigation(String searchWord, PageBean pageBean);
    public Investigation saveInvestigation(Investigation invest);
    public Investigation getInvestigation(Long id);
    public Investigation getInvestigationOfUser(Long id, String userId);
    public void removeInvestigations(String idArray);
    public void setStatus(Investigation invest);
    public boolean saveInvestResult(InvestigationResultDTO investResult);
    public InvestigationStat getInvestStat(Long id);
    public List<InvestUser> getInvestUser(Long id, PageBean pageBean, String searchWord);
}
