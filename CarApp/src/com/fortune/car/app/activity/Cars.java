package com.fortune.car.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.fortune.car.app.Caller;
import com.fortune.car.app.R;
import com.fortune.car.app.bean.Car;
import com.fortune.mobile.params.ComParams;
import com.fortune.mobile.view.ProgressDialog;
import com.fortune.util.ACache;
import com.fortune.util.HttpException;
import com.fortune.util.JsonUtils;
import com.fortune.util.User;
import com.fortune.util.net.HttpUtils;
import com.fortune.util.net.http.RequestCallBack;
import com.fortune.util.net.http.ResponseInfo;
import com.fortune.util.net.http.client.HttpRequest;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xjliu on 2015/8/30.
 *
 */
public class Cars extends BaseActivity {
    private ArrayList<Car> cars;
    public static final int RESULT_CODE_SUCCESS=1000;
    public static final int RESULT_CODE_FAILED=1001;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        cars =(ArrayList<Car>) getIntent().getSerializableExtra(ComParams.INTENT_CAR_LIST);
        if(cars==null){
            cars = new ArrayList<Car>();
            Car car = new Car();
            car.setCarNo("沪F08825-1");
            car.setId(1);
            cars.add(car);
            Car car2 = new Car();
            car2.setCarNo("沪F08825-2");
            car2.setId(2);
            cars.add(car2);
        }
        setContentView(R.layout.cars);
        initViews();
        //loadCarInfo();
    }

    public void initViews(){
        super.initViews();
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ll_cars_list_contain);
        linearLayout.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        for(Car car:cars){
            View carTitleView = inflater.inflate(R.layout.car_info_car_list_item,null);
            if(carTitleView!=null){
                Button carTitle = (Button)carTitleView.findViewById(R.id.car_list_item_title);
                carTitle.setText(car.getCarNo());
                carTitle.setTag(car);
                carTitle.setOnClickListener(clickOnCar);
                linearLayout.addView(carTitleView);
            }
        }
    }

    protected View.OnClickListener clickOnCar = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Object tag = view.getTag();
            if(tag instanceof Car){
                Car car = (Car) tag;
                Log.d(TAG,"点击了车辆准备展示详细信息："+car.getCarNo()+",id="+car.getId());
                Intent intent = new Intent(Cars.this,CarInfoMenu.class);
                intent.putExtra(ComParams.INTENT_CAR_BEAN, car);
                startActivity(intent);
            }else{
                Log.d(TAG,"点击了按钮"+tag);
            }
        }
    };
    public static List<Car> parseCars(String json){
        try {
            CarsBean result = JsonUtils.fromJsonString(CarsBean.class,json);
            if(result!=null){
                return result.getCars();
            }
            return null;
        } catch (JsonSyntaxException e) {
            Log.e(Cars.class.getSimpleName(),"解析Json有问题："+e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }
    public static void loadCarInfo(final Caller caller){
        String url = ComParams.HTTP_LIST_CAR+"phone="+ User.getUserId(caller.getContext())
                +"&token="+User.getToken(caller.getContext());
        String cacheResult = ACache.get(caller.getContext()).getAsString(ComParams.INTENT_CAR_LIST);
        if(cacheResult!=null){
            caller.onDataLoaded(RESULT_CODE_SUCCESS, parseCars(cacheResult));
            return;
        }
        HttpUtils handler = new HttpUtils();
        handler.configUserAgent(ComParams.userAgent);
        final ProgressDialog progDialog = caller.getProgDialog();

        handler.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                progDialog.dismiss();
                String result = responseInfo.result;
                Log.d(getClass().getSimpleName(), "服务器返回：" + result);
                ACache.get(caller.getContext())
                        .put(ComParams.INTENT_CAR_BEAN, result, 60 * 5);
                caller.onDataLoaded(RESULT_CODE_SUCCESS, parseCars(result));
            }
            @Override
            public void onStart() {
                Log.d(caller.getClass().getSimpleName(), "启动WEB请求，要求车辆信息");
                progDialog.show();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                caller.onDataLoaded(RESULT_CODE_SUCCESS, "无法获取车辆信息：" + msg);
                progDialog.dismiss();
            }
        });
    }
    public class CarsBean {
        private List<Car> cars;
        private boolean success;

        public List<Car> getCars() {
            return cars;
        }

        public void setCars(List<Car> cars) {
            this.cars = cars;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
