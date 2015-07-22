package com.fortune.rms.business.portal.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.portal.dao.daoInterface.UserReviewKeywordDaoInterface;
import com.fortune.rms.business.portal.model.UserReviewKeyword;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public class UserReviewKeywordDaoAccess extends BaseDaoAccess<UserReviewKeyword, Long>
        implements
        UserReviewKeywordDaoInterface {

    public UserReviewKeywordDaoAccess() {
        super(UserReviewKeyword.class);
    }

    public boolean isExistUserReviewKeyword(String word) {
        boolean isExistUserReviewKeyword = false;
        Session session = null;
        try {
            session = getSession();
            String sql = "select count(*) from user_review_keyword where word ='" + word + "'";
            Query query = session.createSQLQuery(sql);
            String count = query.list().get(0).toString();
            if (count.equals("0")) {
                isExistUserReviewKeyword = true;
            } else {
                isExistUserReviewKeyword = false;
            }
        } catch (Exception e) {

        } finally {
            if (session != null) {
                session.close();
            }
        }


        return isExistUserReviewKeyword;
    }

    public List<Object[]> getAllUserReviewwords(UserReviewKeyword userReviewKeyword, PageBean pageBean) {
//        Session session = null;
//        Query query = null;
//        try{
//            session = getSession();
//            SearchResult<UserReviewKeyword> result = new SearchResult<UserReviewKeyword>();
//            String hql="from UserReviewKeyword u_r_k, Csp c where c.id= u_r_k.cspId and c.isSp=1";
//            String hql_count="select count(*) from (select u_r_k.ID,u_r_k.CSP_ID,u_r_k.WORD,c.NAME from user_review_keyword u_r_k inner join csp c on c.ID= u_r_k.CSP_ID and c.IS_SP=1)";
//            Query query_count = session.createSQLQuery(hql_count);
//            int count = Integer.parseInt(query_count.list().get(0).toString());
//            result.setRowCount(count);
//            pageBean.setRowCount(result.getRowCount());
//            query = session.createQuery(hql);
//             if(pageBean!=null){
//                query.setFirstResult(pageBean.getStartRow());
//                query.setMaxResults(pageBean.getPageSize());
//            }
//        }catch(Exception e){
//                 e.printStackTrace();
//        }finally {
//             if(session!=null ){
//                session.close();
//             }
//        }
//
//       return query.list();
        String hql = "from UserReviewKeyword u_r_k, Csp c where c.id= u_r_k.cspId and c.isSp=1";
        List<Object> args = new ArrayList<Object>();

            if (userReviewKeyword.getCspId() != null) {
                hql += " and u_r_k.cspId=?";
                args.add(userReviewKeyword.getCspId());
            }
            if(userReviewKeyword.getWord()!=null){
                 hql +=" and u_r_k.word like ?";
                args.add("%"+userReviewKeyword.getWord().trim()+"%");
            }

        List result = null;
        try {
            if (args.size() > 0) {
                result = getObjects(hql, args.toArray(), pageBean);
            } else {
                result = getObjects(hql, null, pageBean);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Object[]>();
    }

    public List<UserReviewKeyword> getAllUserReviewwordsByCspId(long cspId) {
        String hql = "from UserReviewKeyword";
        return this.getHibernateTemplate().find(hql);
    }
}
