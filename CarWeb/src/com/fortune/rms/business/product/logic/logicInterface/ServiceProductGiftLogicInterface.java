package com.fortune.rms.business.product.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.product.model.ServiceProductGift;

import java.util.List;

public interface ServiceProductGiftLogicInterface
		extends
			BaseLogicInterface<ServiceProductGift> {
    public void saveServiceProductGift(List<ServiceProductGift> serviceProductGifts, String serviceProductGiftIds, long serviceProductId);
    public List<ServiceProductGift> getAllServiceProductGiftOfServiceProduct(long serviceProductId);
}
