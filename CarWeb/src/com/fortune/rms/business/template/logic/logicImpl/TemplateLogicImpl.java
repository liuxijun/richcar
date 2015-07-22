package com.fortune.rms.business.template.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.rms.business.csp.logic.logicInterface.CspLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.template.dao.daoInterface.TemplateDaoInterface;
import com.fortune.rms.business.template.logic.logicInterface.TemplateLogicInterface;
import com.fortune.rms.business.template.model.Template;
import com.fortune.util.HzUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("templateLogicInterface")
public class TemplateLogicImpl extends BaseLogicImpl<Template>  implements TemplateLogicInterface{

    private TemplateDaoInterface templateDaoInterface;
    private CspLogicInterface cspLogicInterface;

	     /**
	 * @param templateDaoInterface the templateDaoInterface to set
	 */
    @Autowired
	public void setTemplateDaoInterface(TemplateDaoInterface templateDaoInterface) {
		this.templateDaoInterface = templateDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.templateDaoInterface;
	}
    @Autowired
    public void setCspLogicInterface(CspLogicInterface cspLogicInterface) {
        this.cspLogicInterface = cspLogicInterface;
    }

    public String getCspAliasByCspId(long cspId) {
        Csp csp = this.cspLogicInterface.getCspByCspId(cspId);
        if(csp!=null){
            String alias = csp.getAlias();
            if(alias==null||"".equals(alias.trim())){
                alias = HzUtils.getFullSpell(csp.getName());
                csp.setAlias(alias);
                cspLogicInterface.save(csp);
            }
            return alias;
        }
        return null;
    }

    public long getPageSizeByChannelId(long channelId) {
        return this.templateDaoInterface.getPageSizeByChannelId(channelId,
                ConfigManager.getInstance().getConfig("system.default.channelPageSize",10));
    }
}





