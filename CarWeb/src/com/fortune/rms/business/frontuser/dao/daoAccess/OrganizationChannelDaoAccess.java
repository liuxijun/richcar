package com.fortune.rms.business.frontuser.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.frontuser.dao.daoInterface.OrganizationChannelDaoInterface;
import com.fortune.rms.business.frontuser.model.OrganizationChannel;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-27
 * Time: 22:39:41
 * Org - Ch
 */
@Repository
public class OrganizationChannelDaoAccess  extends BaseDaoAccess<OrganizationChannel, Long>
		implements
        OrganizationChannelDaoInterface {
    public OrganizationChannelDaoAccess() {
        super(OrganizationChannel.class);
    }

    /**
     * 清除栏目和组织的所有关联关系
     * @param channelId 栏目Id
     */
    public void clearChannelReference(long channelId){
        String hql = "delete from OrganizationChannel oc where oc.channelId=" + channelId;
        executeUpdate(hql);
    }
}
