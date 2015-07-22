package com.fortune.rms.business.module.model;

import com.fortune.common.business.base.model.BaseModel;

/**
 * Created by xjliu on 14-9-25.
 * 模版和属性的对应关系表
 */
public class ModuleProperty extends BaseModel {
    private long id;
    private Long propertyId;
    private Long moduleId;
    private Long displayOrder;
    private Long status;

    public ModuleProperty() {
    }

    public ModuleProperty(long id, Long propertyId, Long moduleId, Long displayOrder, Long status) {
        this.id = id;
        this.propertyId = propertyId;
        this.moduleId = moduleId;
        this.displayOrder = displayOrder;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Long displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}
