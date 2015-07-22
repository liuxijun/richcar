package com.fortune.rms.web.product;

import com.fortune.common.Constants;
import com.fortune.rms.business.product.logic.logicInterface.ServiceProductLogicInterface;
import com.fortune.rms.business.product.model.ServiceProduct;
import com.fortune.common.web.base.BaseAction;
import com.fortune.util.BeanUtils;
import com.fortune.util.SearchResult;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/product")
@ParentPackage("default")
@Action(value="serviceProduct")
public class ServiceProductAction extends BaseAction<ServiceProduct> {
	private static final long serialVersionUID = 3243534534534534l;
	private ServiceProductLogicInterface serviceProductLogicInterface;
    private Long cspId;
	@SuppressWarnings("unchecked")
	public ServiceProductAction() {
		super(ServiceProduct.class);
	}
	/**
	 * @param serviceProductLogicInterface the serviceProductLogicInterface to set
	 */
    @Autowired
	public void setServiceProductLogicInterface(
			ServiceProductLogicInterface serviceProductLogicInterface) {
		this.serviceProductLogicInterface = serviceProductLogicInterface;
		setBaseLogicInterface(serviceProductLogicInterface);
	}

    public void setCspId(Long cspId) {
        this.cspId = cspId;
    }

    public String view(){
       String result = super.view();
        BeanUtils.setDefaultValue(obj, "cspId",ServiceProduct.PRIORITY_CSPID);
        return result;
    }

       public String list() {
        objs = serviceProductLogicInterface.getServiceProduct(obj,pageBean);
        return "list";
    }
    public String listOfSelf(){
        objs = serviceProductLogicInterface.getServiceProductOfCsp(obj,pageBean,keyId);
       return "list";
    }
    public String searchServiceProduct(){
       objs = serviceProductLogicInterface.getServiceProduct(obj,pageBean);
       return "list";
    }

    public String getServiceProductsByCspId(){
        log.debug("in getServiceProductsByCspId");
        objs=serviceProductLogicInterface.getServiceProductsByCspId(cspId);
        return Constants.ACTION_LIST;
    }
}