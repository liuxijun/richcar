package com.fortune.rms.business.module.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.module.model.Property;
import com.fortune.rms.business.module.model.PropertySelect;
import com.fortune.util.PageBean;

import java.util.List;

public interface PropertyLogicInterface extends BaseLogicInterface<Property> {
    public static final Byte DATA_TYPE_TEXT=1;
    public static final Byte DATA_TYPE_TEXTAREA=2;
    public static final Byte DATA_TYPE_TEXT_NUM=3;
    public static final Byte DATA_TYPE_DATE = 4;
    public static final Byte DATA_TYPE_COMBO = 5;
    public static final Byte DATA_TYPE_RADIO = 6;
    public static final Byte DATA_TYPE_CHECKBOX = 7;
    public static final Byte DATA_TYPE_WMV=8;
    public static final Byte DATA_TYPE_FLV=9;
    public static final Byte DATA_TYPE_MP4=10;
    public static final Byte DATA_TYPE_FILE_PIC=11;
    public static final Byte DATA_TYPE_FILE_HTML=12;
    public static final Byte DATA_TYPE_FILE_ZIP=13;
    public static final Long STATUS_ON = 1L;
    public static final Long STATUS_OFF = 99L;


    public Property getByCode(String code);
    public String getSelectId(Long propertyId, String value);
    public Property getPropertyByCache(long propertyId);
    public List<Property> getPropertyIdsByDataType(int WMVType, int AVIType, int MP4Type);
    public List<Property> getPropertiesOfModule(Long moduleId, Long status, Byte dataType, PageBean pageBean);
    public List<Property> getPropertiesExcludeSomeProperties(Property property, String excludeProperties, PageBean pageBean);
    public Property getPropertyByModuleIdAndCode(Long moduleId, String code);

    Property saveWithPropertySelects(Property obj, List<PropertySelect> propertySelects);
}
