package com.fortune.rms.business.vote.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.vote.dao.daoInterface.InvestigationQuestionDaoInterface;
import com.fortune.rms.business.vote.model.InvestigationQuestion;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 王明路 on 2014/12/24.
 */
@Repository
public class InvestigationQuestionDaoAccess extends BaseDaoAccess<InvestigationQuestion, Long> implements InvestigationQuestionDaoInterface {
    public InvestigationQuestionDaoAccess() {
        super(InvestigationQuestion.class);
    }

    /**
     * 根据问卷序列删除关联关系
     * @param idArray 问卷Id序列
     */
    public void removeByInvestigation(String idArray){
        String hql = "delete from InvestigationQuestion iq where iq.investigationId in (" + idArray + ")";
        executeUpdate(hql);
    }

    /**
     * 根据问卷Id序列获得问卷的问题书
     * @param idArray 以逗号分隔的Id序列
     * @return id和数字
     */
    public List<Object[]> getInvestQuestionCount(String idArray){
        String hql = "select iq.investigationId, count(*) from InvestigationQuestion iq where iq.investigationId in (" +
                idArray + ") group by iq.investigationId";
        return getHibernateTemplate().find(hql);
    }
}
