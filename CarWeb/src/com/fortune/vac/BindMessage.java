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

public class BindMessage extends BaseMessage implements MessageBody{
    private byte[] checkSource;//16
    private String timeStamp;//10
    private long version;//4

    public void init(){
        super.init();
        tags.add(new Tag("checkSource",16,TAG_TYPE_BYTE_ARRAY,true));
        tags.add(new Tag("timeStamp",10,TAG_TYPE_STRING,true));
        tags.add(new Tag("version",4,TAG_TYPE_LONG,true));
    }
    public BindMessage() {
        AppConfigurator config = AppConfigurator.getInstance();
        timeStamp = StringUtils.date2string(new Date(),"MMddHHmmss");
        try {
            String pwd = config.getConfig("vac.default.pwd","123456");//"yzsjpt/yzsjpt!@");
            // logger.debug("Bind操作时共享密钥："+pwd);
            checkSource = MD5Utils.getMD5ByteArrayFromString(sourceDeviceID + pwd + timeStamp);
        } catch (NoSuchAlgorithmException e) {
            logger.error("MD5处理过程中发生异常："+e.getMessage());
        }
        version = config.getLongConfig("vac.default.version",100);
        init();
    }

    public BindMessage(long sourceDeviceType, String sourceDeviceID, long destinationDeviceType,
                       String destinationDeviceID, byte[] checkSource, String timeStamp,
                       long version) {
        this.sourceDeviceType = sourceDeviceType;
        this.sourceDeviceID = sourceDeviceID;
        this.destinationDeviceType = destinationDeviceType;
        this.destinationDeviceID = destinationDeviceID;
        this.checkSource = checkSource;
        this.timeStamp = timeStamp;
        this.version = version;
        init();
    }

    public BindMessage(byte[] buffers) {
        init();
        setBuffers(buffers);
        bufferPosNow = 12;
        sourceDeviceType = read_32();
        sourceDeviceID = readString(20);
        destinationDeviceType = read_32();
        destinationDeviceID = readString(20);
        checkSource = readByteArray(16);
        timeStamp = readString(10);
        version = read_32();
    }


    public byte[] getCheckSource() {
        return checkSource;
    }

    public void setCheckSource(byte[] checkSource) {
        this.checkSource = checkSource;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
    
    public int getCommandId(){
        return Command.CMDID_Bind;
    }
}
