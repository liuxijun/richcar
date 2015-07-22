package com.fortune.util;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-5-24
 * Time: ����10:24
 * ������ִ�е�Runnable�ӿ�
 */
public interface BatchRunnable extends Runnable {
    public String getLogs();
    public int getResultCode();
    public long getDuration();
    public void afterFinished();
    public void beforeStart();
    public void shutdownNow();
}
