package com.fortune.rms.business.vote.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.vote.dao.daoInterface.OptionResultDaoInterface;
import com.fortune.rms.business.vote.model.OptionResult;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 王明路 on 2014/12/24.
 */
@Repository
public class OptionResultDaoAccess extends BaseDaoAccess<OptionResult, Long> implements OptionResultDaoInterface {
    public OptionResultDaoAccess() {
        super(OptionResult.class);
    }

    /**
     * 根据投票id序列删除选项投票结果
     * @param voteIdArray id序列
     */
    public void removeByVoteIdArray(String voteIdArray){
        String hql = "delete from OptionResult o_r where o_r.optionId in (" +
                "select o.id from Option o where o.questionId in (" +
                "select v.questionId from Vote v where v.id in(" +
                voteIdArray +
                ")" +
                ")" +
                ")";
        executeUpdate(hql);
    }

    /**
     * 根据问卷Id序列删除问卷结果
     * @param investIdArray 逗号分隔的Id序列
     */
    public void removeByInvestigationIdArray(String investIdArray){
        String hql = "delete from OptionResult o_r where o_r.optionId in (" +
                "select o.id from Option o where o.questionId in (" +
                "select iq.questionId from InvestigationQuestion iq where iq.investigationId in(" +
                investIdArray +
                ")" +
                ")" +
                ")";
        executeUpdate(hql);
    }

    /**
     * 删除问题相关的投票结果
     * @param questionId 问题Id
     */
    public void removeByQuestion(Long questionId){
        String hql = "delete from OptionResult o_r where o_r.optionId in (" +
                "select o.id from Option o where o.questionId=" + questionId + ")";
        executeUpdate(hql);
    }
    /**
     * 查询选项的得票数
     * @param questionId 问题Id
     * @return 选项Id和得票数
     */
    public List<Object[]> getOptionPoll(Long questionId){
        String hql = "select o_r.optionId, count(*) from OptionResult o_r where o_r.optionId in (" +
                "select op.id from Option op where op.questionId=" + questionId + ")" +
                " group by o_r.optionId";
        return getHibernateTemplate().find(hql);
    }

    /**
     * 获取问卷选项统计结果
     * @param investId 问卷Id
     * @return option_id，得票数
     */
    public List<Object[]> getInvestOptionPoll(Long investId){
        //select option_id, count(option_id) from option_result where option_id in (select id from question_option
        //where question_id in (select question_id from investigation_question where investigation_id=16)) group by option_id
        String hql = "select optionResult.optionId, count(*) from OptionResult optionResult where " +
                "optionResult.optionId in (select o.id from Option o where o.questionId in (" +
                "select iq.questionId from InvestigationQuestion iq where iq.investigationId=" + investId +
                ")) group by optionResult.optionId";
        return getHibernateTemplate().find(hql);
    }
}
