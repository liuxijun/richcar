package com.fortune.car.app.activity;

import android.os.Bundle;
import com.fortune.car.app.R;

/**
 * Created by xjliu on 2015/8/30.
 *
 */
public class Services extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        initViews();
    }

    public void initViews(){
        super.initViews();
        setTextOf(R.id.tv_title, "服务质量");
    }
}
