package com.fortune.car.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.fortune.car.app.R;
import com.fortune.car.app.bean.Car;
import com.fortune.mobile.params.ComParams;

/**
 * Created by xjliu on 2015/9/26.
 *
 */
public class CarInfoMenu extends BaseActivity implements View.OnClickListener{
    private Car car;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        car = getIntent().getParcelableExtra(ComParams.INTENT_CAR_BEAN);
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
                Intent intent = new Intent(this,CarBaseInfo.class);
                intent.putExtra(ComParams.INTENT_CAR_BEAN, car);
                startActivity(intent);
                break;
            case R.id.btn_detect_info:
                Intent intentForDect = new Intent(this,CarDetectInfo.class);
                intentForDect.putExtra(ComParams.INTENT_CAR_BEAN, car);
                startActivity(intentForDect);
                break;
            default:
                Toast.makeText(this,"ипн╢й╣ож...",Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
