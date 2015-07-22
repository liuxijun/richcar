package com.fortune.vac;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-9-23
 * Time: 上午11:45
 * 批假确认
 */
public class CheckPriceConfirmMessage extends BaseMessage implements MessageBody{
    private String sequenceNumber;//20
    private long errCode;//Integer4	4	：错误代码
                            //0：成功
                            //非0：失败，错误码见附录
    private String endTime;//	Octet String	14	业务结束时间：
                             //YYYYMMDDHHMMSS
                             //年月日时分秒
    private String serviceType;//	Octet String	2	业务类型，见VAC与BSS接口规范附录A 7
    public void init(){
        super.init();
        tags.add(new Tag("sequenceNumber",20,TAG_TYPE_STRING,true));
        tags.add(new Tag("errCode",4,TAG_TYPE_LONG,true));
        tags.add(new Tag("endTime",14,TAG_TYPE_STRING,true));
        tags.add(new Tag("serviceType",2,TAG_TYPE_STRING,true));
    }

    public CheckPriceConfirmMessage(){
        super();
        init();
    }

    public CheckPriceConfirmMessage(byte[] buffer){
        super();
        init();
        bufferPosNow = 12;
        setBuffers(buffer);
        readBuffer(buffer);
    }



    public int getCommandId(){
        return Command.CMDID_CheckPriceConfirm;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public long getErrCode() {
        return errCode;
    }

    public void setErrCode(long errCode) {
        this.errCode = errCode;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
