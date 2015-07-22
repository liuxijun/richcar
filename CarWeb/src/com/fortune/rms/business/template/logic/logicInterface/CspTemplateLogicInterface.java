package com.fortune.rms.business.template.logic.logicInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.template.model.CspTemplate;
import com.fortune.rms.business.template.model.Template;
import com.fortune.util.PageBean;

import java.util.List;

public interface CspTemplateLogicInterface extends BaseLogicInterface<CspTemplate> {
    public Template getCspTemplate(Long cspId);
    public Template getCachedCspTemplate(Long cspId);
    public String getCspIndexUrl(Long cspId);
}