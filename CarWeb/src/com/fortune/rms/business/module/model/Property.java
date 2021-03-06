package com.fortune.rms.business.module.model;

/**
 * Property generated by hbm2java
 */
public class Property implements java.io.Serializable {

	private long id;
	private String code;
	private String name;
	private Byte dataType;
	private Byte isMultiLine;
	private Byte isMerge;
	private Long maxSize;
	private Byte isNull;
	private Byte isMain;
	private String columnName;
    private Byte relatedTable;
	private Long status;
	private String desp;
	private Long moduleId;
	private Long displayOrder;
    private Long colSpan;
    private Long rowSpan;

	public Property() {
	}

	public Property(long id) {
		this.id = id;
	}

    public Property(long id, String code, String name, Byte dataType, Byte multiLine,
                    Byte merge, Long maxSize, Byte aNull, Byte main, String columnName,
                    Byte relatedTable, Long status, String desp, Long moduleId,
                    Long displayOrder, Long colSpan, Long rowSpan) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.dataType = dataType;
        isMultiLine = multiLine;
        isMerge = merge;
        this.maxSize = maxSize;
        isNull = aNull;
        isMain = main;
        this.columnName = columnName;
        this.relatedTable = relatedTable;
        this.status = status;
        this.desp = desp;
        this.moduleId = moduleId;
        this.displayOrder = displayOrder;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
    }

    public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Byte getDataType() {
		return this.dataType;
	}

	public void setDataType(Byte dataType) {
		this.dataType = dataType;
	}
	public Byte getIsMultiLine() {
		return this.isMultiLine;
	}

	public void setIsMultiLine(Byte isMultiLine) {
		this.isMultiLine = isMultiLine;
	}
	public Byte getIsMerge() {
		return this.isMerge;
	}

	public void setIsMerge(Byte isMerge) {
		this.isMerge = isMerge;
	}
	public Long getMaxSize() {
		return this.maxSize;
	}

	public void setMaxSize(Long maxSize) {
		this.maxSize = maxSize;
	}
	public Byte getIsNull() {
		return this.isNull;
	}

	public void setIsNull(Byte isNull) {
		this.isNull = isNull;
	}
	public Byte getIsMain() {
		return this.isMain;
	}

	public void setIsMain(Byte isMain) {
		this.isMain = isMain;
	}
	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

    public Byte getRelatedTable() {
        return relatedTable;
    }

    public void setRelatedTable(Byte relatedTable) {
        this.relatedTable = relatedTable;
    }

    public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
	public String getDesp() {
		return this.desp;
	}

	public void setDesp(String desp) {
		this.desp = desp;
	}
	public Long getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
	public Long getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}

    public Long getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(Long rowSpan) {
        this.rowSpan = rowSpan;
    }

    public Long getColSpan() {
        return colSpan;
    }

    public void setColSpan(Long colSpan) {
        this.colSpan = colSpan;
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
