package com.fortune.rms.business.encoder.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.encoder.dao.daoInterface.EncoderTemplateDaoInterface;
import com.fortune.rms.business.encoder.model.EncoderTemplate;
import com.fortune.rms.business.module.model.Property;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class EncoderTemplateDaoAccess extends BaseDaoAccess<EncoderTemplate,Long> implements EncoderTemplateDaoInterface{

    public EncoderTemplateDaoAccess() {
        super(EncoderTemplate.class);
    }

    public List<EncoderTemplate> getEncoderTemplates(EncoderTemplate e,PageBean pageBean) {
        try {
            List<EncoderTemplate> list = new ArrayList<EncoderTemplate>();
            List<Object> parameters = new ArrayList<Object>();

            String hql = "from EncoderTemplate e ";
            if(e.getTemplateName() != null && !e.getTemplateName().trim().isEmpty()) {
                hql += " where e.templateName like ?";
                parameters.add("%" + e.getTemplateName() + "%");
            }

            list = getObjects(hql, parameters.toArray(), pageBean);

            return list;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<EncoderTemplate> getTemplatesOfModule(Long moduleId) {
        String hql = "from EncoderTemplate et where et.propertyId in (select propertyId from ModuleProperty mp where mp.moduleId=" +
                moduleId+")";
        return this.getHibernateTemplate().find(hql);
    }


}
