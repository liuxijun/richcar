package com.fortune.car.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.fortune.car.app.R;
import com.fortune.mobile.params.ComParams;

/**
 * Created by xjliu on 2015/9/26.
 *
 */
public class CarInfoMenu extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_info_menu);
        initViews();
    }
    public void initViews(){
        super.initViews();
        setClickHandler(null, R.id.btn_base_info, this);
        setClickHandler(null,R.id.btn_detect_info,this);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch(id){
            case R.id.btn_base_info:
                break;
            case R.id.btn_detect_info:
                break;
            default:
                Toast.makeText(this,"ипн╢й╣ож...",Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
