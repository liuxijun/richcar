package com.fortune.util;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-10-16
 * Time: 上午10:10
 * 输出每一行就回调一次
 */
public interface CommandRunnerOutlineCallbackable {
    public int processLine(String line);
}
