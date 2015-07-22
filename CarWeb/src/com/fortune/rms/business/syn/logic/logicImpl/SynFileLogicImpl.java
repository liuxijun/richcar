package com.fortune.rms.business.syn.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.syn.dao.daoInterface.SynFileDaoInterface;
import com.fortune.rms.business.syn.logic.logicInterface.SynFileLogicInterface;
import com.fortune.rms.business.syn.model.SynFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: liupeng
 * Date: 2011-6-20
 * Time: 10:40:33
 * 
 */
@Service("synFileLogicInterface")
public class SynFileLogicImpl extends BaseLogicImpl<SynFile> implements SynFileLogicInterface {
    private SynFileDaoInterface synFileDaoInterface;

    public static String SYNFILE_ADD="1";
    public static String SYNFILE_DEL="2";
    @Autowired
    public void setSynFileDaoInterface(SynFileDaoInterface synFileDaoInterface) {
        this.synFileDaoInterface = synFileDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.synFileDaoInterface;
    }


}
