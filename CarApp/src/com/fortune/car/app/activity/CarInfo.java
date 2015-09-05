package com.fortune.car.app.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fortune.car.app.Caller;
import com.fortune.car.app.R;
import com.fortune.car.app.bean.Car;
import com.fortune.car.app.bean.CarDisplayItem;
import com.fortune.car.app.bean.Conduct;
import com.fortune.car.app.bean.ConductItem;
import com.fortune.mobile.params.ComParams;
import com.fortune.mobile.view.MyImageView;
import com.fortune.mobile.view.ProgressDialog;
import com.fortune.util.*;
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
    public static final int RESULT_CODE_SUCCESS=3000;
    public static final int RESULT_CODE_FAIL=3001;
    private LinearLayout conductsContainer;
    private List<Conduct> conducts;
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
        conducts = null;
        conductsContainer =(LinearLayout) findViewById(R.id.ll_conducts_container);
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
                        String type = item.getType();
                        if("image".equals(type)){
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
                                String value = item.getValue();
                                if("date".equals(type)){
                                    if(value.length()>10){
                                        value = value.substring(0,10);
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

    private Conduct currentConduct=null;
    private ConductItem conductItem = null;
    /**
     * 检查列表
     */
    public void renderConducts(){
        if(conductsContainer==null){
            return;
        }
        conductsContainer.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        if(conducts==null||conducts.size()==0){
            View view = inflater.inflate(R.layout.car_conduct_no_any_data,null);
            conductsContainer.addView(view);
            return;
        }
        for(Conduct conduct:conducts){
            View view = inflater.inflate(R.layout.car_conduct_list_a_row,null);
            setTextOf(view,R.id.tv_conduct_name,conduct.getTitle());
            setTextOf(view,R.id.tv_conduct_date, StringUtils.date2string(conduct.getCreateTime(), "yyyy-MM-dd"));
            view.setTag(conduct);
            setClickHandler(view, R.id.btn_conduct_view, clickOnConduct,conduct);
            conductsContainer.addView(view);
        }
    }

    /**
     * 某个检查的详情
     * @param conduct
     */
    public void renderConduct(Conduct conduct){
        if(conductsContainer==null||conduct==null){
            return;
        }
        conductsContainer.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        View conductView = inflater.inflate(R.layout.car_conduct_view, null);
        if(conductView!=null){
            setTextOf(conductView,R.id.tv_conduct_title,conduct.getTitle());
            setTextOf(conductView,R.id.tv_conduct_time,StringUtils.date2string(conduct.getCreateTime()));
            setTextOf(conductView,R.id.tv_conduct_miles,conduct.getMiles()+"公里");
            String statusText = "完成";
            Integer status = conduct.getStatus();
            if(status==null){
                status = 0;
            }
            switch (status){
                case 1:
                    statusText = "正在检测";
                    break;
                case 2:
                    statusText = "检测完毕";
                    break;
                default:
                    statusText="未知";
                    break;
            }
            setTextOf(conductView,R.id.tv_conduct_status,statusText);

            conductsContainer.addView(conductView);
        }
    }

    public void fillItemsTo(View container,List<ConductItem> items){

    }
    /**
     * 某个检查信息下的结果列表
     * @param items
     */
    public void renderConductItems(List<ConductItem> items){

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

    public void onFinished(int resultCode,Object tag){
        switch(resultCode){
            case RESULT_CODE_SUCCESS:
                if(tag instanceof List){
                    conducts = (List)tag;
                }else{
                    if(conducts==null){
                        conducts = new ArrayList<Conduct>();
                    }
                    conducts.clear();
                }
                break;
            case RESULT_CODE_FAIL:
                if(conducts==null){
                    conducts = new ArrayList<Conduct>();
                }
                conducts.clear();
                break;
            default:
                Log.e(TAG,"返回的结果未知："+resultCode);
                break;
        }
        renderConducts();
    }

    public static List<Conduct> parseJson(String json){
        try {
            ConductsBean result = JsonUtils.fromJsonString(ConductsBean.class,json);
            if(result!=null){
                return result.getConducts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void loadConducts(final Caller caller,String command,int conductId,int carId){
        final String cacheKey = ComParams.INTENT_CAR_CONDUCTS+"_"+conductId+"_"+carId+"_"+command;
        String cacheResult = ACache.get(caller.getContext()).getAsString(cacheKey);
        if(cacheResult!=null){
            caller.onFinished(RESULT_CODE_SUCCESS,parseJson(cacheResult));
            return;
        }
        HttpUtils handler = new HttpUtils();
        handler.configUserAgent(ComParams.userAgent);
        final ProgressDialog progDialog = caller.getProgDialog();

        String url = ComParams.HTTP_LIST_CONDUCTS+"phone="+ User.getUserId(caller.getContext())
                +"&token="+User.getToken(caller.getContext())+"&conductId="+conductId+"&carId="+carId;
        handler.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                progDialog.dismiss();
                String result = responseInfo.result;
                Log.d(getClass().getSimpleName(), "服务器返回：" + result);
                ACache.get(caller.getContext())
                        .put(cacheKey, result, 60 * 5);
                caller.onFinished(RESULT_CODE_SUCCESS,parseJson(result));
            }
            @Override
            public void onStart() {
                Log.d(caller.getClass().getSimpleName(), "启动WEB请求，要求车辆信息");
                progDialog.show();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                caller.onFinished(RESULT_CODE_SUCCESS,"无法获取车辆信息："+msg);
                progDialog.dismiss();
            }
        });

    }
    public class ConductsBean{
        private boolean success;
        private String msg;
        private List<Conduct> conducts;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<Conduct> getConducts() {
            return conducts;
        }

        public void setConducts(List<Conduct> conducts) {
            this.conducts = conducts;
        }
    }

    private View.OnClickListener clickOnConduct = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Object o=view.getTag();
            if(o instanceof Conduct){
                Conduct conduct = (Conduct) o;
                currentConduct = conduct;
                renderConduct(conduct);
            }else{
                Log.e(TAG,"不知道点击了啥.....");
            }
        }
    };
}