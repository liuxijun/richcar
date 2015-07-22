package com.fortune.rms.business.vote.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.vote.model.OptionResult;

import java.util.List;

/**
 * Created by ÍõÃ÷Â· on 2014/12/24.
 */
public interface OptionResultDaoInterface   extends BaseDaoInterface<OptionResult, Long> {
    public void removeByVoteIdArray(String voteIdArray);
    public void removeByInvestigationIdArray(String investIdArray);
    public List<Object[]> getOptionPoll(Long questionId);
    public void removeByQuestion(Long questionId);
    public List<Object[]> getInvestOptionPoll(Long investId);
}
