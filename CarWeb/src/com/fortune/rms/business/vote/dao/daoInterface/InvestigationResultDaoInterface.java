package com.fortune.rms.business.vote.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.vote.model.InvestigationResult;
import com.fortune.util.PageBean;

import java.util.List;

/**
 * Created by ÍõÃ÷Â· on 2014/12/24.
 */
public interface InvestigationResultDaoInterface   extends BaseDaoInterface<InvestigationResult, Long> {
    public void removeByInvestArray(String investIdArray);
    public List<Object[]> getInvestigationPoll(String investIdArray);
    public long getInvestigationTotalPoll(Long id);
    public long getInvestigationAverageDuration(Long id);
    public List<InvestigationResult> getInvestResult(Long id, PageBean pageBean, String searchWord);
}
