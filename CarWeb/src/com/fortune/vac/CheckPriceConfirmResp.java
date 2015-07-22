package com.fortune.vac;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-9-23
 * Time: 上午11:51
 * 确认消息反馈
 */
public class CheckPriceConfirmResp extends BaseMessage implements MessageBody {
    private long resultCode;
    public void init(byte[] buffer){
        setBuffers(buffer);
        bufferPosNow = 0;
        resultCode = read_32();
    }

    public CheckPriceConfirmResp(int resultCode){
        super();
        this.resultCode = resultCode;
    }

    public CheckPriceConfirmResp(byte[] buffer) {
        init(buffer);
    }

    public long getResultCode() {
        return resultCode;
    }

    public void setResultCode(long resultCode) {
        this.resultCode = resultCode;
    }

    public int getCommandId(){
        return Command.CMDID_CheckPriceConfirmResp;
    }
}
