package com.fortune.rms.business.syn.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.syn.dao.daoInterface.SynFileDaoInterface;
import com.fortune.rms.business.syn.model.SynFile;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: liupeng
 * Date: 2011-6-20
 * Time: 10:28:21
 * 
 */
@Repository
public class SynFileDaoAccess extends BaseDaoAccess<SynFile, Long> implements SynFileDaoInterface {

    public SynFileDaoAccess() {
        super(SynFile.class);
    }

}
