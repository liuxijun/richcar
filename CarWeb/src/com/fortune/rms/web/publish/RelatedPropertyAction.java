package com.fortune.rms.web.publish;

import com.fortune.rms.business.publish.logic.logicInterface.RelatedPropertyLogicInterface;
import com.fortune.rms.business.publish.model.RelatedProperty;
import com.fortune.common.web.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/publish")
@ParentPackage("default")
@Action(value="relatedProperty")
public class RelatedPropertyAction extends BaseAction<RelatedProperty> {
	private static final long serialVersionUID = 3243534534534534l;
	private RelatedPropertyLogicInterface relatedPropertyLogicInterface;
	@SuppressWarnings("unchecked")
	public RelatedPropertyAction() {
		super(RelatedProperty.class);
	}
	/**
	 * @param relatedPropertyLogicInterface the relatedPropertyLogicInterface to set
	 */
    @Autowired
	public void setRelatedPropertyLogicInterface(
			RelatedPropertyLogicInterface relatedPropertyLogicInterface) {
		this.relatedPropertyLogicInterface = relatedPropertyLogicInterface;
		setBaseLogicInterface(relatedPropertyLogicInterface);
	}
}
