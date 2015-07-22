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
 * Created by 王明路 on 2014/12/24.
 */
@Repository
public class InvestigationResultDaoAccess extends BaseDaoAccess<InvestigationResult, Long> implements InvestigationResultDaoInterface {
    public InvestigationResultDaoAccess() {
        super(InvestigationResult.class);
    }

    /**
     * 删除在id序列中的问卷
     * @param investIdArray 逗号分隔的问卷Id
     */
    public void removeByInvestArray(String investIdArray){
        String hql = "delete from InvestigationResult ir where ir.investigationId in (" + investIdArray + ")";
        executeUpdate(hql);
    }

    /**
     * 获得问卷的个数
     * @param investIdArray 问卷id序列，逗号分隔
     * @return 问卷id和问卷数的数组
     */
    public List<Object[]> getInvestigationPoll(String investIdArray){
        String hql = "select ir.investigationId, count(ir.investigationId) from InvestigationResult ir where ir.investigationId in (" +
                investIdArray + ") group by ir.investigationId";
        return getHibernateTemplate().find(hql);
    }

    /**
     * 获得一个问卷的问卷个数
     * @param id 问卷Id
     * @return 个数
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
     * 获得一个问卷的平均耗时
     * @param id 问卷Id
     * @return 平均耗时，单位秒
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
     * 查看问卷结果
     * @param id            问卷Id
     * @param pageBean      分页
     * @param searchWord  查询关键词，用户Id和姓名
     * @return InvestigationResult列表
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
