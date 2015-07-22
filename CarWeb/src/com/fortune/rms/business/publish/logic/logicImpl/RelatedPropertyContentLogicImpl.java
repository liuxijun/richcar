package com.fortune.rms.business.publish.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.publish.dao.daoInterface.RelatedPropertyContentDaoInterface;
import com.fortune.rms.business.publish.logic.logicInterface.RelatedPropertyContentLogicInterface;
import com.fortune.rms.business.publish.model.RelatedPropertyContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("relatedPropertyContentLogicInterface")
public class RelatedPropertyContentLogicImpl
		extends
			BaseLogicImpl<RelatedPropertyContent>
		implements
			RelatedPropertyContentLogicInterface {
	private RelatedPropertyContentDaoInterface relatedPropertyContentDaoInterface;

	/**
	 * @param relatedPropertyContentDaoInterface the relatedPropertyContentDaoInterface to set
	 */
    @Autowired
	public void setRelatedPropertyContentDaoInterface(
			RelatedPropertyContentDaoInterface relatedPropertyContentDaoInterface) {
		this.relatedPropertyContentDaoInterface = relatedPropertyContentDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.relatedPropertyContentDaoInterface;
	}

}
