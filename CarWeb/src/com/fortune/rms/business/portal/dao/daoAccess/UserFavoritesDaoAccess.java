package com.fortune.rms.business.portal.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.portal.dao.daoInterface.UserFavoritesDaoInterface;
import com.fortune.rms.business.portal.model.UserFavorites;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;
import com.fortune.util.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class UserFavoritesDaoAccess extends BaseDaoAccess<UserFavorites, Long>
        implements
        UserFavoritesDaoInterface {

    public UserFavoritesDaoAccess() {
        super(UserFavorites.class);
    }

    public SearchResult<UserFavorites> getUserFavoritesCount(UserFavorites userFavorites, PageBean pageBean) {
        Session session = null;
        SearchResult<UserFavorites> result = new SearchResult<UserFavorites>();
        try{
            session = getSession();
            String hql = "select c.name as contentName,count(uf.userIp) as userCount,uf.contentId as contentId from UserFavorites uf,Content c where uf.contentId=c.id";
            List params = new ArrayList();
            if (userFavorites != null) {
                if (userFavorites.getContentName() != null) {
                    hql += " and c.name like ?";
                    params.add("%" + userFavorites.getContentName().trim() + "%");
                }
            }
            hql += " group by uf.contentId,c.name";


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
            List<UserFavorites> qResult = query.list();
            result.setRows(qResult);
        }catch(Exception e){

        }finally {
             if(session!=null){
                session.close();
             }
        }
        return result;
    }
    public List<Object[]> getAllUserFavorites(UserFavorites userFavorites, PageBean pageBean) {
        Session session = null;
        Query query = null;
        try{
                session = getSession();
                String hql = " from UserFavorites uf,Content c,Csp csp where uf.cspId=csp.id and uf.contentId =c.id";
                String hql_count = "select count(*) from (select * from user_favorites uf,content c,csp csp where uf.csp_Id=csp.id and uf.content_Id =c.id";
                if (userFavorites != null) {
                      if (userFavorites.getContentId() != null) {
                           hql += " and uf.contentId =" + userFavorites.getContentId() + "";
                           hql_count += " and uf.content_Id=" + userFavorites.getContentId() + "";
                      }


                  }
                hql += " order by uf.id desc";
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

    /**
     * Redex使用的获取用户收藏列表
     * @param userId    用户Id
     * @param pageBean  分页信息
     * @return UserFavorite列表
     */
    public List<UserFavorites> redexGetUserFavoriteList(String userId, PageBean pageBean){
        String hql = "from UserFavorites fav where fav.userId='" + userId + "' order by fav.time desc";
        try {
            return getObjects(hql, pageBean);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 内容被收藏的总次数
     * @param contentId 内容Id
     * @return 次数
     */
    public Long redexGetContentFavoriteCount(Long contentId){
        String hql = "select count(*) from UserFavorites fav where fav.contentId=" + contentId;
        List l = getHibernateTemplate().find(hql);
        return (l == null || l.size() == 0)? 0 : StringUtils.string2long(l.get(0).toString(), 0);
    }
}
