package com.fortune.rms.business.csp.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.csp.dao.daoInterface.CspProductDaoInterface;
import com.fortune.rms.business.csp.model.CspProduct;
import com.fortune.rms.business.product.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class CspProductDaoAccess extends BaseDaoAccess<CspProduct, Long>
		implements
			CspProductDaoInterface {

	public CspProductDaoAccess() {
		super(CspProduct.class);
	}

    public List<Product> getProductsBySpId(long cspId) {
        String hqlStr ="from Product p where p.status=1 and p.id in(select cp.productId from CspProduct cp where cp.cspId="+cspId+")";
        return this.getHibernateTemplate().find(hqlStr);
    }
}
