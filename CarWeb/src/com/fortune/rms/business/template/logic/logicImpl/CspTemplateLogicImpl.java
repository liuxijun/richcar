package com.fortune.rms.business.template.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.csp.logic.logicInterface.CspLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.template.dao.daoInterface.CspTemplateDaoInterface;
import com.fortune.rms.business.template.logic.logicInterface.CspTemplateLogicInterface;
import com.fortune.rms.business.template.model.CspTemplate;
import com.fortune.rms.business.template.model.Template;
import com.fortune.util.CacheUtils;
import com.fortune.util.DataInitWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("cspTemplateLogicInterface")
public class CspTemplateLogicImpl extends BaseLogicImpl<CspTemplate>
		implements
        CspTemplateLogicInterface {
    private CspLogicInterface cspLogicInterface;
    private CspTemplateDaoInterface cspTemplateDaoInterface;

	     /**
	 * @param cspTemplateDaoInterface the cspTemplateDaoInterface to set
	 */
    @Autowired
	public void setCspTemplateDaoInterface(CspTemplateDaoInterface cspTemplateDaoInterface) {
		this.cspTemplateDaoInterface = cspTemplateDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.cspTemplateDaoInterface;
	}
    @Autowired
    public void setCspLogicInterface(CspLogicInterface cspLogicInterface) {
        this.cspLogicInterface = cspLogicInterface;
    }

    public Template getCspTemplate(Long cspId){
        return cspTemplateDaoInterface.getCspTemplate(cspId);
    }

    public String getCspIndexUrl(Long cspId){
        Template t = getCachedCspTemplate(cspId);
        if(t!=null){
            return t.getFileName();
        }
        return null;
    }
    
    public Template getCachedCspTemplate(Long cspId) {
        return (Template) CacheUtils.get(cspId,"cspTemplatesCache",
                new DataInitWorker(){
                    public Object init(Object cspIdKey,String cacheName){
                        Long cspId = (Long) cspIdKey;
                        Template result=getCspTemplate(cspId);
                        Csp csp = cspLogicInterface.get(cspId);
                        if(csp!=null){
                            result.setFileName("/"+csp.getAlias()+"/"+result.getFileName());
                        }
                        return result;
                    }
                });
    }

}