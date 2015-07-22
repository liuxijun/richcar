package com.fortune.rms.business.module.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.module.dao.daoInterface.PropertyDaoInterface;
import com.fortune.rms.business.module.logic.logicInterface.ModulePropertyLogicInterface;
import com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface;
import com.fortune.rms.business.module.logic.logicInterface.PropertySelectLogicInterface;
import com.fortune.rms.business.module.model.ModuleProperty;
import com.fortune.rms.business.module.model.Property;
import com.fortune.rms.business.module.model.PropertySelect;
import com.fortune.util.CacheUtils;
import com.fortune.util.DataInitWorker;
import com.fortune.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service("propertyLogicInterface")
public class PropertyLogicImpl extends BaseLogicImpl<Property>
		implements
			PropertyLogicInterface {
	private PropertyDaoInterface propertyDaoInterface;
    private PropertySelectLogicInterface propertySelectLogicInterface;
    private ModulePropertyLogicInterface modulePropertyLogicInterface;

	/**
	 * @param propertyDaoInterface the propertyDaoInterface to set
	 */
    @Autowired
	public void setPropertyDaoInterface(
			PropertyDaoInterface propertyDaoInterface) {
		this.propertyDaoInterface = propertyDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.propertyDaoInterface;
	}
    @Autowired
    public void setPropertySelectLogicInterface(PropertySelectLogicInterface propertySelectLogicInterface) {
        this.propertySelectLogicInterface = propertySelectLogicInterface;
    }

    @Autowired
    public void setModulePropertyLogicInterface(ModulePropertyLogicInterface modulePropertyLogicInterface) {
        this.modulePropertyLogicInterface = modulePropertyLogicInterface;
    }

    public Property getByCode(String code) {
        Property p = new Property();
        p.setCode(code);
        List<Property> resultList = search(p,false);
        if(resultList!=null&&resultList.size()>0){
            return resultList.get(0);
        }
        return null;
    }

    public String getSelectId(Long propertyId, String value) {
        PropertySelect bean = new PropertySelect();
        bean.setPropertyId(propertyId);
        bean.setName(value);
        List<PropertySelect> tempList = propertySelectLogicInterface.search(bean);
        if(tempList!=null&&tempList.size()>0){
           return ""+tempList.get(0).getId();
        }
        return null;
    }


    public Property getPropertyByCache(long propertyId) {
        Property property = (Property) CacheUtils.get(propertyId, "propertyCache", new DataInitWorker() {
            public Object init(Object objKey, String cacheName) {
                if (objKey != null) {
                    return propertyDaoInterface.get((Long)objKey);
                }
                return null;
            }
        });
        return property;
    }

    public List<Property> getPropertyIdsByDataType(int WMVType,int AVIType,int MP4Type) {
        return propertyDaoInterface.getPropertyIdsByDataType(WMVType,AVIType,MP4Type);
    }

    public List<Property> getPropertiesOfModule(Long moduleId,Long status, Byte dataType, PageBean pageBean) {
        List<Property> properties = propertyDaoInterface.getPropertiesOfModule(moduleId,status, dataType, pageBean);
        if(pageBean.getOrderBy()!=null){//因为增加了一个中间表module_property，所以排序要在这个里面排
            List<Property> result = new ArrayList<Property>(properties.size());
            ModuleProperty moduleProperty = new ModuleProperty();
            moduleProperty.setModuleId(moduleId);
            //moduleProperty.setStatus(status);
            List<ModuleProperty> forOrder = modulePropertyLogicInterface.search(moduleProperty,new PageBean(0,Integer.MAX_VALUE,"o1.displayOrder","asc"));
            for(ModuleProperty order:forOrder){
                Long propertyId = order.getPropertyId();
                if(propertyId==null){
                    continue;
                }
                for(Property property:properties){
                    if(propertyId.equals(property.getId())){
                        result.add(property);
                        properties.remove(property);
                        break;
                    }
                }
            }
            properties = result;
        }
        return properties;
    }

    public List<Property> getPropertiesExcludeSomeProperties(Property property, String excludeProperties, PageBean pageBean) {
        return propertyDaoInterface.getPropertiesExcludeSomeProperties(property,excludeProperties,pageBean);
    }

    public Property getPropertyByModuleIdAndCode(Long moduleId,String code) {
        return propertyDaoInterface.getPropertyByModuleIdAndCode(moduleId,code);
    }

    public Property saveWithPropertySelects(Property obj, List<PropertySelect> propertySelects) {
        obj = save(obj);
        List<PropertySelect> oldData = propertySelectLogicInterface.getPropertySelectsOfProperty(obj.getId());
        if(propertySelects!=null&&propertySelects.size()>0){
            long index = 0;
            for(PropertySelect item :propertySelects){
                for(PropertySelect oldItem:oldData){
                   if(item.getId()==oldItem.getId()){
                       oldData.remove(oldItem);
                       break;
                   }
                }
                item.setPropertyId(obj.getId());
                item.setDisplayOrder(index);
                index++;
                propertySelectLogicInterface.save(item);
            }
        }
        for(PropertySelect oldItem:oldData){
            propertySelectLogicInterface.remove(oldItem);
        }
        return obj;
    }
}
