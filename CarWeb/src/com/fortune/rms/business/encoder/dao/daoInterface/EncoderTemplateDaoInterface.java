package com.fortune.rms.business.encoder.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.encoder.model.EncoderTemplate;
import com.fortune.rms.business.module.model.Property;
import com.fortune.util.PageBean;

import java.util.List;

public interface EncoderTemplateDaoInterface extends BaseDaoInterface<EncoderTemplate, Long> {
    public List<EncoderTemplate> getEncoderTemplates(EncoderTemplate e, PageBean pageBean);
    public List<EncoderTemplate> getTemplatesOfModule(Long moduleId);
}
