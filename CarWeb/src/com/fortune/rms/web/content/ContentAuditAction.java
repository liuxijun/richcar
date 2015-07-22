package com.fortune.rms.web.content;

import com.fortune.rms.business.content.logic.logicInterface.ContentAuditLogicInterface;
import com.fortune.rms.business.content.model.ContentAudit;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/content")
@ParentPackage("default")
@Action(value = "contentAudit")
public class ContentAuditAction extends BaseAction<ContentAudit> {
    private static final long serialVersionUID = 3243534534534534l;
    private ContentAuditLogicInterface contentAuditLogicInterface;

    @SuppressWarnings("unchecked")
    public ContentAuditAction() {
        super(ContentAudit.class);
    }

    /**
     * @param contentAuditLogicInterface the contentAuditLogicInterface to set
     */
    @Autowired
    public void setContentAuditLogicInterface(
            ContentAuditLogicInterface contentAuditLogicInterface) {
        this.contentAuditLogicInterface = contentAuditLogicInterface;
        setBaseLogicInterface(contentAuditLogicInterface);
    }
}
