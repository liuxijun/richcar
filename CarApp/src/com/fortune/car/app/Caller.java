package com.fortune.car.app;

import android.content.Context;
import com.fortune.mobile.view.ProgressDialog;

/**
 * Created by xjliu on 2015/9/4.
 *
 */
public interface Caller {
    ProgressDialog getProgDialog();
    void onDataLoaded(int resultCode, Object tag);
    Context getContext();
}
