package com.fortune.rms.business.module.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.module.dao.daoInterface.PropertySelectDaoInterface;
import com.fortune.rms.business.module.logic.logicInterface.PropertySelectLogicInterface;
import com.fortune.rms.business.module.model.PropertySelect;
import com.fortune.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("propertySelectLogicInterface")
public class PropertySelectLogicImpl extends BaseLogicImpl<PropertySelect>
		implements
			PropertySelectLogicInterface {
	private PropertySelectDaoInterface propertySelectDaoInterface;

	/**
	 * @param propertySelectDaoInterface the propertySelectDaoInterface to set
	 */
    @Autowired
	public void setPropertySelectDaoInterface(
			PropertySelectDaoInterface propertySelectDaoInterface) {
		this.propertySelectDaoInterface = propertySelectDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.propertySelectDaoInterface;
	}
    public PropertySelect getPropertyId(Long propertyId,String moveType){
        return propertySelectDaoInterface.getPropertyId(propertyId,moveType);
    }

    public List<PropertySelect> getPropertySelectsOfModule(Long moduleId) {
        return propertySelectDaoInterface.getPropertySelectsOfModule(moduleId);
    }

    public List<PropertySelect> getPropertySelectsOfProperty(Long propertyId) {
        PropertySelect bean = new PropertySelect();
        if(propertyId==null||propertyId<=0){
            return new ArrayList<PropertySelect>(0);
        }
        bean.setPropertyId(propertyId);
        return search(bean,new PageBean(0,Integer.MAX_VALUE,"o1.displayOrder","asc"));
    }

    public List<PropertySelect> searchMoveType(String movePropertySelect){
        Long propertyId=Long.parseLong(movePropertySelect);
        return getPropertySelectsOfProperty(propertyId);
    }

    public PropertySelect getPropertySelectsByCode(String code) {
       return propertySelectDaoInterface.getPropertySelectsByCode(code);
    }
}
