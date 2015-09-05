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

        }
    };
    private View.OnClickListener clickOnModifyPicture=new View.OnClickListener(){
        @Override
        public void onClick(View view) {

        }
    };
    private AlertDialog changePwdDialog;
    private View pwdDialogView;
    private void showChangePwdDialog(){
        LayoutInflater inflater = getLayoutInflater();
        pwdDialogView = inflater.inflate(R.layout.dialog_change_pwd,
                (ViewGroup) findViewById(R.id.dialog_changepwd_main_body));
        new AlertDialog.Builder(this).setTitle("�������˺ſ����¼").setView(pwdDialogView)
                .setPositiveButton("ȷ��", null)
                .setNegativeButton("ȡ��", null).show();
        final Button button = changePwdDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(clickOnChangePwdDialogOkBtn);
    }
    private View.OnClickListener clickOnChangePwdDialogOkBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String oldPwd = getTextOf(pwdDialogView, R.id.et_change_pwd_new_pwd, null);
            if(oldPwd==null||"".equals(oldPwd.trim())){
                alert("�����ԭʼ����Ϊ�գ������޸Ŀ��");
                return;
            }
            String newPwd = getTextOf(pwdDialogView,R.id.et_change_pwd_new_pwd,null);
            if(newPwd==null||"".equals(newPwd.trim())){
                alert("������¿���Ϊ�գ������޸Ŀ��");
                return;
            }
            String newPwdConfirm = getTextOf(pwdDialogView,R.id.et_change_pwd_new_pwd_confirm,null);
            if(newPwdConfirm==null||"".equals(newPwdConfirm.trim())){
                alert("����ȷ�Ͽ���Ϊ�գ������޸Ŀ��");
                return;
            }
            if(!newPwd.equals(newPwdConfirm)){
                alert("ȷ�Ͽ��һ�£������޸Ŀ��");
                return;
            }
            HttpUtils httpUtils = new HttpUtils();
            Context context = com.fortune.car.app.activity.RichFriend.this;
            String url = null;
            try {
                url = ComParams.HTTP_CHANGE_PWD+"phone="+User.getUserId(context)+
                        "&token="+User.getToken(context)+"&oldPwd="+ MD5Utils.getMD5String(oldPwd)+"&newPwd="+MD5Utils.getMD5String(newPwd);
            } catch (NoSuchAlgorithmException e) {
                alert("�޷��޸����룬�������޷�����MD5�������"+e.getLocalizedMessage());
                return;
            } catch (Exception e){
                alert("�޷��޸����룬�������޷����Ĵ���"+e.getLocalizedMessage());
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
                            alert("�����޸ĳɹ���");
                        }else{
                            alert("�����޸�ʧ�ܣ�"+jsonBean.getMessage());
                        }
                    }else{
                        alert("������δ֪�Ĵ����޷�ȷ�Ͽ����޸Ľ����");
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    progDialog.dismiss();
                    alert("�����޸�ʧ�ܣ�" + msg);
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
                    .setTitle("ȷ��ע��")
                    .setMessage("��ȷ��Ҫ��ʱ�뿪��")
                    .setPositiveButton("�ǵģ���ȷ�ϣ�", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    User.saveToken(RichFriend.this, null);
                                    Toast.makeText(RichFriend.this, "�Ѿ�ע��", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                    )
                    .setNegativeButton("���ͣ������ˣ�", null)
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
