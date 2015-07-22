package com.fortune.vac;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-9-22
 * Time: ����11:19
 * ���ۼ�ȨӦ��
 */
public class CheckPriceResp   extends BaseMessage implements MessageBody{
    private long resultCode;//	Integer4	4	����롣0 : OK,
                                //����Ϊ������룬����¼
    private long confirmInterval;//	Integer4	4	�ȴ�CheckPriceConfirm ��Ϣ��ʱ�������������ʧ�ܻ��߸����ǲ���ص�ȱʡֵ��Ϊ����1��
    private int needConfirm;//	Interger1	1	ȷ�ϱ�ʶ�������Ҫȷ�ϣ���Ҫ���ŷ�һ��confirm��Ϣ����������SequenceNumber�ͱ���Ϣ��һ��
                               // 0������ȷ��
                               // 1����Ҫȷ��
                               // -1���������ʧ��ȱʡֵ��Ϊ��-1��
    private String originalAdr;//Octet String	36	ԭʼ��ַ����Ҫ����α��ת��ʱ����ԭ�����û����롣
                                  //  MT�����ؽ��շ��û�ԭ����
                                  //  MO�����ط��ͷ��û�α�롣
                                  //  α��ת�����Կ����á�
    //��ѡ����
    private Tlv linkId;//	TLV	20	�������ID���㲥ҵ��������������AAA��������ʽ���£�8λ�����������
                         //Ϊ��(������0)��ʾ��Ч
    private Tlv feeType;//	TLV	2	�Ʒ����ͣ����ֶβ���Ϊ�ա�
                          //  0�����Ʒѣ������ں˼�SP�ԳƵ��ŵ���
                          //  1�����
                          //  2������/�μƷ�
                          //  3����������ȡ
                          //  4���ⶥ�Ʒ�
                          //  5���������Ʒ�
                          //  6����ʱ���Ʒ�
                          //  7�������¼Ʒ�
                          //  8�����¼Ʒ�+��λ���ʣ���ֵ���������ӻ����мƷ������ֶ�ȡֵ���԰��¼Ʒ�+��λ�����ʷ����Ʒ�еİ��¼Ʒѻ���������3���Ե�λ�Ʒѻ���������2��
                          //  �ԻỰ��Ʒѣ�5��6���뷵��
    private Tlv redirect;//	Tlv	4	���ڸ�֪ҵ�����������δ����û����������¼������ͣ�
                            //1.	�趨��
                            //2.	��ȷ��
                            //3.	����ҳ�棨�ο�����ֵ��
                            //4.	���õ���
                            //5.	�ʷ��޸���ʾ
    private Tlv returnMessage;//	Tlv	256	������Ӧ����������Ϣ��ҵ��ϵͳ��
    private Tlv needToNextNode;//	TLV	1	VAC�����Ƿ���Ҫ��ԭ��Ϣת����һ����ڵ㣺
                                  //  0������Ҫ
                                  //  1����Ҫ
    private Tlv SP_ProductId;//	Tlv	12	��дSP_ProductID
    private Tlv SPEC_ProductId;//	Tlv	12	CRM���ɲ�Ʒ��ʶ
    private Tlv productId;//	Tlv	8	CRM��ƷID

    public void init(){
        tags.clear();
        tags.add(new Tag("resultCode",4,TAG_TYPE_LONG,true));
        tags.add(new Tag("confirmInterval",4,TAG_TYPE_LONG,true));
        tags.add(new Tag("needConfirm",1,TAG_TYPE_INT,true));
        tags.add(new Tag("originalAdr",36,TAG_TYPE_STRING,true));
        tags.add(new Tag("linkId",20,TAG_TYPE_TLV,false));
        tags.add(new Tag("feeType",2,TAG_TYPE_TLV,false));
        tags.add(new Tag("redirect",4,TAG_TYPE_TLV,false));
        tags.add(new Tag("returnMessage",256,TAG_TYPE_TLV,false));
        tags.add(new Tag("needToNextNode",1,TAG_TYPE_TLV,false));
        tags.add(new Tag("SP_ProductId",12,TAG_TYPE_TLV,false));
        tags.add(new Tag("SPEC_ProductId",12,TAG_TYPE_TLV,false));
        tags.add(new Tag("productId",8,TAG_TYPE_TLV,false));
    }
    public CheckPriceResp(long resultCode, long confirmInterval, int needConfirm, String originalAdr) {
        this.resultCode = resultCode;
        this.confirmInterval = confirmInterval;
        this.needConfirm = needConfirm;
        this.originalAdr = originalAdr;
        init();
    }

    public void init(byte[] buffer){
        init();
        setBuffers(buffer);
        bufferPosNow = 12;
        resultCode = read_32();
        confirmInterval = read_32();
        needConfirm = read_8();
        originalAdr = readString(36);
        if(bufferPosNow<buffer.length){
            scanTlvs();
        }
    }

    public long getResultCode() {
        return resultCode;
    }

    public void setResultCode(long resultCode) {
        this.resultCode = resultCode;
    }

    public long getConfirmInterval() {
        return confirmInterval;
    }

    public void setConfirmInterval(long confirmInterval) {
        this.confirmInterval = confirmInterval;
    }

    public int getNeedConfirm() {
        return needConfirm;
    }

    public void setNeedConfirm(int needConfirm) {
        this.needConfirm = needConfirm;
    }

    public String getOriginalAdr() {
        return originalAdr;
    }

    public void setOriginalAdr(String originalAdr) {
        this.originalAdr = originalAdr;
    }

    public Tlv getLinkId() {
        return linkId;
    }

    public void setLinkId(Tlv linkId) {
        this.linkId = linkId;
    }

    public Tlv getFeeType() {
        return feeType;
    }

    public void setFeeType(Tlv feeType) {
        this.feeType = feeType;
    }

    public Tlv getRedirect() {
        return redirect;
    }

    public void setRedirect(Tlv redirect) {
        this.redirect = redirect;
    }

    public Tlv getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(Tlv returnMessage) {
        this.returnMessage = returnMessage;
    }

    public Tlv getNeedToNextNode() {
        return needToNextNode;
    }

    public void setNeedToNextNode(Tlv needToNextNode) {
        this.needToNextNode = needToNextNode;
    }

    public Tlv getSP_ProductId() {
        return SP_ProductId;
    }

    public void setSP_ProductId(Tlv SP_ProductId) {
        this.SP_ProductId = SP_ProductId;
    }

    public Tlv getSPEC_ProductId() {
        return SPEC_ProductId;
    }

    public void setSPEC_ProductId(Tlv SPEC_ProductId) {
        this.SPEC_ProductId = SPEC_ProductId;
    }

    public Tlv getProductId() {
        return productId;
    }

    public void setProductId(Tlv productId) {
        this.productId = productId;
    }
    
    public CheckPriceResp(byte[] result){
        init(result);
    }

    public int getCommandId(){
        return Command.CMDID_CheckPriceResp;
    }
}
