package com.fortune.common;

import org.springframework.context.ApplicationContext;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-3-31
 * Time: 14:29:06
 *
 */
public class Constants {
    public static final long publicIPTVOrgId=2;
    public static final long publicIptvVODNodeId=92;
	public static final String SESSION_ADMIN ="sessionOperator";
	public static final String SESSION_ADMIN_PERMISSION ="sessionOperatorPermission";
    public static final String SESSION_ADMIN_MENUS ="sessionOperatorMenus";
    public static final String SESSION_ADMIN_CAN_VISIT_URLS ="adminCanVisitUrls";

    // added by mlwang @2014-11-10
    // 前台用户Session中数据名称
    public static final String SESSION_FRONT_USER = "sessionFrontUser";
    public static final String SESSION_FRONT_USER_CHANNEL = "sessionFrontUserChannel";
    public static final String SESSION_FRONT_USER_TOP_CHANNEL = "sessionFrontUserTopChannel";
    // end of add

    public static final String SESSION_BEAN="sessionBeanName";
    public static final String SESSION_USER_ID="sessionUserId";
    public static final String SESSION_ORG_ID="sessionOrgId";
    public static final String SESSION_USER_NAME="sessionUserName";
    public static final String SESSION_ORG_NAME="sessionOrgName";
    public static final String SESSION_USER_SPID = "sessionUserSpId";
    public static final String SESSION_ADMIN_CSP = "sessionAdminLoginCsp";
    public static final String USER_PHONE_NUMBER="userPhoneNumber";

    public final static String ACTION_SAVE="save";//add or edit
    public final static String ACTION_DELETE="delete";
    public final static String ACTION_ADD="view";
    public final static String ACTION_EDIT="edit";
    public final static String ACTION_TO_EDIT="toEdit";
    public final static String ACTION_LIST="list";
    public final static String ACTION_SEARCH="search";
    public final static String ACTION_BIND="bind";
    public final static String ACTION_TOBIND="toBind";
    public final static String ACTION_LOCK="lock";
    public final static String ACTION_UPDATE="view";
    public final static String ACTION_CREATE_SIMILARITY="createSimilarity";
    public final static String USER_NAVIGATION="navigation";
    public final static String ACTION_CHECK="check";
    public final static String ACTION_TO_CHECK="toCheck";
    public final static String ACTION_SHOW="show";
    public final static String ACTION_VIEW="view";
    public final static String ACTION_NO_PERMISSION="nopermission";
    
    public final static int MEDIA_ONLINE = 2;
    public final static int MEDIA_OFFLINE = 1;
    public static final String CspIdToCspChannel=null;

    //扣费系统常量
    public static final String SESSION_USER = "MediaStackUser";
    public static final String USER_MEDIA_SESSION_HEAD = "play_";
    public static final String SESSION_CORPERATION_ID = "MediaStackCorperationId";
    public static final int PLAY_VALID_TIME = 24;

    public static final long MEDIA_FORMAT_TYPE_MICROSOFT = 1L;
    public static final long MEDIA_FORMAT_TYPE_REAL = 2L;

    public static ApplicationContext SPRING_APPLICATION_CONTEXT;
}
