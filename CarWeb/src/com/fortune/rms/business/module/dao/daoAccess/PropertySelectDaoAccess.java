package com.fortune.rms.business.module.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.module.dao.daoInterface.PropertySelectDaoInterface;
import com.fortune.rms.business.module.model.PropertySelect;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PropertySelectDaoAccess
		extends
			BaseDaoAccess<PropertySelect, Long>
		implements
			PropertySelectDaoInterface {

	public PropertySelectDaoAccess() {
		super(PropertySelect.class);
	}
    //根据xml配置文件的视频类型查找对应的Proper_id
    @SuppressWarnings("unchecked")
    public PropertySelect getPropertyId(Long propertyId,String moveType){
        String hqlStr = "from PropertySelect ps where propertyId="+propertyId+" and ps.name='"+ moveType + "'";
        List<PropertySelect> property = this.getHibernateTemplate().find(hqlStr);
        if (property != null && property.size() ==1) {
            return property.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<PropertySelect> getPropertySelectsOfModule(Long moduleId) {
        String hql = "from PropertySelect";
        if(moduleId!=null&&moduleId>0){
            hql += " ps where ps.propertyId in (select mp.propertyId from ModuleProperty mp where mp.moduleId="+moduleId+")";
        }
        return this.getHibernateTemplate().find(hql);
    }


    public PropertySelect getPropertySelectsByCode(String code) {
        String hql = "from PropertySelect ps where ps.code = '"+code+"'";
        List<PropertySelect> property = this.getHibernateTemplate().find(hql);
        if (property != null && property.size() ==1) {
            return property.get(0);
        }
        return null;
    }
}
