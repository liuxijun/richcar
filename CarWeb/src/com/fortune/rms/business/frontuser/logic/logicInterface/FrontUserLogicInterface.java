package com.fortune.rms.business.frontuser.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.frontuser.model.FrontUser;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.util.PageBean;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-29
 * Time: 13:15:48
 * ǰ̨�û�Logic
 */
public interface FrontUserLogicInterface  extends BaseLogicInterface<FrontUser> {
    public List<FrontUser> searchUsers(Long orgId, String searchValue, PageBean pageBean);
    public List<FrontUser> searchUnAuditUsers(String searchValue, PageBean pageBean);
    public void updateUserStatus(String userId, Long status);
    public List<Long> getUserAuthorizedChannel(FrontUser user);
    public List<Channel> getTopLevelChannel(List channelIdList);
    public List<Channel> getAllTopLevelChannel();

    public boolean checkLoginName(FrontUser frontUser);
    public boolean isUserExists(String userId);

    // added by mlwang @2015-4-8����ȡ����������û�����
    public int getTotalUserCount();
    public int getUnAuditUserCount();
    //=====================================================

    //�Ӱ�start
    public List<Map<String,Object>> getIndexList(String channelIds, PageBean pageBean);
    public List<Map<String,Object>> getColumnList(long channelId, PageBean pageBean);
    //�Ӱ�end
}
