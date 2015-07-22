package com.fortune.rms.business.content.model;

import com.fortune.rms.business.publish.model.Channel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-16
 * Time: 21:49:32
 * RedexPro使用的待审媒体信息
 */
public class RedexAuditContent {
    public static final Long AUDIT_TYPE_ONLINE = 1L;
    public static final Long AUDIT_TYPE_APPEND = 2L;

    private Long id;
    private String name;
    private Long deviceId;
    private Long moduleId;
    private Long status;
    private Date createTime;
    private List<Channel> channelList;       // 所属的频道列表
    private String publisher;                  // 发布媒体的管理员
    private Long auditType;                     // 审核类型 追集/上线审核

    public void setChannelList(List<Channel> channelList) {
        this.channelList = channelList;
    }

    @SuppressWarnings("unchecked")
    public RedexAuditContent() {
        //channelList = new ArrayList();
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Long getAuditType() {
        return auditType;
    }

    public void setAuditType(Long auditType) {
        this.auditType = auditType;
    }

    public void addChannel(Channel ch){
        if(ch != null){
            channelList.add(ch);
        }
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

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
