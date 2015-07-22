package com.fortune.vac;


import com.fortune.util.AppConfigurator;
import com.fortune.util.BeanUtils;
import com.fortune.util.MD5Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-8-28
 * Time: 下午2:51
 * 基本的消息类
 */
public class BaseMessage extends BaseTag{
    public static final int TAG_TYPE_INT = 1;
    public static final int TAG_TYPE_LONG = 2;
    public static final int TAG_TYPE_STRING = 3;
    public static final int TAG_TYPE_TLV = 4;
    public static final int TAG_TYPE_BYTE_ARRAY=5;


    protected Long sourceDeviceType;//4
    protected String sourceDeviceID;//20
    protected Long destinationDeviceType;//4
    protected String destinationDeviceID;//20
    protected List<Tag> tags=new ArrayList<Tag>();

    public void init(){
        AppConfigurator config = AppConfigurator.getInstance();
        sourceDeviceType = config.getLongConfig("vac.default.sourceDeviceType",42);
        sourceDeviceID = config.getConfig("vac.default.sourceDeviceID","421802");
        destinationDeviceType = config.getLongConfig("vac.default.destinationDeviceType",0);
        destinationDeviceID = config.getConfig("vac.default.destinationDeviceID","001801");
        tags.clear();
        tags.add(new Tag("sourceDeviceType",4,TAG_TYPE_LONG,true));
        tags.add(new Tag("sourceDeviceID",20,TAG_TYPE_STRING,true));
        tags.add(new Tag("destinationDeviceType",4,TAG_TYPE_LONG,true));
        tags.add(new Tag("destinationDeviceID",20,TAG_TYPE_STRING,true));
    }
    
    public BaseMessage() {
        init();
    }

    public Long getSourceDeviceType() {
        return sourceDeviceType;
    }

    public void setSourceDeviceType(Long sourceDeviceType) {
        this.sourceDeviceType = sourceDeviceType;
    }

    public String getSourceDeviceID() {
        return sourceDeviceID;
    }

    public void setSourceDeviceID(String sourceDeviceID) {
        this.sourceDeviceID = sourceDeviceID;
    }

    public Long getDestinationDeviceType() {
        return destinationDeviceType;
    }

    public void setDestinationDeviceType(Long destinationDeviceType) {
        this.destinationDeviceType = destinationDeviceType;
    }

    public String getDestinationDeviceID() {
        return destinationDeviceID;
    }    
    
    public void readBuffer(byte[] buffer){
        // bufferPosNow = 0;
        setBuffers(buffer);
        for(int i=0,l=tags.size();i<l;i++){
            Tag tag = tags.get(i);
            //logger.debug("index="+i+",name="+tag.name+",size="+tag.size);
            if(tag.type!=TAG_TYPE_TLV){
                String name = tag.name;
                switch (tag.type){
                    case TAG_TYPE_INT:
                        int value =(int) readBytes(tag.size);
                        BeanUtils.setProperty(this,name,value);
                        break;
                    case TAG_TYPE_LONG:
                        Long longValue = read_32();
                        BeanUtils.setProperty(this,name,longValue);
                        break;
                    case TAG_TYPE_STRING:
                        int pos = bufferPosNow;
                        String strValue = readString(tag.size);
                        BeanUtils.setProperty(this,name,strValue);
                        bufferPosNow = pos + tag.size;
                        break;
                    case TAG_TYPE_BYTE_ARRAY:
                        int posNow = bufferPosNow;
                        byte[] byteArrayValue = readByteArray(tag.size);
                        BeanUtils.setProperty(this,name,byteArrayValue);
                        bufferPosNow = posNow + tag.size;
                        break;
                    case TAG_TYPE_TLV:
                        break;
                }
            }
        }
    }

    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName()).append("{\r\n");
        for (Tag tag : tags) {
            if (tag != null) {
                result.append("\t\t");
                Object valueObj = BeanUtils.getProperty(this, tag.name);
                if (valueObj != null) {
                    String valueStr;
                    if(valueObj instanceof Tlv){
                        valueStr = ((Tlv) valueObj).getValue();
                        if(valueStr==null){
                            valueStr = "";
                        }
                    }else if(valueObj instanceof byte[]){
                        valueStr = MD5Utils.bufferToHex((byte[])valueObj);
                    }else{
                        valueStr = valueObj.toString();
                    }
                    result.append(tag.name).append("=").append(valueStr).append("\r\n");
                } else {
                    result.append(tag.name).append("=").append("").append("\r\n");
                }
            }
        }
        result.append("}\r\n");
        return result.toString();
    }

    public void setDestinationDeviceID(String destinationDeviceID) {
        this.destinationDeviceID = destinationDeviceID;
    }

    public byte[] buildBuffer(){
        byte[] result = new byte[512*1024];
        int pos = 0;
        for(int i=0,l=tags.size();i<l;i++){
            Tag tag = tags.get(i);
            if(tag!=null){
                //logger.debug("name="+tag.name+",idx="+i+",size="+tag.size+",pos="+pos);
                Object valueObj = BeanUtils.getProperty(this, tag.name);
                if(valueObj==null){
                    if(tag.mustOutPut && tag.type!=TAG_TYPE_TLV){//即使是空也要输出的话
                        for(int idx=0;idx<tag.size;idx++){
                            result[pos+idx]=0;
                        }
                        pos +=tag.size;
                    }
                    continue;
                }
                switch (tag.type){
                   case TAG_TYPE_INT:
                       if(valueObj instanceof Integer){
                           int v = (Integer) valueObj;
                           if(tag.size==2){
                               pos = write_16(result,v,pos);
                           }else if(tag.size==1){
                               pos = write_8(result,v,pos);
                           }
                      }
                       break;
                   case TAG_TYPE_LONG:
                       if(valueObj instanceof Long){
                           pos = write_32(result,(Long) valueObj,pos);
                       }
                       break;
                   case TAG_TYPE_STRING:
                       if(valueObj instanceof String){
                           writeString(result,valueObj.toString(),pos);
                       }
                       pos += tag.size;
                       break;
                    case TAG_TYPE_BYTE_ARRAY:
                        if(valueObj instanceof byte[]){
                            byte[] byteArray = (byte[]) valueObj;
                            writeByteArray(result,byteArray,pos,byteArray.length);
                        }
                        pos +=tag.size;
                        break;
                   case TAG_TYPE_TLV:
                       if(valueObj instanceof Tlv){
                           Tlv value =(Tlv) valueObj;
                           Integer tlvTag = Tlv.getTagByName(tag.name);
                           if(tlvTag!=null){
                               value.setTag(tlvTag);
                           }else{
                               logger.error("不认识的标签："+tag.name);
                           }
                           pos = value.buildBuffer(result,pos);
                       }
                       break;
               }
            }
        }
        byte[] data = new byte[pos];
        System.arraycopy(result,0,data,0,pos);
        return data;
    }
    public void scanTlvs(){
        while(bufferPosNow<buffers.length){
            long tag = read_16();
            if(tag>0){
                long length = read_16();
                if(length>0){
                    if(bufferPosNow<buffers.length){
                        String value = readString((int)length);
                        String tagName = Tlv.getNameByTag((int)tag); 
                        if(tagName!=null){
                            Tlv tlv = new Tlv();
                            tlv.setTag((int)tag);
                            tlv.setLength((int)length);
                            tlv.setValue(value);
                            BeanUtils.setProperty(this,tagName,tlv);
                        }
                    }
                }else{
                    break;
                }
            }else{
                break;
            }
            
        }
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
