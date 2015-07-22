package com.fortune.rms.web.csp;

import com.fortune.rms.business.csp.logic.logicInterface.CspDeviceLogicInterface;
import com.fortune.rms.business.csp.model.CspDevice;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.system.model.Device;
import com.fortune.util.JsonUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Namespace("/csp")
@ParentPackage("default")
@Action(value="cspDevice")
@Result(name="deviceList",location="/csp/cspDeviceListJson.jsp")

public class CspDeviceAction extends BaseAction<CspDevice> {
	private static final long serialVersionUID = 3243534534534534l;
	private CspDeviceLogicInterface cspDeviceLogicInterface;
	@SuppressWarnings("unchecked")
	public CspDeviceAction() {
		super(CspDevice.class);
	}
	/**
	 * @param cspDeviceLogicInterface the cspDeviceLogicInterface to set
	 */
    @Autowired
	public void setCspDeviceLogicInterface(
			CspDeviceLogicInterface cspDeviceLogicInterface) {
		this.cspDeviceLogicInterface = cspDeviceLogicInterface;
		setBaseLogicInterface(cspDeviceLogicInterface);
	}

    public List<Device> devices;
    public String getDevicesOfCsp(){
        int cspId = admin.getCspId();
        devices = cspDeviceLogicInterface.getDevicesByCspId(cspId);
        return "deviceList";
    }
    public String getDevicesJson(){
        return JsonUtils.getListJsonString("objs",devices,"totoalCount",devices.size(),getRequestParam("properties",null));
    }
}
