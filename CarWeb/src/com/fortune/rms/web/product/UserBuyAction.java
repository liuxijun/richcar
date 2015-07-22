package com.fortune.rms.web.product;

import com.fortune.rms.business.product.logic.logicInterface.UserBuyLogicInterface;
import com.fortune.rms.business.product.model.UserBuy;
import com.fortune.common.web.base.BaseAction;
import com.fortune.util.JsonUtils;
import com.fortune.util.SearchResult;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
@Namespace("/product")
@ParentPackage("default")
@Action(value="userBuy")
public class UserBuyAction extends BaseAction<UserBuy> {
	private static final long serialVersionUID = 3243534534534534l;
	private UserBuyLogicInterface userBuyLogicInterface;
    private UserBuy userBuy;
    private List<Map<String,Object>> maps;
    
	@SuppressWarnings("unchecked")
	public UserBuyAction() {
		super(UserBuy.class);
	}
	/**
	 * @param userBuyLogicInterface the userBuyLogicInterface to set
	 */
    @Autowired
	public void setUserBuyLogicInterface(
			UserBuyLogicInterface userBuyLogicInterface) {
		this.userBuyLogicInterface = userBuyLogicInterface;
		setBaseLogicInterface(userBuyLogicInterface);
	}

    public String searchSpSalesCount(){

        maps = userBuyLogicInterface.getAllSpSalesCount(userBuy,pageBean);
       
        return "list";
    }

    public String searchProductSalesCount(){
        maps = userBuyLogicInterface.getAllProductSalesCount(userBuy,pageBean);

        return "list";
    }

    public String searchContentSalesCount(){
        maps = userBuyLogicInterface.getAllContentSalesCount(userBuy,pageBean);

        return "list";
    }

    public UserBuy getUserBuy() {
        return userBuy;
    }

    public void setUserBuy(UserBuy userBuy) {
        this.userBuy = userBuy;
    }

    public List<Map<String, Object>> getMaps() {
        return maps;
    }
        @SuppressWarnings("unused")
    public String getJsonObjs() {
        return getJsonArray(maps);
    }

    public void setMaps(List<Map<String, Object>> maps) {
        this.maps = maps;
    }
}
