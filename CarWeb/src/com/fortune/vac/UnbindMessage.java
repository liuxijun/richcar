package com.fortune.vac;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-9-23
 * Time: ����1:19
 * ������Ϣ
 */
public class UnbindMessage extends BaseMessage implements MessageBody {
    public byte[] buildBuffer(){
        return new byte[0];
    }

    public int getCommandId(){
        return Command.CMDID_UnBind;
    }
}
