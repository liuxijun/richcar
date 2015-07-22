package com.fortune.rms.business.portal.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.portal.dao.daoInterface.UserReviewDaoInterface;
import com.fortune.rms.business.portal.model.UserReview;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;
import com.fortune.util.config.Config;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Repository
public class UserReviewDaoAccess extends BaseDaoAccess<UserReview, Long>
		implements
			UserReviewDaoInterface {

	public UserReviewDaoAccess() {
		super(UserReview.class);
	}

    public List<Object[]> getAllUserReviews(UserReview userReview, PageBean pageBean) {
//        Session session = null;
//        Query query = null;
//        try{
//            session = getSession();
//            SearchResult<UserReview> result = new SearchResult<UserReview>();
//            String hql = "from UserReview ur,Content c where c.id=ur.contentId ";
//            String hql_count = "select count(*) from(select ur.*,c.name from user_review ur,content c where c.id=ur.content_id";
///*            if(userReview!=null){
//                if(userReview.getContentId()!=null){
//                     hql+=" and ur.contentId="+userReview.getContentId()+"";
//                     hql_count+=" and ur.content_id="+userReview.getContentId()+"";
//                }
//            }*/
//            hql+=" order by ur.id desc";
//            hql_count +=")";
//            Query  query_count =session.createSQLQuery(hql_count);
//            int count = Integer.parseInt(query_count.list().get(0).toString());
//            result.setRowCount(count);
//            pageBean.setRowCount(result.getRowCount());
//
//            query = session.createQuery(hql);
//
//            if(pageBean!=null){
//                query.setFirstResult(pageBean.getStartRow());
//                query.setMaxResults(pageBean.getPageSize());
//
//            }
//
//        }catch(Exception e){
//             e.printStackTrace();
//        }finally {
//             if(session!=null){
//                session.close();
//             }
//        }
//
//        return query.list();
        pageBean.setOrderBy("ur.time");
        pageBean.setOrderDir("desc");
        String hql = "from UserReview ur,Content c,Csp csp where c.id=ur.contentId  and csp.id=ur.cspId";
        List<Object> args = new ArrayList<Object>();
            if(userReview.getCspId()!=null &&userReview.getCspId()!=0) {
                hql +=" and ur.cspId=?" ;
                args.add(userReview.getCspId());
            }

           if (userReview.getStatus()!=null) {
                hql += " and ur.status=?";
                args.add(userReview.getStatus());
           }
            if (userReview.getContentName()!= null && !"".equals(userReview.getContentName().trim())) {
                hql += " and c.name like ?";
                args.add("%" + userReview.getContentName().trim() + "%");
            }
            if (userReview.getDesp()!= null && !"".equals(userReview.getDesp().trim())) {
                hql += " and ur.desp like ?";
                args.add("%" +userReview.getDesp().trim() + "%");
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

    public void updateUserReviewById(Long id, Long status) {
        Session session = null;
        try{
             session = getSession();
             String  hql = "update user_review set status="+status+" where id="+id+"";
             Query query = session.createSQLQuery(hql);
             query.executeUpdate();
        }catch(Exception e){

        }finally {
             if(session!=null){
                session.close();
             }
        }


    }

    public SearchResult<UserReview> getAllUserReviewsByContentId(UserReview userReview, PageBean pageBean, long contentId) {
        Session session = null;
        SearchResult<UserReview> result = new SearchResult<UserReview>();
        try{
            session = getSession();
            String hql = "select ur.*,c.name from user_review ur,content c where c.id=ur.content_id and content_id="+contentId+"";
            String hql_count = "select count(*) from(select ur.*,c.name from user_review ur,content c where c.id=ur.content_id and content_id="+contentId+")";

            Query  query_count =session.createSQLQuery(hql_count);
            int count = Integer.parseInt(query_count.list().get(0).toString());
            result.setRowCount(count);
            pageBean.setRowCount(result.getRowCount());

            Query query = session.createSQLQuery(hql);

            if(pageBean!=null){
                query.setFirstResult(pageBean.getStartRow());
                query.setMaxResults(pageBean.getPageSize());

            }
            List<UserReview> qResult = query.list();
            result.setRows(qResult);
        }catch(Exception e){

        }finally {
             if(session!=null){
                session.close();
             }
        }


        return result; 
    }

    public List<UserReview> searchUserReviewByContentIdAndCspId(long contentId, long cspId) {
        Config config = new Config();
        String reviewLock = config.getStrValue("user.review.lock", "UTF-8");
        String hql = "from UserReview u where u.contentId="+contentId+" and u.cspId="+cspId+" ";
        if(reviewLock.equals("true")){
            hql += " and u.status = 1";
        }

        return this.getHibernateTemplate().find(hql);
    }

    public List<UserReview> searchUserReviewByContentIdAndCspId1(long contentId, long cspId) {
        Config config = new Config();
        String reviewLock = config.getStrValue("user.review.lock", "UTF-8");
        String hql = "from UserReview u where u.contentId="+contentId+" and u.cspId="+cspId+" ";
        if(reviewLock.equals("true")){
            hql += " and u.status = 1";
        }

        return this.getHibernateTemplate().find(hql);
    }

    public UserReview searchLastUserReviewsByContentIdAndCspId(long contentId, long cspId) {
        Config config = new Config();
        String reviewLock = config.getStrValue("user.review.lock", "UTF-8");
        String hql = "from UserReview u where u.contentId="+contentId+" and u.cspId="+cspId+" ";
        if(reviewLock.equals("true")){
            hql += " and u.status = 1";
        }
        hql += " order by u.time desc";
        List<UserReview> userReviews = this.getHibernateTemplate().find(hql);
        if(userReviews!=null&&userReviews.size()!=0){
            return userReviews.get(0);
        }else{
            return null;
        }

    }

    public void changeUserReviewStatus(long userReviewId, long status,String desp,String time) {
/*        Session session = null;
        try{
             session = getSession();

             Query query = session.createSQLQuery(sql);
             query.executeUpdate();
        }catch(Exception e){
                     e.printStackTrace();
        }finally {
             if(session!=null){
                session.close();
             }
        }*/
         String sql = "update user_review set status = "+status+",desp='"+desp+"',time=to_date('"+time+"' , 'yyyy-mm-dd hh24:mi:ss') where id="+userReviewId+"";
         this.executeSQLUpdate(sql);
    }
}
