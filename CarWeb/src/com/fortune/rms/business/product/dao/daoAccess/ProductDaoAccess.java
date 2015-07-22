package com.fortune.rms.business.product.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.product.dao.daoInterface.ProductDaoInterface;
import com.fortune.rms.business.product.model.Product;
import com.fortune.util.PageBean;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDaoAccess extends BaseDaoAccess<Product, Long>
        implements
        ProductDaoInterface {

    public ProductDaoAccess() {
        super(Product.class);
    }


    public List<Product> getProductType(Long type, PageBean pageBean) {
        String hql = "from Product p where p.status=1  and p.type=" + type;
        //   return  this.getHibernateTemplate().find(hqlStr)  ;
        try {
            return getObjects(hql, new Object[]{}, pageBean);
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public List<Product> getProductsOfStatus(int status) {
        String hqlStr = "from Product p where p.status=" + status + "";
        return this.getHibernateTemplate().find(hqlStr);
    }


    public List<Object[]> getProducts(long contentId) {
        String sql = "select sp.id,sp.name,sp.csp_id,sp.type,p.pay_product_no,p.mobile_product,p.price,p.description,p.status,p.cost_Type from product p,service_product sp " +
                "where p.id = sp.product_id and " +
                " (sp.id in (select csp.service_product_id from content_service_product csp where csp.content_id =" + contentId + " )or sp.id in" +
                "(select spc.SERVICE_PRODUCT_ID from SERVICE_PRODUCT_CHANNEL spc where spc.CHANNEL_ID in " +
                "(select cc.CHANNEL_ID from CONTENT_CHANNEL cc where cc.CONTENT_ID=" + contentId +
                ")))and" +
                " p.mobile_product=1 and p.id != 30697884 and p.status != 0";
        sql += " order by sequence";//排除燕赵世界（大包月）
        Session session = getSession();
        try {
            Query query = session.createSQLQuery(sql);
            return query.list();
        } catch (HibernateException e) {
            logger.error("无法对产品进行搜索：" + e.getMessage() + ",SQL=" + sql);
            e.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList(0);
    }

    public List getProductsByCspId(long cspId, long contentId) {
        String sql = "select sp.id,sp.name,sp.csp_id,sp.type,p.pay_product_no,p.mobile_product,p.price,p.description,p.status,p.cost_Type from product p,service_product sp,content_service_product csp " +
                "where p.id = sp.product_id and sp.id = csp.service_product_id " +
                " and csp.content_id =" + contentId + " and" +
                " p.mobile_Product=1 and p.id != 30697884 and p.status != 0";
        if (cspId > 0) {
            sql += " and sp.csp_Id =" + cspId;
        }
        sql += " order by sequence";//排除燕赵世界（大包月）
        Session session = getSession();
        try {
            Query query = session.createSQLQuery(sql);
            return query.list();
        } catch (HibernateException e) {
            logger.error("无法对产品进行搜索：" + e.getMessage() + ",SQL=" + sql);
            e.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList(0);
//        String hqlStr = "from Product p where p.id in ( select sp.productId  from ServiceProduct sp where sp.cspId ="+cspId+") and p.price = 5";
        //"from Product p where p.id in ( select sp.productId  from ServiceProduct sp where sp.cspId in(select c.cspId from content c where c.id = "+contentId+")) and p.price = 5";
//        return this.getHibernateTemplate().find(hqlStr);
    }


    public List getProductsByChannel(long contentId) {
        Session session = getSession();
        String sql = "select sp.id,sp.name,sp.csp_id,sp.type,p.pay_product_no,p.mobile_product,p.price,p.description,p.status,p.cost_Type" +
                "  from product p,service_product sp " +
                "  where p.id = sp.product_id " +
                "  and sp.id in(select spc.service_Product_Id from Service_Product_Channel spc where spc.channel_Id in " +
                "  (select cc.channel_Id from Content_Channel cc where cc.content_Id =" + contentId + " )) and" +
                " p.mobile_Product=1 and p.id != 30697884 and p.status != 0 order by sequence";//排除燕赵世界（大包月）
        try {

            Query query = session.createSQLQuery(sql);
            return query.list();
        } catch (HibernateException e) {
            logger.error("无法通过频道进行产品搜索：" + e.getMessage() + ",sql=" + sql);
            e.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList(0);
//        String hqlStr = "from Product p where p.id in ( select sp.productId  from ServiceProduct sp where sp.cspId ="+cspId+") and p.price = 5";
        //"from Product p where p.id in ( select sp.productId  from ServiceProduct sp where sp.cspId in(select c.cspId from content c where c.id = "+contentId+")) and p.price = 5";
//        return this.getHibernateTemplate().find(hqlStr);
    }


    public List getProductsByMobileProductFlag(int mobileProductFlag) {
        List list;
        Session session = getSession();
        try {
            String sql = "select sp.id,sp.name,sp.csp_id,sp.type,p.pay_Product_No,p.mobile_product,p.price,p.description," +
                    "p.status,p.cost_type from product p,service_product sp where p.id = sp.product_id and p.mobile_product = " +
                    mobileProductFlag + " order by p.sequence";
            Query query = session.createSQLQuery(sql);
            list = query.list();
            return list;
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public List getMobileProducts() {
        return getProductsByMobileProductFlag(1);
    }
}
