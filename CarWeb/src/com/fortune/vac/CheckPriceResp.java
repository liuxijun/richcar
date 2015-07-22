package com.fortune.vac;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-9-22
 * Time: 上午11:19
 * 批价鉴权应答
 */
public class CheckPriceResp   extends BaseMessage implements MessageBody{
    private long resultCode;//	Integer4	4	结果码。0 : OK,
                                //其它为错误代码，见附录
    private long confirmInterval;//	Integer4	4	等待CheckPriceConfirm 消息的时间间隔。如果操作失败或者该域是不相关的缺省值设为“－1”
    private int needConfirm;//	Interger1	1	确认标识。如果需要确认，需要接着发一条confirm消息，并保持其SequenceNumber和本消息的一致
                               // 0：无需确认
                               // 1：需要确认
                               // -1：如果操作失败缺省值设为“-1”
    private String originalAdr;//Octet String	36	原始地址，主要用于伪码转换时返回原来的用户号码。
                                  //  MT：返回接收方用户原号码
                                  //  MO：返回发送方用户伪码。
                                  //  伪码转换策略可配置。
    //可选参数
    private Tlv linkId;//	TLV	20	事务关联ID，点播业务的事务关联，由AAA产生。格式如下：8位随机序列数；
                         //为空(二进制0)表示无效
    private Tlv feeType;//	TLV	2	计费类型，该字段不能为空。
                          //  0：不计费，仅用于核减SP对称的信道费
                          //  1：免费
                          //  2：按条/次计费
                          //  3：按包月收取
                          //  4：封顶计费
                          //  5：按流量计费
                          //  6：按时长计费
                          //  7：包多月计费
                          //  8：包月计费+单位费率（该值不另外增加话单中计费类型字段取值，对包月计费+单位费率资费类产品中的包月计费话单类型填3，对单位计费话单类型填2）
                          //  对会话类计费，5或6必须返回
    private Tlv redirect;//	Tlv	4	用于告知业务接入网关如何处理用户请求，有以下几种类型：
                            //1.	需定购
                            //2.	需确认
                            //3.	错误页面（参考错误值）
                            //4.	试用到期
                            //5.	资费修改提示
    private Tlv returnMessage;//	Tlv	256	返回相应错误描述信息给业务系统。
    private Tlv needToNextNode;//	TLV	1	VAC决定是否需要将原消息转至下一网络节点：
                                  //  0：不需要
                                  //  1：需要
    private Tlv SP_ProductId;//	Tlv	12	填写SP_ProductID
    private Tlv SPEC_ProductId;//	Tlv	12	CRM构成产品标识
    private Tlv productId;//	Tlv	8	CRM产品ID

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
