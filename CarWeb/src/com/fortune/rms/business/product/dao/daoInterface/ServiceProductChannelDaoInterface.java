package com.fortune.rms.business.product.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.product.model.ServiceProductChannel;

import java.util.List;

public interface ServiceProductChannelDaoInterface extends
        BaseDaoInterface<ServiceProductChannel, Long> {

    public List<ServiceProductChannel> getSelectedChannel(long serviceProductId);
    public void deleteByServiceProductIds(String serviceProductIds);
}
