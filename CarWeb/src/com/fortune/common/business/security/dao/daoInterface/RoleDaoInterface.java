package com.fortune.common.business.security.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.security.model.Role;
import com.fortune.util.PageBean;

import java.util.List;
import java.util.Map;

public interface RoleDaoInterface extends BaseDaoInterface<Role, Long> {
    List<Map<String,Object>> getAdminCountOfRoles(List<Role> roles);
    List<Map<String,Object>> listRoles(String name, PageBean pageBean);
}