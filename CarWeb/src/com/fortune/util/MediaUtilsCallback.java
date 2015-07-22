package com.fortune.util;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-7-29
 * Time: ионГ10:06
 *
 */
public interface MediaUtilsCallback {
    public void onProcess(int process, int pos, int size, int cpu, int memory);
    public void onSetLength(int length, Date fileDate, long fileSize);
    public void onLog(String log);

}
