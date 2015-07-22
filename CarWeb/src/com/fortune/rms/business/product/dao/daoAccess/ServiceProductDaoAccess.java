package com.fortune.rms.business.product.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.product.dao.daoInterface.ServiceProductDaoInterface;
import com.fortune.rms.business.product.model.ServiceProduct;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ServiceProductDaoAccess
		extends
			BaseDaoAccess<ServiceProduct, Long>
		implements
			ServiceProductDaoInterface {

	public ServiceProductDaoAccess() {
		super(ServiceProduct.class);
	}

    public List<ServiceProduct> getServiceProductsByCspId(long cspId) {
        String hqlStr = "from ServiceProduct sp where sp.cspId in ("+cspId+",543737280)";//增加联通产品显示，联通CSPID为543737280
        return this.getHibernateTemplate().find(hqlStr);
    }

    public List<ServiceProduct> getServiceProducts(Long contentId){
        String hql = "from ServiceProduct sp where (sp.id in (select csp.serviceProductId from ContentServiceProduct csp where csp.contentId ="+contentId+")" +
                     " or sp.id in (select spc.serviceProductId from ServiceProductChannel spc where spc.channelId in" +
                                     " (select cc.channelId from ContentChannel cc where cc.contentId ="+contentId+" )" +
                                    ")" +
                ") and sp.status = 0";//status=0为状态有效
        return  this.getHibernateTemplate().find(hql);
    }
    public List<ServiceProduct> getServiceProductByContentId(long contentId) {
        String hql = "from ServiceProduct sp where sp.id in (select csp.serviceProductId from ContentServiceProduct csp where csp.contentId ="+contentId+") and sp.status = 0";//status=0为状态有效
        return  this.getHibernateTemplate().find(hql);
    }

    public List<ServiceProduct> getServiceProductByChannelOfContent(long contentId) {
        String hql = "from ServiceProduct sp where sp.id in (select spc.serviceProductId from ServiceProductChannel spc where spc.channelId in (select cc.channelId from ContentChannel cc where cc.contentId ="+contentId+" ))";
        return this.getHibernateTemplate().find(hql);
    }

    public List<ServiceProduct> getDemandServiceProductByContentId(long contentId) {
        String hql = "from ServiceProduct sp where sp.id in (select csp.serviceProductId from ContentServiceProduct csp where csp.contentId ="+contentId+") and sp.status = 0 and sp.type=2";//status=0为状态有效,type=2为点播
        return  this.getHibernateTemplate().find(hql);
    }

    public ServiceProduct getServiceProductBySpId(String productId) {
//        ServiceProduct sp=new ServiceProduct();
        List list = null;
        Session session = getSession();
        try {
            String sql = "select sp.csp_id from service_product sp,product p where p.id = sp.product_id and p.MOBILE_PRODUCT = 1";
            Query query =session.createSQLQuery(sql);
            list = query.list();
        } catch (DataAccessResourceFailureException e) {
            logger.error("Hibernate错误:"+e.getMessage());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalStateException e) {
            logger.error("Hibernate错误:"+e.getMessage());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (HibernateException e) {
            logger.error("Hibernate错误:"+e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
//        return list==null||list.size() == 0?null:list.get(0);
        return  null;
    }
}
