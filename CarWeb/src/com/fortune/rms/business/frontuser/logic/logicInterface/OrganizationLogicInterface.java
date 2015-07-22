package com.fortune.rms.business.frontuser.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.frontuser.model.OrgChannelMap;
import com.fortune.rms.business.frontuser.model.Organization;
import com.fortune.rms.business.frontuser.model.OrganizationChannel;
import com.fortune.rms.business.publish.model.Channel;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-27
 * Time: 15:24:15
 * 用户组织管理Interface
 */
public interface OrganizationLogicInterface extends BaseLogicInterface<Organization> {
    public static final Long MAP_BY_ORG = 1L;
    public static final Long MAP_BY_CHANNEL=2L;


    public List<Organization> getOrgList(String parentId);
    public List<Organization> getOrganizationByName(String name);
    public List<OrgChannelMap> getOrgChannelMap(Long by);
    public List<OrganizationChannel> saveOrgChannels(Organization organization);
    List<Organization> saveOrganizations(List<Organization> orgList);
    public int getOrganizationUserCount(Long orgId);
    public void referenceChannelOrg(long orgId, long channelId, boolean childrenIncluded);
    public void referenceChannelAllOrg(long channelId);

    public List<Organization> getOrganes(int parentId);
}
