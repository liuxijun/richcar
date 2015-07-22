package com.fortune.rms.business.live.model;

import java.util.Date;

/**
 * Created by 王明路 on 2015/3/9.
 * 一个转码任务信息，用于和客户端交换信息
 */
public class Task {
    private Long id;
    private String title;
    private Long sourceId;          // 源id
    private String templateIds;    // 配置id，逗号分隔
    private String streamName;      // 智能流名称
    private String serverIp;        // 直播服务器地址
    private Integer serverPort;     // 直播服务器端口
    private Long type;               // 时间类型
    private Date startTime;         // 开始时间，不含日期
    private Date endTime;           // 结束时间
    private Date startDate;         // 开始日期
    private Date endDate;           // 结束日期
    private String weekDay;         // 重复执行的星期
    private String filePath;        // 录制保存路径
    private boolean needRecord;     // 需要录制
    private boolean needLive;       // 需要直播

    public boolean getNeedRecord() {
        return needRecord;
    }

    public void setNeedRecord(boolean needRecord) {
        this.needRecord = needRecord;
    }

    public boolean getNeedLive() {
        return needLive;
    }

    public void setNeedLive(boolean needLive) {
        this.needLive = needLive;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getTemplateIds() {
        return templateIds;
    }

    public void setTemplateIds(String templateIds) {
        this.templateIds = templateIds;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public Task() {
        serverPort = 80;
    }
}
