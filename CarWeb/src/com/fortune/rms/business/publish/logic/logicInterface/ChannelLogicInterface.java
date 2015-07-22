package com.fortune.rms.business.publish.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.publish.model.ChannelDTO;
import com.fortune.util.PageBean;

import java.util.List;

public interface ChannelLogicInterface extends BaseLogicInterface<Channel> {
    public static final Integer AUDIT_FLAG_DO_NOT_NEED_AUDIT = 1;//免审标志，1为免审，否则要审

    public List<Channel> getChildren(long parentId, List<Channel> channels);

    public List<Channel> getAllChildren(long parentId, List<Channel> channels);
    
    public String getAllChildIds(long parentId);
    public List<Channel> getAvailableChannelOfCsp(int type, int cspId);
    public List<Channel> getAvailableChannelOfCsp(int type, int cspId, int status);
    public List<Channel> getCspChannel(int cspId);
    public boolean hasPrivilegeToChannel(long cspId, long channelId);
    public boolean hasPrivilegeToChannelSX(long cspId, long channelId);
    public boolean isLeafChannel(Long channelId);
    //根据父节点查询2级节点
    public List<Channel> getChannelList(Long parentId);
    public List getSonChannelId(Long channelId);
    public List<Channel> getChannelList(Long parentId, List visibleChannelIdList); // added by mlwang @2014-11-10，增加候选列表

    public String saveChannels(List<Channel> channels, long cspId);

    // added by mlwang, @2014-11-3，获取栏目是否需要审核
    public boolean channelNeedAudit(String channels);
    public ChannelDTO getChannelDTO(Long channelId, long level, List channelList);
    Long getSystemChannelId();
    // end of added
}
