package com.fortune.rms.business.vote.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.vote.model.Option;
import com.fortune.rms.business.vote.model.Question;

import java.util.List;

/**
 * Created by ÍõÃ÷Â· on 2014/12/29.
 */
public interface QuestionLogicInterface  extends BaseLogicInterface<Question> {
    public Option saveOption(Option option);
    public void saveOptions(List<Option> optionList);
    public Question saveQuestion(Question question);
    public void removeQuestionByVote(String voteIdArray);
    public void removeQuestionByInvest(String investIdArray);
    public void removeInvestQuestion(Long investId, String exceptedIdArray);
    public List<Question> getInvestigationQuestion(Long investId, boolean stat);

}
