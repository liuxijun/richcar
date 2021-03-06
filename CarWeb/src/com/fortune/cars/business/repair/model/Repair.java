package com.fortune.cars.business.repair.model;
import com.fortune.common.business.base.model.BaseModel;

import java.util.Date;
import java.util.List;

/**
 * Repair generated by hbm2java
 */
public class Repair extends BaseModel implements java.io.Serializable {

	private Integer id;
	private String fileId;
	private Date createTime;
	private Date modifyTime;
	private String carNo;
	private String fault0;
	private String fault1;
	private String fault2;
	private String fault3;
	private String fault4;
	private String fault5;
	private String fault6;
	private String fault7;
	private String fault8;
	private String fault9;
	private String fault10;
	private String fault11;
	private String item0;
	private String item1;
	private String item2;
	private String item3;
	private String item4;
	private String item5;
	private String item6;
	private String item7;
	private String item8;
	private String item9;
	private String item10;
	private String item11;
	private Date inTime;
	private Date outTime;
	private String recepton;
	private String workers;
	private String qc;
	private Integer type;
	private Integer status;
    private List<Parts> parts;

	public Repair() {
	}

    public Repair(Integer id, String fileId, Date createTime, Date modifyTime, String carNo,
                  String fault0, String fault1, String fault2, String fault3, String fault4,
                  String fault5, String fault6, String fault7, String fault8, String fault9,
                  String fault10, String fault11, String item0, String item1, String item2,
                  String item3, String item4, String item5, String item6, String item7,
                  String item8, String item9, String item10, String item11, Date inTime,
                  Date outTime, String recepton, String workers, String qc, Integer type,
				  Integer status) {
        this.id = id;
        this.fileId = fileId;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.carNo = carNo;
        this.fault0 = fault0;
        this.fault1 = fault1;
        this.fault2 = fault2;
        this.fault3 = fault3;
        this.fault4 = fault4;
        this.fault5 = fault5;
        this.fault6 = fault6;
        this.fault7 = fault7;
        this.fault8 = fault8;
        this.fault9 = fault9;
        this.fault10 = fault10;
        this.fault11 = fault11;
        this.item0 = item0;
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
        this.item5 = item5;
        this.item6 = item6;
        this.item7 = item7;
        this.item8 = item8;
        this.item9 = item9;
        this.item10 = item10;
        this.item11 = item11;
        this.inTime = inTime;
        this.outTime = outTime;
        this.recepton = recepton;
        this.workers = workers;
        this.qc = qc;
		this.type = type;
        this.status = status;
    }

    public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getFileId() {
		return this.fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getCarNo() {
		return this.carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public String getFault0() {
		return this.fault0;
	}

	public void setFault0(String fault0) {
		this.fault0 = fault0;
	}
	public String getFault1() {
		return this.fault1;
	}

	public void setFault1(String fault1) {
		this.fault1 = fault1;
	}
	public String getFault2() {
		return this.fault2;
	}

	public void setFault2(String fault2) {
		this.fault2 = fault2;
	}
	public String getFault3() {
		return this.fault3;
	}

	public void setFault3(String fault3) {
		this.fault3 = fault3;
	}
	public String getFault4() {
		return this.fault4;
	}

	public void setFault4(String fault4) {
		this.fault4 = fault4;
	}
	public String getFault5() {
		return this.fault5;
	}

	public void setFault5(String fault5) {
		this.fault5 = fault5;
	}
	public String getFault6() {
		return this.fault6;
	}

	public void setFault6(String fault6) {
		this.fault6 = fault6;
	}
	public String getFault7() {
		return this.fault7;
	}

	public void setFault7(String fault7) {
		this.fault7 = fault7;
	}
	public String getFault8() {
		return this.fault8;
	}

	public void setFault8(String fault8) {
		this.fault8 = fault8;
	}
	public String getFault9() {
		return this.fault9;
	}

	public void setFault9(String fault9) {
		this.fault9 = fault9;
	}
	public String getFault10() {
		return this.fault10;
	}

	public void setFault10(String fault10) {
		this.fault10 = fault10;
	}
	public String getFault11() {
		return this.fault11;
	}

	public void setFault11(String fault11) {
		this.fault11 = fault11;
	}
	public String getItem0() {
		return this.item0;
	}

	public void setItem0(String item0) {
		this.item0 = item0;
	}
	public String getItem1() {
		return this.item1;
	}

	public void setItem1(String item1) {
		this.item1 = item1;
	}
	public String getItem2() {
		return this.item2;
	}

	public void setItem2(String item2) {
		this.item2 = item2;
	}
	public String getItem3() {
		return this.item3;
	}

	public void setItem3(String item3) {
		this.item3 = item3;
	}
	public String getItem4() {
		return this.item4;
	}

	public void setItem4(String item4) {
		this.item4 = item4;
	}
	public String getItem5() {
		return this.item5;
	}

	public void setItem5(String item5) {
		this.item5 = item5;
	}
	public String getItem6() {
		return this.item6;
	}

	public void setItem6(String item6) {
		this.item6 = item6;
	}
	public String getItem7() {
		return this.item7;
	}

	public void setItem7(String item7) {
		this.item7 = item7;
	}
	public String getItem8() {
		return this.item8;
	}

	public void setItem8(String item8) {
		this.item8 = item8;
	}
	public String getItem9() {
		return this.item9;
	}

	public void setItem9(String item9) {
		this.item9 = item9;
	}
	public String getItem10() {
		return this.item10;
	}

	public void setItem10(String item10) {
		this.item10 = item10;
	}
	public String getItem11() {
		return this.item11;
	}

	public void setItem11(String item11) {
		this.item11 = item11;
	}

	public Date getInTime() {
		return inTime;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	public String getRecepton() {
		return recepton;
	}

	public void setRecepton(String recepton) {
		this.recepton = recepton;
	}

	public String getWorkers() {
		return workers;
	}

	public void setWorkers(String workers) {
		this.workers = workers;
	}

	public String getQc() {
		return qc;
	}

	public void setQc(String qc) {
		this.qc = qc;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @SuppressWarnings("JpaAttributeTypeInspection")
    public List<Parts> getParts() {
        return parts;
    }

    public void setParts(List<Parts> parts) {
        this.parts = parts;
    }


}
