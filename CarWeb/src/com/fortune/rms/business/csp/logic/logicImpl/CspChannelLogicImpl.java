package com.fortune.rms.business.csp.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.content.model.ContentChannel;
import com.fortune.rms.business.csp.dao.daoInterface.CspChannelDaoInterface;
import com.fortune.rms.business.csp.logic.logicInterface.CspChannelLogicInterface;
import com.fortune.rms.business.csp.model.CspChannel;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wang
 * Date: 13-1-29
 * Time: ����3:08
 */
@Service("cspChannelLogicInterface")
public class CspChannelLogicImpl extends BaseLogicImpl<CspChannel>
        implements CspChannelLogicInterface{
    private CspChannelDaoInterface cspChannelDaoInterface;
    @Autowired
    public void setCspChannelDaoInterface(CspChannelDaoInterface cspChannelDaoInterface) {
        this.cspChannelDaoInterface = cspChannelDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.cspChannelDaoInterface;
    }

    public List<CspChannel> getCspChannelByCspId(long cspId,int type){
        return this.cspChannelDaoInterface.getCspBindChannels(cspId,type);
    }

    public List<CspChannel> getCspChannelByCspId(long cspId,int type,int status){
        return this.cspChannelDaoInterface.getCspBindChannels(cspId,type,status);
    }

    //����󶨵�Ƶ����Ϣ
    public  boolean saveCspChannel(String chooseChannel,long cspId){
        //����chooseChannel�ַ�����������ӵ����ݿ�
        boolean result =false;
                //����cspId ɾ��ԭ����cspChannel ��ص�����
                if(cspId>0){
                    cspChannelDaoInterface.deleteCspChannel(cspId);
                if (!chooseChannel.equals("")) {
                if (chooseChannel != null) {
                String[] chooseChannels = chooseChannel.split(",");
                for(int i=0;i<chooseChannels.length;i++){
                    Long channelId =Long.parseLong(chooseChannels[i]);

                    if(channelId != null){
                        CspChannel cd =new CspChannel(-1,cspId,channelId);
                        cspChannelDaoInterface.save(cd);
                    }
                }
               }
            }
          result=true;
        }
        return result;
   }
    public boolean hasPrivilegeToChannel(Long cspId,Long channelId){
        Object result = CacheUtils.get(""+cspId+"_"+channelId,"cspChannelCache",new DataInitWorker(){
            public Object init(Object key,String cacheName){
                boolean exist=false;
                String[] data = key.toString().split("_");
                Long cspId = StringUtils.string2long(data[0],-1);
                Long channelId = StringUtils.string2long(data[1],-1);
                exist=cspChannelDaoInterface.hasPrivilegeToChannel(cspId,channelId);
                return exist;
            }
        });
        return result.equals(Boolean.TRUE);
    }
    public List<Channel>  getParentToChannel(Long parentId){
        return this.cspChannelDaoInterface.getChannel(parentId);
    }
}
