package com.fortune.rms.business.csp.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.csp.model.CspChannel;
import com.fortune.rms.business.publish.model.Channel;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wang
 * Date: 13-1-29
 * Time: ÏÂÎç3:12
 */
public interface CspChannelDaoInterface extends BaseDaoInterface<CspChannel,Long> {
    public List<CspChannel> getCspBindChannels(long cspId, int type);
    public List<CspChannel> getCspBindChannels(long cspId, int type, int status);
    public void deleteCspChannel(long cspId);
    public boolean hasPrivilegeToChannel(Long cspId, Long channelId);
    public List<Channel> getChannel(Long parentId);
}
