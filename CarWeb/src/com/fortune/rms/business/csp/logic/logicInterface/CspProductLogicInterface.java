package com.fortune.rms.business.csp.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.csp.model.CspProduct;
import com.fortune.rms.business.product.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CspProductLogicInterface
		extends
			BaseLogicInterface<CspProduct> {
    public List<Product> getProductsBySpId(long spId);
    public void saveProductToCsp(List<Long> productIds, long cspId);
}
