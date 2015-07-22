package com.fortune.rms.business.module.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.module.model.Property;
import com.fortune.util.PageBean;

import java.util.List;

public interface PropertyDaoInterface extends BaseDaoInterface<Property, Long> {
    public List<Property> getPropertyIdsByDataType(int WMVType, int AVIType, int MP4Type);
    public List<Property> getPropertiesOfModule(Long moduleId, Long status, Byte dataType, PageBean pageBean);
    public Property getPropertyByModuleIdAndCode(Long moduleId, String code);
    public List<Property> getPropertiesExcludeSomeProperties(Property property, String excludeProperties, PageBean pageBean);
}