package com.fortune.rms.business.frontuser.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.frontuser.model.OrganizationChannel;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-27
 * Time: 22:26:18
 * Ƶ�����û���Ķ�Ӧ��ϵ
 */
public interface OrganizationChannelDaoInterface  extends BaseDaoInterface<OrganizationChannel, Long> {
    public void clearChannelReference(long channelId);
}
