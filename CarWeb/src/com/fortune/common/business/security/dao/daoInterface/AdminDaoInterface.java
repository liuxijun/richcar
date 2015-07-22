package com.fortune.common.business.security.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.security.model.Admin;

import java.util.List;

public interface AdminDaoInterface extends BaseDaoInterface<Admin, Integer> {

    //���ص�½����Ա���е���ʹ�õ�Action������
    public List getAllTargetByLogin(String login);

   

}