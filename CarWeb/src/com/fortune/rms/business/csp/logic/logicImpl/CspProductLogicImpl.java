package com.fortune.rms.business.csp.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.csp.dao.daoInterface.CspProductDaoInterface;
import com.fortune.rms.business.csp.logic.logicInterface.CspProductLogicInterface;
import com.fortune.rms.business.csp.model.CspProduct;
import com.fortune.rms.business.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service("cspProductLogicInterface")
public class CspProductLogicImpl extends BaseLogicImpl<CspProduct>
		implements
			CspProductLogicInterface {
	private CspProductDaoInterface cspProductDaoInterface;

	/**
	 * @param cspProductDaoInterface the cspProductDaoInterface to set
	 */
    @Autowired
	public void setCspProductDaoInterface(
			CspProductDaoInterface cspProductDaoInterface) {
		this.cspProductDaoInterface = cspProductDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.cspProductDaoInterface;
	}

    public List<Product> getProductsBySpId(long spId) {
        return cspProductDaoInterface.getProductsBySpId(spId);
    }

    public void saveProductToCsp(List<Long> productIds, long cspId) {
         if(productIds==null){
            productIds = new ArrayList<Long>();
        }
        CspProduct object = new CspProduct();
        object.setCspId(cspId);
        List<CspProduct> oldProducts = super.search(object);
        if(oldProducts != null){
            for(CspProduct cp : oldProducts){
                boolean productFound = false;
                for(Long productId :productIds){
                    if(cp.getProductId().equals(productId)){
                       productFound = true;
                       productIds.remove(productId);
                       break;
                    }
                }
                if(!productFound){
                    cspProductDaoInterface.remove(cp);
                }
            }
        }
        for(Long productId :productIds){
            if(productId != null){
                CspProduct cp =new CspProduct(-1,cspId,productId);
                cspProductDaoInterface.save(cp);
            }
        }
    }
}
