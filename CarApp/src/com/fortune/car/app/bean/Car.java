package com.fortune.car.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xjliu on 2015/9/3.
 *
 */
public class Car  implements Parcelable {
    private int id;
    private String carNo;
    private List<CarDisplayItem> values=new ArrayList<CarDisplayItem>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public List<CarDisplayItem> getValues() {
        return values;
    }

    public void setValues(List<CarDisplayItem> values) {
        this.values = values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dst, int flag) {
        dst.writeInt(id);
        dst.writeString(carNo);
        dst.writeList(values);
    }
    public static final Parcelable.Creator<Car> CREATOR = new Parcelable.Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            Car result = new Car();
            result.id=in.readInt();
            result.carNo = in.readString();
            in.readList(result.values,Car.class.getClassLoader());
            return result;
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

}
