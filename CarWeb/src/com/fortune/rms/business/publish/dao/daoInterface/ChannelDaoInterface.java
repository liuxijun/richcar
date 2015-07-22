package com.fortune.rms.business.publish.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.publish.model.Channel;

import java.util.List;

public interface ChannelDaoInterface extends BaseDaoInterface<Channel, Long> {
    public List<Channel> getChannelsByCspId(long cspId);
    public List<Channel> getCspChannel(long cspId, int status);
    public List getSonChannelId(Long channelId);
    public boolean hasPrivilegeToChannelSX(long cspId, long channelId);
}