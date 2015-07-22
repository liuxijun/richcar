package com.fortune.rms.business.module.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.module.model.PropertySelect;

import java.util.List;

public interface PropertySelectDaoInterface
		extends
			BaseDaoInterface<PropertySelect, Long> {
         public PropertySelect getPropertyId(Long propertyId, String moveType);
      public List<PropertySelect> getPropertySelectsOfModule(Long moduleId);
    public PropertySelect getPropertySelectsByCode(String code);
}