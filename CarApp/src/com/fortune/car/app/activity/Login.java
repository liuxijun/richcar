package com.fortune.car.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import com.fortune.car.app.*;
import com.fortune.mobile.params.ComParams;
import com.fortune.util.HttpException;
import com.fortune.util.JsonUtils;
import com.fortune.util.MD5Utils;
import com.fortune.util.User;
import com.fortune.util.net.HttpUtils;
import com.fortune.util.net.http.RequestCallBack;
import com.fortune.util.net.http.ResponseInfo;
import com.fortune.util.net.http.client.HttpRequest;

import java.security.NoSuchAlgorithmException;

/**
 * Created by xjliu on 2015/8/30.
 *
 */
public class Login extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    public void initViews(){
        super.initViews();
        final Button button =(Button) findViewById(R.id.btn_login);
        setClickHandler(null,R.id.btn_register,this);
        if(button!=null){
            button.setOnClickListener(this);
        }
        final CheckBox agreeBox = (CheckBox) findViewById(R.id.checkBoxAgreeLicense);
        if(agreeBox!=null){
            agreeBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(button!=null){
                        button.setEnabled(agreeBox.isChecked());
                    }
                }
            });
        }
        setTextOf(R.id.tv_title,"用户登录");
    }

    @Override
    public void onClick(View view) {
        int id = -1;
        if(view!=null){
            id = view.getId();
        }
        switch(id){
            case R.id.btn_login:
                checkLogin();
                break;
            case R.id.btn_register:
                Toast.makeText(this, "暂未开放注册功能！", Toast.LENGTH_LONG).show();
                break;
        }
    }

    boolean rememberUserId = false;
    boolean autoLogin = true;
    public void checkLogin(){
        if(!checked(R.id.checkBoxAgreeLicense,true)){
            Toast.makeText(this, "不同意许可，将无法使用本系统提供的完整服务！", Toast.LENGTH_LONG).show();
            return;
        }
        String userId = getTextOf(null, R.id.etUserPhone, null);
        String errorLog = "";
        if(userId==null||"".equals(userId)){
            errorLog += "未输入帐号！";
            Log.e(TAG, errorLog);
        }else{
            rememberUserId =checked(null,R.id.checkBoxRememberUserId);
            if(rememberUserId){
                User.saveUserId(this, userId);
            }else{
                User.saveUserId(this,null);
            }
        }
        String pwd = getTextOf(null, R.id.etUserPwd, null);
        if(pwd==null||"".equals(pwd)){
            errorLog += "未输入口令！";
            Log.e(TAG,errorLog);
        }else{
            autoLogin = checked(R.id.checkBoxAutoLogin,true);
        }
        if(!"".equals(errorLog)){
            Toast.makeText(this,"无法登录："+errorLog,Toast.LENGTH_LONG).show();
            return;
        }
        Log.d(TAG, "准备登录....." + userId + "," + pwd);
        doLogin(userId, pwd);
    }

    public void doLogin(String userId,String pwd){
        HttpUtils handler = new HttpUtils();
        String md5Pwd = pwd;
        try {
            md5Pwd = MD5Utils.getMD5String(pwd);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG,"MD5计算时发生异常："+e.getLocalizedMessage());
        }
        String url = ComParams.HTTP_LOGIN + "userId="+userId+"&pwd="+md5Pwd;
        Log.d(TAG, "准备发起登录请求：" + url);
        handler.configUserAgent(ComParams.userAgent);
        handler.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.d(TAG, "登录返回数据：" + responseInfo.result);
                loginBean result = JsonUtils.fromJsonString(loginBean.class, responseInfo.result);
                progDialog.dismiss();
                if (result != null) {
                    if (result.isSuccess()) {
                        loginSuccessed(result.getUserId(), result.getToken());
                    } else {
                        loginFailed(result.getMsg());
                    }
                    return;
                } else {
                    Log.e(TAG, "无法从服务器获取到正确的返回结果：" + responseInfo.result);
                }
                loginFailed("服务器返回数据异常，无法登录！请稍候再试！");
            }

            @Override
            public void onStart() {
                Log.d(TAG, "启动WEB请求，要求登录");
                progDialog.show();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e(TAG, "请求登录失败！");
                progDialog.dismiss();
            }
        });
    }

    public void loginSuccessed(String userId,String token){
        Log.d(TAG, "用户登录成功，记录用户登录状态：" + userId + "," + token);
        if(rememberUserId){
            User.saveUserId(this, userId);
        }
        if(autoLogin){
            User.saveToken(this, token);
        }else{
            User.saveToken(this, null);
        }
        Intent intent = new Intent(this, com.fortune.car.app.RichFriend.class);
        intent.putExtra("loginSuccess",true);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void loginFailed(String msg){
        Log.d(TAG,"用户登录失败，记录用户登录状态！");
        if(msg==null){
            msg = "错误的帐号或者口令，或已经超时";
        }
        Toast.makeText(this,"登录失败了："+msg,Toast.LENGTH_LONG).show();
    }

    @SuppressWarnings("unused")
    public class loginBean{
        private String userId;
        private String token;
        private boolean success;
        private String msg;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

}
