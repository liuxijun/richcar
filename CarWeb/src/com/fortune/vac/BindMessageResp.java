package com.fortune.vac;

import com.fortune.util.AppConfigurator;
import com.fortune.util.MD5Utils;
import com.fortune.util.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-8-28
 * Time: 下午2:41
 * 绑定消息
 */

public class BindMessageResp extends BaseMessage implements MessageBody{
    private long resultCode;
    public void init(){
        tags.clear();
        tags.add(new Tag("resultCode",4,TAG_TYPE_LONG,true));
    }
    public BindMessageResp() {
        init();
    }

    public BindMessageResp(long resultCode) {
        init();
        this.resultCode = resultCode;
    }

    public BindMessageResp(byte[] buffers) {
        init();
        setBuffers(buffers);
        bufferPosNow = 12;
        resultCode = read_32();
    }

    public long getResultCode() {
        return resultCode;
    }

    public void setResultCode(long resultCode) {
        this.resultCode = resultCode;
    }

    public int getCommandId(){
        return Command.CMDID_BindResp;
    }
}
