package com.fortune.vac;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-8-28
 * Time: 下午3:29
 * 鉴权批价请求
 */
public class CheckPriceMessage  extends BaseMessage implements MessageBody {

    private String sequenceNumber;//20
    private Long OAType;//	Integer4	4	业务发起端地址类型，参考附录B
    private String OANetworkID;//	Octet String	10	业务发起端用户归属网络标识。如：WCDMA、GSM等
    private String OA;//	Octet String 	36	业务发起端地址。填写手机号码
    private Long DAType;//	Integer4	4	目标地址类型，参见附录B
    private String DANetworkID;//	Octet String	10	目标端用户归属网络标识。如：WCDMA、GSM等
    private String DA;//	Octet String 	36	目标地址。填写手机号码
    private Long FAType;//	Integer4	4	付费地址类型，填1。保留作为以后可以独立设定付费方时使用。参考附录B
    private String FANetworkID;//	Octet String	10	付费用户归属的网络标识。保留作为以后可以独立设定付费方时使用。
    private String FA;//	Octet String	36	付费地址。填手机号码。保留作为以后可以独立设定付费方时使用。
    private Long serviceIDType;//Integer4	4 	  1：SPID+ServiceID
                                                //    3：SPID+SP_ProductID
                                                //    5: SPID+SPEC_ProductID
    private String spId;//	Octet String	21	填写企业代码
    private String serviceId;///FeatureString	Octet String	21	ServiceIDType
/*
            为1时填写ServiceID
    为3时填写SP_ProductID
    为5时填写SPEC_ProductID(业务部件能知晓用户定购或点播CRM的产品构成)
*/
    private String productId;//	Octet String	8	CRM产品ID, ServiceIDType为5时有效，其他填8个空格
    private Long serviceUpDownType;//	Integer4	4	业务上下行类型：
    //                                                      1: MOAT
    //                                                      2: AOMT
    //                                                      3: MOMT 终端到终端
    //                                                      4、P TO E，终端到邮箱
    //                                                      9: 其它
    //                                                      vac对彩信p to e、sp彩信、在信、wap push业务需要判断具体取值，对其他业务不鉴权本字段
    private String beginTime;//	Octet String	14	开始时间YYYYMMDDHHMMSS
    //                                                  年月日时分秒
    private Integer resentTimes;//	Integer1	1	重发次数：
    //                                             0：只发一次
    //                                            1-255：重发次数
    //                                                 默认取值0
    private Long operationType;//	Integer4	4	0、鉴权计费
    //                                ServiceIDType=1时，如果请求中有LinkID，进行点播关系鉴权，无LinkID，进行订购关系鉴权，
    //                                鉴权通过则进行计费。
    //                                        1、定购ServiceIDType必须为3
    //                                        2、退定 ServiceIDType必须为3
    //                                        3、退定ServiceType对应的所有业务（终端侧不能单独退订业务除外）：
    //                                                     ServiceIDType必须为3
    //                                        4、点播：业务平台能判断是点播，直接填4
    //                                        6、定购关系鉴权(用于检查用户是否订购了此业务)
    //                                        7、屏蔽业务能力
    //                                        8、恢复业务能力
    private String serviceType;//	Octet String	2	业务类型，见VAC与BSS接口规范附录A 7
    //                                  当Operation Type=1、2、3、4时，VAC不对鉴权发起方的业务类型与产品的业务类型进行比较鉴权
   // 可选参数
    private Tlv linkId;//	TLV	20	事务关联ID，点播业务的事务关联，由AAA产生。格式如下：8位随机序列数；
    //                                    为空(二进制0)表示无效
    private Tlv SMSFormat;//	TLV	4	短信内容格式
    //0：ASCII串
    //3：短信写卡操作
    // 4：二进制信息
    //8：UCS2编码
    //15：含GB汉字
    //……
    private Tlv SMSContentLen;//	TLV	4	短信内容长度
    private Tlv SMSContent;//	TLV	SMSContentLen	短信内容（短信上行过程短信网关需要将内容发送给平台分析）
    private Tlv cpId;//	TLV	8	CP代码
    private Tlv contentId;//	TLV	22	内容代码
    private Tlv orderMethod;//	TLV	4	订购渠道，取值见VAC与BSS接口规范附录，在定购时业务系统必须填写
    private Tlv pushId;//	TLV	20	营销方式,推荐方ID
    private Tlv feeType;//	TLV	2	计费类型，该字段不能为空。
    //        0：不计费，仅用于核减SP对称的信道费；
    //        1：免费
    //2：按条/次计费
    //3：按包月收取
    //4：封顶计费
    //5：按流量计费
    //6：按时长计费
    //7：包多月计费
    private Tlv fee;//	TLV	8	单位，分。
    //增值系统不批价的不带fee字段，同时VAC侧支持按照业务类型进行配置是否批价处理。
    //对代收费业务fee、FeeType必须正确填写（FeeType只能填2、3），如本字段为空，返回3002错误。
    private Tlv accessNo;//	TLV	21	用户联通在信、彩信等接入号，对在信类、彩信类业务必须填写

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
