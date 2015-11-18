package com.fortune.car.app.activity;

import android.os.Bundle;
import android.view.View;
import com.fortune.car.app.R;
import com.fortune.util.User;

/**
 * Created by xjliu on 2015/9/1.
 *
 */
public class About extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();
    }
    public void initViews(){
        super.initViews();
        setTextOf(R.id.tv_title,"关于睿乔");
        if(!User.isLogined(this)){
            setVisibleOf(null,R.id.linearLayoutBottom, View.GONE);
        }
    }
}