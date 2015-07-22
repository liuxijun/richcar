package com.fortune.rms.business.system.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.system.model.SystemLog;
import com.fortune.util.PageBean;

import java.util.Date;
import java.util.List;

public interface SystemLogDaoInterface
		extends
			BaseDaoInterface<SystemLog, Long> {
    public List<Object[]> getSystemLogAll(SystemLog systemLog, Date startTime, Date stopTime, PageBean pageBean);
}