package com.fortune.rms.business.product.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.product.dao.daoInterface.UserBuyDaoInterface;
import com.fortune.rms.business.product.logic.logicInterface.ProductLogicInterface;
import com.fortune.rms.business.product.logic.logicInterface.UserBuyLogicInterface;
import com.fortune.rms.business.product.model.Product;
import com.fortune.rms.business.product.model.UserBuy;
import com.fortune.rms.business.system.logic.logicInterface.PhoneRangeLogicInterface;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;
import com.fortune.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("userBuyLogicInterface")
public class UserBuyLogicImpl extends BaseLogicImpl<UserBuy>
		implements
			UserBuyLogicInterface {
	private UserBuyDaoInterface userBuyDaoInterface;
    private PhoneRangeLogicInterface phoneRangeLogicInterface;

	/**
	 * @param userBuyDaoInterface the userBuyDaoInterface to set
	 */
    @Autowired
	public void setUserBuyDaoInterface(UserBuyDaoInterface userBuyDaoInterface) {
		this.userBuyDaoInterface = userBuyDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.userBuyDaoInterface;
	}

    @Autowired
    public void setPhoneRangeLogicInterface(PhoneRangeLogicInterface phoneRangeLogicInterface) {
        this.phoneRangeLogicInterface = phoneRangeLogicInterface;
    }

    public List<Map<String,Object>> getAllSpSalesCount(UserBuy userBuy, PageBean pageBean) {
        List<Object[]> tempResult = userBuyDaoInterface.getAllSpSalesCount(userBuy,pageBean);
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        for(int i =0;i<tempResult.size();i++){
            Object[] objs= tempResult.get(i);
            Map<String,Object> row = new HashMap<String,Object>();
            row.put("spId",objs[0]);
            row.put("spName",objs[1]);
            row.put("salesAmount",objs[2]);
            row.put("buyNum",objs[3]);

            result.add(row);
        }
        return result;
    }

    public List<Map<String,Object>> getAllProductSalesCount(UserBuy userBuy, PageBean pageBean) {
        List<Object[]> tempResult =userBuyDaoInterface.getAllProductSalesCount(userBuy,pageBean);
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        for(int i=0;i<tempResult.size();i++){
            Object[] objs = tempResult.get(i);
            Map<String,Object> row = new HashMap<String,Object>();
            row.put("spId",objs[0]);
            row.put("spName",objs[1]);
            row.put("serviceProductId",objs[2]);
            row.put("serviceProductName",objs[3]);
            row.put("salesAmount",objs[4]);
            row.put("buyNum",objs[5]);

            result.add(row);
        }
        return result;
    }

    public List<Map<String,Object>> getAllContentSalesCount(UserBuy userBuy, PageBean pageBean) {
        List<Object[]> tempResult =userBuyDaoInterface.getAllContentSalesCount(userBuy,pageBean);
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        for(int i=0;i<tempResult.size();i++){
            Object[] objs = tempResult.get(i);
            Map<String,Object> row = new HashMap<String,Object>();
            row.put("contentId",objs[0]);
            row.put("contentName",objs[1]);
            row.put("salesAmount",objs[2]);
            row.put("buyNum",objs[3]);
            row.put("cpId",objs[4]);
            row.put("cpName",objs[5]);
            row.put("spId",objs[6]);
            row.put("spName",objs[7]);

            result.add(row);

        }
        return result;
    }

    public List<UserBuy> getUnSetAreaIdData(UserBuy userBuy, PageBean pageBean) {
        return userBuyDaoInterface.getUnSetAreaIdData(userBuy,pageBean);
    }


    public UserBuy saveUserBuyLog(String userId, String productId, Long contentId, Long contentPropertyId,Long channelId, Csp csp,Product product) {
        Date now = new Date();
        UserBuy userBuy = new UserBuy();
        userBuy.setCpId(csp.getId());
        userBuy.setBuyTime(now);
        userBuy.setChannelId(channelId);
        userBuy.setContentId(contentId);
        userBuy.setContentPropertyId(contentPropertyId);
        userBuy.setPrice(product.getPrice());
        userBuy.setUserId(userId);
        userBuy.setIsGift(0L);
        userBuy.setSpId(csp.getId());
        userBuy.setProductId(productId);
        userBuy.setServiceProductType(product.getType());
        userBuy.setStartTime(now);
        Long phone = StringUtils.string2long(userId,-1);
        if(phone>0){

        }
        Long length = product.getValidLength();
        Long unit = product.getLengthUnit();
        if(unit==null){
            unit = 1L;
        }
        if(unit==1L){//按天
            unit = 24*3600*1000L;
        }else{//按小时
            unit = 3600*1000L;
        }
        Date endTime = new Date(now.getTime()+length*unit);
        if(ProductLogicInterface.TYPE_FOR_MONTH.equals(product.getType())){
            //如果是包月产品，按照自然月进行设置截至日期
            Calendar cl = Calendar.getInstance();
            cl.setTime(endTime);
            cl.set(Calendar.DAY_OF_MONTH, 0);
            endTime = cl.getTime();
        }
        userBuy.setEndTime(endTime);
        return save(userBuy);
    }

    public boolean hasBuy(String userId,Date now,Long contentId,Long contentPropertyId,Long channelId,Long productType,List<String> productIds){
        PageBean pageBean = new PageBean(1,10,null,null);
        List<UserBuy> result = userBuyDaoInterface.hasBuy(userId, now, contentId, contentPropertyId, channelId, productType, productIds,pageBean);
        return result!=null&&result.size()>0;
    }

}
