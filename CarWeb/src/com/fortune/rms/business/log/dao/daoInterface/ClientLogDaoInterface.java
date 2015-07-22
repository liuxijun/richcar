package com.fortune.rms.business.log.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.log.model.ClientLog;

import java.util.List;

public interface ClientLogDaoInterface extends BaseDaoInterface<ClientLog,Long> {
    public List<ClientLog> checkPhoneCode(String phoneCode);
}
