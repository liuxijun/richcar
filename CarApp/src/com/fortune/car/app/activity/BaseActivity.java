package com.fortune.car.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.fortune.car.app.Caller;
import com.fortune.car.app.R;
import com.fortune.mobile.params.ComParams;
import com.fortune.mobile.view.ProgressDialog;
import com.fortune.util.ACache;
import com.fortune.util.HttpException;
import com.fortune.util.Util;
import com.fortune.util.net.HttpUtils;
import com.fortune.util.net.http.RequestCallBack;
import com.fortune.util.net.http.ResponseInfo;
import com.fortune.util.net.http.client.HttpRequest;

/**
 * Created by xjliu on 2015/8/30.
 *
 */
public class BaseActivity extends Activity implements Caller {
    public static final int RESULT_CODE_SUCCESS = 3000;
    public static final int RESULT_CODE_FAIL = 3001;
    public static final int REQUEST_CODE_MAIN = 6000;
    public static final int RESULT_LOGIN_SUCCESS=9000;
    public static final int RESULT_LOGIN_FAILED=RESULT_LOGIN_SUCCESS+1;

    protected ProgressDialog progDialog;
    protected String TAG = getClass().getSimpleName();
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ComParams.userAgent = Util.getAgent(this, false);
        progDialog = com.fortune.mobile.view.ProgressDialog.show(this);
        progDialog.setCancelable(true);
    }
    public void initViews(){
        setClickHandler(null, R.id.iv_left,clickOnBack);
        setClickHandler(null, R.id.btn_home, clickOnHome);
        setClickHandler(null,R.id.iv_share,clickOnShare);
        setClickHandler(null,R.id.btn_message,clickOnNotImpl);
        setClickHandler(null, R.id.btn_personal_center, clickOnNotImpl);
    }
    public ProgressDialog getProgDialog() {
        return progDialog;
    }

    public void setProgDialog(ProgressDialog progDialog) {
        this.progDialog = progDialog;
    }

    public boolean setClickHandler(View parent,int rId,View.OnClickListener onClickListener){
        return setClickHandler(parent,rId,onClickListener,null);
    }
    public boolean setClickHandler(View parent,int rId,View.OnClickListener onClickListener,Object tag){
        View view;
        if(parent==null){
            view = findViewById(rId);
        }else{
            view = parent.findViewById(rId);
        }
        if(view!=null){
            view.setOnClickListener(onClickListener);
            if(tag!=null){
                view.setTag(tag);
            }
            return true;
        }
        return false;
    }

    public boolean setTextOf(int rId,String text){
        try {
            TextView tv =(TextView) findViewById(rId);
            if(tv!=null){
                tv.setText(text);
                return true;
            }
        } catch (Exception e) {
            Log.e("BaseAcitivity.setTextOf", "无法设置资源" + rId + "到“" + text + "”");
        }
        return false;
    }
    public boolean setVisibleOf(View parent,int rId,int visible){
        View view;
        if(parent==null){
            view = findViewById(rId);
        }else{
            view = parent.findViewById(rId);
        }
        if(view!=null){
            view.setVisibility(visible);
            return true;
        }else{
            return false;
        }
    }
    public static boolean setTextOf(View view,int rId,String text){
        if(view!=null){
            try {
                View child = view.findViewById(rId);
                if(child!=null){
                    if(child instanceof EditText){
                        ((EditText)child).setText(text);
                    }else if(child instanceof TextView){
                        ((TextView)child).setText(text);
                    }else{
                        Log.e("BaseActivity","无法识别："+child.getClass().getSimpleName());
                        return false;
                    }
                    return true;
                }
            } catch (Exception e) {
                Log.e("BaseAcitivity.setTextOf","无法设置资源"+rId+"到“"+text+"”");
            }
        }
        return false;
    }
    public boolean checked(int rId) {
        return checked(null,rId,false);
    }
    public boolean checked(int rId,boolean defaultVal) {
        return checked(null,rId,defaultVal);
    }
    public boolean checked(View parent,int rId){
        return checked(parent,rId,false);
    }
    public boolean checked(View parent,int rId,boolean defaultVal){
        try {
            CheckBox checkBox;
            if(parent!=null){
                checkBox = (CheckBox) parent.findViewById(rId);
            }else{
                checkBox = (CheckBox)findViewById(rId);
            }
            if(checkBox!=null){
                return checkBox.isChecked();
            }else{
                Log.e(TAG,"没有找到CheckBox组件："+rId);
            }
        } catch (Exception e) {
            Log.e(TAG,"发生异常："+e.getLocalizedMessage());
            e.printStackTrace();
        }
        return defaultVal;
    }

    public String getTextOf(View parent,int rId,String defaultVal){
        EditText text=null;
        try {
            if(parent==null){
                text = (EditText) findViewById(rId);
            }else{
                text = (EditText) parent.findViewById(rId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(text!=null){
            return text.getText().toString();
        }
        return defaultVal;
    }

    public void onDataLoaded(int resultCode, Object tag){
        Log.d(TAG,"调用结束，返回码："+resultCode+",tag="+tag);
    }
    public Context getContext(){
        return this;
    }
    public void alert(String message){
        alert(message,null);
    }
    public void alert(String message, final View.OnClickListener onClickListener){
        AlertDialog dialog =null;
        dialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("警告")
                .setMessage(message)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(onClickListener!=null){
                            onClickListener.onClick(null);
                        }
                    }
                })
                .show();
    }

    public View.OnClickListener clickOnHome = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(BaseActivity.this,com.fortune.car.app.RichFriend.class);
            //intentForDect.putExtra(ComParams.INTENT_CAR_BEAN, car);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    };
    public void notImp(){
        Toast.makeText(BaseActivity.this,"该功能暂时尚未实现，敬请期待...",Toast.LENGTH_SHORT).show();
    }
    public View.OnClickListener clickOnNotImpl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            notImp();
        }
    };
    public View.OnClickListener clickOnShare = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            notImp();
        }
    };
    public View.OnClickListener clickOnBack = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    };

    public void onBackPressed(){
        finish();
    }

    public static void executeHttpGet(final Caller caller, String url) {
        final String cacheKey = url;
        String cacheResult = ACache.get(caller.getContext()).getAsString(cacheKey);
        if (cacheResult != null) {
            Log.d(caller.getClass().getSimpleName(), "已经从缓存中获取数据，直接返回：" + cacheResult);
            caller.onDataLoaded(RESULT_CODE_SUCCESS, cacheResult);
            return;
        }
        HttpUtils handler = new HttpUtils();
        handler.configUserAgent(ComParams.userAgent);
        final ProgressDialog progDialog = caller.getProgDialog();
        Log.d(caller.getClass().getSimpleName(), "准备发起web请求，获取检查接口数据：" + url);
        handler.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                progDialog.dismiss();
                String result = responseInfo.result;
                Log.d(caller.getClass().getSimpleName(), "服务器返回：" + result);
                ACache.get(caller.getContext())
                        .put(cacheKey, result, 60 * 2);
                caller.onDataLoaded(RESULT_CODE_SUCCESS, result);
            }

            @Override
            public void onStart() {
                Log.d(caller.getClass().getSimpleName(), "启动WEB请求，要求车辆信息");
                progDialog.show();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                caller.onDataLoaded(RESULT_CODE_SUCCESS, "无法获取车辆信息：" + msg);
                progDialog.dismiss();
            }
        });
    }
}
