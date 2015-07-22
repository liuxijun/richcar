package com.fortune.common.business.security.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.common.business.security.dao.daoInterface.PermissionDaoInterface;
import com.fortune.common.business.security.model.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class PermissionDaoAccess extends BaseDaoAccess<Permission, Integer>
		implements
			PermissionDaoInterface {

	public PermissionDaoAccess() {
		super(Permission.class);
	}

    @SuppressWarnings("unchecked")
    public List<Permission> getPermissionOfOperator(Integer operatorId,Integer cspId) {
        String hqlStr = "from Permission";
        //if(operatorId!=null && operatorId>1){
        if(operatorId!=null && operatorId>0){ //root   Ҳ��Ȩ�� zbxue
            hqlStr = "from Permission p where p.permissionId in (select rp.permissionid from RolePermission rp " +
                    "where rp.roleid in(select opr.roleId from AdminRole opr where opr.adminId=" +
                operatorId;
            if(cspId!=null&&cspId>=0){
                hqlStr += " and opr.cspId="+cspId;
            }
            hqlStr+="))";
        }else{
            //�����root������Ϊ������������ֻ����ϵͳ��ɫ�µ�
        }
        return this.getHibernateTemplate().find(hqlStr);
    }
}
