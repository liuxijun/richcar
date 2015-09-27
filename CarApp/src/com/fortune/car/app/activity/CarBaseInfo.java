package com.fortune.car.app.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.fortune.car.app.R;
import com.fortune.car.app.bean.Car;
import com.fortune.car.app.bean.CarDisplayItem;
import com.fortune.mobile.params.ComParams;
import com.fortune.mobile.view.MyImageView;
import java.util.List;

/**
 * Created by xjliu on 2015/8/30.
 *
 */
public class CarBaseInfo extends BaseActivity {
    private Car car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_info_baseinfo);
        car = getIntent().getParcelableExtra(ComParams.INTENT_CAR_BEAN);
        initViews();
    }

    public void initViews() {
        if (car != null) {
            List<CarDisplayItem> items = car.getValues();
            if (items != null) {
                LayoutInflater inflater = getLayoutInflater();
                LinearLayout baseInfoContainer = (LinearLayout) findViewById(R.id.ll_base_info_container);
                if (baseInfoContainer != null) {
                    baseInfoContainer.removeAllViews();
                    for (CarDisplayItem item : items) {
                        View itemView;
                        int rId = R.layout.cars_baseinfo_item;
                        String type = item.getType();
                        if ("image".equals(type)) {
                            rId = R.layout.cars_baseinfo_item_image;
                        }
                        itemView = inflater.inflate(rId, null);
                        if (itemView != null) {
                            setTextOf(itemView, R.id.tv_car_base_info_label, item.getFieldLabel() + ":");
                            if (rId == R.layout.cars_baseinfo_item_image) {
                                MyImageView pic = (MyImageView) itemView.findViewById(R.id.tv_car_base_info_value);
                                if (pic != null) {
                                    String picUrl = item.getValue();
                                    if (!picUrl.startsWith("http://")) {
                                        picUrl = ComParams.HTTP_BASE + picUrl;
                                    }
                                    pic.setImage(picUrl, null, true);
                                }
                            } else {
                                String value = item.getValue();
                                if ("date".equals(type)) {
                                    if (value.length() > 10) {
                                        value = value.substring(0, 10);
                                    }
                                }
                                setTextOf(itemView, R.id.tv_car_base_info_value, value);
                            }
                            baseInfoContainer.addView(itemView);
                        }
                    }
                }
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        return super.onKeyDown(keyCode, event);
    }

}