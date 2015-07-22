package com.fortune.rms.business.product.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.product.dao.daoInterface.ProductDaoInterface;
import com.fortune.rms.business.product.dao.daoInterface.ServiceProductDaoInterface;
import com.fortune.rms.business.product.logic.logicInterface.ServiceProductLogicInterface;
import com.fortune.rms.business.product.model.Product;
import com.fortune.rms.business.product.model.ServiceProduct;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service("serviceProductLogicInterface")
public class ServiceProductLogicImpl extends BaseLogicImpl<ServiceProduct>
		implements
			ServiceProductLogicInterface {
	private ServiceProductDaoInterface serviceProductDaoInterface;
    private ProductDaoInterface productDaoInterface;

	/**
	 * @param serviceProductDaoInterface the serviceProductDaoInterface to set
	 */
    @Autowired
	public void setServiceProductDaoInterface(
			ServiceProductDaoInterface serviceProductDaoInterface) {
		this.serviceProductDaoInterface = serviceProductDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.serviceProductDaoInterface;
	}
    @Autowired
    public void setProductDaoInterface(ProductDaoInterface productDaoInterface) {
        this.productDaoInterface = productDaoInterface;
    }
    public List<ServiceProduct> getServiceProductsByCspId(Long cspId){
        return serviceProductDaoInterface.getServiceProductsByCspId(cspId);
    }

    public List<ServiceProduct> getServiceProduct(ServiceProduct serviceProduct, PageBean pageBean){
                           List<ServiceProduct> result;
        try {
            result = serviceProductDaoInterface.getObjects(serviceProduct, pageBean);
            List<ServiceProduct> objs = result;
            if(objs!=null){
                for(ServiceProduct sp :objs){
                    if(sp.getProductId()!=null){
                        Long productId = sp.getProductId();
                        String name = "";
                        try {
                                Product product = productDaoInterface.get(productId);
                                if(product!=null){
                                    name = product.getName();
                                }
                        } catch (Exception e) {
                            sp.setProductName("ID:"+productId);
                        }
                        sp.setProductName(name);
                    }
                }
            }
        } catch (Exception e) {
            result = new ArrayList<ServiceProduct>();
        }
          return result;
      }

    public List<ServiceProduct> getServiceProductOfCsp(ServiceProduct serviceProduct, PageBean pageBean, long cspId) {
          List<ServiceProduct> result;
        try {
            String hql="o1.cspId="+cspId+"";
            result = serviceProductDaoInterface.getObjects(serviceProduct, pageBean,null,hql);
            List<ServiceProduct> objs = result;
            if(objs!=null){
                for(ServiceProduct sp :objs){
                    if(sp.getProductId()!=null){
                        Long productId = sp.getProductId();
                        String name = "";
                        try {
                                Product product = productDaoInterface.get(productId);
                                if(product!=null){
                                    name = product.getName();
                                }
                        } catch (Exception e) {
                            sp.setProductName("ID:"+productId);
                        }
                        sp.setProductName(name);
                    }
                }
            }
        } catch (Exception e) {
            result = new ArrayList<ServiceProduct>();
        }
          return result;
    }

    public List<ServiceProduct> getServiceProductOfServiceProductId(ServiceProduct serviceProduct, PageBean pageBean, long serviceProductId) {
         List<ServiceProduct> result;
        try {
           // String hqlEx="where id !="+serviceProductId+"";
            result = serviceProductDaoInterface.getObjects(serviceProduct, pageBean,null,"");
            List<ServiceProduct> objs = result;
            if(objs!=null){
                for(ServiceProduct sp :objs){
                    if(sp.getProductId()!=null){
                        Long productId = sp.getProductId();
                        String name = "";
                        try {
                                Product product = productDaoInterface.get(productId);
                                if(product!=null){
                                    name = product.getName();
                                }
                        } catch (Exception e) {
                            sp.setProductName("ID:"+productId);
                        }
                        sp.setProductName(name);
                    }
                }
            }
        } catch (Exception e) {
            result = new ArrayList<ServiceProduct>();
        }
          return result;
    }

    public List<ServiceProduct> getServiceProductsByCspId(long cspId) {
           return  serviceProductDaoInterface.getServiceProductsByCspId(cspId);
    }

    public List<ServiceProduct> getServiceProductByContentId(long contentId) {
        return serviceProductDaoInterface.getServiceProducts(contentId);
/*
        List<ServiceProduct> serviceProducts_1 =  serviceProductDaoInterface.getServiceProductByContentId(contentId);
        List<ServiceProduct> serviceProducts_2 =  serviceProductDaoInterface.getServiceProductByChannelOfContent(contentId);

        if(serviceProducts_1 != null && serviceProducts_1.size() > 0) {
            serviceProducts_1.addAll(serviceProducts_2);
            return  serviceProducts_1;
        }else {
            return serviceProducts_2;
        }
*/
    }

    public List<ServiceProduct> getDemandServiceProductByContentId(long contentId) {
        return serviceProductDaoInterface.getDemandServiceProductByContentId(contentId);
    }

    public ServiceProduct getServiceProductBySpId(String productId) {
        return serviceProductDaoInterface.getServiceProductBySpId(productId);
    }

}
