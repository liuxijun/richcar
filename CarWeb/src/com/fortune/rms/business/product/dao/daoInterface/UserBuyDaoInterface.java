package com.fortune.rms.business.product.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.product.model.Product;
import com.fortune.rms.business.product.model.UserBuy;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;

import java.util.Date;
import java.util.List;

public interface UserBuyDaoInterface extends BaseDaoInterface<UserBuy, Long> {
    public List<UserBuy> getUnSetAreaIdData(UserBuy userBuy, PageBean pageBean);
    public List<Object[]> getAllSpSalesCount(UserBuy userBuy, PageBean pageBean);
    public List<Object[]> getAllProductSalesCount(UserBuy userBuy, PageBean pageBean);
    public List<Object[]> getAllContentSalesCount(UserBuy userBuy, PageBean pageBean);
    public List<UserBuy> hasBuy(String userId, Date now, Long contentId, Long contentPropertyId, Long channelId,
                                Long productType, List<String> productIds, PageBean pageBean);
}