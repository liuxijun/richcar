package com.fortune.car.app.activity;

import android.os.Bundle;
import android.view.View;
import com.fortune.car.app.R;

/**
 * Created by xjliu on 2015/8/30.
 *
 */
public class RepairSolutions extends Solutions {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setType(1);
    }

    public void initViews(){
        super.initViews();
        setTextOf(R.id.tv_title, "维修方案");
        setVisibleOf(null, R.id.ll_fail_type, View.VISIBLE);
        setTextOf(R.id.tv_car_solution_items_label, "维修项目");
    }
}
