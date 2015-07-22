package com.fortune.rms.business.encoder.model;

import com.fortune.rms.business.system.model.Device;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-7-28
 * Time: 上午11:57
 * 转码任务
 */
public class EncoderTask implements java.io.Serializable{
    private long id;
    private String name;
    private Long encoderId;
    private Long streamServerId;
    private Date startTime;
    private Date stopTime;
    private Long clipId;
    private Integer status;
    private Integer process;
    private Integer length;
    private Long templateId;
    private String sourceFileName;
    private String desertFileName;
    private EncoderTemplate template;
    private Device encoder;
    private Device streamServer;
    private String encodeLog;
    private Date fileDate;
    private Long fileSize;

    public EncoderTask() {
    }

    public EncoderTask(long id) {
        this.id = id;
    }

    public EncoderTask(long id, String name, Long encoderId, Long streamServerId, Date startTime, Date stopTime,
                       Long clipId, Integer status, Integer process, Integer length, Long templateId,
                       String sourceFileName, String desertFileName, String encodeLog,Date fileDate,Long fileSize) {
        this.id = id;
        this.name = name;
        this.encoderId = encoderId;
        this.streamServerId = streamServerId;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.clipId = clipId;
        this.status = status;
        this.process = process;
        this.length = length;
        this.templateId = templateId;
        this.sourceFileName = sourceFileName;
        this.desertFileName = desertFileName;
        this.encodeLog = encodeLog;
        this.fileDate = fileDate;
        this.fileSize = fileSize;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStreamServerId() {
        return streamServerId;
    }

    public void setStreamServerId(Long streamServerId) {
        this.streamServerId = streamServerId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    
    public Integer getProcess() {
        return process;
    }

    @SuppressWarnings("JpaAttributeTypeInspection")
    public EncoderTemplate getTemplate() {
        return template;
    }

    public void setTemplate(EncoderTemplate template) {
        this.template = template;
    }

    public void setProcess(Integer process) {
        this.process = process;
    }

    public Long getEncoderId() {
        return encoderId;
    }

    public void setEncoderId(Long encoderId) {
        this.encoderId = encoderId;
    }

    public Long getClipId() {
        return clipId;
    }

    public void setClipId(Long clipId) {
        this.clipId = clipId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getSourceFileName() {
        return sourceFileName;
    }

    public void setSourceFileName(String sourceFileName) {
        this.sourceFileName = sourceFileName;
    }

    public String getDesertFileName() {
        return desertFileName;
    }

    public void setDesertFileName(String desertFileName) {
        this.desertFileName = desertFileName;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Date getFileDate() {
        return fileDate;
    }

    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @SuppressWarnings("JpaAttributeTypeInspection")
    public Device getEncoder() {
        return encoder;
    }

    public void setEncoder(Device encoder) {
        this.encoder = encoder;
    }

    @SuppressWarnings("JpaAttributeTypeInspection")
    public Device getStreamServer() {
        return streamServer;
    }

    public void setStreamServer(Device streamServer) {
        this.streamServer = streamServer;
    }

    public String getEncodeLog() {
        return encodeLog;
    }

    public void setEncodeLog(String encodeLog) {
        this.encodeLog = encodeLog;
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
