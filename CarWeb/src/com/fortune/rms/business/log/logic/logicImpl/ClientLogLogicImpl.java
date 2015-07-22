package com.fortune.rms.business.log.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.log.dao.daoInterface.ClientLogDaoInterface;
import com.fortune.rms.business.log.logic.logicInterface.ClientLogLogicInterface;
import com.fortune.rms.business.log.model.ClientLog;
import com.fortune.rms.business.system.logic.logicInterface.PhoneRangeLogicInterface;
import com.fortune.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("clientLogLogicInterface")
public class ClientLogLogicImpl extends BaseLogicImpl<ClientLog>
        implements ClientLogLogicInterface {

    private ClientLogDaoInterface clientLogDaoInterface;
    private PhoneRangeLogicInterface phoneRangeLogicInterface;
    @Autowired
    public void setClientLogDaoInterface(ClientLogDaoInterface clientLogDaoInterface) {
        this.clientLogDaoInterface = clientLogDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.clientLogDaoInterface;
    }
    @Autowired
    public void setPhoneRangeLogicInterface(PhoneRangeLogicInterface phoneRangeLogicInterface){
        this.phoneRangeLogicInterface = phoneRangeLogicInterface;
    }
    public boolean checkPhoneCode(String phoneCode){
        List<ClientLog> list =  clientLogDaoInterface.checkPhoneCode(phoneCode);
        return (list!=null&&list.size()>0);
    }

    public ClientLog saveLog(String userId, String ip, String userAgent, String clientVersion, String phoneCode, int type, int areaId, Date time) {
        return null;
    }

    public ClientLog save(ClientLog log){
        if(log==null){
            return null;
        }
        Long status = log.getStatus();
        if(!STATUS_ACTIVITY.equals(status)){
            Long areaId = log.getAreaId();
            if(areaId==null){
                String phone = log.getPhoneCode();
                if(phone!=null){
                    areaId = phoneRangeLogicInterface.getAreaIdOfPhone(StringUtils.string2long(phone,-1));
                    log.setAreaId(areaId);
                }
            }

        }
        return super.save(log);
    }
}
