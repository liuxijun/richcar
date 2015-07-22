package com.fortune.vac;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-8-28
 * Time: ����3:29
 * ��Ȩ��������
 */
public class CheckPriceMessage  extends BaseMessage implements MessageBody {

    private String sequenceNumber;//20
    private Long OAType;//	Integer4	4	ҵ����˵�ַ���ͣ��ο���¼B
    private String OANetworkID;//	Octet String	10	ҵ������û����������ʶ���磺WCDMA��GSM��
    private String OA;//	Octet String 	36	ҵ����˵�ַ����д�ֻ�����
    private Long DAType;//	Integer4	4	Ŀ���ַ���ͣ��μ���¼B
    private String DANetworkID;//	Octet String	10	Ŀ����û����������ʶ���磺WCDMA��GSM��
    private String DA;//	Octet String 	36	Ŀ���ַ����д�ֻ�����
    private Long FAType;//	Integer4	4	���ѵ�ַ���ͣ���1��������Ϊ�Ժ���Զ����趨���ѷ�ʱʹ�á��ο���¼B
    private String FANetworkID;//	Octet String	10	�����û������������ʶ��������Ϊ�Ժ���Զ����趨���ѷ�ʱʹ�á�
    private String FA;//	Octet String	36	���ѵ�ַ�����ֻ����롣������Ϊ�Ժ���Զ����趨���ѷ�ʱʹ�á�
    private Long serviceIDType;//Integer4	4 	  1��SPID+ServiceID
                                                //    3��SPID+SP_ProductID
                                                //    5: SPID+SPEC_ProductID
    private String spId;//	Octet String	21	��д��ҵ����
    private String serviceId;///FeatureString	Octet String	21	ServiceIDType
/*
            Ϊ1ʱ��дServiceID
    Ϊ3ʱ��дSP_ProductID
    Ϊ5ʱ��дSPEC_ProductID(ҵ�񲿼���֪���û�������㲥CRM�Ĳ�Ʒ����)
*/
    private String productId;//	Octet String	8	CRM��ƷID, ServiceIDTypeΪ5ʱ��Ч��������8���ո�
    private Long serviceUpDownType;//	Integer4	4	ҵ�����������ͣ�
    //                                                      1: MOAT
    //                                                      2: AOMT
    //                                                      3: MOMT �ն˵��ն�
    //                                                      4��P TO E���ն˵�����
    //                                                      9: ����
    //                                                      vac�Բ���p to e��sp���š����š�wap pushҵ����Ҫ�жϾ���ȡֵ��������ҵ�񲻼�Ȩ���ֶ�
    private String beginTime;//	Octet String	14	��ʼʱ��YYYYMMDDHHMMSS
    //                                                  ������ʱ����
    private Integer resentTimes;//	Integer1	1	�ط�������
    //                                             0��ֻ��һ��
    //                                            1-255���ط�����
    //                                                 Ĭ��ȡֵ0
    private Long operationType;//	Integer4	4	0����Ȩ�Ʒ�
    //                                ServiceIDType=1ʱ�������������LinkID�����е㲥��ϵ��Ȩ����LinkID�����ж�����ϵ��Ȩ��
    //                                ��Ȩͨ������мƷѡ�
    //                                        1������ServiceIDType����Ϊ3
    //                                        2���˶� ServiceIDType����Ϊ3
    //                                        3���˶�ServiceType��Ӧ������ҵ���ն˲಻�ܵ����˶�ҵ����⣩��
    //                                                     ServiceIDType����Ϊ3
    //                                        4���㲥��ҵ��ƽ̨���ж��ǵ㲥��ֱ����4
    //                                        6��������ϵ��Ȩ(���ڼ���û��Ƿ񶩹��˴�ҵ��)
    //                                        7������ҵ������
    //                                        8���ָ�ҵ������
    private String serviceType;//	Octet String	2	ҵ�����ͣ���VAC��BSS�ӿڹ淶��¼A 7
    //                                  ��Operation Type=1��2��3��4ʱ��VAC���Լ�Ȩ���𷽵�ҵ���������Ʒ��ҵ�����ͽ��бȽϼ�Ȩ
   // ��ѡ����
    private Tlv linkId;//	TLV	20	�������ID���㲥ҵ��������������AAA��������ʽ���£�8λ�����������
    //                                    Ϊ��(������0)��ʾ��Ч
    private Tlv SMSFormat;//	TLV	4	�������ݸ�ʽ
    //0��ASCII��
    //3������д������
    // 4����������Ϣ
    //8��UCS2����
    //15����GB����
    //����
    private Tlv SMSContentLen;//	TLV	4	�������ݳ���
    private Tlv SMSContent;//	TLV	SMSContentLen	�������ݣ��������й��̶���������Ҫ�����ݷ��͸�ƽ̨������
    private Tlv cpId;//	TLV	8	CP����
    private Tlv contentId;//	TLV	22	���ݴ���
    private Tlv orderMethod;//	TLV	4	����������ȡֵ��VAC��BSS�ӿڹ淶��¼���ڶ���ʱҵ��ϵͳ������д
    private Tlv pushId;//	TLV	20	Ӫ����ʽ,�Ƽ���ID
    private Tlv feeType;//	TLV	2	�Ʒ����ͣ����ֶβ���Ϊ�ա�
    //        0�����Ʒѣ������ں˼�SP�ԳƵ��ŵ��ѣ�
    //        1�����
    //2������/�μƷ�
    //3����������ȡ
    //4���ⶥ�Ʒ�
    //5���������Ʒ�
    //6����ʱ���Ʒ�
    //7�������¼Ʒ�
    private Tlv fee;//	TLV	8	��λ���֡�
    //��ֵϵͳ�����۵Ĳ���fee�ֶΣ�ͬʱVAC��֧�ְ���ҵ�����ͽ��������Ƿ����۴���
    //�Դ��շ�ҵ��fee��FeeType������ȷ��д��FeeTypeֻ����2��3�����籾�ֶ�Ϊ�գ�����3002����
    private Tlv accessNo;//	TLV	21	�û���ͨ���š����ŵȽ���ţ��������ࡢ������ҵ�������д

    public void init(){
        super.init();
        tags.add(new Tag("sequenceNumber",20,TAG_TYPE_STRING,true));
        tags.add(new Tag("OAType",4,TAG_TYPE_LONG,true));
        tags.add(new Tag("OANetworkID",10,TAG_TYPE_STRING,true));
        tags.add(new Tag("OA",36,TAG_TYPE_STRING,true));
        tags.add(new Tag("DAType",4,TAG_TYPE_LONG,true));
        tags.add(new Tag("DANetworkID",10,TAG_TYPE_STRING,true));
        tags.add(new Tag("DA",36,TAG_TYPE_STRING,true));
        tags.add(new Tag("FAType",4,TAG_TYPE_LONG,true));
        tags.add(new Tag("FANetworkID",10,TAG_TYPE_STRING,true));
        tags.add(new Tag("FA",36,TAG_TYPE_STRING,true));
        tags.add(new Tag("serviceIDType",4,TAG_TYPE_LONG,true));
        tags.add(new Tag("spId",21,TAG_TYPE_STRING,true));
        tags.add(new Tag("serviceId",21,TAG_TYPE_STRING,true));
        tags.add(new Tag("productId",8,TAG_TYPE_STRING,true));
        tags.add(new Tag("serviceUpDownType",4,TAG_TYPE_LONG,true));
        tags.add(new Tag("beginTime",14,TAG_TYPE_STRING,true));
        tags.add(new Tag("resentTimes",1,TAG_TYPE_INT,true));
        tags.add(new Tag("operationType",4,TAG_TYPE_LONG,true));
        tags.add(new Tag("serviceType",2,TAG_TYPE_STRING,true));
        tags.add(new Tag("linkId",20,TAG_TYPE_TLV,false));
        tags.add(new Tag("SMSFormat",4,TAG_TYPE_TLV,false));
        tags.add(new Tag("SMSContentLen",4,TAG_TYPE_TLV,false));
        tags.add(new Tag("SMSContent",-1,TAG_TYPE_TLV,false));
        tags.add(new Tag("cpId",8,TAG_TYPE_TLV,false));
        tags.add(new Tag("contentId",22,TAG_TYPE_TLV,false));
        tags.add(new Tag("orderMethod",4,TAG_TYPE_TLV,false));
        tags.add(new Tag("pushId",20,TAG_TYPE_TLV,false));
        tags.add(new Tag("feeType",2,TAG_TYPE_TLV,false));
        tags.add(new Tag("fee",8,TAG_TYPE_TLV,false));
        tags.add(new Tag("accessNo",21,TAG_TYPE_TLV,false));
    }

    public CheckPriceMessage() {
        super();
        init();
    }
    public CheckPriceMessage(byte[] dataBuffer) {
        super();
        init();
        bufferPosNow = 12;
        setBuffers(dataBuffer);
        readBuffer(dataBuffer);
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Long getOAType() {
        return OAType;
    }

    public void setOAType(Long OAType) {
        this.OAType = OAType;
    }

    public String getOANetworkID() {
        return OANetworkID;
    }

    public void setOANetworkID(String OANetworkID) {
        this.OANetworkID = OANetworkID;
    }

    public String getOA() {
        return OA;
    }

    public void setOA(String OA) {
        this.OA = OA;
    }

    public Long getDAType() {
        return DAType;
    }

    public void setDAType(Long DAType) {
        this.DAType = DAType;
    }

    public String getDANetworkID() {
        return DANetworkID;
    }

    public void setDANetworkID(String DANetworkID) {
        this.DANetworkID = DANetworkID;
    }

    public String getDA() {
        return DA;
    }

    public void setDA(String DA) {
        this.DA = DA;
    }

    public Long getFAType() {
        return FAType;
    }

    public void setFAType(Long FAType) {
        this.FAType = FAType;
    }

    public String getFANetworkID() {
        return FANetworkID;
    }

    public void setFANetworkID(String FANetworkID) {
        this.FANetworkID = FANetworkID;
    }

    public String getFA() {
        return FA;
    }

    public void setFA(String FA) {
        this.FA = FA;
    }

    public Long getServiceIDType() {
        return serviceIDType;
    }

    public void setServiceIDType(Long serviceIDType) {
        this.serviceIDType = serviceIDType;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Long getServiceUpDownType() {
        return serviceUpDownType;
    }

    public void setServiceUpDownType(Long serviceUpDownType) {
        this.serviceUpDownType = serviceUpDownType;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public Integer getResentTimes() {
        return resentTimes;
    }

    public void setResentTimes(Integer resentTimes) {
        this.resentTimes = resentTimes;
    }

    public Long getOperationType() {
        return operationType;
    }

    public void setOperationType(Long operationType) {
        this.operationType = operationType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Tlv getLinkId() {
        return linkId;
    }

    public void setLinkId(Tlv linkId) {
        this.linkId = linkId;
    }

    public Tlv getSMSFormat() {
        return SMSFormat;
    }

    public void setSMSFormat(Tlv SMSFormat) {
        this.SMSFormat = SMSFormat;
    }

    public Tlv getSMSContentLen() {
        return SMSContentLen;
    }

    public void setSMSContentLen(Tlv SMSContentLen) {
        this.SMSContentLen = SMSContentLen;
    }

    public Tlv getSMSContent() {
        return SMSContent;
    }

    public void setSMSContent(Tlv SMSContent) {
        this.SMSContent = SMSContent;
    }

    public Tlv getCpId() {
        return cpId;
    }

    public void setCpId(Tlv cpId) {
        this.cpId = cpId;
    }

    public Tlv getContentId() {
        return contentId;
    }

    public void setContentId(Tlv contentId) {
        this.contentId = contentId;
    }

    public Tlv getOrderMethod() {
        return orderMethod;
    }

    public void setOrderMethod(Tlv orderMethod) {
        this.orderMethod = orderMethod;
    }

    public Tlv getPushId() {
        return pushId;
    }

    public void setPushId(Tlv pushId) {
        this.pushId = pushId;
    }

    public Tlv getFeeType() {
        return feeType;
    }

    public void setFeeType(Tlv feeType) {
        this.feeType = feeType;
    }

    public Tlv getFee() {
        return fee;
    }

    public void setFee(Tlv fee) {
        this.fee = fee;
    }

    public Tlv getAccessNo() {
        return accessNo;
    }

    public void setAccessNo(Tlv accessNo) {
        this.accessNo = accessNo;
    }

    public int getCommandId(){
        return Command.CMDID_CheckPrice;
    }
}
