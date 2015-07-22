package com.fortune.rms.business.vote.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.vote.dao.daoInterface.OptionResultDaoInterface;
import com.fortune.rms.business.vote.model.OptionResult;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ����· on 2014/12/24.
 */
@Repository
public class OptionResultDaoAccess extends BaseDaoAccess<OptionResult, Long> implements OptionResultDaoInterface {
    public OptionResultDaoAccess() {
        super(OptionResult.class);
    }

    /**
     * ����ͶƱid����ɾ��ѡ��ͶƱ���
     * @param voteIdArray id����
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
     * �����ʾ�Id����ɾ���ʾ���
     * @param investIdArray ���ŷָ���Id����
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
     * ɾ��������ص�ͶƱ���
     * @param questionId ����Id
     */
    public void removeByQuestion(Long questionId){
        String hql = "delete from OptionResult o_r where o_r.optionId in (" +
                "select o.id from Option o where o.questionId=" + questionId + ")";
        executeUpdate(hql);
    }
    /**
     * ��ѯѡ��ĵ�Ʊ��
     * @param questionId ����Id
     * @return ѡ��Id�͵�Ʊ��
     */
    public List<Object[]> getOptionPoll(Long questionId){
        String hql = "select o_r.optionId, count(*) from OptionResult o_r where o_r.optionId in (" +
                "select op.id from Option op where op.questionId=" + questionId + ")" +
                " group by o_r.optionId";
        return getHibernateTemplate().find(hql);
    }

    /**
     * ��ȡ�ʾ�ѡ��ͳ�ƽ��
     * @param investId �ʾ�Id
     * @return option_id����Ʊ��
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
