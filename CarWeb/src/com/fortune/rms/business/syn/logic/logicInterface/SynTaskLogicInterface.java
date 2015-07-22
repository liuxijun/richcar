package com.fortune.rms.business.syn.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.syn.model.SynTask;
import com.fortune.util.PageBean;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: liupeng
 * Date: 2011-6-20
 * Time: 10:35:44
 *
 */
public interface SynTaskLogicInterface extends BaseLogicInterface<SynTask> {
    public static final Long STATUS_RUNNING=1L;
    public static final Long STATUS_WAITING=2L;
    public static final Long STATUS_FINISHED=3L;
    public static final Long STATUS_ERROR=4L;
    public static final Long STATUS_FILE_NOT_EXISTS=805L;
    public static final Long STATUS_IO_ERROR=509L;
    public static final Long STATUS_CANCELED=806L;
    /**
     {name:"正在分发",value:"1"},
     {name:"队列中",value:"2"},
     {name:"执行完毕",value:"3"},
     {name:"执行错误",value:"4"},
     {name:"文件不存在",value:"805"},
     {name:"发生IO异常",value:"509"},
     {name:"影片源丢失",value:"801"},
     {name:"重复的任务",value:"802"},
     {name:"发生错误",value:"804"},
     {name:"被取消",value:"806"}     */

    public void addSynTask(long synFileId, long synLevel);
    public void pushCurrentTask(long synFileId)  throws Exception;
    public void updateSynTaskStatus(long synTaskId, long synStatus);
    public void updateSynTaskProcess(long synTaskId, long nStartPos, long nEndPos);
    public void downFile(String fileUrl, String md5, long synId, String masterIp);
    public void delFile(String fileUrl, long synId);
    public List<SynTask> searchSynTask();
    public List<SynTask> searchMasterSynTask();
    public List<SynTask> searchSlaveSynTask(String deviceId);
    public void pushSynTask() throws Exception;
    public void pushSynTask(List<SynTask> synTasks) throws Exception;
    public List<SynTask> searchSynTaskByPage(SynTask synTask, PageBean pageBean, long spId);
    public void reSync(long synTaskId)  throws Exception;
}
