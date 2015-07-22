package com.fortune.rms.business.vote.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.vote.dao.daoInterface.InvestigationQuestionDaoInterface;
import com.fortune.rms.business.vote.model.InvestigationQuestion;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ����· on 2014/12/24.
 */
@Repository
public class InvestigationQuestionDaoAccess extends BaseDaoAccess<InvestigationQuestion, Long> implements InvestigationQuestionDaoInterface {
    public InvestigationQuestionDaoAccess() {
        super(InvestigationQuestion.class);
    }

    /**
     * �����ʾ�����ɾ��������ϵ
     * @param idArray �ʾ�Id����
     */
    public void removeByInvestigation(String idArray){
        String hql = "delete from InvestigationQuestion iq where iq.investigationId in (" + idArray + ")";
        executeUpdate(hql);
    }

    /**
     * �����ʾ�Id���л���ʾ��������
     * @param idArray �Զ��ŷָ���Id����
     * @return id������
     */
    public List<Object[]> getInvestQuestionCount(String idArray){
        String hql = "select iq.investigationId, count(*) from InvestigationQuestion iq where iq.investigationId in (" +
                idArray + ") group by iq.investigationId";
        return getHibernateTemplate().find(hql);
    }
}
