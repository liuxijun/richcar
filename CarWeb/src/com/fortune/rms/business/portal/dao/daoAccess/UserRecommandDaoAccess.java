package com.fortune.rms.business.portal.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.portal.dao.daoInterface.UserRecommandDaoInterface;
import com.fortune.rms.business.portal.model.UserRecommand;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class UserRecommandDaoAccess extends BaseDaoAccess<UserRecommand, Long>
        implements
        UserRecommandDaoInterface {

    public UserRecommandDaoAccess() {
        super(UserRecommand.class);
    }

    public List<Object[]> getAllUserRecommand(UserRecommand userRecommand, PageBean pageBean) {
        Session session = null;
        Query query = null;
        try{
            session = getSession();
            String hql = " from UserRecommand ur,Content c,Csp csp where ur.cspId=csp.id and ur.contentId =c.id";
            String hql_count = "select count(*) from (select * from user_recommand ur,content c,csp csp where ur.csp_Id=csp.id and ur.content_Id =c.id";
            if (userRecommand != null) {
                if (userRecommand.getContentId() != null) {
                    hql += " and ur.contentId =" + userRecommand.getContentId() + "";
                    hql_count += " and ur.content_Id=" + userRecommand.getContentId() + "";
                }
            }

            hql += " order by ur.id desc";
            hql_count += ")";

            Query query_count = session.createSQLQuery(hql_count);

            String count =query_count.list().get(0).toString();


            pageBean.setRowCount(new Integer(count));

            query = session.createQuery(hql);

            if (pageBean != null) {
                query.setFirstResult(pageBean.getStartRow());
                query.setMaxResults(pageBean.getPageSize());
            }

        }catch(Exception e){

        }finally {
             if(session!=null){
                session.close();
             }
        }

        return query.list();
    }


    public SearchResult<UserRecommand> getUserRecommandCount(UserRecommand userRecommand, PageBean pageBean) {
        Session session = null;
        SearchResult<UserRecommand> result = new SearchResult<UserRecommand>();
        try{
            session = getSession();
            String hql = "select c.name as contentName,count(ur.userIp) as userCount,ur.contentId as contentId from UserRecommand ur,Content c where ur.contentId=c.id";
            List params = new ArrayList();
            if (userRecommand != null) {
                if (userRecommand.getContentName() != null) {
                    hql += " and c.name like ?";
                    params.add("%" + userRecommand.getContentName().trim() + "%");
                }


            }
            hql +=" group by ur.contentId,c.name";


            int count = this.getHibernateTemplate().find(hql, params.toArray()).size();
            result.setRowCount(count);
            pageBean.setRowCount(result.getRowCount());
            Query query = session.createQuery(hql);
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i, params.get(i));
            }
            if (pageBean != null) {
                query.setFirstResult(pageBean.getStartRow());
                query.setMaxResults(pageBean.getPageSize());
            }
            List<UserRecommand> qResult = query.list();
            result.setRows(qResult);
        }catch(Exception e){

        }finally {
             if(session!=null){
                session.close();
             }
        }

        return result;
    }

}
