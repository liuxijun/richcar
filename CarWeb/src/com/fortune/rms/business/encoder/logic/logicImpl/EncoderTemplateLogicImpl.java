package com.fortune.rms.business.encoder.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.encoder.dao.daoInterface.EncoderTemplateDaoInterface;
import com.fortune.rms.business.encoder.logic.logicInterface.EncoderTemplateLogicInterface;
import com.fortune.rms.business.encoder.model.EncoderTemplate;
import com.fortune.rms.business.module.model.Property;
import com.fortune.util.CacheUtils;
import com.fortune.util.DataInitWorker;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("encoderTemplateLogicInterface")
public class EncoderTemplateLogicImpl extends BaseLogicImpl<EncoderTemplate> implements EncoderTemplateLogicInterface{
    private EncoderTemplateDaoInterface encoderTemplateDaoInterface;

    @Autowired
    public void setEncoderTemplateDaoInterface(EncoderTemplateDaoInterface encoderTemplateDaoInterface) {
        this.encoderTemplateDaoInterface = encoderTemplateDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.encoderTemplateDaoInterface;
    }


    public List<EncoderTemplate> getEncoderTemplates(EncoderTemplate e,PageBean pageBean) {
          return encoderTemplateDaoInterface.getEncoderTemplates(e,pageBean);
    }

    public EncoderTemplate getEncoderTemplateOfProperty(Long propertyId) {
        return (EncoderTemplate) CacheUtils.get(propertyId, "propertyEncodeTemplate",new DataInitWorker(){
            public Object init(Object keyId,String cacheName){
                EncoderTemplate bean = new EncoderTemplate();
                long id=StringUtils.string2long(keyId.toString(),0);
                if(id>0){
                    bean.setPropertyId(id);
                    List<EncoderTemplate> results = search(bean);
                    if(results!=null&&results.size()>0){
                        return results.get(0);
                    }
                }
                return null;
            }
        });
    }

    public List<EncoderTemplate> getTemplatesOfModule(Long moduleId) {
        return encoderTemplateDaoInterface.getTemplatesOfModule(moduleId);
    }

}
