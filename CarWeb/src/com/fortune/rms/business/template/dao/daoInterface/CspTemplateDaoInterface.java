package com.fortune.rms.business.template.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.template.model.CspTemplate;
import com.fortune.rms.business.template.model.Template;
import com.fortune.util.PageBean;

import java.util.List;

public interface CspTemplateDaoInterface extends BaseDaoInterface<CspTemplate, Long> {
     public Template getCspTemplate(Long cspId);   
}