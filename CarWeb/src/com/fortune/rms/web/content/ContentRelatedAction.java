package com.fortune.rms.web.content;

import com.fortune.rms.business.content.logic.logicInterface.ContentRelatedLogicInterface;
import com.fortune.rms.business.content.model.ContentRelated;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/content")
@ParentPackage("default")
@Action(value = "contentRelated")
public class ContentRelatedAction extends BaseAction<ContentRelated> {
    private static final long serialVersionUID = 3243534534534534l;
    private ContentRelatedLogicInterface contentRelatedLogicInterface;

    @SuppressWarnings("unchecked")
    public ContentRelatedAction() {
        super(ContentRelated.class);
    }

    /**
     * @param contentRelatedLogicInterface the contentRelatedLogicInterface to set
     */
    @Autowired
    public void setContentRelatedLogicInterface(
            ContentRelatedLogicInterface contentRelatedLogicInterface) {
        this.contentRelatedLogicInterface = contentRelatedLogicInterface;
        setBaseLogicInterface(contentRelatedLogicInterface);
    }
}
