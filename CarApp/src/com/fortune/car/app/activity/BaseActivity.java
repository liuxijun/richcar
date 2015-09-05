package com.fortune.car.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.fortune.car.app.Caller;
import com.fortune.mobile.params.ComParams;
import com.fortune.mobile.view.ProgressDialog;
import com.fortune.util.Util;
import org.w3c.dom.Text;

/**
 * Created by xjliu on 2015/8/30.
 *
 */
public class BaseActivity extends Activity implements Caller {
    protected ProgressDialog progDialog;
    protected String TAG = getClass().getSimpleName();
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       ComParams.userAgent = Util.getAgent(this, false);
        progDialog = com.fortune.mobile.view.ProgressDialog.show(this);
        progDialog.setCancelable(true);
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
    public void onFinished(int resultCode,Object tag){
        Log.d(TAG,"调用结束，返回码："+resultCode+",tag="+tag);
    }
    public Context getContext(){
        return this;
    }
    public void alert(String message){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("警告")
                .setMessage(message)
                .setNegativeButton("取消", null)
                .show();
    }
}
