package com.fortune.rms.business.user;

import com.fortune.rms.business.product.model.Product;
import com.fortune.rms.business.product.model.ServiceProduct;
import com.fortune.util.cache.Cacheable;

import java.io.Serializable;
import java.util.List;
/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-7-9
 * Time: 11:04:40
 * 产品表的缓存
 */
public class ServiceProductCache  implements Cacheable, Serializable {
    public int getSize(){
        return 1;
    }
    public ServiceProduct serviceProduct;
    public Product product;
    public List<ServiceProduct> masterServiceProduct;
    public List<ServiceProduct> giftServiceProduct;

}
