package com.fortune.rms.business.product.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.product.model.ServiceProductGift;

import java.util.List;

public interface ServiceProductGiftDaoInterface
		extends
			BaseDaoInterface<ServiceProductGift, Long> {
    public void deleteServiceProductGiftByParam(String serviceProductGiftIds, long serviceProductGiftId);
    public List<ServiceProductGift> getAllServiceProductGiftOfServiceProduct(long serviceProductId);

}