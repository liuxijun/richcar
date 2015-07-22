package com.fortune.rms.business.user.model;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-27
 * Time: 9:49:04
 * 用户类型及人数
 */
public class UserTypeDetail {
    private UserType userType;  // 用户类型
    private Long userCount;     // 该用户类型的人数

    public UserTypeDetail(UserType userType) {
        this.userType = userType;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Long getUserCount() {
        return userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }
}
