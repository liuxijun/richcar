package com.fortune.car.app.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.fortune.car.app.R;
import com.fortune.car.app.bean.JsonBean;
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
import java.util.List;

/**
 * Created by xjliu on 2015/8/30.
 *
 */
public class RichFriend extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_friend);
        setViews();
    }
    public void setViews(){
        for(ButtonBean buttonBean:buttonBeans){
            setClickHandler(null,buttonBean.getButtonId(),buttonBean.getOnClickListener());
        }
    }
    private View.OnClickListener clickOnModifyNickName=new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            alert("还未完全实现该功能！");
        }
    };
    private View.OnClickListener clickOnModifyPicture=new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            alert("还未完全实现该功能！");
        }
    };
    private AlertDialog changePwdDialog;
    private View pwdDialogView;
    private void showChangePwdDialog(){
        LayoutInflater inflater = getLayoutInflater();
        pwdDialogView = inflater.inflate(R.layout.dialog_change_pwd,
                (ViewGroup) findViewById(R.id.dialog_changepwd_main_body));
        changePwdDialog = new AlertDialog.Builder(this).setTitle("修改口令").setView(pwdDialogView)
                .setPositiveButton("确定", null)
                .setNegativeButton("取消", null).show();
        final Button button = changePwdDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(clickOnChangePwdDialogOkBtn);
    }
    private View.OnClickListener clickOnChangePwdDialogOkBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String oldPwd = getTextOf(pwdDialogView, R.id.et_change_pwd_old_pwd, null);
            if(oldPwd==null||"".equals(oldPwd.trim())){
                alert("输入的原始口令为空！不能修改口令！");
                return;
            }
            String newPwd = getTextOf(pwdDialogView,R.id.et_change_pwd_new_pwd,null);
            if(newPwd==null||"".equals(newPwd.trim())){
                alert("输入的新口令为空！不能修改口令！");
                return;
            }
            String newPwdConfirm = getTextOf(pwdDialogView,R.id.et_change_pwd_new_pwd_confirm,null);
            if(newPwdConfirm==null||"".equals(newPwdConfirm.trim())){
                alert("输入确认口令为空！不能修改口令！");
                return;
            }
            if(!newPwd.equals(newPwdConfirm)){
                alert("确认口令不一致！不能修改口令！");
                return;
            }
            HttpUtils httpUtils = new HttpUtils();
            Context context = com.fortune.car.app.activity.RichFriend.this;
            String url;
            try {
                url = ComParams.HTTP_CHANGE_PWD+"phone="+User.getUserId(context)+
                        "&token="+User.getToken(context)+"&oldPwd="+ MD5Utils.getMD5String(oldPwd)+"&newPwd="+MD5Utils.getMD5String(newPwd);
            } catch (NoSuchAlgorithmException e) {
                alert("无法修改密码，发生了无法理解的MD5编码错误："+e.getLocalizedMessage());
                return;
            } catch (Exception e){
                alert("无法修改密码，发生了无法理解的错误："+e.getLocalizedMessage());
                return;
            }
            progDialog.show();
            httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    progDialog.dismiss();
                    String result = responseInfo.result;
                    JsonBean jsonBean = JsonUtils.fromJsonString(JsonBean.class,result);
                    if(jsonBean!=null){
                        if(jsonBean.isSuccess()){
                            User.saveToken(RichFriend.this,null);
                            alert("口令修改成功！将强制重新登录！", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                }
                            });
                        }else{
                            alert("口令修改失败："+jsonBean.getMessage());
                        }
                    }else{
                        alert("发生了未知的错误，无法确认口令修改结果！");
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    progDialog.dismiss();
                    alert("口令修改失败：" + msg);
                }
            });
        }
    };
    private View.OnClickListener clickOnChangePwd=new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            showChangePwdDialog();
        }
    };
    private View.OnClickListener clickOnLogout = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            new AlertDialog.Builder(view.getContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("确认注销")
                    .setMessage("您确认要暂时离开吗？")
                    .setPositiveButton("是的，我确认！", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    User.saveToken(RichFriend.this, null);
                                    Toast.makeText(RichFriend.this, "已经注销", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                    )
                    .setNegativeButton("矮油，按错了！", null)
                    .show();

        }
    };

    private ButtonBean[] buttonBeans=new ButtonBean[]{
            new ButtonBean(R.id.btn_car_friend_change_modify_nickname,clickOnModifyNickName),
            new ButtonBean(R.id.btn_car_friend_change_modify_picture,clickOnModifyPicture),
            new ButtonBean(R.id.btn_car_friend_change_logout,clickOnLogout),
            new ButtonBean(R.id.btn_car_friend_change_pwd,clickOnChangePwd)
    };
    public class ButtonBean{
        private int buttonId;
        private View.OnClickListener onClickListener;

        public ButtonBean(int buttonId, View.OnClickListener onClickListener) {
            this.buttonId = buttonId;
            this.onClickListener = onClickListener;
        }

        public int getButtonId() {
            return buttonId;
        }

        public void setButtonId(int buttonId) {
            this.buttonId = buttonId;
        }

        public View.OnClickListener getOnClickListener() {
            return onClickListener;
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }
    }
}
