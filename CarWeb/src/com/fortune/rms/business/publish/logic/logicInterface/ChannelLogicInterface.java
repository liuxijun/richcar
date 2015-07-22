package com.fortune.rms.business.publish.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.publish.model.ChannelDTO;
import com.fortune.util.PageBean;

import java.util.List;

public interface ChannelLogicInterface extends BaseLogicInterface<Channel> {
    public static final Integer AUDIT_FLAG_DO_NOT_NEED_AUDIT = 1;//�����־��1Ϊ���󣬷���Ҫ��

    public List<Channel> getChildren(long parentId, List<Channel> channels);

    public List<Channel> getAllChildren(long parentId, List<Channel> channels);
    
    public String getAllChildIds(long parentId);
    public List<Channel> getAvailableChannelOfCsp(int type, int cspId);
    public List<Channel> getAvailableChannelOfCsp(int type, int cspId, int status);
    public List<Channel> getCspChannel(int cspId);
    public boolean hasPrivilegeToChannel(long cspId, long channelId);
    public boolean hasPrivilegeToChannelSX(long cspId, long channelId);
    public boolean isLeafChannel(Long channelId);
    //���ݸ��ڵ��ѯ2���ڵ�
    public List<Channel> getChannelList(Long parentId);
    public List getSonChannelId(Long channelId);
    public List<Channel> getChannelList(Long parentId, List visibleChannelIdList); // added by mlwang @2014-11-10�����Ӻ�ѡ�б�

    public String saveChannels(List<Channel> channels, long cspId);

    // added by mlwang, @2014-11-3����ȡ��Ŀ�Ƿ���Ҫ���
    public boolean channelNeedAudit(String channels);
    public ChannelDTO getChannelDTO(Long channelId, long level, List channelList);
    Long getSystemChannelId();
    // end of added
}
