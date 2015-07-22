package com.fortune.common.business.security.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.security.model.AdminChannel;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-11-3
 * Time: 16:25:28
 * 发布管理员关联的栏目Dao Interface
 */
public interface AdminChannelDaoInterface  extends BaseDaoInterface<AdminChannel, Integer> {
    public List<AdminChannel> getListByAdmin(Long adminId);
    public void removeByAdmin(Long adminId);

}
