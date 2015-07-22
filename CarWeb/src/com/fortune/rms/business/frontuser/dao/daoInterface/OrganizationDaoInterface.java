package com.fortune.rms.business.frontuser.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.frontuser.model.Organization;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-27
 * Time: 15:30:29
 * 组织管理dao
 */
public interface OrganizationDaoInterface extends BaseDaoInterface<Organization, Long> {
    public List<Organization> getChildrenOrganization(String parentId);
    public List<Organization> getOrganes(int parentId);
}
