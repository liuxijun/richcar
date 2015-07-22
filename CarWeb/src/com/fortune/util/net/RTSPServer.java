package com.fortune.util.net;

import com.fortune.util.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-2-20
 * Time: 10:28:16
 * RTSP·þÎñÆ÷¶Ë
 */
public class RTSPServer extends TcpServer{
    public static final String cmdDescribe="DESCRIBE";
    public static final String cmdOption="OPTION";
    public static final String cmdTeardown="TEARDOWN";
    public static final String cmdSetup="SETUP";
    public static final String cmdPlay="PLAY";
    public static final String cmdPause="PAUSE";

    private RTSPServerEvent event;
    private int cseq = 1;

    public RTSPServer(String bindAddress, int port, int threadPoolNumber,RTSPServerEvent event) {
        super(bindAddress,port,threadPoolNumber);
        this.event = event;
    }

    public RTSPServer(String bindAddress, int port,RTSPServerEvent event) {
        this(bindAddress,port,-1,event);
    }

    public RTSPServer(int port,RTSPServerEvent event) {
        this(null,port,-1,event);
    }

    public void execute(BaseProtocolContext context){
        context.setResponseHeader("Server","FortuneVS/1.4.8(Platform/JAVA)");
        cseq = StringUtils.string2int(context.getHeader("CSEQ"),1);
        context.setResponseHeader("CSeq",""+cseq);
/*
        context.setResponseHeader("","");
        context.setResponseHeader("","");
*/
        String command = context.getCommand();
        if(command!=null){
            command = command.toUpperCase();
            if(cmdDescribe.equals(command)){
               event.onDescribe(context);
            }else if(cmdOption.equals(command)){
               event.onOption(context);
            }else if(cmdTeardown.equals(command)){
               event.onTeardown(context);
            }else if(cmdSetup.equals(command)){
               event.onSetup(context);
            }else if(cmdPlay.equals(command)){
               event.onPlay(context);
            }else if(cmdPause.equals(command)){
               event.onPause(context);
            }
        }
        if(!context.isResponseWritten()){
            context.setResponseHeaders(500,0);
        }
    }

    
}
