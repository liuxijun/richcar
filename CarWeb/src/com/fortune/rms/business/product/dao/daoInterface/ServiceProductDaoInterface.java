package com.fortune.rms.business.product.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.product.model.ServiceProduct;

import java.util.List;

public interface ServiceProductDaoInterface
		extends
			BaseDaoInterface<ServiceProduct, Long> {

    public List<ServiceProduct> getServiceProductsByCspId(long cspId);
    public List<ServiceProduct> getServiceProducts(Long contentId);
    @Deprecated
    public List<ServiceProduct> getServiceProductByContentId(long contentId);
    @Deprecated
    public List<ServiceProduct> getServiceProductByChannelOfContent(long contentId);
    public List<ServiceProduct> getDemandServiceProductByContentId(long contentId);
    public ServiceProduct getServiceProductBySpId(String productId);
}