package com.fortune.rms.business.csp.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.csp.dao.daoInterface.CspDeviceDaoInterface;
import com.fortune.rms.business.csp.logic.logicInterface.CspDeviceLogicInterface;
import com.fortune.rms.business.csp.model.CspDevice;
import com.fortune.rms.business.system.model.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service("cspDeviceLogicInterface")
public class CspDeviceLogicImpl extends BaseLogicImpl<CspDevice>
		implements
			CspDeviceLogicInterface {
    private CspDeviceDaoInterface cspDeviceDaoInterface;
    
    @Autowired
	public void setCspDeviceDaoInterface(
			CspDeviceDaoInterface cspDeviceDaoInterface) {
		this.cspDeviceDaoInterface = cspDeviceDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.cspDeviceDaoInterface;
	}

    public List<Device> getDeviceOfCsp(long cspId) {
        return cspDeviceDaoInterface.getDeviceOfCsp(cspId);
    }

    public List<Device> getDevicesByCspId(long cspId) {
        return cspDeviceDaoInterface.getDevicesByCspId(cspId);
    }

    public void saveDeviceToCsp(List<Long> deviceIds, long cspId) {
        if(deviceIds==null){
            deviceIds = new ArrayList<Long>();
        }
        CspDevice object = new CspDevice();
        object.setCspId(cspId);
        List<CspDevice> oldDevices = super.search(object);
        if(oldDevices != null){
            for(CspDevice cd : oldDevices){
                boolean deviceFound = false;
                for(Long deviceId :deviceIds){
                    if(cd.getDeviceId().equals(deviceId)){
                       deviceFound = true;
                       deviceIds.remove(deviceId);
                       break;
                    }
                }
                if(!deviceFound){
                    cspDeviceDaoInterface.remove(cd);
                }
            }
        }
        for(Long deviceId :deviceIds){
            if(deviceId != null){
                CspDevice cd =new CspDevice(-1,cspId,deviceId);
                cspDeviceDaoInterface.save(cd);
            }
        }
    }
}
