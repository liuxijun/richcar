package com.fortune.rms.business.system.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.system.model.ShortMessageLog;

import java.util.Date;

public interface ShortMessageLogLogicInterface extends BaseLogicInterface<ShortMessageLog> {
    public ShortMessageLog updateSubmitReport(String sn, int status, Date time, String smgIp);
}
