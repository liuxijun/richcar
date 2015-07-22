package com.fortune.rms.business.encoder.model;

public class EncoderTemplate  implements java.io.Serializable {
    private long id;
    private String templateName;
    private String templateCode;
    private String VEncoderType;
    private Long VBitrate;
    private Long VFrameRate;
    private Long VKeyframeInterval;
    private Long VWidth;
    private Long VHeight;
    private Long VFixedQp;
    private Long VMaxQp;
    private String ACodec;
    private Long AChannel;
    private Long ASampleRate;
    private Long AType;
    private Long ABitrate;
    private String fileFormat;
    private Long propertyId;


    public EncoderTemplate() {

    }

    public EncoderTemplate(long id, String templateName, String templateCode, String VEncoderType, Long VBitrate,
                           Long VFrameRate, Long VKeyframeInterval, Long VWidth, Long VHeight, Long VFixedQp,
                           Long VMaxQp, String ACodec, Long AChannel, Long ASampleRate, Long AType, Long ABitrate,
                           String fileFormat, Long propertyId) {
        this.id = id;
        this.templateName = templateName;
        this.templateCode = templateCode;
        this.VEncoderType = VEncoderType;
        this.VBitrate = VBitrate;
        this.VFrameRate = VFrameRate;
        this.VKeyframeInterval = VKeyframeInterval;
        this.VWidth = VWidth;
        this.VHeight = VHeight;
        this.VFixedQp = VFixedQp;
        this.VMaxQp = VMaxQp;
        this.ACodec = ACodec;
        this.AChannel = AChannel;
        this.ASampleRate = ASampleRate;
        this.AType = AType;
        this.ABitrate = ABitrate;
        this.fileFormat = fileFormat;
        this.propertyId = propertyId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getVEncoderType() {
        return VEncoderType;
    }

    public void setVEncoderType(String VEncoderType) {
        this.VEncoderType = VEncoderType;
    }

    public Long getVBitrate() {
        return VBitrate;
    }

    public void setVBitrate(Long VBitrate) {
        this.VBitrate = VBitrate;
    }

    public Long getVFrameRate() {
        return VFrameRate;
    }

    public void setVFrameRate(Long VFrameRate) {
        this.VFrameRate = VFrameRate;
    }

    public Long getVKeyframeInterval() {
        return VKeyframeInterval;
    }

    public void setVKeyframeInterval(Long VKeyframeInterval) {
        this.VKeyframeInterval = VKeyframeInterval;
    }

    public Long getVWidth() {
        return VWidth;
    }

    public void setVWidth(Long VWidth) {
        this.VWidth = VWidth;
    }

    public Long getVHeight() {
        return VHeight;
    }

    public void setVHeight(Long VHeight) {
        this.VHeight = VHeight;
    }

    public Long getVFixedQp() {
        return VFixedQp;
    }

    public void setVFixedQp(Long VFixedQp) {
        this.VFixedQp = VFixedQp;
    }

    public Long getVMaxQp() {
        return VMaxQp;
    }

    public void setVMaxQp(Long VMaxQp) {
        this.VMaxQp = VMaxQp;
    }

    public String getACodec() {
        return ACodec;
    }

    public void setACodec(String ACodec) {
        this.ACodec = ACodec;
    }

    public Long getAChannel() {
        return AChannel;
    }

    public void setAChannel(Long AChannel) {
        this.AChannel = AChannel;
    }

    public Long getASampleRate() {
        return ASampleRate;
    }

    public void setASampleRate(Long ASampleRate) {
        this.ASampleRate = ASampleRate;
    }

    public Long getAType() {
        return AType;
    }

    public void setAType(Long AType) {
        this.AType = AType;
    }

    public Long getABitrate() {
        return ABitrate;
    }

    public void setABitrate(Long ABitrate) {
        this.ABitrate = ABitrate;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    /**
     * toString return json format string of this bean
     * @return String
     */
    public String toString() {
        return com.fortune.util.JsonUtils.getJsonString(this);
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public String getSimpleJson() {
        return com.fortune.util.JsonUtils.getJsonString(this, null);
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public String getObjJson() {
        return com.fortune.util.JsonUtils.getJsonString(this, "obj.");
    }
}
