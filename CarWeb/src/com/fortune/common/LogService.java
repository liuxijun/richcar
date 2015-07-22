package com.fortune.common;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-4-5
 * Time: 18:34:10
 * 系统日志处理
 */
public interface LogService {
    public void log(String object, String type, String content, String username, String orgName, String clientIp) ;
}
