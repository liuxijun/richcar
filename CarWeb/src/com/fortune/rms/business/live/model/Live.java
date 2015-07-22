package com.fortune.rms.business.live.model;

import java.util.Date;
import java.util.List;

/**
 * Created by 王明路 on 2015/2/26.
 * 直播和录制信息
 */
public class Live {
    public static final int LIVE_STATUS_NORMAL = 1;           // 正常
    public static final int LIVE_STATUS_LOCKED = 9;           // 被锁定
    public static final int LIVE_STATUS_EXPIRED = 0;      // 过期
    public static final int LIVE_STATUS_WORKING = 2;      // 正在直播/录制
    public static final int LIVE_STATUS_STARTING = 3;   // 正在启动，等待transcenter回调
    public static final int LIVE_STATUS_STOPPING = 4;   // 正在停止，等待transcenter回调

    public static final int LIVE_TYPE_ONE_TIME = 1;         //一次性
    public static final int LIVE_TYPE_REPEAT = 2;           //循环任务
    public static final int LIVE_TYPE_ALWAYS = 3;           //7x24

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
    private int foreshow;       // 是否显示预告
    private Long serverId;       // 直播服务器Id
    //private Long tunnel;         // 直播通道号，直播可能对应多个通道号，这里记录智能流的索引url
    private String channel;          // 智能流url名称
    private Long moduleId;
    private List<LiveChannel> channelList;  // 直播的栏目
    private Long creator;         // 创建的管理员Id
    private Long isLive;            // 是不是直播，录制保存时该字段为false

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

    private int type;           // 类型

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /*直播的时间信息以统一转码上的为准，数据库中的仅供显示和判断*/
    private Date startTime;     // 开始时间，不含日期
    private Date endTime;       // 结束时间，不含日期
    private Date startDate;     // 开始日期，不含时间
    private Date endDate;       // 结束日期，不含时间
    private String weekDay;     // 重复任务循环的星期，用逗号分隔0-星期一 6-星期日

    private String userTypes;   // 逗号分隔的用户类型
    private int needRecord;    // 是否需要录制
    private String recordChannels; // 录制后自动发布的栏目，用逗号分隔，如果该字段为空，则用直播所属栏目

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
