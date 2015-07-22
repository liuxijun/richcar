package com.fortune.common.business.security.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.common.business.security.dao.daoInterface.AdminRoleDaoInterface;
import com.fortune.common.business.security.logic.logicInterface.RoleLogicInterface;
import com.fortune.common.business.security.model.AdminRole;
import com.fortune.common.business.security.model.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class AdminRoleDaoAccess extends BaseDaoAccess<AdminRole, Long>
		implements
        AdminRoleDaoInterface {

	public AdminRoleDaoAccess() {
		super(AdminRole.class);
	}

    @SuppressWarnings("unchecked")
    public List<Role> getRolesOfOperator(int operatorId,int cspId){
        String hqlStr = "from Role r where 1=1";
        //����в���Ա��ID���������������Ա�Ľ�ɫ
        if(operatorId>0){
            hqlStr = "from Role r where r.roleid in(select or1.roleId from " +
                    "AdminRole or1 where or1.adminId="+operatorId;
            if(cspId>0){//�����csp��Ϣ�������Ͳ�����ص�csp
                hqlStr += " and or1.cspId=" +
                    cspId;
            }
            hqlStr +=")";
        }
        //���cspId����1����ֻ����csp������
        if(cspId>1){
            hqlStr +=" and r.type = "+ RoleLogicInterface.ROLE_TYPE_CSP;
        }else{
            hqlStr +=" and r.type="+RoleLogicInterface.ROLE_TYPE_SYSTEM;
        }
        return getHibernateTemplate().find(hqlStr);
    }
}
