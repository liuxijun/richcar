package com.fortune.rms.business.csp.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.csp.model.CspProduct;
import com.fortune.rms.business.product.model.Product;


import java.util.List;

public interface CspProductDaoInterface
		extends
			BaseDaoInterface<CspProduct, Long> {
    public List<Product> getProductsBySpId(long spId);
}