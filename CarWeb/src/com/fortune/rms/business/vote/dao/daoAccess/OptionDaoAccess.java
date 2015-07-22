package com.fortune.rms.business.vote.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.vote.dao.daoInterface.OptionDaoInterface;
import com.fortune.rms.business.vote.model.Option;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 王明路 on 2014/12/24.
 */
@Repository
public class OptionDaoAccess extends BaseDaoAccess<Option, Long> implements OptionDaoInterface {
    public OptionDaoAccess() {
        super(Option.class);
    }

    /**
     * 根据投票Id序列删除选项
     * @param voteIdArray 投票Id序列
     */
    public void removeByVoteArray(String voteIdArray){
        String hql = "delete from Option o where o.questionId in (" +
                "select v.questionId from Vote v where v.id in(" +
                voteIdArray +
                ")" +
                ")";
        executeUpdate(hql);
    }

    /**
     * 根据问卷Id删除所属问题选项
     * @param investIdArray 问卷Id序列
     */
    public void removeByInvestArray(String investIdArray){
        String hql = "delete from Option o where o.questionId in (" +
                "select iq.questionId from InvestigationQuestion iq where iq.investigationId in(" +
                investIdArray +
                ")" +
                ")";
        executeUpdate(hql);
    }

    /**
     * 删除问题的选项
     * @param questionId 问题Id
     */
    public void removeByQuestion(Long questionId){
        executeUpdate("delete from Option o where o.questionId=" + questionId);
    }

    /**
     * 获取问卷的所有问题选项，按照sequence排好序
     * @param investId 问卷Id
     * @return 选项列表
     */
    public List<Option> getInvestQuestionOption(Long investId){
        String hql = "from Option o where o.questionId in (" +
                "select iq.questionId from InvestigationQuestion iq where iq.investigationId=" + investId +
                ") order by o.questionId, o.sequence";
        return getHibernateTemplate().find(hql);
    }
}
