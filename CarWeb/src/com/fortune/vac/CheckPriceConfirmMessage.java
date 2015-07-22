package com.fortune.vac;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-9-23
 * Time: ����11:45
 * ����ȷ��
 */
public class CheckPriceConfirmMessage extends BaseMessage implements MessageBody{
    private String sequenceNumber;//20
    private long errCode;//Integer4	4	���������
                            //0���ɹ�
                            //��0��ʧ�ܣ����������¼
    private String endTime;//	Octet String	14	ҵ�����ʱ�䣺
                             //YYYYMMDDHHMMSS
                             //������ʱ����
    private String serviceType;//	Octet String	2	ҵ�����ͣ���VAC��BSS�ӿڹ淶��¼A 7
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
