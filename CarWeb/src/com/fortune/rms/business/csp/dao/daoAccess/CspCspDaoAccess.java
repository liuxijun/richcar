package com.fortune.rms.business.csp.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.csp.dao.daoInterface.CspCspDaoInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.csp.model.CspCsp;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class CspCspDaoAccess extends BaseDaoAccess<CspCsp, Long>
		implements
			CspCspDaoInterface {

	public CspCspDaoAccess() {
		super(CspCsp.class);
	}

    public List<Csp> getCpOfSp(long spId) {
        String hqlStr = "from Csp c1 where c1.isCp =1 and c1.status=1  ";
        if(spId>0){
           hqlStr += " and c1.id in (select c2.cspId from CspCsp c2 where c2.masterCspId="+spId+" )";
        }
        return this.getHibernateTemplate().find(hqlStr);
    }

    public List<Csp> getCpsBySpId(long spId) {
        String hqlStr = "from Csp c1 where c1.isCp =1 and c1.status=1 and c1.id in (select c2.cspId from CspCsp c2 where c2.masterCspId="+spId+" )";
        return this.getHibernateTemplate().find(hqlStr);
    }
}
