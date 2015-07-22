package com.fortune.rms.business.content.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.content.dao.daoInterface.ContentAuditDaoInterface;
import com.fortune.rms.business.content.model.ContentAudit;
import org.springframework.stereotype.Repository;

@Repository
public class ContentAuditDaoAccess extends BaseDaoAccess<ContentAudit, Long>
		implements
			ContentAuditDaoInterface {

	public ContentAuditDaoAccess() {
		super(ContentAudit.class);
	}

}
