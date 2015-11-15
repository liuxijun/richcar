package com.fortune.car.app.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.fortune.car.app.R;
import com.fortune.car.app.bean.Repair;
import com.fortune.mobile.params.ComParams;
import com.fortune.util.JsonUtils;
import com.fortune.util.StringUtils;
import com.fortune.util.User;

import java.util.List;

/**
 * Created by xjliu on 2015/8/30.
 *
 */
public class Solutions extends BaseActivity {
    private int type = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_detail);
        initViews();
        initRepairs();
    }

    public void initViews(){
        super.initViews();
        setTextOf(R.id.tv_title, "养护方案");
        setVisibleOf(null, R.id.ll_fail_type, View.GONE);
    }

    public void setType(int type){
        this.type = type;
    }

    public void initRepairs(){
        String url = ComParams.HTTP_LIST_CAR_REPAIRS+type+"&phone="+ User.getPhone(this)+"&token="+User.getToken(this);
        executeHttpGet(this,url);
    }
    public String appendIfNotEmpty(String src,String tail){
        if(tail==null||"".equals(tail.trim())){
            return src;
        }
        if(src==null||"".equals(src.trim())){
            return tail;
        }
        return src+"\n"+tail;
    }
    public void fillData(Repair repair){
        setTextOf(R.id.tv_file_no,repair.getFileId());
        setTextOf(R.id.tv_car_no,repair.getCarNo());
        setTextOf(R.id.tv_create_time, StringUtils.date2string(repair.getCreateTime()));
        String fault = appendIfNotEmpty(repair.getFault0(),null);
        fault = appendIfNotEmpty(fault,repair.getFault1());
        fault = appendIfNotEmpty(fault,repair.getFault2());
        fault = appendIfNotEmpty(fault,repair.getFault3());
        fault = appendIfNotEmpty(fault,repair.getFault4());
        fault = appendIfNotEmpty(fault,repair.getFault5());
        fault = appendIfNotEmpty(fault,repair.getFault6());
        fault = appendIfNotEmpty(fault,repair.getFault7());
        fault = appendIfNotEmpty(fault,repair.getFault8());
        fault = appendIfNotEmpty(fault,repair.getFault9());
        fault = appendIfNotEmpty(fault,repair.getFault10());
        fault = appendIfNotEmpty(fault,repair.getFault11());
        setTextOf(R.id.tv_fail_type,fault);
        String items = appendIfNotEmpty(repair.getItem0(),null);
        items = appendIfNotEmpty(items,repair.getItem1());
        items = appendIfNotEmpty(items,repair.getItem2());
        items = appendIfNotEmpty(items,repair.getItem3());
        items = appendIfNotEmpty(items,repair.getItem4());
        items = appendIfNotEmpty(items,repair.getItem5());
        items = appendIfNotEmpty(items,repair.getItem6());
        items = appendIfNotEmpty(items,repair.getItem7());
        items = appendIfNotEmpty(items,repair.getItem8());
        items = appendIfNotEmpty(items,repair.getItem9());
        items = appendIfNotEmpty(items,repair.getItem10());
        items = appendIfNotEmpty(items,repair.getItem11());
        setTextOf(R.id.tv_car_solution_items_label,"养护项目");
        setTextOf(R.id.tv_car_solution_items,items);
        setTextOf(R.id.tv_in_time,StringUtils.date2string(repair.getInTime()));
        setTextOf(R.id.tv_out_time,StringUtils.date2string(repair.getOutTime()));
        setTextOf(R.id.tv_workers,repair.getWorkers());
        setTextOf(R.id.tv_qr,repair.getQc());
        setTextOf(R.id.tv_agent,repair.getRecepton());
    }
    public void onDataLoaded(int resultCode, Object tag){
        boolean dataFilled=false;
        try {
            RepairSolutionBean result = JsonUtils.fromJsonString(RepairSolutionBean.class, (String)tag);
            if (result != null) {
                if(result.isSuccess()){
                    List<Repair> repairs = result.getRepairs();
                    if(repairs!=null&&repairs.size()>0){
                        fillData(repairs.get(0));
                        dataFilled = true;
                    }else{
                        Log.e(TAG,"数据获取结果为0");
                    }
                }else{
                    Log.e(TAG,"数据初始化错误！"+result.getMsg());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!dataFilled){
            alert("没有相应的数据！");
        }
    }
    public class RepairSolutionBean{
        private boolean success;
        private String msg;
        private List<Repair> repairs;

        public RepairSolutionBean() {
        }

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

        public List<Repair> getRepairs() {
            return repairs;
        }

        public void setRepairs(List<Repair> repairs) {
            this.repairs = repairs;
        }
    }

}
