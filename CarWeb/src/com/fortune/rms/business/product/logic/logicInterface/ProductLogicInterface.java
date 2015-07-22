package com.fortune.rms.business.product.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.product.model.Product;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;

import java.util.List;
import java.util.Map;

public interface ProductLogicInterface extends BaseLogicInterface<Product> {

    public static final int STATUS_ONLY_UN_SUBSCRIBE=10;//����
    public static final int STATUS_ONLY_SUBSCRIBE=11;   //�ɶ�
    public static final int STATUS_ONLY_DISPLAY=12;     //չʾ
    public static final int STATUS_HIDDEN  = 13;         //����
    public static final int STATUS_OK       = 1;         //����
    public static final int STATUS_INVALID = 2;         //��Ч
    public final static int PRODUCT_ONLINE = 1;         //����
    public final static int PRODUCT_OFFLINE = 0;        //����
    public static final Long TYPE_FOR_ONCE  = 2L;        //����
    public static final Long TYPE_FOR_MONTH = 1L;        //����

    public List<Product> getAllProductOfStatus(Product product, PageBean pageBean);
    public List<Product> getAllProduct(Product product, PageBean pageBean);
    public List<Product> getProductType(Long type, PageBean pageBean);
    public List<Product> getProductsOfStatus(int status);
    public List<Map> getProductsOfCanBeBuy(long contentId);

    public List<Map> getProductsByContentId_Pc(long contentId);
    public List<Map<String,Object>> getMobileProducts();
    public List<Map<String,Object>> getUserMobileProducts(String userTelephone, int fixedDescriptionLength);
    public List<Map<String,Object>> getUserMobileProducts(String userTelephone);
    public List<Map<String,Object>> getUserMobileProducts_Pc(String userTelephone);

    Product getProductByPayId(String productId);
}
