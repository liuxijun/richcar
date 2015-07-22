package com.fortune.rms.business.template.logic.logicInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.template.model.Template;

import java.util.List;

public interface TemplateLogicInterface extends BaseLogicInterface<Template> {
    public String getCspAliasByCspId(long cspId);
    public long getPageSizeByChannelId(long channelId);

}