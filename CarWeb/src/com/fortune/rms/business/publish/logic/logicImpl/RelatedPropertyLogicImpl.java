package com.fortune.rms.business.publish.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.publish.dao.daoInterface.RelatedPropertyDaoInterface;
import com.fortune.rms.business.publish.logic.logicInterface.RelatedPropertyLogicInterface;
import com.fortune.rms.business.publish.model.RelatedProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("relatedPropertyLogicInterface")
public class RelatedPropertyLogicImpl extends BaseLogicImpl<RelatedProperty>
		implements
			RelatedPropertyLogicInterface {
	private RelatedPropertyDaoInterface relatedPropertyDaoInterface;

	/**
	 * @param relatedPropertyDaoInterface the relatedPropertyDaoInterface to set
	 */
    @Autowired
	public void setRelatedPropertyDaoInterface(
			RelatedPropertyDaoInterface relatedPropertyDaoInterface) {
		this.relatedPropertyDaoInterface = relatedPropertyDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.relatedPropertyDaoInterface;
	}

}
