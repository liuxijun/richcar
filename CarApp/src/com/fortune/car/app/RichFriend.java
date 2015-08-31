package com.fortune.car.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.fortune.car.app.activity.*;
import com.fortune.mobile.params.ComParams;
import com.fortune.mobile.view.ProgressDialog;
import com.fortune.util.*;
import com.fortune.util.net.HttpUtils;
import com.fortune.util.net.http.RequestCallBack;
import com.fortune.util.net.http.ResponseInfo;
import com.fortune.util.net.http.client.HttpRequest;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RichFriend extends BaseActivity {
    public static String TAG = RichFriend.class.getSimpleName();
    public List<ActivityBean> activityBeans = new ArrayList<ActivityBean>();
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setViews();
        activityBeans.add(new ActivityBean(R.id.tv_home_menu_body01, RichTechNavigate.class, false));
        activityBeans.add(new ActivityBean(R.id.tv_home_menu_body02, Cars.class, true));
        activityBeans.add(new ActivityBean(R.id.tv_home_menu_body03, Solutions.class, true));
        activityBeans.add(new ActivityBean(R.id.tv_home_menu_body04, RepairSolutions.class, true));
        activityBeans.add(new ActivityBean(R.id.tv_home_menu_body05, RepairProgress.class, true));
        activityBeans.add(new ActivityBean(R.id.tv_home_menu_body06, Services.class, true));
        activityBeans.add(new ActivityBean(R.id.tv_home_menu_body07, Products.class, true));
        activityBeans.add(new ActivityBean(R.id.tvHomeButtonLeft, com.fortune.car.app.activity.RichFriend.class, true));
        activityBeans.add(new ActivityBean(R.id.tvHomeButtonRight, Messages.class, true));
    }
    public void setViewInfo(View view,int w,int h,int x,int y,int fontSize,int fontColor,float rate,View.OnClickListener onClickListener){
        if(w>=0){
            w = Math.round(w*rate);
        }
        if(h>=0){
            h = Math.round(h*rate);
        }
        if(x>=0){
            x = Math.round(x*rate);
        }
        if(y>=0){
            y = Math.round(y*rate);
        }
        if(view.getLayoutParams() instanceof FrameLayout.LayoutParams){
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w,h);
            lp.gravity = Gravity.TOP| Gravity.LEFT;
            if(x>=0&&y>=0){
                lp.setMargins(x,y,0,0);
            }
            Log.d(TAG, "布局是FrameLayout，设置w=" + w + ",h=" + h + ",x=" + x + ",y=" + y);
            view.setLayoutParams(lp);
        }else if(view.getLayoutParams() instanceof RelativeLayout.LayoutParams){
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w,h);
            //lp.gravity = Gravity.TOP| Gravity.LEFT;
            if(x>=0&&y>=0){
                lp.setMargins(x,y,0,0);
            }
            Log.d(TAG,"布局类型RelativeLayout，设置w="+w+",h="+h+",x="+x+",y="+y);
            view.setLayoutParams(lp);
        }else{
            Log.d(TAG,"未知布局类型，w="+w+",h="+h+",x="+x+",y="+y);
        }
        if(view instanceof TextView){
            Log.d(TAG,"TextView尝试设置字体和颜色,fontSize="+fontSize+",fontColor=0x"+String.format("%x",fontColor));
            ((TextView) view).setTextSize(fontSize * rate);
            ((TextView) view).setTextColor(fontColor);
        }
        if(onClickListener!=null){
            view.setFocusable(true);
            view.setOnClickListener(onClickListener);
        }
    }
    public void setViewInfo(String title,int id,int w,int h,int x,int y,int fontSize,int fontColor,float rate,
                            View.OnClickListener onClickListener){
        View view = findViewById(id);
        if(view!=null){
            setViewInfo(view,w,h,x,y,fontSize,fontColor,rate,onClickListener);
        }else{
            Log.e(TAG,"无法设置" +title+"，没有找到资源："+id);
        }
    }
    public void setViews(){
        int screenWidth = Util.getWindowWidth(this);
        int height=1896,width=1080,centerICONWidth=1080,centerICONHeight=1416;
        int buttonWidth=280,buttonHeight=130;
        int buttonPosX=100,buttonPosY=1680;
        int menuBarWidth=244,menuBarHeight=248;
        int posX = width/2+10,posY = height/2-10,radius = 400;
        int menuCount=7;

        List<ViewBean> views = new ArrayList<ViewBean>();
        views.add(new ViewBean("主体窗口",R.id.tv_home_body_main,
                width,height,
                0,0,
                40,0xFFFFFFFF));
        views.add(new ViewBean("中心圆盘",R.id.ivCenterICON,
                centerICONWidth,centerICONHeight,
                posX-centerICONWidth/2,posY-centerICONHeight/2,
                40,0xFFFFFFFF));
        views.add(new ViewBean("左边按钮",R.id.tvHomeButtonLeft,buttonWidth,buttonHeight,buttonPosX,buttonPosY,18,0xFF928e8f,onLeftButtonClick));
        views.add(new ViewBean("右边按钮",R.id.tvHomeButtonRight,buttonWidth,buttonHeight,(width-buttonPosX-buttonWidth),buttonPosY,18,0xFF928e8f,onRightButtonClick));
        int[] menuIds = new int[]{
                R.id.tv_home_menu_body01,R.id.tv_home_menu_body02,
                R.id.tv_home_menu_body03,R.id.tv_home_menu_body04,
                R.id.tv_home_menu_body05,R.id.tv_home_menu_body06,R.id.tv_home_menu_body07};
        for(int i=0;i<menuCount;i++){
            double v = 2*Math.PI/menuCount*(1-i);
            views.add(new ViewBean("菜单"+i,menuIds[i],
                    menuBarWidth,menuBarHeight,
                    posX-(int)(radius*Math.sin(v))-menuBarWidth/2,
                    posY-(int)(radius*Math.cos(v))-menuBarWidth/2,0,0,onMenuItemClick));
        }
        float rate = screenWidth*1.0f/width;

        for(ViewBean viewBean:views){
            setViewInfo(viewBean.getTitle(),viewBean.getrId(),viewBean.getWidth(),viewBean.getHeight(),
                    viewBean.getX(),viewBean.getY(),viewBean.getFontSize(),viewBean.getFontColor(),rate,viewBean.getOnClickListener());
        }
    }

    public View.OnClickListener onLeftButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "点了我！");
            onMenuItemClick.onClick(view);
        }
    };
    public View.OnClickListener onRightButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "点了右边的按钮！");
            new AlertDialog.Builder(view.getContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("确认注销")
                    .setMessage("您确认要暂时离开吗？")
                    .setPositiveButton("是的，我确认！", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            User.saveToken(RichFriend.this,null);
                            Toast.makeText(RichFriend.this,"已经注销",Toast.LENGTH_SHORT).show();
                        }
                    }
                    )
                    .setNegativeButton("矮油，按错了！", null)
                    .show();
        }
    };
    private Class willStartCls = null;
    public View.OnClickListener onMenuItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG,"点了Menu！");
            int id=view.getId();
            boolean needLogin=true;
            willStartCls = null;
            for(ActivityBean bean:activityBeans){
                if(bean.getrId()==id){
                    willStartCls = bean.getActivity();
                    needLogin = bean.isNeedLogin();
                    break;
                }
            }
            if(willStartCls!=null){
                if(needLogin&&!User.isLogined(RichFriend.this)){
                    showLogin();
                }else{
                    startActivity(willStartCls);
                }
            }else{
                Log.d(TAG,"点击到的东西我还不能响应："+id+","+view.getClass().getSimpleName());
            }
        }
    };
    public void startActivity(Class cls){
        Log.d(TAG, "将要启动：" + cls.getSimpleName());
        Intent intent = new Intent(this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
    }
    public class ActivityBean{
        int rId;
        Class activity;
        boolean needLogin;

        public ActivityBean(int rId, Class activity, boolean needLogin) {
            this.rId = rId;
            this.activity = activity;
            this.needLogin = needLogin;
        }

        public int getrId() {
            return rId;
        }

        public void setrId(int rId) {
            this.rId = rId;
        }

        public Class getActivity() {
            return activity;
        }

        public void setActivity(Class activity) {
            this.activity = activity;
        }

        public boolean isNeedLogin() {
            return needLogin;
        }

        public void setNeedLogin(boolean needLogin) {
            this.needLogin = needLogin;
        }
    }
    private View loginDialog;
    public void showLogin(){
        LayoutInflater inflater = getLayoutInflater();
        loginDialog = inflater.inflate(R.layout.dialog_login,
                    (ViewGroup) findViewById(R.id.login_dialog_main_body));
        new AlertDialog.Builder(this).setTitle("请输入账号口令登录").setView(loginDialog)
            .setPositiveButton("确定", onLoginClick)
            .setNegativeButton("取消", null).show();
    }

    DialogInterface.OnClickListener onLoginClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            String userId = getEditText(loginDialog,R.id.login_et_user,null);
            if(userId==null||"".equals(userId)){
                Log.e(TAG,"未输入帐号！");

            }
            String pwd = getEditText(loginDialog,R.id.login_et_pwd,null);
            if(userId==null||"".equals(userId)){
                Log.e(TAG,"未输入口令！");
            }
            Log.d(TAG,"准备登录....."+userId+","+pwd);
            doLogin(userId,pwd);
        }
    };

    public void doLogin(String userId,String pwd){
        HttpUtils handler = new HttpUtils();
        String md5Pwd = pwd;
        try {
            md5Pwd = MD5Utils.getMD5String(pwd);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG,"MD5计算时发生异常："+e.getLocalizedMessage());
        }
        String url = ComParams.HTTP_LOGIN + "userId="+userId+"&pwd="+md5Pwd;
        Log.d(TAG,"准备发起登录请求："+url);
        handler.send(HttpRequest.HttpMethod.GET,url , new RequestCallBack<String>() {
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
                }else{
                    Log.e(TAG,"无法从服务器获取到正确的返回结果："+responseInfo.result);
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
                Log.e(TAG,"请求登录失败！");
                progDialog.dismiss();
            }
        });
    }
    public void loginSuccessed(String userId,String token){
        Log.d(TAG,"用户登录成功，记录用户登录状态："+userId+","+token);
        User.saveUserId(this,userId);
        User.saveToken(this,token);
        startActivity(willStartCls);
    }
    public void loginFailed(String msg){
        Log.d(TAG,"用户登录失败，记录用户登录状态！");
        if(msg==null){
            msg = "错误的帐号或者口令，或已经超时";
        }
        Toast.makeText(this,"登录失败了："+msg,Toast.LENGTH_LONG).show();
    }
    public String getEditText(View view,int id,String defaultVal){
        try {
            EditText et = (EditText) view.findViewById(id);
            if(et!=null){
                String val = et.getText().toString();
                if(!"".equals(val)){
                    return val;
                }
            }
        } catch (Exception e) {
            Log.e(TAG,"尝试获取数据时发生异常："+e.getLocalizedMessage());
            e.printStackTrace();
        }
        return defaultVal;
    }

    private boolean keyBackPressedJustNow=false;
    private Date keyBackPressedTime = new Date(0);
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ){
            if(keyBackPressedJustNow){
                long twiceLength = System.currentTimeMillis()-keyBackPressedTime.getTime();
                if(twiceLength<2000){
                    ULog.d("连续两次按返回键，间隔是：" + twiceLength + ",小于2000毫秒，所以退出！");
                    System.exit(0);
                }else{
                    ULog.d("连续两次按返回键，间隔是："+twiceLength+",大于2秒，所以不退出！");
                }
            }
            keyBackPressedJustNow=true;
            keyBackPressedTime = new Date();
            Toast.makeText(this, "再次按返回键退出", Toast.LENGTH_SHORT).show();
            return true;
        }else{
            keyBackPressedJustNow=false;
        }

        return super.onKeyDown(keyCode, event);
    }

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
