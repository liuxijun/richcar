package com.fortune.rms.business.system.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.system.model.IP2PhoneLog;

import java.util.Date;

public interface IP2PhoneLogLogicInterface extends BaseLogicInterface<IP2PhoneLog> {
    public IP2PhoneLog createLog(String ip, String token, String uniKey, String phone, int resultCode, String logs, Date startTime, String userAgent);
}
