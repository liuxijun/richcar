package com.fortune.rms.business.product.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.product.model.ServiceProductChannel;

import java.util.List;


public interface ServiceProductChannelLogicInterface extends BaseLogicInterface<ServiceProductChannel> {
    public List<ServiceProductChannel> getSelectedChannel(long serviceProductId);
    public void deleteByServiceProductIds(String serviceProductIds);
    public void bulkBindingChannel(String serviceProductIds, String channels);
}
