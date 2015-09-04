package com.fortune.car.app.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.fortune.car.app.R;
import com.fortune.car.app.bean.Car;
import com.fortune.mobile.params.ComParams;
import com.fortune.util.HttpException;
import com.fortune.util.User;
import com.fortune.util.net.HttpUtils;
import com.fortune.util.net.http.RequestCallBack;
import com.fortune.util.net.http.ResponseInfo;
import com.fortune.util.net.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xjliu on 2015/8/30.
 *
 */
public class CarInfo extends BaseActivity {
    private long carId=-1;
    private HttpUtils handler = null;
    private Car car;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cars);
        //loadCarInfo();
    }

    private void initViews(){
    }

    protected View.OnClickListener clickOnCar = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            Log.d(TAG,"点击了按钮"+view.getTag());
        }
    };
    public void loadCarInfo(){
        String url = ComParams.HTTP_LIST_CAR+"phone="+ User.getPhone(this)+"&token="+User.getToken(this)+"&carId="+carId;
        handler = new HttpUtils();
        handler.configUserAgent(ComParams.userAgent);
        handler.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                progDialog.dismiss();
            }

            @Override
            public void onStart() {
                Log.d(TAG, "启动WEB请求，要求车辆信息");
                progDialog.show();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                progDialog.dismiss();
            }
        });
    }
}
