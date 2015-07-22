package com.fortune.rms.web.template;

import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.template.logic.logicInterface.CspTemplateLogicInterface;
import com.fortune.rms.business.template.model.CspTemplate;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/template")
@ParentPackage("default")
@Action(value="cspTemplate")
public class CspTemplateAction extends BaseAction<CspTemplate> {
	private static final long serialVersionUID = 3243534534534534l;
	private CspTemplateLogicInterface cspTemplateLogicInterface;
	@SuppressWarnings("unchecked")
	public CspTemplateAction() {
		super(CspTemplate.class);
	}
	/**
	 * @param cspTemplateLogicInterface the cspTemplateLogicInterface to set
	 */
    @Autowired
	public void setCspTemplateLogicInterface(CspTemplateLogicInterface cspTemplateLogicInterface) {
		this.cspTemplateLogicInterface = cspTemplateLogicInterface;
		setBaseLogicInterface(cspTemplateLogicInterface);
	}


    
    }