package com.fortune.rms.business.log.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.log.dao.daoInterface.ContentZtLogDaoInterface;
import com.fortune.rms.business.log.logic.logicInterface.ContentZtLogLogicInterface;
import com.fortune.rms.business.log.model.ContentZtLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("contentZtLogLogicInterface")
public class ContentZtLogLogicImpl extends BaseLogicImpl<ContentZtLog>
        implements
            ContentZtLogLogicInterface {

    private ContentZtLogDaoInterface contentZtLogDaoInterface;

    /**
     * @param contentZtLogDaoInterface the contentZtLogDaoInterface to set
     */
    @Autowired
    public void setContentZtLogDaoInterface(ContentZtLogDaoInterface contentZtLogDaoInterface) {
        this.contentZtLogDaoInterface = contentZtLogDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.contentZtLogDaoInterface;
    }
}
