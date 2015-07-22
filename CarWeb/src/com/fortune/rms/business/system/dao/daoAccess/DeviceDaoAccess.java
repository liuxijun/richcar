package com.fortune.rms.business.system.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.system.dao.daoInterface.DeviceDaoInterface;
import com.fortune.rms.business.system.model.Device;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class DeviceDaoAccess extends BaseDaoAccess<Device, Long>
		implements
			DeviceDaoInterface {

	public DeviceDaoAccess() {
		super(Device.class);
	}

    @SuppressWarnings("unchecked")
    public List<Device> getDevicesOfStatus(int status) {
        String hql = "from Device d where d.status = "+status+"";
        return this.getHibernateTemplate().find(hql);
    }

    @SuppressWarnings("unchecked")
    public List<Device> getDevicesExceptMasterDevice(String masterIp) {
        String hql = "from Device d where d.ip != '"+masterIp+"' and d.type=3";
        return this.getHibernateTemplate().find(hql);
    }

    @SuppressWarnings("unchecked")
    public List<Device> getDevicesByCspId(long type,long status,long cspId) {
        String hql = "from Device d where 1=1";
        if(type>=0){
            hql += " and d.type="+type;
        }
        if(status>=0){
            hql+=" and d.status="+status;
        }
        if(cspId>0){
            hql+= " and d.id in (select cd.deviceId from CspDevice cd where cd.cspId="+cspId+")";

        }
        return this.getHibernateTemplate().find(hql);
    }
    public List<Device> getDevicesByCspId(long cspId) {
        return getDevicesByCspId(-1,1,cspId);
    }

    @SuppressWarnings("unchecked")
    public String getDeviceUrlByDeviceName(String name) {
        String deviceUrl = null;
        String hql = "from Device d where d.name='"+name+"'";
        List<Device> devices = this.getHibernateTemplate().find(hql);
        if(devices!=null &&devices.size()!=0){
              deviceUrl = devices.get(0).getUrl();
        }
        return deviceUrl;
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> getServerCount() {
        String hql = "select d.type,count(*) from Device d group by d.type";
        return getHibernateTemplate().find(hql);
    }

    /**
     * 根据服务器ip获得服务器详细信息
     * @param ip 服务器ip
     * @return Device
     */
    @SuppressWarnings("unchecked")
    public Device getDeviceByIp(String ip,long type){
        String hql = "from Device d";
        String condition = "lower(d.ip)='" + ip.toLowerCase() + "' or lower(d.url) like '%" + ip.toLowerCase() + "%'";
        if(type>=0){
            condition ="("+condition+") and d.type="+type;
        }
        hql += " where "+condition;
        List<Device> deviceList = getHibernateTemplate().find(hql);
        return (deviceList != null && deviceList.size()>0)? deviceList.get(0) : null;
    }

    public void remove(Device device){
        if(device==null)return;
        String deviceName = device.getName();
        long id = device.getId();
        logger.debug(onRecordRemoved(deviceName,"CONTENT","update CONTENT set DEVICE_ID=-" + id + ",STATUS="
                + ContentLogicInterface.STATUS_DELETE + " where DEVICE_ID=" + id));
        logger.debug(removeRelatedData(new String[]{"CSP_DEVICE"},device.getName(),"DEVICE_ID",device.getId()));
        super.remove(device);
    }
}
