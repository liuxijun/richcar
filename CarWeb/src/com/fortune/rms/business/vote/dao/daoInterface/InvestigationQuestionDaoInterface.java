package com.fortune.rms.business.vote.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.vote.model.InvestigationQuestion;

import java.util.List;

/**
 * Created by ÍõÃ÷Â· on 2014/12/24.
 */
public interface InvestigationQuestionDaoInterface   extends BaseDaoInterface<InvestigationQuestion, Long> {
    public void removeByInvestigation(String idArray);
    public List<Object[]> getInvestQuestionCount(String idArray);
}
