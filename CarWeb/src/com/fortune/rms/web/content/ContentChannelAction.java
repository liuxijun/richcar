package com.fortune.rms.web.content;

import com.fortune.rms.business.content.logic.logicInterface.ContentChannelLogicInterface;
import com.fortune.rms.business.content.model.ContentChannel;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/content")
@ParentPackage("default")
@Action(value = "contentChannel")
public class ContentChannelAction extends BaseAction<ContentChannel> {
    private static final long serialVersionUID = 3243534534534534l;
    private ContentChannelLogicInterface contentChannelLogicInterface;

    @SuppressWarnings("unchecked")
    public ContentChannelAction() {
        super(ContentChannel.class);
    }

    /**
     * @param contentChannelLogicInterface the contentChannelLogicInterface to set
     */
    @Autowired
    public void setContentChannelLogicInterface(
            ContentChannelLogicInterface contentChannelLogicInterface) {
        this.contentChannelLogicInterface = contentChannelLogicInterface;
        setBaseLogicInterface(contentChannelLogicInterface);
    }
}
