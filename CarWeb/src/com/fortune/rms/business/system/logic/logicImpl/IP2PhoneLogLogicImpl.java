package com.fortune.rms.business.system.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.system.dao.daoInterface.IP2PhoneLogDaoInterface;
import com.fortune.rms.business.system.logic.logicInterface.IP2PhoneLogLogicInterface;
import com.fortune.rms.business.system.model.IP2PhoneLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-10-18
 * Time: ÏÂÎç4:12
 *
 */
@Service("ip2PhoneLogLogicInterface")
public class IP2PhoneLogLogicImpl extends BaseLogicImpl<IP2PhoneLog> implements IP2PhoneLogLogicInterface {
    private IP2PhoneLogDaoInterface ip2PhoneLogDaoInterface;
    @Autowired
    public void setIp2PhoneLogDaoInterface(IP2PhoneLogDaoInterface ip2PhoneLogDaoInterface) {
        this.ip2PhoneLogDaoInterface = ip2PhoneLogDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.ip2PhoneLogDaoInterface;
    }
    public IP2PhoneLog createLog( String ip, String token, String uniKey, String phone, int resultCode, String logs, Date startTime,String userAgent){
        IP2PhoneLog log = new IP2PhoneLog(-1,ip,token, uniKey,phone,resultCode,logs,startTime,userAgent);
        return save(log);
    }
}
