package com.fortune.rms.business.live.model;

import java.util.Date;

/**
 * Created by ����· on 2015/3/9.
 * һ��ת��������Ϣ�����ںͿͻ��˽�����Ϣ
 */
public class Task {
    private Long id;
    private String title;
    private Long sourceId;          // Դid
    private String templateIds;    // ����id�����ŷָ�
    private String streamName;      // ����������
    private String serverIp;        // ֱ����������ַ
    private Integer serverPort;     // ֱ���������˿�
    private Long type;               // ʱ������
    private Date startTime;         // ��ʼʱ�䣬��������
    private Date endTime;           // ����ʱ��
    private Date startDate;         // ��ʼ����
    private Date endDate;           // ��������
    private String weekDay;         // �ظ�ִ�е�����
    private String filePath;        // ¼�Ʊ���·��
    private boolean needRecord;     // ��Ҫ¼��
    private boolean needLive;       // ��Ҫֱ��

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
