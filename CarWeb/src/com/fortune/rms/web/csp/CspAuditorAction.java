package com.fortune.rms.web.csp;

import com.fortune.common.Constants;
import com.fortune.rms.business.csp.logic.logicInterface.CspAuditorLogicInterface;
import com.fortune.rms.business.csp.model.CspAuditor;
import com.fortune.common.web.base.BaseAction;
import com.fortune.util.JsonUtils;
import com.fortune.util.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/csp")
@ParentPackage("default")
@Action(value="cspAuditor")
public class CspAuditorAction extends BaseAction<CspAuditor> {
	private static final long serialVersionUID = 3243534534534534l;
	private CspAuditorLogicInterface cspAuditorLogicInterface;
	@SuppressWarnings("unchecked")
	public CspAuditorAction() {
		super(CspAuditor.class);
	}
	/**
	 * @param cspAuditorLogicInterface the cspAuditorLogicInterface to set
	 */
    @Autowired
	public void setCspAuditorLogicInterface(
			CspAuditorLogicInterface cspAuditorLogicInterface) {
		this.cspAuditorLogicInterface = cspAuditorLogicInterface;
		setBaseLogicInterface(cspAuditorLogicInterface);
	}

    /**
     * 获取Csp绑定管理员的信息
     */
    public String searchAuditorsByCspId(){
		Integer keyId = StringUtils.string2int(getKeyId(), -1);
		objs = cspAuditorLogicInterface.getAuditorsByCspId(keyId);
        return Constants.ACTION_LIST;
    }
}
