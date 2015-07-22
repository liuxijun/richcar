package com.fortune.rms.business.module.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.module.dao.daoInterface.PropertyDaoInterface;
import com.fortune.rms.business.module.model.Property;
import com.fortune.util.PageBean;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PropertyDaoAccess extends BaseDaoAccess<Property, Long>
		implements
			PropertyDaoInterface {

	public PropertyDaoAccess() {
		super(Property.class);
	}

    @SuppressWarnings("unchecked")
    public List<Property> getPropertyIdsByDataType(int WMVType,int AVIType,int MP4Type){
        String hql = "from Property p where p.dataType in ("+ WMVType +","+ AVIType +","+MP4Type+")";
        return this.getHibernateTemplate().find(hql);
    }

    public List<Property> getPropertiesOfModule(Long moduleId,Long status, Byte dataType, PageBean pageBean) {
        String hql = "from Property o1 where o1.id in " +
                "(select o2.propertyId from ModuleProperty o2 where o2.moduleId="+moduleId;
        if(status!=null&&status>0){
            //²»ÔÙ¿¼ÂÇModulePropertyµÄstatus×´Ì¬
            hql+=") and o1.status="+status;
        }else{
            hql+=")";
        }
        if(dataType!=null&&dataType>0){
            hql +=" and o1.dataType="+dataType;
        }
        try {
            return getObjects(hql,pageBean);
        } catch (Exception e) {
            return new ArrayList<Property>(0);
        }
    }

    public Property getPropertyByModuleIdAndCode(Long moduleId,String code) {
        String hql = "from Property p where moduleId="+moduleId+" and  code='"+code+"'";
        List list = this.getHibernateTemplate().find(hql);
        return (list!=null && list.size()>0)?(Property)list.get(0):null;
    }

    public List<Property> getPropertiesExcludeSomeProperties(Property property, String excludeProperties, PageBean pageBean){
        String expandSql = null;
        if(excludeProperties!=null&&!"".equals(excludeProperties)){
            expandSql = "o1.id not in("+excludeProperties+")";
        }
        try {
            return getObjects(property,pageBean,true,expandSql);
        } catch (Exception e) {
            return new ArrayList<Property>(0);
        }
    }
    public void remove(Property property){
        if(property == null)return;
        logger.debug(removeRelatedData(new String[]{
                "CONTENT_PROPERTY",
                "PROPERTY_SELECT",
                "RELATED",
                "RELATED_PROPERTY",
                "ENCODER_TEMPLATE"},property.getName(),"PROPERTY_ID",property.getId()));
        super.remove(property);
    }
}
