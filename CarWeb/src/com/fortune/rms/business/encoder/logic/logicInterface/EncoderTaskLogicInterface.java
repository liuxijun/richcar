package com.fortune.rms.business.encoder.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.rms.business.encoder.model.EncoderTask;
import com.fortune.rms.business.encoder.model.EncoderTemplate;
import com.fortune.util.PageBean;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-7-28
 * Time: …œŒÁ11:46
 * ±‡¬Î»ŒŒÒ
 */
public interface EncoderTaskLogicInterface  extends BaseLogicInterface<EncoderTask> {
    public static final Integer STATUS_START_ID=800;
    public static final Integer STATUS_RUNNING=1;
    public static final Integer STATUS_WAITING=2;
    public static final Integer STATUS_FINISHED=3;
    public static final Integer STATUS_ERROR=STATUS_START_ID+4;
    public static final Integer STATUS_NOT_EXISTS=STATUS_START_ID+5;
    public static final Integer STATUS_CANCEL=STATUS_START_ID+6;
    public static final Integer STATUS_DUMP_TASK=802;
    public static final Integer STATUS_CLIP_LOST=801;
    public EncoderTask startTask(Content content, ContentProperty clip, long encoderId, long streamServerId, long templateId);
    public void taskFinished(EncoderTask task);
    public String updateLog(EncoderTask task, String logInfo);
    public String updateLog(EncoderTask task, String logInfo, boolean updateStatus);
    public boolean startEncoder(String sourceFile, Long encodeTemplateId);
    public EncoderTask createTaskForTemplate(Long clipId, Long templateId);
    public EncoderTask createTaskForTemplate(Long taskId, Long clipId, Long templateId);
    public EncoderTask createTaskForTemplate(Content content, ContentProperty contentProperty, EncoderTask task, EncoderTemplate template);
    public List<EncoderTask> createEncoderTasksForAllTemplate(Content content, ContentProperty contentProperty);
    public List searchTask(String taskName, String sourceFileName, String contentName, Long encoderId, Long templateId,
                           Long cspId, Integer status, Date beginDate, Date endDate,
                           PageBean pageBean);
    public List<EncoderTask> checkTasksOfClip(Long clipId);
    public List<EncoderTask> getWaitingTasks(Long encoderId, Long templateId, PageBean pageBean);
    public List<EncoderTask> getWaitingTasks(Long encoderId, Long streamServerId, Long templateId, PageBean pageBean);
    public List<EncoderTask> getRunningTasks(Long encoderId, Long templateId);
    public List<EncoderTask> getTasks(Long encoderId, Long streamServerId, Long templateId, Integer status, PageBean pageBean);
    public List<EncoderTask> getTasks(Long encoderId, Long templateId, Integer status, PageBean pageBean);
    public boolean checkDumpTask(EncoderTask task);
    public List<EncoderTask> getDumpTasks(EncoderTask task);
    public EncoderTask onTaskStart(EncoderTask task);
    public Map<Integer, Integer> getTaskCount();
    Map<Integer,Integer> getTaskCount(Long encoderId, Long streamServerId, Long cspId);
}
