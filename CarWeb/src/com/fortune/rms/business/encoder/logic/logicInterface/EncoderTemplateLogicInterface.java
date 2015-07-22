package com.fortune.rms.business.encoder.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.encoder.model.EncoderTemplate;
import com.fortune.rms.business.module.model.Property;
import com.fortune.util.PageBean;

import java.util.List;

public interface EncoderTemplateLogicInterface extends BaseLogicInterface<EncoderTemplate> {
    public List<EncoderTemplate> getEncoderTemplates(EncoderTemplate e, PageBean pageBean);
    public EncoderTemplate getEncoderTemplateOfProperty(Long propertyId);
    public List<EncoderTemplate> getTemplatesOfModule(Long moduleId);
}
