package com.fortune.rms.business.log.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.log.dao.daoInterface.ChannelDemandLogDaoInterface;
import com.fortune.rms.business.log.logic.logicInterface.ChannelDemandLogLogicInterface;
import com.fortune.rms.business.log.model.ChannelDemandLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-20
 * Time: ÉÏÎç10:31
 * To change this template use File | Settings | File Templates.
 */
@Service("channelDemandLogLogicInterface")
public class ChannelDemandLogLogicImpl extends BaseLogicImpl<ChannelDemandLog>
        implements ChannelDemandLogLogicInterface {
    private ChannelDemandLogDaoInterface channelDemandLogDaoInterface;

    /**
     * @param channelDemandLogDaoInterface the channelDemandLogDaoInterface to set
     */
    @Autowired
    public void setChannelDemandLogDaoInterface(ChannelDemandLogDaoInterface channelDemandLogDaoInterface) {
        this.channelDemandLogDaoInterface = channelDemandLogDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.channelDemandLogDaoInterface;
    }

    public List getChannelDemandFromVisitLog(Date dateStatics) {
        return channelDemandLogDaoInterface.getChannelDemandFromVisitLog(dateStatics);
    }
}
