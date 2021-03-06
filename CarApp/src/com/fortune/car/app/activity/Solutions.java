package com.fortune.car.app.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fortune.car.app.R;
import com.fortune.car.app.bean.Parts;
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
    boolean displayDetail = false;
    List<Repair> allRepairs=null;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_detail);
        initViews();
    }

    public void initViews(){
        super.initViews();
        setTextOf(R.id.tv_title, "养护方案");
        setVisibleOf(null, R.id.ll_fail_type, View.GONE);
        setVisibleOf(null, R.id.tv_fail_type_line, View.GONE);
        initRepairs();
    }

    public void setType(int type){
        this.type = type;
    }

    public void initRepairs(){
        String phone = User.getPhone(this);
        if(phone==null||"".equals(phone)){
            phone = User.getUserId(this);
        }
        String url = ComParams.HTTP_LIST_CAR_REPAIRS+type+"&phone="+ phone+"&token="+User.getToken(this);
        executeHttpGet(this, url);
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
    public String getStatusText(Integer status){
        if(status==null){
            return "";
        }
        switch(status){
            case 1:
                return "排队中";
            case 2:
                return "正在进行";
            case 3:
                return "已经完成";
        }
        return "未知";
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void renderRepairs(List<Repair> repairs){
        allRepairs = repairs;
        displayDetail = false;
        LinearLayout repairsConatiner = (LinearLayout)findViewById(R.id.ll_solution_list_contain);
        if(repairsConatiner!=null){
            repairsConatiner.removeAllViews();
            if(repairs!=null){
                int repairCount = repairs.size();
                if(repairCount<=1){
                    repairsConatiner.setVisibility(View.GONE);
                    if(repairCount==1){
                        fillData(repairs.get(0));
                    }
                }else{
                    setVisibleOf(null,R.id.ll_top_conainer_for_solutions,View.VISIBLE);
                    setVisibleOf(null,R.id.ll_solution_items_contain,View.GONE);
                    LayoutInflater inflater = getLayoutInflater();
                    LinearLayout header =(LinearLayout) inflater.inflate(R.layout.item_solution,null);
                    setVisibleOf(header,R.id.btnViewDetail,View.INVISIBLE);
                    repairsConatiner.addView(header);
                    int i=0;
                    for(Repair repair:repairs){
                        LinearLayout repairLine =(LinearLayout) inflater.inflate(R.layout.item_solution,null);
                        i++;
                        if(i%2==0){
                            try {
                                repairLine.setAlpha(0.6f);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        setTextOf(repairLine,R.id.tvCreateTime,StringUtils.date2string(repair.getInTime()));
                        setTextOf(repairLine, R.id.tvStatus, getStatusText(repair.getStatus()));
                        setTextOf(repairLine, R.id.tvReception, repair.getRecepton());
                        repairLine.setTag(repair);
                        Button viewButton =(Button) repairLine.findViewById(R.id.btnViewDetail);
                        if(viewButton!=null){
                            viewButton.setTag(repair);
                            viewButton.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View view) {
                                    view.setAlpha(0.5f);
                                    Repair data =(Repair) view.getTag();
                                    fillData(data);
                                }
                            });
                        }
                        repairsConatiner.addView(repairLine);
                    }
                }
            }else{
                repairsConatiner.setVisibility(View.GONE);
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void fillData(Repair repair){
        displayDetail = true;
        setVisibleOf(null,R.id.ll_top_conainer_for_solutions,View.GONE);
        setVisibleOf(null,R.id.ll_solution_items_contain,View.VISIBLE);
        try {
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
            setTextOf(R.id.tv_car_solution_items_label,"养护项目：");
            setTextOf(R.id.tv_car_solution_items,items);
            setTextOf(R.id.tv_in_time,StringUtils.date2string(repair.getInTime()));
            setTextOf(R.id.tv_out_time,StringUtils.date2string(repair.getOutTime()));
            setTextOf(R.id.tv_workers,repair.getWorkers());
            setTextOf(R.id.tv_qr,repair.getQc());
            setTextOf(R.id.tv_agent,repair.getRecepton());
            List<Parts> partses = repair.getParts();
            if(partses!=null&&partses.size()>0){
                setVisibleOf(null,R.id.tv_parts_items_table,View.VISIBLE);
                LayoutInflater inflater = getLayoutInflater();
                LinearLayout partsListTable = (LinearLayout) findViewById(R.id.partsListTable);
                if(partsListTable!=null){
                    partsListTable.removeAllViews();
                    float totalPrice = 0,totalManHour=0;
                    LinearLayout title =(LinearLayout) inflater.inflate(R.layout.item_parts,null);
                    if(title!=null){
                        setTextOf(title,R.id.tvName,"配件名称");
                        setTextOf(title,R.id.tvHome,"产地");
                        setTextOf(title,R.id.tvLevel,"品质");
                        setTextOf(title,R.id.tvManHour,"工时费");
                        setTextOf(title, R.id.tvPrice, "价格");
                        title.setAlpha(0.8f);
                        partsListTable.addView(title);
                    }
                    int i=0;
                    for(Parts parts:partses){
                        Log.d(TAG,"正在添加配件："+parts.getName()+"...");
                        LinearLayout partsLayout =(LinearLayout) inflater.inflate(R.layout.item_parts,null);
                        if(partsLayout!=null){
                            i++;
                            if(i%2==0){
                                partsLayout.setAlpha(0.8f);
                            }
                            setTextOf(partsLayout,R.id.tvName,parts.getName());
                            setTextOf(partsLayout,R.id.tvHome,parts.getHomeland());
                            setTextOf(partsLayout,R.id.tvLevel,parts.getLevel());
                            setTextOf(partsLayout,R.id.tvPrice,String.format("%1$,.2f", parts.getPrice()));
                            setTextOf(partsLayout,R.id.tvManHour,String.format("%1$,.2f", parts.getManHour()));
                            totalPrice += parts.getPrice();
                            totalManHour += parts.getManHour();
                            partsListTable.addView(partsLayout);
                        }else{
                            Log.e(TAG,"未能初始化配件显示组件");
                        }
                    }
                    i++;
                    LinearLayout bottom =(LinearLayout) inflater.inflate(R.layout.item_parts,null);
                    if(bottom!=null){
                        setTextOf(bottom,R.id.tvName,"");
                        setTextOf(bottom,R.id.tvHome,"");
                        setTextOf(bottom,R.id.tvLevel,"合计");
                        TextView level =(TextView) bottom.findViewById(R.id.tvLevel);
                        if(level!=null){
                            level.setText("合计");
                            //level.setTextColor(0x181818);
                        }
                        setTextOf(bottom, R.id.tvPrice, String.format("%1$,.2f", totalPrice));
                        setTextOf(bottom, R.id.tvManHour, String.format("%1$,.2f", totalManHour));
                        if(i%2==0){
                            bottom.setAlpha(0.8f);
                        }
                        partsListTable.addView(bottom);
                    }
    //                setTextOf(R.id.totalPrice,String.format("%0.2f",total));
                }
            }else{
                setVisibleOf(null,R.id.tv_parts_items_table,View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onDataLoaded(int resultCode, Object tag){
        boolean dataFilled=false;
        try {
            RepairSolutionBean result = JsonUtils.fromJsonString(RepairSolutionBean.class, (String)tag);
            if (result != null) {
                if(result.isSuccess()){
                    List<Repair> repairs = result.getRepairs();
                    if(repairs!=null&&repairs.size()>0){
                        renderRepairs(repairs);
                        //fillData(repairs.get(0));
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(displayDetail){
                displayDetail = false;
                renderRepairs(allRepairs);
                return true;
            }
        }
        return super.onKeyDown(keyCode,event);
    }
}
