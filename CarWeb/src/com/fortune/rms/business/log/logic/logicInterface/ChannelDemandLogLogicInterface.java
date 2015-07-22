package com.fortune.rms.business.log.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.log.model.ChannelDemandLog;

import java.util.Date;
import java.util.List;

public interface ChannelDemandLogLogicInterface extends BaseLogicInterface<ChannelDemandLog> {
    public List getChannelDemandFromVisitLog(Date dateStatics);
}
