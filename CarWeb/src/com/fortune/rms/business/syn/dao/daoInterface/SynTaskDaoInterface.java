package com.fortune.rms.business.syn.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.syn.model.SynTask;
import com.fortune.util.PageBean;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: liupeng
 * Date: 2011-6-20
 * Time: 10:24:36
 * 
 */
public interface SynTaskDaoInterface extends BaseDaoInterface<SynTask, Long> {
    public void updateTaskSynStatus(long synId, long synStatus);

    public void updateTaskSynProcess(long synTaskId, long startPos, long endPos);

    public List<Object[]> searchSynTask();

    public List<Object[]> searchCurrentSynTask(long synFileId);

    public List<Object[]> searchSynTaskByPage(SynTask synTask, PageBean pageBean, long spId);

    public List<Object[]> searchMasterSynTask();

    public List<Object[]> searchSlaveSynTask(String deviceIp);
}
