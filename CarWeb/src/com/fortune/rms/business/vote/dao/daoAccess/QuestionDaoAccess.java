package com.fortune.rms.business.vote.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.vote.dao.daoInterface.QuestionDaoInterface;
import com.fortune.rms.business.vote.model.Option;
import com.fortune.rms.business.vote.model.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ����· on 2014/12/24.
 */
@Repository
public class QuestionDaoAccess extends BaseDaoAccess<Question, Long> implements QuestionDaoInterface {
    public QuestionDaoAccess() {
        super(Question.class);
    }

    /**
     * ��ѯ�����ѡ��
     * @param questionId ����Id
     * @return ѡ���б�
     */
    public List<Option> getQuestionOptionList(Long questionId){
        String hql = "from Option op where op.questionId=" + questionId + " order by op.sequence";
        return getHibernateTemplate().find(hql);
    }

    /**
     * �������ѡ�ѡ��ͶƱ���
     * @param reservedIds Ҫ������ѡ��Id
     * @param currentIds �������������ѡ��Id
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
     * ����ͶƱIdɾ������
     * @param voteIdArray ͶƱId����
     */
    public void removeByVote(String voteIdArray){
        String hql = "delete from Question q where q.id in (" +
                "select v.questionId from Vote v where v.id in (" + voteIdArray + "))";
        executeUpdate(hql);
    }

    /**
     * �����ʾ�Idɾ������
     * @param investIdArray �ʾ�Id����
     */
    public void removeByInvest(String investIdArray){
        String hql = "delete from Question q where q.id in (" +
                "select iq.questionId from InvestigationQuestion iq where iq.investigationId in (" + investIdArray + "))";
        executeUpdate(hql);
    }

    /**
     * ����ʾ�������б�exceptedIdArray�е�����
     * @param investId          �ʾ�Id
     * @param exceptedIdArray  ������������Id
     * @return question�б�
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
