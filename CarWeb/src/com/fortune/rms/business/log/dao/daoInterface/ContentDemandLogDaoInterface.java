package com.fortune.rms.business.log.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.log.model.ContentDemandLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-6
 * Time: ионГ9:36
 * To change this template use File | Settings | File Templates.
 */
public interface ContentDemandLogDaoInterface extends BaseDaoInterface<ContentDemandLog, Long> {

    public Map getContentDemandFromVisitLog(Date dateStatics, long type, long liveChannelId);
}
