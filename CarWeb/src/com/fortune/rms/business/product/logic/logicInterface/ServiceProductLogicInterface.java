package com.fortune.rms.business.product.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.product.model.ServiceProduct;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;

import java.util.List;

public interface ServiceProductLogicInterface
		extends
			BaseLogicInterface<ServiceProduct> {
    public List<ServiceProduct> getServiceProduct(ServiceProduct serviceProduct, PageBean pageBean);
    public List<ServiceProduct> getServiceProductOfCsp(ServiceProduct serviceProduct, PageBean pageBean, long cspId);
    public List<ServiceProduct> getServiceProductOfServiceProductId(ServiceProduct serviceProduct, PageBean pageBean, long serviceProductId);

    public List<ServiceProduct> getServiceProductsByCspId(long cspId);
    public List<ServiceProduct> getServiceProductByContentId(long contentId);
    public List<ServiceProduct> getDemandServiceProductByContentId(long contentId);
    public ServiceProduct getServiceProductBySpId(String productId);
}
