package com.fortune.rms.business.template.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.template.model.Template;

import java.util.List;

public interface TemplateDaoInterface extends BaseDaoInterface<Template, Long> {
   public Long getPageSizeByChannelId(long channelId, int defaultSize);
}