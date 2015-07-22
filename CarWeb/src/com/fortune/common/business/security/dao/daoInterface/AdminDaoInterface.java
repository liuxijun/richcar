package com.fortune.common.business.security.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.security.model.Admin;

import java.util.List;

public interface AdminDaoInterface extends BaseDaoInterface<Admin, Integer> {

    //返回登陆管理员所有的能使用的Action方法名
    public List getAllTargetByLogin(String login);

   

}