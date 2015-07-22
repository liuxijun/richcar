package com.fortune.rms.business.frontuser.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.frontuser.dao.daoInterface.OrganizationDaoInterface;
import com.fortune.rms.business.frontuser.model.Organization;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-27
 * Time: 15:32:00
 * 组织管理Dao实现
 */
@Repository
public class OrganizationDaoAccess extends BaseDaoAccess<Organization, Long>
		implements
        OrganizationDaoInterface {

    public OrganizationDaoAccess() {
        super(Organization.class);
    }

    
    public List<Organization> getChildrenOrganization(String parentId){
        String hqlStr;
        if( Long.parseLong(parentId) > 0){
            hqlStr ="from Organization where parentId="+parentId+" order by sequence";
        }else{
            hqlStr ="from Organization where parentId<0 order by sequence";
        }
//         String hqlStr="from Channel where parentId="+parentId;
        return this.getHibernateTemplate().find(hqlStr);
    }

    public List<Organization> getOrganes(int parentId){
        String hql = "from Organization where parentId ="+parentId;
                   return  this.getHibernateTemplate().find(hql);
    }
}
