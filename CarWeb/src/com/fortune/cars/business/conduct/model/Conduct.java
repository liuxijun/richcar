package com.fortune.cars.business.conduct.model;
import com.fortune.common.business.base.model.BaseModel;

import java.util.Date;
import java.util.List;

/**
 * Conduct generated by hbm2java
 */
public class Conduct extends BaseModel implements java.io.Serializable {

	private Integer id;
	private int carId;
	private String title;
	private Date createTime;
	private Integer miles ;
	private Integer status;
	private List<ConductItem> items;
	public Conduct() {
	}

	public Conduct(int carId) {
		this.carId = carId;
	}
	public Conduct(int carId, String title, Date createTime, Integer status) {
		this.carId = carId;
		this.title = title;
		this.createTime = createTime;
		this.status = status;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public int getCarId() {
		return this.carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getMiles() {
		return miles;
	}

	public void setMiles(Integer miles) {
		this.miles = miles;
	}

	@SuppressWarnings("JpaAttributeTypeInspection")
	public List<ConductItem> getItems() {
		return items;
	}

	public void setItems(List<ConductItem> items) {
		this.items = items;
	}
}
