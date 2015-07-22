package com.fortune.common.business.security.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.common.business.security.dao.daoInterface.RoleDaoInterface;
import com.fortune.common.business.security.model.Role;
import com.fortune.util.PageBean;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RoleDaoAccess extends BaseDaoAccess<Role, Long>
		implements
			RoleDaoInterface {

	public RoleDaoAccess() {
		super(Role.class);
	}
    public List<Map<String, Object>> formatData(List<Object> data){
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        for(Object d:data){
            Object[] row = (Object[])d;
            Map<String,Object> rec = new HashMap<String,Object>();
            rec.put("id",row[0]);
            rec.put("name",row[1]);
            rec.put("type",row[2]);
            rec.put("memo",row[3]);
            rec.put("count",row[4]);
            result.add(rec);
        }
        return result;
    }
    public List<Map<String, Object>> getAdminCountOfRoles(List<Role> roles) {
        String hql = "select a.id,a.name,a.type,a.memo,b.rc from role a ," +
                "(select count(*) rc ,role_id from admin_role ar group by role_id) b where a.id = b.role_id ";
        String ids = "";
        if(roles!=null&&roles.size()>0){
            for(Role role:roles){
                if(!ids.equals("")){
                    ids+=",";
                }
                ids+=role.getRoleid();
            }
        }
        if(!"".equals(ids)){
            hql+= " and a.id in ("+ids+")";
        }
        try {
            return formatData(sql(hql,null,new PageBean(0,Integer.MAX_VALUE,null,null)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String,Object>>(0);
    }

    public List<Map<String, Object>> listRoles(String name,PageBean pageBean) {
        String hql = "select a.id,a.name,a.type,a.memo,b.rc from role a ," +
                "(select count(*) rc ,role_id from admin_role ar group by role_id) b where a.id = b.role_id";
        List<Object> parameters = new ArrayList<Object>();
        if(name!=null&&!"".equals(name.trim())){
            parameters.add("%"+name+"%");
        }
        try {
            return formatData(sql(hql,parameters.toArray(),pageBean));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String,Object>>(0);
    }
}
