package com.fortune.util.net;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-2-21
 * Time: 9:49:37
 * Server接受的事件
 */
public interface RTSPServerEvent {
    public void onDescribe(BaseProtocolContext context);
    public void onOption(BaseProtocolContext context);
    public void onTeardown(BaseProtocolContext context);
    public void onSetup(BaseProtocolContext context);
    public void onPlay(BaseProtocolContext context);
    public void onPause(BaseProtocolContext context);
}
