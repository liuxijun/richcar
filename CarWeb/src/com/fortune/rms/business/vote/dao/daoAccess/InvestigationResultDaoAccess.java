package com.fortune.rms.business.vote.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.vote.dao.daoInterface.InvestigationResultDaoInterface;
import com.fortune.rms.business.vote.model.InvestigationResult;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ����· on 2014/12/24.
 */
@Repository
public class InvestigationResultDaoAccess extends BaseDaoAccess<InvestigationResult, Long> implements InvestigationResultDaoInterface {
    public InvestigationResultDaoAccess() {
        super(InvestigationResult.class);
    }

    /**
     * ɾ����id�����е��ʾ�
     * @param investIdArray ���ŷָ����ʾ�Id
     */
    public void removeByInvestArray(String investIdArray){
        String hql = "delete from InvestigationResult ir where ir.investigationId in (" + investIdArray + ")";
        executeUpdate(hql);
    }

    /**
     * ����ʾ�ĸ���
     * @param investIdArray �ʾ�id���У����ŷָ�
     * @return �ʾ�id���ʾ���������
     */
    public List<Object[]> getInvestigationPoll(String investIdArray){
        String hql = "select ir.investigationId, count(ir.investigationId) from InvestigationResult ir where ir.investigationId in (" +
                investIdArray + ") group by ir.investigationId";
        return getHibernateTemplate().find(hql);
    }

    /**
     * ���һ���ʾ���ʾ����
     * @param id �ʾ�Id
     * @return ����
     */
    public long getInvestigationTotalPoll(Long id){
        String hql =  "select count(*) from InvestigationResult ir where ir.investigationId=" + id;
        List<Object> objectList = getHibernateTemplate().find(hql);
        if( objectList != null && objectList.size()>0){
            return StringUtils.string2long(objectList.get(0).toString(), 0);
        }else{
            return 0;
        }
    }

    /**
     * ���һ���ʾ��ƽ����ʱ
     * @param id �ʾ�Id
     * @return ƽ����ʱ����λ��
     */
    public long getInvestigationAverageDuration(Long id){
        String hql =  "select avg(ir.duration) from InvestigationResult ir where ir.investigationId=" + id;
        List<Object> objectList = getHibernateTemplate().find(hql);
        if( objectList != null && objectList.size()>0){
            try {
                return (long)Double.parseDouble(objectList.get(0).toString());
            }catch(Exception e){ return 0;}
        }else{
            return 0;
        }
    }

    /**
     * �鿴�ʾ���
     * @param id            �ʾ�Id
     * @param pageBean      ��ҳ
     * @param searchWord  ��ѯ�ؼ��ʣ��û�Id������
     * @return InvestigationResult�б�
     */
    public List<InvestigationResult> getInvestResult(Long id, PageBean pageBean, String searchWord){
        String hql = "from InvestigationResult ir where ir.investigationId=" + id;
        if(searchWord != null && !searchWord.isEmpty()){
            hql += " and ir.userId in (select u.userId from FrontUser u where u.userId like '%" + searchWord +
                    "%' or u.name like '%" + searchWord + "%')";
        }

        try {
            return getObjects(hql, pageBean);
        } catch (Exception e) {
            return new ArrayList<InvestigationResult>();
        }
    }
}
