package com.fortune.rms.business.vote.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.vote.model.Option;
import com.fortune.rms.business.vote.model.Question;

import java.util.List;

/**
 * Created by ÍõÃ÷Â· on 2014/12/24.
 */
public interface QuestionDaoInterface   extends BaseDaoInterface<Question, Long> {
    public List<Option> getQuestionOptionList(Long questionId);
    public void clearOption(String reservedIds, String currentIds);
    public void removeByVote(String voteIdArray);
    public void removeByInvest(String investIdArray);
    public List<Question> getInvestQuestionList(Long investId, String exceptedIdArray);
}
