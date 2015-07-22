package com.fortune.rms.business.content.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.content.model.ContentAudit;

public interface ContentAuditLogicInterface
		extends
			BaseLogicInterface<ContentAudit> {
    public static final long AUDIT_TYPE_CP_ONLINE=2;
    public static final long AUDIT_TYPE_CP_OFFLINE=1;
    public static final long AUDIT_TYPE_SP_ONLINE=4;
    public static final long AUDIT_TYPE_SP_OFFLINE=3;
    public static final long AUDIT_RESULT_WAITING=0;

}
