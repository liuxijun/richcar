package com.fortune.rms.business.module.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.module.model.PropertySelect;

import java.util.List;

public interface PropertySelectLogicInterface
		extends
			BaseLogicInterface<PropertySelect> {
    //根据影片类型找到对应的id
    public PropertySelect getPropertyId(Long propertyId, String moveType);
    public List<PropertySelect> getPropertySelectsOfModule(Long moduleId);
    public List<PropertySelect> getPropertySelectsOfProperty(Long propertyId);
    public List<PropertySelect> searchMoveType(String movePropertySelect);
    public PropertySelect getPropertySelectsByCode(String code);
}
