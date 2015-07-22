package com.fortune.rms.business.live.model;

import java.util.Date;

/**
 * Created by ����· on 2015/2/26.
 * ¼��
 */
public class Record {
    public static final int RECORD_STATUS_NORMAL = 1;           // ����
    public static final int RECORD_STATUS_LOCKED = 9;           // ������
    public static final int RECORD_STATUS_UN_AUDITED = 0;      // δ���

    public static final int RECORD_TYPE_ONE_TIME = 1;         //һ����
    public static final int RECORD_TYPE_REPEAT = 0;           //ѭ������
    public static final int RECORD_TYPE_ALWAYS = 9;           //7x24

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
    private int autoSnap;       // �Ƿ��Զ���ȡ����
    private Long serverId;       // ֱ��������Id
    private Long cspId;          // csp

    public Long getCspId() {
        return cspId;
    }

    public void setCspId(Long cspId) {
        this.cspId = cspId;
    }

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private Date startTime;     // ��ʼʱ�䣬��������
    private Date endTime;       // ����ʱ�䣬��������
    private Date startDate;     // ��ʼ���ڣ�����ʱ��
    private Date endDate;       // �������ڣ�����ʱ��
    private String weekDay;     // �ظ�����ѭ�������ڣ��ö��ŷָ�0-����һ 6-������

    private String userTypes;   // ���ŷָ����û�����
    private String recordChannels; // ¼�ƺ��Զ���������Ŀ���ö��ŷָ�

    public Record() {
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

    public int getAutoSnap() {
        return autoSnap;
    }

    public void setAutoSnap(int autoSnap) {
        this.autoSnap = autoSnap;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
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

    public String getRecordChannels() {
        return recordChannels;
    }

    public void setRecordChannels(String recordChannels) {
        this.recordChannels = recordChannels;
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
