package com.fortune.rms.business.user.model;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-27
 * Time: 9:49:04
 * �û����ͼ�����
 */
public class UserTypeDetail {
    private UserType userType;  // �û�����
    private Long userCount;     // ���û����͵�����

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
