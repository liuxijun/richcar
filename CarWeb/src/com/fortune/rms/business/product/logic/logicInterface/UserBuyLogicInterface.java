package com.fortune.rms.business.product.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.product.model.Product;
import com.fortune.rms.business.product.model.UserBuy;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserBuyLogicInterface extends BaseLogicInterface<UserBuy> {

    public List<Map<String,Object>> getAllSpSalesCount(UserBuy userBuy, PageBean pageBean);
    public List<Map<String,Object>> getAllProductSalesCount(UserBuy userBuy, PageBean pageBean);
    public List<Map<String,Object>> getAllContentSalesCount(UserBuy userBuy, PageBean pageBean);
    public List<UserBuy> getUnSetAreaIdData(UserBuy userBuy, PageBean pageBean);
    UserBuy saveUserBuyLog(String userId, String productId, Long contentId, Long contentPropertyId, Long channelId, Csp csp, Product product);
    public boolean hasBuy(String userId, Date now, Long contentId, Long contentPropertyId, Long channelId, Long productType, List<String> productIds);
}
