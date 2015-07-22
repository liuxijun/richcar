package com.fortune.util;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-7-5
 * Time: 13:01:05
 * 下载进度回调接口
 */
public interface HttpProcessCaller {
    public int callBack(long downloaded, long totalSize);
    public int finished(long downloaded, long totalSize);
    public int error(int errorCode, String message);
    public void beforeStart(long totalSize);
}
