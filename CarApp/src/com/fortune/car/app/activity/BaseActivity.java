package com.fortune.car.app.activity;

import android.app.Activity;
import android.os.Bundle;
import com.fortune.mobile.view.ProgressDialog;

/**
 * Created by xjliu on 2015/8/30.
 *
 */
public class BaseActivity extends Activity {
    protected ProgressDialog progDialog;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progDialog = com.fortune.mobile.view.ProgressDialog.show(this);
        progDialog.setCancelable(true);
    }

    public ProgressDialog getProgDialog() {
        return progDialog;
    }

    public void setProgDialog(ProgressDialog progDialog) {
        this.progDialog = progDialog;
    }
}
