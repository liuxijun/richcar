package com.fortune.rms.business.live.model;

import java.util.Date;

/**
 * Created by 王明路 on 2015/2/26.
 * 录制
 */
public class Record {
    public static final int RECORD_STATUS_NORMAL = 1;           // 正常
    public static final int RECORD_STATUS_LOCKED = 9;           // 被锁定
    public static final int RECORD_STATUS_UN_AUDITED = 0;      // 未审核

    public static final int RECORD_TYPE_ONE_TIME = 1;         //一次性
    public static final int RECORD_TYPE_REPEAT = 0;           //循环任务
    public static final int RECORD_TYPE_ALWAYS = 9;           //7x24

    private Long id;
    private String title;       // 标题
    private Long taskId;        // 统一转码上对应的任务Id
    private Date createTime;    // 创建时间
    private int status;         // 状态
    private int autoControl;    // 一次性任务是否允许系统自动启停
    private String suffix;       /*后缀，循环任务显示的标题后追加的东东，类似于字串模板*/
    private String actor;        // 主讲
    private String intro;        // 简介
    private String poster;       // 海报地址
    private int autoSnap;       // 是否自动截取海报
    private Long serverId;       // 直播服务器Id
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

    private Date startTime;     // 开始时间，不含日期
    private Date endTime;       // 结束时间，不含日期
    private Date startDate;     // 开始日期，不含时间
    private Date endDate;       // 结束日期，不含时间
    private String weekDay;     // 重复任务循环的星期，用逗号分隔0-星期一 6-星期日

    private String userTypes;   // 逗号分隔的用户类型
    private String recordChannels; // 录制后自动发布的栏目，用逗号分隔

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
