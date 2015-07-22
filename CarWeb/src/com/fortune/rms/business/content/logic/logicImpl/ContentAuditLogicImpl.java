package com.fortune.rms.business.content.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.content.dao.daoInterface.ContentAuditDaoInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentAuditLogicInterface;
import com.fortune.rms.business.content.model.ContentAudit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("contentAuditLogicInterface")
public class ContentAuditLogicImpl extends BaseLogicImpl<ContentAudit>
		implements
			ContentAuditLogicInterface {
	private ContentAuditDaoInterface contentAuditDaoInterface;

	/**
	 * @param contentAuditDaoInterface the contentAuditDaoInterface to set
	 */
    @Autowired
	public void setContentAuditDaoInterface(
			ContentAuditDaoInterface contentAuditDaoInterface) {
		this.contentAuditDaoInterface = contentAuditDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.contentAuditDaoInterface;
	}

}
