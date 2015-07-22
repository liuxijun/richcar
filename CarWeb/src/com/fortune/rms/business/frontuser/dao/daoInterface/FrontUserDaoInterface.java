package com.fortune.rms.business.frontuser.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.frontuser.model.FrontUser;
import com.fortune.util.PageBean;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-29
 * Time: 13:17:54
 * 前台用户
 */
public interface FrontUserDaoInterface  extends BaseDaoInterface<FrontUser, String> {
    public List<FrontUser> searchUsers(Long orgId, String searchValue, PageBean pageBean);
    public List<FrontUser> searchUsers(Long orgId, String searchValue, PageBean pageBean, Long status);
    public void setUserStatus(String userId, Long status);
    public int getUserCountByType(Long typeId);
    public int getUserCountByOrg(Long orgId, boolean includeChildren);
    // added by mlwang, @2015-4-8，根据状态获取用户数量
    public int getUserCountByStatus(Long status);

    public boolean checkLoginName(FrontUser frontUser);
}
