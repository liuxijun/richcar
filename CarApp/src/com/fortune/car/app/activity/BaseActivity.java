package com.fortune.car.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.fortune.mobile.view.ProgressDialog;
import org.w3c.dom.Text;

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

    public boolean setClickHandler(View parent,int rId,View.OnClickListener onClickListener){
        View view;
        if(parent==null){
            view = findViewById(rId);
        }else{
            view = parent.findViewById(rId);
        }
        if(view!=null){
            view.setOnClickListener(onClickListener);
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

}
