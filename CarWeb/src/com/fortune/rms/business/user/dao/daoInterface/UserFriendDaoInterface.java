package com.fortune.rms.business.user.dao.daoInterface;

import com.fortune.rms.business.user.model.AllResp;

/**
 * Created with IntelliJ IDEA.
 * User: liupeng
 * Date: 12-7-19
 * Time: обнГ3:58
 */
public interface UserFriendDaoInterface {
    public AllResp getAllFriendFromDb(String userName, Integer userType);
    public AllResp getAllFriendFromHttp(String userId, Integer userType);
}
