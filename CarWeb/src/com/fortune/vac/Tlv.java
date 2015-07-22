package com.fortune.vac;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-8-28
 * Time: ÉÏÎç11:09
 * Tag,Length,Value
 */
public class Tlv {
    public static Map<Integer,String> TLV_TAGS=new HashMap<Integer,String>();
    public static Map<String,Integer> TAG_TLVS = new HashMap<String,Integer>();
    static {
        TLV_TAGS.put(0x0001, "linkId");
        TLV_TAGS.put(0x0002, "mediaType");
        TLV_TAGS.put(0x0003, "clientIP");
        TLV_TAGS.put(0x0004, "amount");
        TLV_TAGS.put(0x0005, "pkTotal");
        TLV_TAGS.put(0x0006, "pkNumber");
        TLV_TAGS.put(0x0007, "SMSFormat");
        TLV_TAGS.put(0x0008, "SMSContentLen");
        TLV_TAGS.put(0x0009, "SMSContent");
        TLV_TAGS.put(0x000A, "SPDealResult");
        TLV_TAGS.put(0x1201, "sessionId ");
        TLV_TAGS.put(0x1202, "feeType");
        TLV_TAGS.put(0x1403, "feeCode");
        TLV_TAGS.put(0x1204, "fixedFee");
        TLV_TAGS.put(0x1205, "sessionEnd");
        TLV_TAGS.put(0x1207, "serviceType");
        TLV_TAGS.put(0x1208, "needToNextNode");
        TLV_TAGS.put(0x1209, "originalDA");
        TLV_TAGS.put(0x120A, "transcoding");
        TLV_TAGS.put(0x1401, "errCode");
        TLV_TAGS.put(0x1501, "returnMessage");
        TLV_TAGS.put(0x1502, "serviceStatues");
        TLV_TAGS.put(0x1503, "userStatus");
        TLV_TAGS.put(0x1504, "redirect");
        TLV_TAGS.put(0x1506, "destAddrList");
        TLV_TAGS.put(0x1507, "pushId");
        TLV_TAGS.put(0x1508, "fee");
        TLV_TAGS.put(0x1509, "cpId");
        TLV_TAGS.put(0x1510, "contentId");
        TLV_TAGS.put(0x1511, "orderMethod");
        TLV_TAGS.put(0x1512, "SP_ProductId");
        TLV_TAGS.put(0x1513, "SPEC_ProductId");
        TLV_TAGS.put(0x1514, "productId");
        TLV_TAGS.put(0x1515, "accessNo");
        for(Integer key:TLV_TAGS.keySet()){
            String value = TLV_TAGS.get(key);
            TAG_TLVS.put(value,key);
        }
    }
    private int tag;
    private int length;
    private String value;

    public Tlv(){

    }

    public Tlv(int tag,int length,String value){
        this.tag = tag;
        this.length = length;
        this.value = value;
    }

    public Tlv(int length,String value){
        this.length = length;
        this.value = value;
    }
    public static Integer getTagByName(String name){
        return TAG_TLVS.get(name);
    }
    public static String getNameByTag(Integer tag){
        return TLV_TAGS.get(tag);
    }
    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public int buildBuffer(byte[] result,int startPos){
        startPos = BaseTag.write_16(result,tag,startPos);
        startPos = BaseTag.write_16(result,length,startPos);
        startPos = BaseTag.writeString(result,value,startPos);
        return startPos;
    }
    public String toString(){
        return "Tlv{\r\n\t\tname:"+getNameByTag(tag)+"\r\n\t\ttag:"+tag+"\r\n\t\tlength:"+length+"\r\n\t\tvalue:"+value+"\r\n\t}";
    }
}
