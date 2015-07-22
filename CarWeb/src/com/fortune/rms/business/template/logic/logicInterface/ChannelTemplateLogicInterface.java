package com.fortune.rms.business.template.logic.logicInterface;


import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.template.model.ChannelTemplate;
import com.fortune.rms.business.template.model.Template;
import com.fortune.util.PageBean;

import java.util.List;


public interface ChannelTemplateLogicInterface extends BaseLogicInterface<ChannelTemplate> {
    public static final int TEMPLATE_TYPE_INDEX=1;
    public static final int TEMPLATE_TYPE_LIST=2;
    public static final int TEMPLATE_TYPE_DETAIL=3;
    public static final int TEMPLATE_TYPE_CSP=4;
    public Template getChannelIndexTemplate(Long channelId);
    public Template getChannelListTemplate(Long channelId);
    public Template getChannelDetailTemplate(Long channelId);
    public Template getChannelTemplate(Long channelId, int templateType);
}