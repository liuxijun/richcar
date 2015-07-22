package com.fortune.rms.business.live.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.live.dao.daoInterface.RecordDaoInterface;
import com.fortune.rms.business.live.model.Record;
import org.springframework.stereotype.Repository;

/**
 * Created by ÍõÃ÷Â· on 2015/2/26.
 */
@Repository
public class RecordDaoAccess extends BaseDaoAccess<Record, Long>
        implements
        RecordDaoInterface {
    public RecordDaoAccess() {
        super(Record.class);
    }
}
