package com.fortune.rms.business.content.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.content.dao.daoInterface.ContentRecommendDaoInterface;
import com.fortune.rms.business.content.model.ContentRecommend;
import com.fortune.rms.business.publish.model.Recommend;
import com.fortune.util.PageBean;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;
@Repository
public class ContentRecommendDaoAccess
		extends
			BaseDaoAccess<ContentRecommend, Long>
		implements
			ContentRecommendDaoInterface {

	public ContentRecommendDaoAccess() {
		super(ContentRecommend.class);
	}

    @SuppressWarnings("unchecked")
/*    public List<ContentRecommend> getContentIds(String recommendCode,PageBean pageBean) {
        String hql = "from ContentRecommend cr where cr.recommendId" +
                " in (select r.id from Recommend r where r.code like ?) and " +
                " cr.contentId in ( select cc.contentId from ContentCsp cc where cc.status=2 and cc.cspId in (select r.cspId from Recommend r where r.code like '"+recommendCode+"') " +
                ") order by cr.displayOrder";
        try {
            return getObjects(hql,new Object[]{recommendCode},pageBean);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }


        return new ArrayList<ContentRecommend>();
    }*/

    public List<ContentRecommend> getContentIds(String recommendCode,PageBean pageBean) {
        return getContentIds(recommendCode, pageBean, null, -1);
    }

    public List<ContentRecommend> getContentIds(String recommendCode, PageBean pageBean, List<Long> channelIdList, long userType){
        if(recommendCode==null||"".equals(recommendCode.trim())){
            return new ArrayList<ContentRecommend>(0);
        }
        String hql = "from ContentRecommend cr where cr.recommendId" +
                " in (select r.id from Recommend r where r.code like ?) and " +
                " cr.contentId in ( select cc.contentId from ContentCsp cc where cc.status=2" +
                ")";

        if("mobile_index_slider".equalsIgnoreCase(recommendCode.trim())) {
                hql = "from ContentRecommend cr where cr.recommendId" +
                        " in (select r.id from Recommend r where r.code like ?) and " +
                        " (cr.contentId in ( select cc.contentId from ContentCsp cc where cc.status=2" +
                        ") or cr.contentId in (select c.id from Content c where c.isSpecial = 2))";
        }

        if(channelIdList != null && channelIdList.size() > 0){
            String ids = "";
            for(Long id : channelIdList){
                ids += "".equals(ids)? id : ","+id;
            }
            hql += " and cr.contentId in ( select cc.contentId from ContentChannel cc where cc.channelId in (" + ids + "))";
        }
        if( userType > 0){
            // userType ∑≈‘⁄property6¿Ô
            hql += " and cr.contentId in (select c.id from Content c where c.userTypes like '%" + userType + "%')";
        }


         hql += " order by cr.displayOrder";
        try {
            return getObjects(hql,new Object[]{recommendCode},pageBean);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<ContentRecommend>();
    }




    public String getRecommendCodeById(long recommendId){
       String recommendCode="";
       Session session = null;
       try{
          session = getSession();
          String sql ="select code from recommend where id = "+recommendId+"";
           Query query = session.createSQLQuery(sql);
           List<Recommend> recommends = null;
           recommends = query.list();
           if(recommends != null && recommends.size() !=0){
               recommendCode = query.list().get(0).toString();
           }
       }catch(Exception e){
         e.printStackTrace();
       }finally {
           if(session!=null){
                session.close();
           }
       }

       return recommendCode;
    }
    public List<ContentRecommend> getContentRecommendByDisplayOrder(long displayOrder,long recommendId){
             try{
                String sql = "from ContentRecommend where displayOrder = "+displayOrder+" and recommendId = "+recommendId;
                return this.getHibernateTemplate().find(sql);
             }catch (Exception e){
                 e.printStackTrace();
             }
        return null;
    }

}
