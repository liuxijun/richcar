package com.fortune.rms.business.vote.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.vote.model.Option;

import java.util.List;

/**
 * Created by ÍõÃ÷Â· on 2014/12/24.
 */
public interface OptionDaoInterface extends BaseDaoInterface<Option, Long> {
    public void removeByVoteArray(String voteIdArray);
    public void removeByInvestArray(String investIdArray);
    public void removeByQuestion(Long questionId);
    public List<Option> getInvestQuestionOption(Long investId);
}
