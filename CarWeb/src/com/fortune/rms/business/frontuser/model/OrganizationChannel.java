package com.fortune.rms.business.frontuser.model;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-27
 * Time: 22:28:02
 * 用户组和频道的对应关系
 */
public class OrganizationChannel {
    private Long id;
    private Long organizationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrganizationChannel(Long id, Long organizationId, Long channelId) {

        this.id = id;
        this.organizationId = organizationId;
        this.channelId = channelId;
    }

    private Long channelId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public OrganizationChannel() {

    }
}
