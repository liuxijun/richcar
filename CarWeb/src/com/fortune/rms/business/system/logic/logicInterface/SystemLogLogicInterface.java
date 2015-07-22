package com.fortune.rms.business.system.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.system.model.SystemLog;
import com.fortune.util.PageBean;

import java.util.Date;
import java.util.List;

public interface SystemLogLogicInterface extends BaseLogicInterface<SystemLog> {
    public List<SystemLog> getSystemLogAll(SystemLog systemLog, Date startTime, Date stopTime, PageBean pageBean);
    public List<SystemLog> export(SystemLog systemLog, Date startTime, Date stopTime, PageBean pageBean);
    public SystemLog getSystemLogId(Long id);
    public void saveDebugLog(SystemLog systemLog);
    public SystemLog saveLog(String clientIp, Admin admin, String action, String logInfo);
    public SystemLog saveMachineLog(String logInfo);
}
