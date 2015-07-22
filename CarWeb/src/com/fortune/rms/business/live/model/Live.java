package com.fortune.rms.business.live.model;

import java.util.Date;
import java.util.List;

/**
 * Created by ����· on 2015/2/26.
 * ֱ����¼����Ϣ
 */
public class Live {
    public static final int LIVE_STATUS_NORMAL = 1;           // ����
    public static final int LIVE_STATUS_LOCKED = 9;           // ������
    public static final int LIVE_STATUS_EXPIRED = 0;      // ����
    public static final int LIVE_STATUS_WORKING = 2;      // ����ֱ��/¼��
    public static final int LIVE_STATUS_STARTING = 3;   // �����������ȴ�transcenter�ص�
    public static final int LIVE_STATUS_STOPPING = 4;   // ����ֹͣ���ȴ�transcenter�ص�

    public static final int LIVE_TYPE_ONE_TIME = 1;         //һ����
    public static final int LIVE_TYPE_REPEAT = 2;           //ѭ������
    public static final int LIVE_TYPE_ALWAYS = 3;           //7x24

    private Long id;
    private String title;       // ����
    private Long taskId;        // ͳһת���϶�Ӧ������Id
    private Date createTime;    // ����ʱ��
    private int status;         // ״̬
    private int autoControl;    // һ���������Ƿ�����ϵͳ�Զ���ͣ
    private String suffix;       /*��׺��ѭ��������ʾ�ı����׷�ӵĶ������������ִ�ģ��*/
    private String actor;        // ����
    private String intro;        // ���
    private String poster;       // ������ַ
    private int foreshow;       // �Ƿ���ʾԤ��
    private Long serverId;       // ֱ��������Id
    //private Long tunnel;         // ֱ��ͨ���ţ�ֱ�����ܶ�Ӧ���ͨ���ţ������¼������������url
    private String channel;          // ������url����
    private Long moduleId;
    private List<LiveChannel> channelList;  // ֱ������Ŀ
    private Long creator;         // �����Ĺ���ԱId
    private Long isLive;            // �ǲ���ֱ����¼�Ʊ���ʱ���ֶ�Ϊfalse

    public Long getIsLive() {
        return isLive;
    }

    public void setIsLive(Long isLive) {
        this.isLive = isLive;
    }

    public Long getCspId() {
        return cspId;
    }

    public void setCspId(Long cspId) {
        this.cspId = cspId;
    }

    private Long cspId;             // csp

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public List<LiveChannel> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<LiveChannel> channelList) {
        this.channelList = channelList;
    }

    private int type;           // ����

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /*ֱ����ʱ����Ϣ��ͳһת���ϵ�Ϊ׼�����ݿ��еĽ�����ʾ���ж�*/
    private Date startTime;     // ��ʼʱ�䣬��������
    private Date endTime;       // ����ʱ�䣬��������
    private Date startDate;     // ��ʼ���ڣ�����ʱ��
    private Date endDate;       // �������ڣ�����ʱ��
    private String weekDay;     // �ظ�����ѭ�������ڣ��ö��ŷָ�0-����һ 6-������

    private String userTypes;   // ���ŷָ����û�����
    private int needRecord;    // �Ƿ���Ҫ¼��
    private String recordChannels; // ¼�ƺ��Զ���������Ŀ���ö��ŷָ���������ֶ�Ϊ�գ�����ֱ��������Ŀ

    public Live() {
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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAutoControl() {
        return autoControl;
    }

    public void setAutoControl(int autoControl) {
        this.autoControl = autoControl;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public int getForeshow() {
        return foreshow;
    }

    public void setForeshow(int foreshow) {
        this.foreshow = foreshow;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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

    public String getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(String userTypes) {
        this.userTypes = userTypes;
    }

    public int getNeedRecord() {
        return needRecord;
    }

    public void setNeedRecord(int needRecord) {
        this.needRecord = needRecord;
    }

    public String getRecordChannels() {
        return recordChannels;
    }

    public void setRecordChannels(String recordChannels) {
        this.recordChannels = recordChannels;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
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
        return com.fortune.util.JsonUtils.getJsonString(this, "");
    }
}
