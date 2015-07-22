package com.fortune.rms.business.product.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.product.dao.daoInterface.ServiceProductGiftDaoInterface;
import com.fortune.rms.business.product.model.ServiceProductGift;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class ServiceProductGiftDaoAccess
		extends
			BaseDaoAccess<ServiceProductGift, Long>
		implements
			ServiceProductGiftDaoInterface {

	public ServiceProductGiftDaoAccess() {
		super(ServiceProductGift.class);
	}

    public void deleteServiceProductGiftByParam(String serviceProductGiftIds, long serviceProductGiftId) {
       String hqlStr ="delete from ServiceProductGift spg where spg.serviceProductId="+serviceProductGiftId+" and " +
               " spg.giftServiceProductId in("+serviceProductGiftIds+")";
       executeUpdate(hqlStr);
    }

    public List<ServiceProductGift> getAllServiceProductGiftOfServiceProduct(long serviceProductId) {
        String hqlStr="from ServiceProductGift spg where spg.serviceProductId="+serviceProductId+"";
        return this.getHibernateTemplate().find(hqlStr); 
    }
}
