package com.fortune.rms.business.product.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.product.dao.daoInterface.UserBuyDaoInterface;
import com.fortune.rms.business.product.model.UserBuy;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;
import com.fortune.util.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserBuyDaoAccess extends BaseDaoAccess<UserBuy, Long>
		implements
			UserBuyDaoInterface {

	public UserBuyDaoAccess() {
		super(UserBuy.class);
	}


    public List<UserBuy> getUnSetAreaIdData(UserBuy userBuy, PageBean pageBean) {
        try {
            return getObjects(userBuy,pageBean,false,"(o1.areaId is null or o1.areaId<=0)");
        } catch (Exception e) {
            logger.error("无法获取没有区域信息的用户购买数据："+e.getMessage());
            return new ArrayList<UserBuy>(0);
        }
    }

    public List<Object[]> getAllSpSalesCount(UserBuy userBuy, PageBean pageBean) {
        Session session = null;
        List<Object[]> objects = null;
        try{
            session = getSession();
            String hql ="select c.id as spId, c.name as spName,sum(ub.price) as salesAmount,count(*) as buyNum from Csp c,UserBuy ub where c.id=ub.spId";
            List params = new ArrayList();
            if(userBuy!=null){
                if(userBuy.getSpId()!=null){
                      hql +=" and ub.spId ="+userBuy.getSpId()+"";
                }
    
                if(userBuy.getStartTime()!=null){
                      hql +=" and ub.buyTime>?";
                    params.add(userBuy.getStartTime());
                }
                if(userBuy.getEndTime()!=null){
    
                      hql +=" and ub.buyTime<?";
                    params.add(userBuy.getEndTime());
                }
    
            }
    
            hql+=" group by c.id,c.name";
    
            int count = this.getHibernateTemplate().find(hql,params.toArray()).size();
            pageBean.setRowCount(count);
            Query query = session.createQuery(hql);
            for(int i=0;i<params.size();i++){
                query.setParameter(i,params.get(i));
            }
            
            if(pageBean!=null){
                query.setFirstResult(pageBean.getStartRow());
                query.setMaxResults(pageBean.getPageSize());
            }
            objects = query.list();
        }catch(Exception e){
            logger.error(e.getMessage());
        }finally {
            if(session != null){
               session.close();
            }
        }

        return objects;
    }

    public List<Object[]> getAllProductSalesCount(UserBuy userBuy, PageBean pageBean) {
        Session session = null;
        List<Object[]> objects = null;
        try{
            session = getSession();
            String hql="select c.id as spId,c.name as spName,sp.id as serviceProductId, sp.name as serviceProductName,sum(ub.price) as salesAmount,count(*) as buyNum " +
                           " from UserBuy ub,ServiceProduct sp,Csp c where ub.serviceProductId=sp.id and ub.spId=c.id ";

            List params = new ArrayList();
            if(userBuy!=null){
                       if(userBuy.getSpId()!=null){
                             hql+=" and ub.spId ="+userBuy.getSpId()+"";
                       }
                       if(userBuy.getServiceProductType()!=null){
                             hql+=" and sp.type ="+userBuy.getServiceProductType();
                       }
                       if(userBuy.getStartTime()!=null){
                             hql+=" and ub.buyTime>?";
                           params.add(userBuy.getStartTime());
                       }
                       if(userBuy.getEndTime()!=null){

                             hql+=" and ub.buyTime<?";
                           params.add(userBuy.getEndTime());
                       }

                   }

            hql+="group by sp.id,sp.name,c.id,c.name";

            int count = this.getHibernateTemplate().find(hql,params.toArray()).size();

            pageBean.setRowCount(count);
            Query query = session.createQuery(hql);
            for(int i=0;i<params.size();i++){
                query.setParameter(i,params.get(i));
            }
            if(pageBean!=null){
                query.setFirstResult(pageBean.getStartRow());
                query.setMaxResults(pageBean.getPageSize());
            }
            objects = query.list();

        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            if(session != null){
                session.close();
            }
        }

        return objects;
    }

    public List<Object[]> getAllContentSalesCount(UserBuy userBuy, PageBean pageBean) {
        Session session = null;
        List<Object[]> objects = null;
        try{
              session = getSession();
              String hql1="select contentId,sum(price),count(*) from UserBuy group by contentId";
            String hql="select distinct c_u.contentId,c_u.contentName,c_u.salesAmount,c_u.buyNum, co.csp_id as cpId,c1.name as cpName,ub1.SP_ID as spId,c2.NAME as spName \n" +
                    "     from \n" +
                    "         (select c.id as contentId,c.name as contentName,sum(ub.price) as salesAmount,count(*)as buyNum \n" +
                    "          from  \n" +
                    "               content c,user_buy ub where c.id=ub.content_id group by c.id,c.name) c_u \n" +
                    "              inner  join \n" +
                    "                  content co on c_u.contentId= co.id  inner join Csp c1 on c1.id=co.csp_id\n" +
                    "                    inner join \n" +
                    "                        user_buy ub1 on ub1.CONTENT_ID=c_u.contentId\n" +
                    "                      inner join \n" +
                    "                        csp c2 on c2.ID=ub1.SP_ID";
           String hql_count="select count(*) from(select  distinct c_u.contentId,c_u.contentName,c_u.salesAmount,c_u.buyNum, co.csp_id as cpId,c1.name as cpName,ub1.SP_ID as spId,c2.NAME as spName \n" +
                    "     from \n" +
                    "         (select c.id as contentId,c.name as contentName,sum(ub.price) as salesAmount,count(*)as buyNum \n" +
                    "          from  \n" +
                    "               content c,user_buy ub where c.id=ub.content_id group by c.id,c.name) c_u \n" +
                    "              inner  join \n" +
                    "                  content co on c_u.contentId= co.id  inner join Csp c1 on c1.id=co.csp_id\n" +
                    "                    inner join \n" +
                    "                        user_buy ub1 on ub1.CONTENT_ID=c_u.contentId\n" +
                    "                      inner join \n" +
                    "                        csp c2 on c2.ID=ub1.SP_ID";
            List params = new ArrayList();
            if(userBuy!=null){
                  if(userBuy.getSpId()!=null){
                       hql+=" and ub1.sp_id ="+userBuy.getSpId()+"";
                       hql_count+=" and ub1.sp_id ="+userBuy.getSpId()+"";
                  }
                  if(userBuy.getCpId()!=null){
                       hql+=" and co.csp_id = "+userBuy.getCpId()+"";
                       hql_count+=" and co.csp_id = "+userBuy.getCpId()+"";
                  }
                  if(userBuy.getStartTime()!=null){
                      hql+=" and ub1.buy_time>?";
                      hql_count+=" and ub1.buy_time>?";
                      params.add(userBuy.getStartTime());
                }
                if(userBuy.getEndTime()!=null){
    
                      hql+=" and ub1.buy_time<?";
                      hql_count+=" and ub1.buy_time<?";
                      params.add(userBuy.getEndTime());
                }
        
            }
            hql_count+=")";
    
            Query query_count =session.createSQLQuery(hql_count);
            for(int i=0;i<params.size();i++){
                query_count.setParameter(i,params.get(i));
            }
    
            int count = Integer.parseInt(query_count.list().get(0).toString());
    
            pageBean.setRowCount(count);
            Query query = session.createSQLQuery(hql);
            for(int i=0;i<params.size();i++){
                query.setParameter(i,params.get(i));
            }
             if(pageBean!=null){
                query.setFirstResult(pageBean.getStartRow());
                query.setMaxResults(pageBean.getPageSize());
            }
            objects = query.list();
        }catch(Exception e){
            logger.error(e.getMessage());
        }finally {
            if(session != null){
               session.close();
            }
        }

        return objects;
    }

    public List<UserBuy> hasBuy(String userId, Date now, Long contentId, Long contentPropertyId,
                          Long channelId,Long productType,List<String> productIds,PageBean pageBean) {
        logger.debug("检查用户是否购买了：time="+ StringUtils.date2string(now)+",userId="+userId);
        if(now==null){
            return new ArrayList<UserBuy>(0);
        }
        List<Object> parameters = new ArrayList<Object>();
        boolean canSearch = false;
        String checkTime = StringUtils.date2string(now);
///*
        String hql = "from UserBuy ub where ub.userId like ? and ub.startTime<to_date('" +checkTime+
                "','YYYY-MM-DD HH24:MI:SS') " +
                "  and ub.endTime>to_date('" +checkTime+
                "','YYYY-MM-DD HH24:MI:SS') ";//按次的购买
//*/
/*
        String hql = "from UserBuy ub where ub.userId like ? and ub.startTime<? " +
                "  and ub.endTime > ? ";//按次的购买
*/
///*
        parameters.add(userId);
//        parameters.add(now);
//        parameters.add(now);
//*/
        if(contentId!=null&&contentId>0){
            canSearch = true;
            //hql += " and (ub.contentId="+contentId+" and ub.contentId not in (select cc.contentId from ContentChannel cc" +" where cc.channelId = "+15884423+")";
            hql += " and ub.contentId="+contentId;//+" and ub.contentId not in (select cc.contentId from ContentChannel cc" +" where cc.channelId = "+15884423+")";
        }
        if(channelId!=null&&channelId>0){
            //这个不能从UserBuy表里走，要再查一下
            canSearch = true;
            hql+= " and ub.contentId in (select cc.contentId from ContentChannel cc where cc.channelId = "+channelId+")";
        }
        if(productIds!=null&&productIds.size()>0){
            canSearch = true;
            hql += " and (";
            boolean added = false;
            for(String productId:productIds){
                if(added){
                    hql +=" or ";
                }else{
                    added = true;
                }
                hql+=" ub.productId like '" +productId+
                        "'";
                //parameters.add(productId);
            }
            hql+=")";
        }
        if(productType==null||productType<0){
            productType = 2L;//1=包月 ，2=按次
        }
        hql+=" and ub.productId in (select p.payProductNo from Product p where p.type="+productType+")";
        List<UserBuy> result;
        if(!canSearch){
            return  new ArrayList<UserBuy>(0);
        }
        try {
            logger.debug("准备执行的hql="+hql);
            result = getObjects(hql,parameters.toArray(),pageBean);
        } catch (Exception e) {
            logger.error("无法检查用户购买情况："+userId+",e="+e.getMessage());
            e.printStackTrace();
            result = new ArrayList<UserBuy>(0);
        }
        return result;
    }
}
