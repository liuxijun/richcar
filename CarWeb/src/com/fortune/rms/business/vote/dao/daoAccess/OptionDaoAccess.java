package com.fortune.rms.business.vote.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.vote.dao.daoInterface.OptionDaoInterface;
import com.fortune.rms.business.vote.model.Option;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ����· on 2014/12/24.
 */
@Repository
public class OptionDaoAccess extends BaseDaoAccess<Option, Long> implements OptionDaoInterface {
    public OptionDaoAccess() {
        super(Option.class);
    }

    /**
     * ����ͶƱId����ɾ��ѡ��
     * @param voteIdArray ͶƱId����
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
     * �����ʾ�Idɾ����������ѡ��
     * @param investIdArray �ʾ�Id����
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
     * ɾ�������ѡ��
     * @param questionId ����Id
     */
    public void removeByQuestion(Long questionId){
        executeUpdate("delete from Option o where o.questionId=" + questionId);
    }

    /**
     * ��ȡ�ʾ����������ѡ�����sequence�ź���
     * @param investId �ʾ�Id
     * @return ѡ���б�
     */
    public List<Option> getInvestQuestionOption(Long investId){
        String hql = "from Option o where o.questionId in (" +
                "select iq.questionId from InvestigationQuestion iq where iq.investigationId=" + investId +
                ") order by o.questionId, o.sequence";
        return getHibernateTemplate().find(hql);
    }
}
