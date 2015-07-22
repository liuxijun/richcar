package com.fortune.rms.business.publish.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.publish.dao.daoInterface.RelatedDaoInterface;
import com.fortune.rms.business.publish.logic.logicInterface.RelatedLogicInterface;
import com.fortune.rms.business.publish.model.Related;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("relatedLogicInterface")
public class RelatedLogicImpl extends BaseLogicImpl<Related>
		implements
			RelatedLogicInterface {
	private RelatedDaoInterface relatedDaoInterface;

	/**
	 * @param relatedDaoInterface the relatedDaoInterface to set
	 */
    @Autowired
	public void setRelatedDaoInterface(RelatedDaoInterface relatedDaoInterface) {
		this.relatedDaoInterface = relatedDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.relatedDaoInterface;
	}

}
