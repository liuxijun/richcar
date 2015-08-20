package com.fortune.rms.web.csp;

import com.fortune.common.Constants;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.csp.logic.logicInterface.CspLogicInterface;
import com.fortune.rms.business.csp.logic.logicInterface.CspModuleLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.csp.model.CspModule;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.util.*;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Namespace("/csp")
@ParentPackage("default")
@Action(value="cspModule")
public class CspModuleAction extends BaseAction<CspModule> {
	private static final long serialVersionUID = 3243534534534534l;
	private CspModuleLogicInterface cspModuleLogicInterface;
    private CspLogicInterface cspLogicInterface;
    private DeviceLogicInterface deviceLogicInterface;
    public String filter;
    private Long deviceId;
    List<SimpleFileInfo> list;

	@SuppressWarnings("unchecked")
	public CspModuleAction() {
		super(CspModule.class);
	}
	/**
	 * @param cspModuleLogicInterface the cspModuleLogicInterface to set
	 */
    @Autowired
	public void setCspModuleLogicInterface(
			CspModuleLogicInterface cspModuleLogicInterface) {
		this.cspModuleLogicInterface = cspModuleLogicInterface;
		setBaseLogicInterface(cspModuleLogicInterface);
	}
    @Autowired
    public void setCspLogicInterface(CspLogicInterface cspLogicInterface) {
        this.cspLogicInterface = cspLogicInterface;
    }

    @Autowired
    public void setDeviceLogicInterface(DeviceLogicInterface deviceLogicInterface) {
        this.deviceLogicInterface = deviceLogicInterface;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    /**
     * 获取Csp绑定模板的信息
     */
    public String searchModulesByCspId(){
        Integer keyId = StringUtils.string2int(getKeyId(), -1);
        objs =cspModuleLogicInterface.getModulesByCspId(keyId);
        return Constants.ACTION_LIST;
    }


    public String getDefaultModule(){
        obj = cspModuleLogicInterface.getDefaultModule(admin.getCspId());
        return Constants.ACTION_VIEW;
    }
    public String getFilesJson() {
        return JsonUtils.getListJsonString("objs", list, "totalCount", pageBean.getRowCount());
    }
    //xml存放的固定目录（选择目录）
    public String listFiles(){
        String filePath = getRequestParam("filePath","");
        String newfilePath =filePath.substring(1,filePath.length());
        Admin admin = (Admin) session.get(Constants.SESSION_ADMIN);
        int cspId = admin.getCspId();

        //根据cspId到csp中找到对应的csp别名
        Csp csp=cspLogicInterface.getCspByCspId(cspId);
        String alias=csp.getAlias();
        if (filter == null || "".equals(filter)) {
            filter = "*.*";
        } else {
            if(!filter.startsWith("*")) {
                filter = "*" +filter;
            }

            if(!filter.endsWith("*")) {
                filter += "*";
            }
        }
        if(alias==null){
            alias = "";
        }
        String realDir;
        AppConfigurator config = AppConfigurator.getInstance();
        if(config.getBoolConfig("importXml.useSystemDir",false)||(deviceId==null||deviceId<=0)){
            realDir = config.getConfig("importXml.baseFileDir", "E:/baseDir/")+alias;
        }else{
            Device device = deviceLogicInterface.get(deviceId);
            realDir = device.getLocalPath()+"/";
        }
        String realOpenDir = realDir+newfilePath;
        log.debug("准备列目录："+realOpenDir);
        list = FileUtils.listFiles(realOpenDir , filter, pageBean, false);
        return "listFiles";
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
}
