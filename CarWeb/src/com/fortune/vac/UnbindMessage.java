package com.fortune.vac;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-9-23
 * Time: 下午1:19
 * 握手消息
 */
public class UnbindMessage extends BaseMessage implements MessageBody {
    public byte[] buildBuffer(){
        return new byte[0];
    }

    public int getCommandId(){
        return Command.CMDID_UnBind;
    }
}
