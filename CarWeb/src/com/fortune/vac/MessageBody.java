package com.fortune.vac;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-8-28
 * Time: ����2:40
 * ��Ϣ��
 */
public interface MessageBody {
    public int getCommandId();
    public byte[] buildBuffer();
}
