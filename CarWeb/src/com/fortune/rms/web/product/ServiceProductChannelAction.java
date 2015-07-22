package com.fortune.rms.web.product;


import com.fortune.common.Constants;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.product.logic.logicInterface.ServiceProductChannelLogicInterface;
import com.fortune.rms.business.product.model.ServiceProductChannel;
import com.fortune.util.JsonUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

@Namespace("/product")
@ParentPackage("default")
@Action(value="serviceProductChannel")
public class ServiceProductChannelAction extends BaseAction<ServiceProductChannel> {

    @SuppressWarnings("unchecked")
    public ServiceProductChannelAction() {
        super(ServiceProductChannel.class);
    }


    private ServiceProductChannelLogicInterface  serviceProductChannelLogicInterface;

    /**
     * @param serviceProductChannelLogicInterface the serviceProductChannelLogicInterface to set
     */
    @Autowired
    public void setServiceProductChannelLogicInterface(
            ServiceProductChannelLogicInterface serviceProductChannelLogicInterface) {
        this.serviceProductChannelLogicInterface = serviceProductChannelLogicInterface;
        setBaseLogicInterface(serviceProductChannelLogicInterface);
    }

    public long serviceProductId;

    public void setServiceProductId(long serviceProductId) {
        this.serviceProductId = serviceProductId;
    }

    public String getSelectedChannel() {
        try {
            List  list =  serviceProductChannelLogicInterface.getSelectedChannel(serviceProductId);
            JsonUtils jsonUtils = new JsonUtils();
            String output = "{success:\"true\",serviceProductChannel:" + jsonUtils.getListJson(list) +"}";
            directOut(output);
            log.debug(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @SuppressWarnings("unused")
    public void directOut(String result){
        try{
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("Cache-Control","no-cache");
            PrintWriter pw=new PrintWriter(new OutputStreamWriter(response.getOutputStream(),"utf-8"));

            pw.print(result);
            pw.close();
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }

    private String channelIds;
    private String serviceProductIds;

    public void setChannelIds(String channelIds) {
        this.channelIds = channelIds;
    }

    public void setServiceProductIds(String serviceProductIds) {
        this.serviceProductIds = serviceProductIds;
    }

    public String changeSelectedChannel() {
        if(serviceProductIds != null && !serviceProductIds.trim().isEmpty()) {
            //清空绑定关系
            serviceProductChannelLogicInterface.deleteByServiceProductIds(serviceProductIds);
            //批量绑定服务产品与频道关系
            serviceProductChannelLogicInterface.bulkBindingChannel(serviceProductIds, channelIds);
        }

        return Constants.ACTION_SAVE;
    }

}
