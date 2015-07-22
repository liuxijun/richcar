package com.fortune.rms.business.vote.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.vote.dao.daoInterface.QuestionDaoInterface;
import com.fortune.rms.business.vote.model.Option;
import com.fortune.rms.business.vote.model.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 王明路 on 2014/12/24.
 */
@Repository
public class QuestionDaoAccess extends BaseDaoAccess<Question, Long> implements QuestionDaoInterface {
    public QuestionDaoAccess() {
        super(Question.class);
    }

    /**
     * 查询问题的选项
     * @param questionId 问题Id
     * @return 选项列表
     */
    public List<Option> getQuestionOptionList(Long questionId){
        String hql = "from Option op where op.questionId=" + questionId + " order by op.sequence";
        return getHibernateTemplate().find(hql);
    }

    /**
     * 清除问题选项及选项投票结果
     * @param reservedIds 要保留的选项Id
     * @param currentIds 现有问题的所有选项Id
     */
    public void clearOption(String reservedIds, String currentIds){
        if( reservedIds == null || reservedIds.isEmpty() ) reservedIds = "-1";
        if( currentIds == null || currentIds.isEmpty() ) currentIds = "-1";

        String hql = "delete from OptionResult o where o.optionId in (" + currentIds +
                ") and o.optionId not in (" + reservedIds + ")";
        executeUpdate(hql);
        hql = "delete from Option o where o.id in (" + currentIds + ") and o.id not in (" + reservedIds + ")";
        executeUpdate(hql);
    }

    /**
     * 根据投票Id删除问题
     * @param voteIdArray 投票Id序列
     */
    public void removeByVote(String voteIdArray){
        String hql = "delete from Question q where q.id in (" +
                "select v.questionId from Vote v where v.id in (" + voteIdArray + "))";
        executeUpdate(hql);
    }

    /**
     * 根据问卷Id删除问题
     * @param investIdArray 问卷Id序列
     */
    public void removeByInvest(String investIdArray){
        String hql = "delete from Question q where q.id in (" +
                "select iq.questionId from InvestigationQuestion iq where iq.investigationId in (" + investIdArray + "))";
        executeUpdate(hql);
    }

    /**
     * 获得问卷的问题列表，exceptedIdArray中的例外
     * @param investId          问卷Id
     * @param exceptedIdArray  不包含的问题Id
     * @return question列表
     */
    public List<Question> getInvestQuestionList(Long investId, String exceptedIdArray){
        String hql = "from Question q where q.id in (" +
                "select iq.questionId from InvestigationQuestion iq where iq.investigationId=" + investId +
                ")";
        if(exceptedIdArray != null && !exceptedIdArray.isEmpty()){
            hql += " and q.id not in (" + exceptedIdArray + ")";
        }

        return getHibernateTemplate().find(hql);
    }
}
