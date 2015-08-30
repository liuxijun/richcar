package com.fortune.cars.business.cars.model;
import com.fortune.common.business.base.model.BaseModel;

import java.util.Date;

/**
 * Car generated by hbm2java
 */
public class Car extends BaseModel implements java.io.Serializable {

	private Integer id;
	private Date createTime;
	private Integer creator;
	private String userId;
	private String password;
	private String sn;
	private String carNo;
	private String product;
	private String productType;
	private String productHometown;
	private String salesCompany;
	private String vinCode;
	private String engineCode;
	private String enineType;
	private String gearbox;
	private String lengthWidthHeight;
	private String carColor;
	private String innerColor;
	private String gasType;
	private String emissionType;
	private Float emission;
	private String tyreType;
	private String carType;
	private Integer mileage;
	private Integer maintainTimes;
	private Date motStime;
	private Date motEtime;
	private Date productionDate;
	private Date insureStime;
	private Date insureEtime;
	private String insureType;
	private String insureCompany;
	private String carPictureTop;
	private String carPictureLeft;
	private String carPictureFront;
	private String carPictureBottom;
	private String carPictureRight;
	private String carPictureBack;
	private Integer status;

	public Car() {
	}

	public Car(Date createTime, Integer creator, String userId, String sn,
			String carNo, String product, String productType,
			String productHometown, String salesCompany, String vinCode,
			String engineCode, String enineType, String gearbox,
			String lengthWidthHeight, String carColor, String innerColor,
			String gasType, String emissionType, Float emission,
			String tyreType, String carType, Integer mileage,
			Integer maintainTimes, Date motStime, Date motEtime,
			Date productionDate, Date insureStime, Date insureEtime,String insureType,
			String insureCompany, String carPictureTop, String carPictureLeft,
			String carPictureFront, String carPictureBottom,
			String carPictureRight, String carPictureBack,Integer status) {
		this.createTime = createTime;
		this.creator = creator;
		this.userId = userId;
		this.sn = sn;
		this.carNo = carNo;
		this.product = product;
		this.productType = productType;
		this.productHometown = productHometown;
		this.salesCompany = salesCompany;
		this.vinCode = vinCode;
		this.engineCode = engineCode;
		this.enineType = enineType;
		this.gearbox = gearbox;
		this.lengthWidthHeight = lengthWidthHeight;
		this.carColor = carColor;
		this.innerColor = innerColor;
		this.gasType = gasType;
		this.emissionType = emissionType;
		this.emission = emission;
		this.tyreType = tyreType;
		this.carType = carType;
		this.mileage = mileage;
		this.maintainTimes = maintainTimes;
		this.motStime = motStime;
		this.motEtime = motEtime;
		this.productionDate = productionDate;
		this.insureStime = insureStime;
		this.insureEtime = insureEtime;
		this.insureType = insureType;
		this.insureCompany = insureCompany;
		this.carPictureTop = carPictureTop;
		this.carPictureLeft = carPictureLeft;
		this.carPictureFront = carPictureFront;
		this.carPictureBottom = carPictureBottom;
		this.carPictureRight = carPictureRight;
		this.carPictureBack = carPictureBack;
		this.status = status;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getCreator() {
		return this.creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSn() {
		return this.sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getCarNo() {
		return this.carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public String getProduct() {
		return this.product;
	}

	public void setProduct(String product) {
		this.product = product;
	}
	public String getProductType() {
		return this.productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getProductHometown() {
		return this.productHometown;
	}

	public void setProductHometown(String productHometown) {
		this.productHometown = productHometown;
	}
	public String getSalesCompany() {
		return this.salesCompany;
	}

	public void setSalesCompany(String salesCompany) {
		this.salesCompany = salesCompany;
	}
	public String getVinCode() {
		return this.vinCode;
	}

	public void setVinCode(String vinCode) {
		this.vinCode = vinCode;
	}
	public String getEngineCode() {
		return this.engineCode;
	}

	public void setEngineCode(String engineCode) {
		this.engineCode = engineCode;
	}
	public String getEnineType() {
		return this.enineType;
	}

	public void setEnineType(String enineType) {
		this.enineType = enineType;
	}
	public String getGearbox() {
		return this.gearbox;
	}

	public void setGearbox(String gearbox) {
		this.gearbox = gearbox;
	}
	public String getLengthWidthHeight() {
		return this.lengthWidthHeight;
	}

	public void setLengthWidthHeight(String lengthWidthHeight) {
		this.lengthWidthHeight = lengthWidthHeight;
	}
	public String getCarColor() {
		return this.carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}
	public String getInnerColor() {
		return this.innerColor;
	}

	public void setInnerColor(String innerColor) {
		this.innerColor = innerColor;
	}
	public String getGasType() {
		return this.gasType;
	}

	public void setGasType(String gasType) {
		this.gasType = gasType;
	}
	public String getEmissionType() {
		return this.emissionType;
	}

	public void setEmissionType(String emissionType) {
		this.emissionType = emissionType;
	}
	public Float getEmission() {
		return this.emission;
	}

	public void setEmission(Float emission) {
		this.emission = emission;
	}
	public String getTyreType() {
		return this.tyreType;
	}

	public void setTyreType(String tyreType) {
		this.tyreType = tyreType;
	}
	public String getCarType() {
		return this.carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}
	public Integer getMileage() {
		return this.mileage;
	}

	public void setMileage(Integer mileage) {
		this.mileage = mileage;
	}
	public Integer getMaintainTimes() {
		return this.maintainTimes;
	}

	public void setMaintainTimes(Integer maintainTimes) {
		this.maintainTimes = maintainTimes;
	}
	public Date getMotStime() {
		return this.motStime;
	}

	public void setMotStime(Date motStime) {
		this.motStime = motStime;
	}
	public Date getMotEtime() {
		return this.motEtime;
	}

	public void setMotEtime(Date motEtime) {
		this.motEtime = motEtime;
	}
	public Date getProductionDate() {
		return this.productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}
	public Date getInsureStime() {
		return this.insureStime;
	}

	public void setInsureStime(Date insureStime) {
		this.insureStime = insureStime;
	}
	public Date getInsureEtime() {
		return this.insureEtime;
	}

	public void setInsureEtime(Date insureEtime) {
		this.insureEtime = insureEtime;
	}
	public String getInsureCompany() {
		return this.insureCompany;
	}

	public void setInsureCompany(String insureCompany) {
		this.insureCompany = insureCompany;
	}
	public String getCarPictureTop() {
		return this.carPictureTop;
	}

	public void setCarPictureTop(String carPictureTop) {
		this.carPictureTop = carPictureTop;
	}
	public String getCarPictureLeft() {
		return this.carPictureLeft;
	}

	public void setCarPictureLeft(String carPictureLeft) {
		this.carPictureLeft = carPictureLeft;
	}
	public String getCarPictureFront() {
		return this.carPictureFront;
	}

	public void setCarPictureFront(String carPictureFront) {
		this.carPictureFront = carPictureFront;
	}
	public String getCarPictureBottom() {
		return this.carPictureBottom;
	}

	public void setCarPictureBottom(String carPictureBottom) {
		this.carPictureBottom = carPictureBottom;
	}
	public String getCarPictureRight() {
		return this.carPictureRight;
	}

	public void setCarPictureRight(String carPictureRight) {
		this.carPictureRight = carPictureRight;
	}
	public String getCarPictureBack() {
		return this.carPictureBack;
	}

	public void setCarPictureBack(String carPictureBack) {
		this.carPictureBack = carPictureBack;
	}

	public String getInsureType() {
		return insureType;
	}

	public void setInsureType(String insureType) {
		this.insureType = insureType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
