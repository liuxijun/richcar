package com.fortune.rms.business.template.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.template.dao.daoInterface.CspTemplateDaoInterface;
import com.fortune.rms.business.template.model.CspTemplate;
import com.fortune.rms.business.template.model.Template;
import com.fortune.util.PageBean;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class CspTemplateDaoAccess extends BaseDaoAccess<CspTemplate, Long>
        implements
        CspTemplateDaoInterface {

    public CspTemplateDaoAccess() {
        super(CspTemplate.class);
    }


    public Template getCspTemplate(Long cspId) {
        String hqlStr = "from Template t where t.id in(select ct.templateId from " +
                "CspTemplate ct where ct.cspId="+cspId+")";
        List results = this.getHibernateTemplate().find(hqlStr);
        if(results!=null&&results.size()>0){
            return (Template) results.get(0);
        }
        return null;
    }
}