package com.fortune.car.app.bean;

/**
 * Created by xjliu on 2015/11/14.
 *
 */
public class Parts extends BaseModel implements java.io.Serializable {

    private Integer id;
    private Integer repairId;
    private String name;
    private String homeland;
    private String level;
    private Float price;
    private Integer priceDiscount;
    private Float manHour;
    private Integer manHourDiscount;
    private Integer status;

    public Parts() {
    }

    public Parts(Integer id,Integer repairId, String name, String homeland, String level,
                 Float price, Integer priceDiscount, Float manHour, Integer manHourDiscount,
                 Integer status) {
        this.id = id;
        this.repairId = repairId;
        this.name = name;
        this.homeland = homeland;
        this.level = level;
        this.price = price;
        this.priceDiscount = priceDiscount;
        this.manHour = manHour;
        this.manHourDiscount = manHourDiscount;
        this.status = status;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getHomeland() {
        return this.homeland;
    }

    public void setHomeland(String homeland) {
        this.homeland = homeland;
    }
    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    public Float getPrice() {
        return this.price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
    public Float getManHour() {
        return this.manHour;
    }

    public void setManHour(Float manHour) {
        this.manHour = manHour;
    }

    public Integer getPriceDiscount() {
        return priceDiscount;
    }

    public void setPriceDiscount(Integer priceDiscount) {
        this.priceDiscount = priceDiscount;
    }

    public Integer getManHourDiscount() {
        return manHourDiscount;
    }

    public void setManHourDiscount(Integer manHourDiscount) {
        this.manHourDiscount = manHourDiscount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRepairId() {
        return repairId;
    }

    public void setRepairId(Integer repairId) {
        this.repairId = repairId;
    }
}
