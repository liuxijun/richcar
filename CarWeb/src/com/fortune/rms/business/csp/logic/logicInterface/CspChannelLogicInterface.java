package com.fortune.rms.business.csp.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.csp.model.CspChannel;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.util.PageBean;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wang
 * Date: 13-1-29
 * Time: 下午3:04
 */
public interface CspChannelLogicInterface extends BaseLogicInterface<CspChannel> {

    public List<CspChannel> getCspChannelByCspId(long cspId, int type);
    public List<CspChannel> getCspChannelByCspId(long cspId, int type, int status);
    //保存绑定信息
    public boolean saveCspChannel(String chooseChannel, long cspId);
    //查询绑定的channel

    public boolean hasPrivilegeToChannel(Long cspId, Long channelId);
    public List<Channel> getParentToChannel(Long parentId);

}
