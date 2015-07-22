package com.fortune.rms.business.log.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.log.model.ChannelDemandLog;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-20
 * Time: ионГ10:33
 * To change this template use File | Settings | File Templates.
 */
public interface ChannelDemandLogDaoInterface extends BaseDaoInterface<ChannelDemandLog,Long> {
    public List getChannelDemandFromVisitLog(Date dateStatics);
}
