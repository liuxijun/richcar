package com.fortune.rms.business.log.logic.logicInterface;


import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.log.model.ContentDemandLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ContentDemandLogLogicInterface extends BaseLogicInterface<ContentDemandLog> {
    //ÿ��ͳ���õķ���
    public Map getContentDemandFromVisitLog(Date dateStatics, long type, long liveChannelId);
}
