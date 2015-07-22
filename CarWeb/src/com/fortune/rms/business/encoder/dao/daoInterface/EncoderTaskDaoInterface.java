package com.fortune.rms.business.encoder.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.encoder.model.EncoderTask;
import com.fortune.util.PageBean;

import java.util.Date;
import java.util.List;

public interface EncoderTaskDaoInterface extends BaseDaoInterface<EncoderTask, Long> {
    public List searchTask(String taskName, String sourceFileName, String contentName, Long encoderId, Long templateId,
                           Long cspId, Integer status, Date beginDate, Date endDate, PageBean pageBean);
    public String updateLog(EncoderTask task, boolean updateStatus);
    public List<Object[]> getTaskCount(Long encoderId, Long streamServerId, Long cspId);
    int getUnFinishedTaskCountOfContent(long id);
    public int getTaskCountOfContentWithStatusCondition(long contentId, int taskStatus, boolean exclude);
    public int getTaskCountOfContentExcludeStatus(long contentId, int excludeTaskStatus);
    int getTaskCountOfContent(long contentId, int taskStatus);
}
