package com.fortune.rms.business.csp.model;

import com.fortune.rms.business.module.model.Module;
import com.fortune.rms.business.product.model.Product;
import com.fortune.rms.business.system.model.Device;

import java.util.List;

/**
 * Csp generated by hbm2java
 */
public class Csp implements java.io.Serializable {

	private long id;
	private String name;
	private String address;
	private String phone;
	private String email;
	private Long status;
	private Long isCp;
	private Long isSp;
	private Long isCpOnlineAudit;
	private Long isCpOfflineAudit;
	private Long isSpOnlineAudit;
	private Long isSpOfflineAudit;
    private List<Device> devices;
    private List<Csp> csps;
    private List<Product> products;
    private List<Module> modules;
    private boolean selected;
    private Long defaultModuleId;
    private String alias;
    private String spId;
    //�ж��Ƿ��Ǵ���� 
    private Long type;


    public Long getDefaultModuleId() {
        return defaultModuleId;
    }

    public void setDefaultModuleId(Long defaultModuleId) {
        this.defaultModuleId = defaultModuleId;
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection", "JpaAttributeTypeInspection"})

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
    @SuppressWarnings({"JpaAttributeMemberSignatureInspection", "JpaAttributeTypeInspection"})

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection", "JpaAttributeTypeInspection"})
       public List<Csp> getCsps() {
        return csps;
    }

    public void setCsps(List<Csp> csps) {
        this.csps = csps;
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection", "JpaAttributeTypeInspection"})
    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public Csp() {
	}

	public Csp(long id) {
		this.id = id;
	}

    public Csp(long id, String name, String address, String phone, String email, Long status, Long cp, Long sp, Long cpOnlineAudit, Long cpOfflineAudit, Long spOnlineAudit, Long spOfflineAudit, String alias, String spId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.status = status;
        isCp = cp;
        isSp = sp;
        isCpOnlineAudit = cpOnlineAudit;
        isCpOfflineAudit = cpOfflineAudit;
        isSpOnlineAudit = spOnlineAudit;
        isSpOfflineAudit = spOfflineAudit;
        this.alias = alias;
        this.spId = spId;
    }

    public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
	public Long getIsCp() {
		return this.isCp;
	}

	public void setIsCp(Long isCp) {
		this.isCp = isCp;
	}
	public Long getIsSp() {
		return this.isSp;
	}

	public void setIsSp(Long isSp) {
		this.isSp = isSp;
	}
	public Long getIsCpOnlineAudit() {
		return this.isCpOnlineAudit;
	}

	public void setIsCpOnlineAudit(Long isCpOnlineAudit) {
		this.isCpOnlineAudit = isCpOnlineAudit;
	}
	public Long getIsCpOfflineAudit() {
		return this.isCpOfflineAudit;
	}

	public void setIsCpOfflineAudit(Long isCpOfflineAudit) {
		this.isCpOfflineAudit = isCpOfflineAudit;
	}
	public Long getIsSpOnlineAudit() {
		return this.isSpOnlineAudit;
	}

	public void setIsSpOnlineAudit(Long isSpOnlineAudit) {
		this.isSpOnlineAudit = isSpOnlineAudit;
	}
	public Long getIsSpOfflineAudit() {
		return this.isSpOfflineAudit;
	}

	public void setIsSpOfflineAudit(Long isSpOfflineAudit) {
		this.isSpOfflineAudit = isSpOfflineAudit;
	}

        public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    /**
	 * toString return json format string of this bean
	 * @return String
	 */
	public String toString() {
		return com.fortune.util.JsonUtils.getJsonString(this);
	}

	@SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
	public String getSimpleJson() {
		return com.fortune.util.JsonUtils.getJsonString(this, null);
	}

	@SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
	public String getObjJson() {
		return com.fortune.util.JsonUtils.getJsonString(this, "obj.");
	}


}