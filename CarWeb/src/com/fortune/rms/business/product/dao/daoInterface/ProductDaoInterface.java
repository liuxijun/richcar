package com.fortune.rms.business.product.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.product.model.Product;
import com.fortune.util.PageBean;

import java.util.List;

public interface ProductDaoInterface extends BaseDaoInterface<Product, Long> {

    public List<Product> getProductType(Long type, PageBean pageBean);
    public List<Product> getProductsOfStatus(int status);
    @Deprecated
    public List getProductsByCspId(long cspId, long contentId);
    @Deprecated
    public List getProductsByChannel(long contentId);
    //现在推荐使用getProducts方法
    public List<Object[]> getProducts(long contentId);
    @Deprecated
    public List getProductsByMobileProductFlag(int mobileProductFlag);
    public List getMobileProducts();
}