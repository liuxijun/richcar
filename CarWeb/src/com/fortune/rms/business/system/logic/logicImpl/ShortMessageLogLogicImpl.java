package com.fortune.rms.business.system.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.system.dao.daoInterface.ShortMessageLogDaoInterface;
import com.fortune.rms.business.system.logic.logicInterface.ShortMessageLogLogicInterface;
import com.fortune.rms.business.system.model.ShortMessageLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("shortMessageLogLogicInterface")
public class ShortMessageLogLogicImpl extends BaseLogicImpl<ShortMessageLog>
		implements
        ShortMessageLogLogicInterface {
	private ShortMessageLogDaoInterface shortMessageLogDaoInterface;
	/**
	 * @param shortMessageLogDaoInterface the ShortMessageLogDaoInterface to set
	 */
    @Autowired
	public void setShortMessageLogDaoInterface(
			ShortMessageLogDaoInterface shortMessageLogDaoInterface) {
		this.shortMessageLogDaoInterface = shortMessageLogDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.shortMessageLogDaoInterface;
	}

    public ShortMessageLog updateSubmitReport(String sn, int status, Date time, String smgIp) {
        if(sn!=null&&!"".equals(sn)){
            ShortMessageLog log = new ShortMessageLog();
            log.setSn(sn);
            List<ShortMessageLog> logs = search(log);
            if(logs!=null&&logs.size()>0){
                log = logs.get(0);
                log.setResponseCode((long)status);
                log.setResponseTime(time);
                log.setResponseIp(smgIp);
                return save(log);
            }
        }
        return null;
    }
}
