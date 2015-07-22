package com.fortune.rms.web.publish;

import com.fortune.rms.business.publish.logic.logicInterface.RelatedPropertyContentLogicInterface;
import com.fortune.rms.business.publish.model.RelatedPropertyContent;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/publish")
@ParentPackage("default")
@Action(value="relatedPropertyContent")
public class RelatedPropertyContentAction
		extends
			BaseAction<RelatedPropertyContent> {
	private static final long serialVersionUID = 3243534534534534l;
	private RelatedPropertyContentLogicInterface relatedPropertyContentLogicInterface;
	@SuppressWarnings("unchecked")
	public RelatedPropertyContentAction() {
		super(RelatedPropertyContent.class);
	}
	/**
	 * @param relatedPropertyContentLogicInterface the relatedPropertyContentLogicInterface to set
	 */
    @Autowired
	public void setRelatedPropertyContentLogicInterface(
			RelatedPropertyContentLogicInterface relatedPropertyContentLogicInterface) {
		this.relatedPropertyContentLogicInterface = relatedPropertyContentLogicInterface;
		setBaseLogicInterface(relatedPropertyContentLogicInterface);
	}
}
