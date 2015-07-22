package com.fortune.rms.business.portal.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.portal.dao.daoInterface.UserScoringDaoInterface;
import com.fortune.rms.business.portal.model.UserScoring;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;
import com.fortune.util.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserScoringDaoAccess extends BaseDaoAccess<UserScoring, Long>
        implements
        UserScoringDaoInterface {

    public UserScoringDaoAccess() {
        super(UserScoring.class);
    }

    public SearchResult<UserScoring> getAllUserScoringCount(UserScoring userScoring, PageBean pageBean) {
        Session session = null;
        SearchResult<UserScoring> result = new SearchResult<UserScoring>();
        try {
            session = getSession();
            String hql =
                    "select c.NAME as contentName,u_s.avgScore,u_s.userNum,u_s.score1,u_s.score2,u_s.score3,u_s.score4,u_s.score5,u_s.CONTENT_ID as contentId from \n" +
                            "(select\n" +
                            "  sum(case when score=5 then 1 else 0 end) as score5,\n" +
                            "  sum(case when score=4 then 1 else 0 end) as score4,\n" +
                            "  sum(case when score=3 then 1 else 0 end) as score3,\n" +
                            "  sum(case when score=2 then 1 else 0 end) as score2,\n" +
                            "  sum(case when score=1 then 1 else 0 end) as score1,\n" +
                            "  content_id,csp_id,trunc(sum(score)/count(score), 0) as avgScore,count(user_ip) as userNum\n" +
                            "  from user_scoring group by content_id,csp_id ) u_s,content c, csp csp  where u_s.CONTENT_ID=c.ID and u_s.CSP_ID=csp.ID \n";
            String hql_count = "select count(*) from (\n" +
                    "    select c.NAME as contentName,u_s.avgScore,u_s.userNum,u_s.score1,u_s.score2,u_s.score3,u_s.score4,u_s.score5,u_s.CONTENT_ID as contentId  from  \n" +
                    "        (select\n" +
                    "          sum(case when score=5 then 1 else 0 end) as score5,\n" +
                    "          sum(case when score=4 then 1 else 0 end) as score4,\n" +
                    "          sum(case when score=3 then 1 else 0 end) as score3,\n" +
                    "          sum(case when score=2 then 1 else 0 end) as score2,\n" +
                    "          sum(case when score=1 then 1 else 0 end) as score1,\n" +
                    "         content_id,csp_id,trunc(sum(score)/count(score), 0) as avgScore,count(user_ip) as userNum\n" +
                    "      from user_scoring group by content_id,csp_id ) u_s,content c, csp csp  where u_s.CONTENT_ID=c.ID and u_s.CSP_ID=csp.ID\n";
            List params = new ArrayList();
            if (userScoring != null) {
                if (userScoring.getCspId() != null) {
                    hql += " and u_s.csp_id=? ";
                    hql_count += " and u_s.csp_id=?";
                    params.add(userScoring.getCspId());
                }
                if (userScoring.getContentName() != null) {
                    hql += " and  c.name like ?";
                    hql_count += " and c.name like ?";
                    params.add("%" + userScoring.getContentName().trim() + "%");
                }
            }
            hql_count += ")";

            Query query_count = session.createSQLQuery(hql_count);
            for (int i = 0; i < params.size(); i++) {
                query_count.setParameter(i, params.get(i));
            }

            int count = Integer.parseInt(query_count.list().get(0).toString());
            result.setRowCount(count);
            pageBean.setRowCount(result.getRowCount());
            Query query = session.createSQLQuery(hql);
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i, params.get(i));
            }
            if (pageBean != null) {
                query.setFirstResult(pageBean.getStartRow());
                query.setMaxResults(pageBean.getPageSize());
            }
            List<UserScoring> qResult = query.list();
            result.setRows(qResult);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }


        return result;
    }

    public List<Object[]> getUserScoringByContentIdAndCspId(UserScoring userScoring, PageBean pageBean) {
//        Session session = null;
//        Query query = null;
//        try{
//             session = getSession();
//            String hql = " from UserScoring us,Content c ,Csp csp where us.contentId=c.id and us.cspId=csp.id";
//            String hql_count = "select count(*) from( select * from user_scoring us,content c ,csp csp where us.content_Id=c.id and us.csp_Id=csp.id";
//            session = getSession();
//            if (userScoring != null) {
//                if (userScoring.getContentId() != null) {
//                    hql += " and us.contentId =" + userScoring.getContentId() + "";
//                    hql_count += " and us.content_Id=" + userScoring.getContentId() + "";
//                }
//
//            }
//            hql += " order by us.id desc";
//            hql_count += ")";
//
//            Query query_count = session.createSQLQuery(hql_count);
//
//            String count =query_count.list().get(0).toString();
//
//
//            pageBean.setRowCount(new Integer(count));
//
//            query = session.createQuery(hql);
//
//            if (pageBean != null) {
//                query.setFirstResult(pageBean.getStartRow());
//                query.setMaxResults(pageBean.getPageSize());
//            }
//
//        }catch(Exception e){
//              e.printStackTrace();
//        }finally {
//             if(session!=null){
//                session.close();
//             }
//        }
        //        return query.list();
        String hql = "from UserScoring us,Content c ,Csp csp where us.contentId=c.id and us.cspId=csp.id";
        List<Object> args = new ArrayList<Object>();
        if(userScoring.getCspId()!=null){
            hql +=" and us.cspId=?";
            args.add(userScoring.getCspId());
        }
        if (userScoring.getContentId() != null) {
            hql += " and us.contentId=?";
            args.add(userScoring.getContentId());
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

    public Map<String, String> getUserScoringCountByContentIdAndCspId(long contentId, long cspId) {
        Session session = null;
        Map<String, String> userScoringCounts = null;
        try {
            session = getSession();

            String sql = "select\n" +
                    "  sum(case when score=1 then 1 else 0 end) as score1,\n" +
                    "  sum(case when score=2 then 1 else 0 end) as score2,\n" +
                    "  sum(case when score=3 then 1 else 0 end) as score3,\n" +
                    "  sum(case when score=4 then 1 else 0 end) as score4,\n" +
                    "  sum(case when score=5 then 1 else 0 end) as score5\n" +
                    "from user_scoring where content_id =" + contentId + " and csp_id=" + cspId + "";
            Query query = session.createSQLQuery(sql);
            List qResult = query.list();

            if (qResult != null) {
                userScoringCounts = new HashMap<String, String>();
                Object[] objs = null;
                if (qResult.size() == 1) {
                    objs = (Object[]) qResult.get(0);
                }
                if (objs != null) {
                    for (int i = 0, l = objs.length; i < l; i++) {
                        String value = "0";
                        if (objs[i] != null) {
                            value = objs[i].toString();
                        }
                        userScoringCounts.put("score" + (i + 1), value);
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            if (session != null) {
                session.close();
            }
        }


        return userScoringCounts;
    }

    /**
     * 获取单个媒体评分范围内的评分次数
     * @param contentId 媒体Id
     * @param min   最小分数（包含）
     * @param max   最大分数（包含）
     * @return 次数
     */
    public Long redexScoreRangeCount(long contentId, int min, int max){
        String hql = "select count(*) from UserScoring s where s.contentId=" + contentId +
                " and s.score>=" + min +
                " and s.score<=" + max;
        List l = getHibernateTemplate().find(hql);
        return (l == null || l.size() == 0)? 0 : StringUtils.string2long(l.get(0).toString(), 0);
    }
}
