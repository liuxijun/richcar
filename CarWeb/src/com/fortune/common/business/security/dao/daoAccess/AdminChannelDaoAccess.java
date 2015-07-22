package com.fortune.common.business.security.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.common.business.security.dao.daoInterface.AdminChannelDaoInterface;
import com.fortune.common.business.security.model.AdminChannel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-11-3
 * Time: 16:26:55
 * 发布管理员关联栏目Dao实现
 */
@Service("adminChannelDaoInterface")
public class AdminChannelDaoAccess  extends BaseDaoAccess<AdminChannel, Integer>
		implements
        AdminChannelDaoInterface {
    public AdminChannelDaoAccess() {
        super(AdminChannel.class);
    }

    @SuppressWarnings("unchecked")
     public List<AdminChannel> getListByAdmin(Long adminId){
         String hql = "from AdminChannel c where c.adminId=" + adminId;
         return getHibernateTemplate().find(hql);
     }

    public void removeByAdmin(Long adminId){
        String hql = "delete from AdminChannel c where c.adminId=" + adminId;
        executeUpdate(hql);
    }

}
