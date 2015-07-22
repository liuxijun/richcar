package com.fortune.vac;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-8-28
 * Time: 下午2:40
 * 消息体
 */
public interface MessageBody {
    public int getCommandId();
    public byte[] buildBuffer();
}
