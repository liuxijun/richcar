package com.fortune.rms.business.encoder.logic.logicImpl;

import com.fortune.rms.business.encoder.logic.logicInterface.EncoderTaskLogicInterface;
import com.fortune.rms.business.encoder.model.EncoderTask;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.util.AppConfigurator;
import com.fortune.util.PageBean;
import com.fortune.util.SpringUtils;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-7-29
 * Time: 上午9:21
 * 编码任务管理
 */
@SuppressWarnings("unused")
public interface EncodeTaskManagerInterface {
    public void onThreadTerminated(EncodeWorker worker);
    public void onTaskError(EncoderTask task);
    public void onTaskStart(EncoderTask task);
    public void onTaskFinished(EncoderTask task);
    public void onTaskProcess(EncoderTask task);
    public void onSetLength(EncoderTask task, int length, Date fileDate, long fileSize);
    public void startEncodeTask(EncoderTask task);
    public void onWorkerIdle(EncodeWorker worker);
    public void onSystemStartup();
    public void shutdown();
    public String updateLog(EncoderTask task, String logInfo);
}
