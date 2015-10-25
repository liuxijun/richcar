package com.fortune.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by xjliu on 2015/8/30.
 *
 */
public class User {
    public static final String TAG = "User";

    // ------------------------------------------------------------------------------------------
    public static final String SHAREDPREFERENCES_ACCESS = "ACCESS";
    public static final String SHAREDPREFERENCES_ACCESS_TOKEN = "ACCESS_TOKEN";
    public static String SHAREDPREFERENCES_ACCESS_TOKEN_NEW = "";
    public static String SHAREDPREFERENCES_ACCESS_TOKEN_OLD = "";
    public static final String SHAREDPREFERENCES_ACCESS_PHONE = "ACCESS_PHONE";
    public static final String SHAREDPREFERENCES_USER_INFO_FILENAME = "USER_INFO";
    public static final String SHAREDPREFERENCES_USER_INFO_USERNAME = "USER_NAME";
    public static final String SHAREDPREFERENCES_USER_INFO_PASSWORD = "PASSWORD";
    public static final String SHAREDPREFERENCES_USER_INFO_USER_ID = "USER_ID";
    public static final String SHAREDPREFERENCES_USER_INFO_USER_BIND_ID = "USER_BIND_ID";
    /** �û��Ƿ��½ */
    public static final String SHAREDPREFERENCES_USER_INFO_ISLOGIN = "IS_LOGIN";
    /** �û��ǳ� */
    public static final String SHAREDPREFERENCES_USER_INFO_NICKNAME = "NICKNAME";
    /** �û�ͷ�� */
    public static final String SHAREDPREFERENCES_USER_INFO_HEADIMG = "HEAD_IMG";

    // ------------------------------------------------------------------------------------------

    /**
     * �ж��û��Ƿ��Ѿ���¼
     *
     * @param context
     * @return �ѵ�¼������true�����򣬷���false
     */
    public static boolean isLogined(Context context) {
        // SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCES_USER_INFO_FILENAME,
        // Context.MODE_PRIVATE);
        // return sp.getBoolean(SHAREDPREFERENCES_USER_INFO_ISLOGIN, false);
        //
        return !TextUtils.isEmpty(getToken(context));
    }

    /**
     * ����tokenֵ
     *
     * @param context
     * @param token
     * @return �����Ƿ�ɹ�
     */

    public static synchronized boolean saveToken(Context context, String token) {
        return context.getSharedPreferences(SHAREDPREFERENCES_ACCESS, Context.MODE_PRIVATE).edit()
                .putString(SHAREDPREFERENCES_ACCESS_TOKEN, token).commit();
    }

    /**
     * ��ȡtokenֵ
     *
     * @param context
     * @return token, ��������ڣ��ⷵ�ؿմ�
     */
    public static synchronized String getToken(Context context) {
        if (!TextUtils.isEmpty(SHAREDPREFERENCES_ACCESS_TOKEN_NEW)) {
            saveToken(context, SHAREDPREFERENCES_ACCESS_TOKEN_NEW);
            SHAREDPREFERENCES_ACCESS_TOKEN_NEW = "";
        }

        SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCES_ACCESS, Context.MODE_PRIVATE);
        return SHAREDPREFERENCES_ACCESS_TOKEN_OLD = sp.getString(SHAREDPREFERENCES_ACCESS_TOKEN, "");
    }

    /**
     * �����ֻ�����
     *
     * @param context
     * @param phone
     * @return �����Ƿ�ɹ�
     */

    public static boolean savePhone(Context context, String phone) {
        //Log.d(TAG,"savePhone phone = " + phone);
        return context.getSharedPreferences(SHAREDPREFERENCES_ACCESS, Context.MODE_PRIVATE).edit()
                .putString(SHAREDPREFERENCES_ACCESS_PHONE, phone).commit();

    }

    /**
     * ��ȡ�ֻ�����
     *
     * @param context
     * @return phone, ��������ڣ��ⷵ�ؿմ�
     */
    public static String getPhone(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCES_ACCESS, Context.MODE_PRIVATE);
        return sp.getString(SHAREDPREFERENCES_ACCESS_PHONE, "");
    }

    /**
     * ����uid��userBindIdֵ
     *
     * @param context
     * @param userId
     * @param userBindId
     * @return �����Ƿ�ɹ�
     */
    public static boolean saveUserId(Context context, String userId, String userBindId) {
        //Log.d(TAG,"saveUserId uid = " + userId + "BindId = " + userBindId);
        return context.getSharedPreferences(SHAREDPREFERENCES_USER_INFO_FILENAME, Context.MODE_PRIVATE).edit()
                .putString(SHAREDPREFERENCES_USER_INFO_USER_ID, userId)
                .putString(SHAREDPREFERENCES_USER_INFO_USER_BIND_ID, userBindId).commit();
    }

    /**
     * ����uidֵ
     *
     * @param context
     * @param userId
     * @return �����Ƿ�ɹ�
     */
    public static boolean saveUserId(Context context, String userId) {
        //Log.d(TAG,"saveUserId uid = " + userId);
        return context.getSharedPreferences(SHAREDPREFERENCES_USER_INFO_FILENAME, Context.MODE_PRIVATE).edit()
                .putString(SHAREDPREFERENCES_USER_INFO_USER_ID, userId).commit();
    }

    /**  @deprecated see {@link #saveToken(android.content.Context, String)
     *
     * �����û���½״̬
     *
     * @param context
     * @param isLogin
     * @return �����Ƿ�ɹ�
     */
    public static boolean saveUserLoginStatu(Context context, boolean isLogin) {
        // ULog.d( "saveUserLoginStatu isLogin = " + isLogin);
        // return context.getSharedPreferences(SHAREDPREFERENCES_USER_INFO_FILENAME, Context.MODE_PRIVATE).edit()
        // .putBoolean(SHAREDPREFERENCES_USER_INFO_ISLOGIN, isLogin).commit();
        saveToken(context, "");
        return false;
    }

    /**
     * �����û��ǳ�
     *
     * @param context
     * @param nickname
     * @return �����Ƿ�ɹ�
     */
    public static boolean saveUserNickname(Context context, String nickname) {
        //Log.d(TAG,"saveUserNickname nickname = " + nickname);
        return context.getSharedPreferences(SHAREDPREFERENCES_USER_INFO_FILENAME, Context.MODE_PRIVATE).edit()
                .putString(SHAREDPREFERENCES_USER_INFO_NICKNAME, nickname).commit();
    }

    /**
     * �����û�ͷ��
     *
     * @param context
     * @param imgid
     * @return �����Ƿ�ɹ�
     */
    public static boolean saveUserHeadimg(Context context, int imgid) {
        //Log.d(TAG,"saveUserHeadimg imgid = " + imgid);
        return context.getSharedPreferences(SHAREDPREFERENCES_USER_INFO_FILENAME, Context.MODE_PRIVATE).edit()
                .putInt(SHAREDPREFERENCES_USER_INFO_HEADIMG, imgid).commit();
    }

    /**
     * ��ȡ�û�ͷ����
     *
     * @param context
     * @return headImgid, ��������ڣ��ⷵ�ؿմ�
     */
    public static int getUserHeadimgId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCES_USER_INFO_FILENAME, Context.MODE_PRIVATE);
        return sp.getInt(SHAREDPREFERENCES_USER_INFO_HEADIMG, 0);
    }

    /**
     * ��ȡ�û��ǳ�
     *
     * @param context
     * @return nickname, ��������ڣ��ⷵ�ؿմ�
     */
    public static String getUserNickname(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCES_USER_INFO_FILENAME, Context.MODE_PRIVATE);
        return sp.getString(SHAREDPREFERENCES_USER_INFO_NICKNAME, "");
    }

    /**
     * ��ȡuidֵ
     *
     * @param context
     * @return uid, ��������ڣ��ⷵ�ؿմ�
     */
    public static String getUserId(Context context) {
        // return context.getPackageName();
        SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCES_USER_INFO_FILENAME, Context.MODE_PRIVATE);
        return sp.getString(SHAREDPREFERENCES_USER_INFO_USER_ID, "");
    }
}
