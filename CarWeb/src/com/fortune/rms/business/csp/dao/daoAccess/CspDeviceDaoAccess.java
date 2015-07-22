package com.fortune.rms.business.csp.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.csp.dao.daoInterface.CspDeviceDaoInterface;
import com.fortune.rms.business.csp.model.CspDevice;
import com.fortune.rms.business.system.model.Device;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class CspDeviceDaoAccess extends BaseDaoAccess<CspDevice, Long>
		implements
			CspDeviceDaoInterface {

	public CspDeviceDaoAccess() {
		super(CspDevice.class);
	}
      public List<Device> getDeviceOfCsp(long cspId) {
        String hqlStr = "from Device d where d.status=1";
        if(cspId>0){
           hqlStr += " and d.id in (select cd.deviceId from CspDevice cd where cspId="+cspId+" )";
        }
        return this.getHibernateTemplate().find(hqlStr);
    }

    public List<Device> getDevicesByCspId(long cspId) {
        String hqlStr = "from Device d where d.status=1 and d.id in (select cd.deviceId from CspDevice cd where cspId="+cspId+" )";
        return this.getHibernateTemplate().find(hqlStr);
    }
}
