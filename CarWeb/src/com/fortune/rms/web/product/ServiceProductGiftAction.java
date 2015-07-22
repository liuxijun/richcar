package com.fortune.rms.web.product;

import com.fortune.common.Constants;
import com.fortune.rms.business.product.logic.logicInterface.ServiceProductGiftLogicInterface;
import com.fortune.rms.business.product.model.ServiceProductGift;
import com.fortune.common.web.base.BaseAction;
import com.fortune.util.JsonUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
@Namespace("/product")
@ParentPackage("default")
@Action(value="serviceProductGift")
public class ServiceProductGiftAction extends BaseAction<ServiceProductGift> {
	private static final long serialVersionUID = 3243534534534534l;
	private ServiceProductGiftLogicInterface serviceProductGiftLogicInterface;
    private String serviceProductGiftString;
    private List<ServiceProductGift> serviceProductGifts;
    private String serviceProductGiftIds;


    @SuppressWarnings("unchecked")
	public ServiceProductGiftAction() {
		super(ServiceProductGift.class);
	}
	/**
	 * @param serviceProductGiftLogicInterface the serviceProductGiftLogicInterface to set
	 */
    @Autowired
	public void setServiceProductGiftLogicInterface(
			ServiceProductGiftLogicInterface serviceProductGiftLogicInterface) {
		this.serviceProductGiftLogicInterface = serviceProductGiftLogicInterface;
		setBaseLogicInterface(serviceProductGiftLogicInterface);
	}
    public String  saveServiceProductGift(){
        serviceProductGiftLogicInterface.saveServiceProductGift(serviceProductGifts, serviceProductGiftIds,keyId);
        writeSysLog("保存赠送服务产品： "+keyId+",serviceProductGiftId="+serviceProductGiftIds);
        return Constants.ACTION_SAVE;
    }
    public String searchServiceProductGift(){
        serviceProductGifts = serviceProductGiftLogicInterface.getAllServiceProductGiftOfServiceProduct(keyId);
        JsonUtils jsonUtils = new JsonUtils();
        directOut(jsonUtils.getListJson("serviceProductGifts",serviceProductGifts, "totalCount", serviceProductGifts.size()));
        return null;
    }

    public String getServiceProductGiftString() {
        return serviceProductGiftString;
    }

    public void setServiceProductGiftString(String serviceProductGiftString) {
         long keyId = getRequestIntParam("keyId",0);
        this.serviceProductGiftString = serviceProductGiftString;
        if(serviceProductGiftString!=null){
           String[] serviceProductGiftArray = serviceProductGiftString.split(",");
           if(serviceProductGiftArray.length!=0){
               serviceProductGifts = new ArrayList<ServiceProductGift>();
              for(int i=0;i<serviceProductGiftArray.length;i++){                  
                   String []serviceProductGift =serviceProductGiftArray[i].split("_");
                   String serviceProductGiftId="";
                   if(serviceProductGift.length!=0){
                       serviceProductGiftId = serviceProductGift[0];
                           if(serviceProductGiftIds ==null|| serviceProductGiftIds ==""){
                             serviceProductGiftIds = serviceProductGiftId;
                          }else{
                              serviceProductGiftIds +=","+serviceProductGiftId;
                          }
                   }
                  if(serviceProductGift.length==3){
                      ServiceProductGift serviceProductGift1 = new ServiceProductGift();
                      serviceProductGift1.setServiceProductId(keyId);
                      serviceProductGift1.setGiftServiceProductId(Long.parseLong(serviceProductGiftId));
                      try{
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            serviceProductGift1.setStartTime(simpleDateFormat.parse(serviceProductGift[1]));
                            serviceProductGift1.setEndTime(simpleDateFormat.parse(serviceProductGift[2]));
                      }catch(Exception ex){
                          log.debug(ex.getStackTrace());
                      }
                   serviceProductGifts.add(serviceProductGift1);
                  }

              }
           }
        }
    }

    public List<ServiceProductGift> getServiceProductGifts() {
        return serviceProductGifts;
    }

    public void setServiceProductGifts(List<ServiceProductGift> serviceProductGifts) {
        this.serviceProductGifts = serviceProductGifts;
    }

    public String getServiceProductGiftIds() {
        return serviceProductGiftIds;
    }

    public void setServiceProductGiftIds(String serviceProductGiftIds) {
        this.serviceProductGiftIds = serviceProductGiftIds;
    }

}
