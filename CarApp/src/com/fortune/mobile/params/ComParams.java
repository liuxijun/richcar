package com.fortune.mobile.params;

/**
 * 公共数据
 * 
 * @author dongfang
 * 
 */
public class ComParams {
	public static String userAgent=null;//播放器的代理
	// ------------------------------------------------------------------------------

	public static final String INTENT_CAR_LIST = "intentCarList";
	public static final String INTENT_CAR_BEAN = "intentCarBean";
	public static final String INTENT_CAR_CONDUCTS ="intentCarConducts";

	// ------------------------------------------------------------------------------
	//private static final String HTTP_BASE = "http://test.fortune-net.cn:8087";
	//private static final String HTTP_BASE = "http://iptv.inhe.net";
	public static final String HTTP_BASE = "http://103.21.116.242";
	//public static final String HTTP_BASE = "http://192.168.1.161";
    //public static final String HTTP_BASE = "http://192.168.1.10";
	/** 登陆 */
	public static final String HTTP_LOGIN = HTTP_BASE + "/mobile/doLogin.jsp?";
	/** 车辆信息，基本信息 */
	public static final String HTTP_LIST_CAR = HTTP_BASE + "/mobile/car.jsp?";
	/** 车辆信息，基本信息 */
	public static final String HTTP_LIST_CONDUCTS = HTTP_BASE + "/mobile/conducts.jsp?";
	public static final String HTTP_CHANGE_PWD = HTTP_BASE+"/mobile/users.jsp?command=changePwd&";
	public static final String HTTP_LIST_CAR_REPAIRS=HTTP_BASE+"/mobile/repair.jsp?type=";
	/** 获取升级信息 */
	//public static final String HTTP_GET_UPDATE = HTTP_BASE + HTTP_PROJECT_NAME+"checkUpdateAndroid.jsp";

	// ------------play view mode and frompage ---------
}