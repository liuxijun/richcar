package com.fortune.rms.business.product.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.product.dao.daoInterface.ServiceProductGiftDaoInterface;
import com.fortune.rms.business.product.logic.logicInterface.ServiceProductGiftLogicInterface;
import com.fortune.rms.business.product.model.ServiceProductGift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("serviceProductGiftLogicInterface")
public class ServiceProductGiftLogicImpl
		extends
			BaseLogicImpl<ServiceProductGift>
		implements
			ServiceProductGiftLogicInterface {
	private ServiceProductGiftDaoInterface serviceProductGiftDaoInterface;

	/**
	 * @param serviceProductGiftDaoInterface the serviceProductGiftDaoInterface to set
	 */
    @Autowired
	public void setServiceProductGiftDaoInterface(
			ServiceProductGiftDaoInterface serviceProductGiftDaoInterface) {
		this.serviceProductGiftDaoInterface = serviceProductGiftDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.serviceProductGiftDaoInterface;
	}

    public void saveServiceProductGift(List<ServiceProductGift> serviceProductGifts, String serviceProductGiftIds, long serviceProductId) {
       this.serviceProductGiftDaoInterface.deleteServiceProductGiftByParam(serviceProductGiftIds,serviceProductId);
       if(serviceProductGifts.size()!=0){
           for(int i=0;i<serviceProductGifts.size();i++){
                this.serviceProductGiftDaoInterface.save(serviceProductGifts.get(i));
           }
       }
    }

    public List<ServiceProductGift> getAllServiceProductGiftOfServiceProduct(long serviceProductId) {
        return this.serviceProductGiftDaoInterface.getAllServiceProductGiftOfServiceProduct(serviceProductId);
    }
}
