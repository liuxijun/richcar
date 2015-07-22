package com.fortune.rms.business.log.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.log.dao.daoInterface.ContentDemandLogDaoInterface;
import com.fortune.rms.business.log.logic.logicInterface.ContentDemandLogLogicInterface;
import com.fortune.rms.business.log.model.ContentDemandLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("contentDemandLogLogicInterface")
public class ContentDemandLogLogicImpl extends  BaseLogicImpl<ContentDemandLog>
        implements
             ContentDemandLogLogicInterface {
    private ContentDemandLogDaoInterface contentDemandLogDaoInterface;

    /**
     * @param contentDemandLogDaoInterface the contentDemandLogDaoInterface to set
     */
    @Autowired
    public void setContentDemandLogDaoInterface(
            ContentDemandLogDaoInterface contentDemandLogDaoInterface) {
        this.contentDemandLogDaoInterface = contentDemandLogDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.contentDemandLogDaoInterface;
    }

    public Map getContentDemandFromVisitLog(Date dateStatics, long type,long liveChannelId) {
        return contentDemandLogDaoInterface.getContentDemandFromVisitLog(dateStatics,type,liveChannelId);
    }
}
