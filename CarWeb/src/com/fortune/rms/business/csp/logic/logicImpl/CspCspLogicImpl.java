package com.fortune.rms.business.csp.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.csp.dao.daoInterface.CspCspDaoInterface;
import com.fortune.rms.business.csp.logic.logicInterface.CspCspLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.csp.model.CspCsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service("cspCspLogicInterface")
public class CspCspLogicImpl extends BaseLogicImpl<CspCsp>
		implements
			CspCspLogicInterface {
	private CspCspDaoInterface cspCspDaoInterface;

	/**
	 * @param cspCspDaoInterface the cspCspDaoInterface to set
	 */
    @Autowired
	public void setCspCspDaoInterface(CspCspDaoInterface cspCspDaoInterface) {
		this.cspCspDaoInterface = cspCspDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.cspCspDaoInterface;
	}

    /**
     *
     * @param cspIds
     * @param cspId
     */
    public void saveCspToCsp(List<Long> cspIds, long cspId) {
        if(cspIds==null){
            cspIds = new ArrayList<Long>();
        }
        CspCsp object =  new CspCsp();
        object.setMasterCspId(cspId);
        List<CspCsp> oldCspCsps =super.search(object);
        if(oldCspCsps != null){
           for(CspCsp cc:oldCspCsps){
               boolean cspFound = false;
               for(Long cspNewId:cspIds){
                   if(cc.getCspId().equals(cspNewId)){
                       cspFound = true;
                       cspIds.remove(cspId);
                       break;
                   }
               }
               if(cspFound){
                    cspCspDaoInterface.remove(cc);
               }
           }
        }
        for(Long cspNewId:cspIds){
             if(cspNewId != null){
                CspCsp cc = new CspCsp(-1,cspId,cspNewId);
                cspCspDaoInterface.save(cc);
             }
        }
    }

    public List<Csp> getCpsOfStatus(long spId) {
        return this.cspCspDaoInterface.getCpOfSp(spId);  
     }

    public List<Csp> getCpsBySpId(long spId) {
        return this.cspCspDaoInterface.getCpsBySpId(spId);
    }
}
