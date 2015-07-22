package com.fortune.rms.web.template;

import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.template.logic.logicInterface.ChannelTemplateLogicInterface;
import com.fortune.rms.business.template.model.ChannelTemplate;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/template")
@ParentPackage("default")
@Action(value="channelTemplate")
public class ChannelTemplateAction extends BaseAction<ChannelTemplate> {
	private static final long serialVersionUID = 3243534534534534l;
	private ChannelTemplateLogicInterface channelTemplateLogicInterface;
	@SuppressWarnings("unchecked")
	public ChannelTemplateAction() {
		super(ChannelTemplate.class);
	}

    /**
	 * @param channelTemplateLogicInterface the channelTemplateLogicInterface to set
	 */
    @Autowired
    public void setChannelTemplateLogicInterface(ChannelTemplateLogicInterface channelTemplateLogicInterface) {
        this.channelTemplateLogicInterface = channelTemplateLogicInterface;
        this.setBaseLogicInterface(channelTemplateLogicInterface);
    }

    public String view(){
        Long channelId = com.fortune.util.StringUtils.string2long(keyId.toString(),-1);
        try {
            obj = channelTemplateLogicInterface.get(channelId);
        } catch (Exception e) {
            obj = new ChannelTemplate();
            obj.setChannelId(channelId);
            obj.setCreateor(admin.getLogin());
            log.warn("没有这个频道的模版，只返回缺省值："+keyId);
        }
        return "view";
    }

}