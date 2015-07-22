package com.fortune.rms.business.product.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.product.dao.daoInterface.ServiceProductChannelDaoInterface;
import com.fortune.rms.business.product.logic.logicInterface.ServiceProductChannelLogicInterface;
import com.fortune.rms.business.product.model.ServiceProductChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("serviceProductChannelLogicImpl")
public class ServiceProductChannelLogicImpl extends
        BaseLogicImpl<ServiceProductChannel> implements ServiceProductChannelLogicInterface {

    private ServiceProductChannelDaoInterface serviceProductChannelDaoInterface;

    /**
     * @param serviceProductChannelDaoInterface the serviceProductChannelDaoInterface to set
     */
    @Autowired
    public void ServiceProductChannelDaoInterface(
            ServiceProductChannelDaoInterface serviceProductChannelDaoInterface) {
        this.serviceProductChannelDaoInterface = serviceProductChannelDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.serviceProductChannelDaoInterface;
    }


    public List<ServiceProductChannel> getSelectedChannel(long serviceProductId) {
        return serviceProductChannelDaoInterface.getSelectedChannel(serviceProductId);
    }


    public void deleteByServiceProductIds(String serviceProductIds) {
         serviceProductChannelDaoInterface.deleteByServiceProductIds(serviceProductIds);
    }

    public void bulkBindingChannel(String serviceProductIds,String channels) {
          if(channels != null && !channels.trim().isEmpty()) {
              String[] spcIds = serviceProductIds.split(",");
              String[] chIds = channels.split(",");
              for(String serviceProductId : spcIds) {
                  for(String channelId : chIds) {
                      ServiceProductChannel serviceProductChannel = new ServiceProductChannel();
                      serviceProductChannel.setChannelId(Long.valueOf(channelId));
                      serviceProductChannel.setServiceProductId(Long.valueOf(serviceProductId));
                      serviceProductChannelDaoInterface.save(serviceProductChannel);
                  }
              }
          }
    }
}
