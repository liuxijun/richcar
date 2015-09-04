package com.fortune.car.app.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fortune.car.app.R;
import com.fortune.car.app.bean.Car;
import com.fortune.car.app.bean.CarDisplayItem;
import com.fortune.mobile.params.ComParams;
import com.fortune.mobile.view.MyImageView;
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
    private TabBean[] tabs = new TabBean[]{
            new TabBean(R.id.tv_carInfo_baseInfo,R.id.ll_base_info_container,true),
            new TabBean(R.id.tv_carInfo_conducts,R.id.ll_conducts_container,false)
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_info);
        car = getIntent().getParcelableExtra(ComParams.INTENT_CAR_BEAN);
        initViews();
        for(TabBean tabBean:tabs){
            setClickHandler(null,tabBean.getTabMenuId(),clickOnTabMenu);
        }
    }

    private void initViews(){
        if(car!=null){
            List<CarDisplayItem> items = car.getValues();
            if(items!=null){
                LayoutInflater inflater = getLayoutInflater();
                LinearLayout baseInfoContainer = (LinearLayout) findViewById(R.id.ll_base_info_container);
                if(baseInfoContainer!=null){
                    baseInfoContainer.removeAllViews();
                    for(CarDisplayItem item:items){
                        View itemView;
                        int rId = R.layout.cars_baseinfo_item;
                        if("image".equals(item.getType())){
                            rId = R.layout.cars_baseinfo_item_image;
                        }
                        itemView = inflater.inflate(rId,null);
                        if(itemView!=null){
                            setTextOf(itemView, R.id.tv_car_base_info_label, item.getFieldLabel()+":");
                            if(rId==R.layout.cars_baseinfo_item_image){
                                MyImageView pic = (MyImageView) itemView.findViewById(R.id.tv_car_base_info_value);
                                if(pic!=null){
                                    String picUrl = item.getValue();
                                    if(!picUrl.startsWith("http://")){
                                        picUrl = ComParams.HTTP_BASE+picUrl;
                                    }
                                    pic.setImage(picUrl,null,true);
                                }
                            }else{
                                setTextOf(itemView, R.id.tv_car_base_info_value, item.getValue());
                            }
                            baseInfoContainer.addView(itemView);
                        }
                    }
                }
            }
        }
    }



    public void setTabMenu(TabBean tabBean,boolean selected,int resourceId){
        tabBean.setSelected(selected);
        TextView textView = (TextView) findViewById(tabBean.getTabMenuId());
        if(textView!=null){
            textView.setBackgroundResource(resourceId);
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(tabBean.getContainId());
        if(linearLayout!=null){
            if(selected){
                linearLayout.setVisibility(View.VISIBLE);
            }else{
                linearLayout.setVisibility(View.GONE);
            }
        }else{
            Log.e(TAG,"没找到容器："+tabBean.getContainId());
        }
    }
    public void selectTab(TabBean tabBean){
        setTabMenu(tabBean,true,R.color.focused);
    }
    public void unSelectTab(TabBean tabBean){
        setTabMenu(tabBean, false, R.color.menuTextColor);
    }
    public void tabClicked(int clickId){
        for(TabBean tabBean:tabs){
            if(tabBean.tabMenuId==clickId){
                if(!tabBean.selected){
                    selectTab(tabBean);
                }
            }else{
                if(tabBean.selected) {
                    unSelectTab(tabBean);
                }
            }
        }
    }
    public View.OnClickListener clickOnTabMenu= new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            tabClicked(view.getId());
        }
    };
    public class TabBean {
        int tabMenuId;
        int containId;
        boolean selected;

        public TabBean(int tabMenuId, int containId, boolean selected) {
            this.tabMenuId = tabMenuId;
            this.containId = containId;
            this.selected = selected;
        }

        public int getTabMenuId() {
            return tabMenuId;
        }

        public void setTabMenuId(int tabMenuId) {
            this.tabMenuId = tabMenuId;
        }

        public int getContainId() {
            return containId;
        }

        public void setContainId(int containId) {
            this.containId = containId;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
