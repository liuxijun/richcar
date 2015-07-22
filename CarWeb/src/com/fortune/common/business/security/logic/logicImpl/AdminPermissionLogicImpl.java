package com.fortune.common.business.security.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.security.dao.daoInterface.AdminPermissionDaoInterface;
import com.fortune.common.business.security.logic.logicInterface.AdminPermissionLogicInterface;
import com.fortune.common.business.security.model.AdminPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("adminPermissionLogicInterface")
public class AdminPermissionLogicImpl
		extends
			BaseLogicImpl<AdminPermission>
		implements
        AdminPermissionLogicInterface {

    private AdminPermissionDaoInterface adminPermissionDaoInterface;


    /**
	 * @param adminPermissionDaoInterface the adminPermissionDaoInterface to set
	 */
    @Autowired
	public void setOperatorPermissionDaoInterface(
			AdminPermissionDaoInterface adminPermissionDaoInterface) {
		this.adminPermissionDaoInterface = adminPermissionDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.adminPermissionDaoInterface;
	}

	public void removeByOperatorId(Long opid) {
		AdminPermission object = new AdminPermission();
		object.setAdminId(opid);
		try {
			adminPermissionDaoInterface.removeByObject(object);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
