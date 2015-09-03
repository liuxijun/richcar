package com.fortune.car.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xjliu on 2015/9/3.
 * 显示车辆的基本信息的一个条目
 */
public class CarDisplayItem  implements Parcelable {
    private int id;
    private String fieldLabel;
    private String name;
    private String value;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dst, int flag) {
        dst.writeInt(id);
        dst.writeString(fieldLabel);
        dst.writeString(name);
        dst.writeString(value);
        dst.writeString(type);
    }

    public static final Parcelable.Creator<CarDisplayItem> CREATOR = new Parcelable.Creator<CarDisplayItem>() {
        @Override
        public CarDisplayItem createFromParcel(Parcel in) {
            CarDisplayItem result = new CarDisplayItem();
            result.id=in.readInt();
            result.fieldLabel = in.readString();
            result.name = in.readString();
            result.value = in.readString();
            result.type = in.readString();
            return result;
        }
        @Override
        public CarDisplayItem[] newArray(int size) {
            return new CarDisplayItem[size];
        }
    };

}
