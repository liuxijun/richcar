package com.fortune.rms.web.content;

import com.fortune.rms.business.content.logic.logicInterface.ContentServiceProductLogicInterface;
import com.fortune.rms.business.content.model.ContentServiceProduct;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/content")
@ParentPackage("default")
@Action(value = "contentServiceProduct")
public class ContentServiceProductAction
        extends
        BaseAction<ContentServiceProduct> {
    private static final long serialVersionUID = 3243534534534534l;
    private ContentServiceProductLogicInterface contentServiceProductLogicInterface;

    @SuppressWarnings("unchecked")
    public ContentServiceProductAction() {
        super(ContentServiceProduct.class);
    }

    /**
     * @param contentServiceProductLogicInterface
     *         the contentServiceProductLogicInterface to set
     */
    @Autowired
    public void setContentServiceProductLogicInterface(
            ContentServiceProductLogicInterface contentServiceProductLogicInterface) {
        this.contentServiceProductLogicInterface = contentServiceProductLogicInterface;
        setBaseLogicInterface(contentServiceProductLogicInterface);
    }
}
