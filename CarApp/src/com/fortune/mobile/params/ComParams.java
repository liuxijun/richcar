package com.fortune.mobile.params;

/**
 * 公共数据
 * 
 * @author dongfang
 * 
 */
public class ComParams {
	public static String userAgent=null;//播放器的代理
	public static final boolean IS_PLAYER_ACTIVITY = true; // 使用内置播放器播放
	public static final boolean LIVE_PLAYER_USE_VOD_PLAYER_ACTIVITY = true; // 使用内置播放器播放
	// ------------------------------------------------------------------------------

	public static final int MOVIE_TYPE_VOD = 0;// 点播
	public static final int MOVIE_TYPE_Live = 1; // 直播
	public static final int RESULT_CODE_WEB_VIEW_DETAIL=1008;
	public static final int RESULT_CODE_WEB_VIEW_SPECIAL_LIST=RESULT_CODE_WEB_VIEW_DETAIL+1;
	public static final int RESULT_CODE_WEB_VIEW_CHANNEL_LIST=RESULT_CODE_WEB_VIEW_DETAIL+2;
	public static final int RESULT_CODE_WEB_VIEW_LIVE_LIST=RESULT_CODE_WEB_VIEW_DETAIL+3;
	public static final int RESULT_CODE_WEB_VIEW_OPTION=RESULT_CODE_WEB_VIEW_DETAIL+4;
	public static final int RESULT_CODE_WEB_VIEW_VOD=RESULT_CODE_WEB_VIEW_DETAIL+5;
	public static final int RESULT_CODE_WEB_VIEW_HOME=RESULT_CODE_WEB_VIEW_DETAIL+6;
	public static final int RESULT_CODE_WEB_VIEW_DOWNLOAD = RESULT_CODE_WEB_VIEW_DETAIL+7;
	// ------------------------------------------------------------------------------

	public static final String INTENT_TODO = "TODO";
	public static final String INTENT_HOMEBEAN = "homebean";
	public static final String INTENT_URL = "INTENT_URL";
	public static final String INTENT_CAR_LIST = "intentCarList";
	public static final String INTENT_CAR_BEAN = "intentCarBean";
	public static final String INTENT_CAR_CONDUCTS ="intentCarConducts";
	public static final String INTENT_ORDER_BEAN = "order";

	// ------------------------------------------------------------------------------
	//private static final String HTTP_BASE = "http://test.fortune-net.cn:8087";
	//private static final String HTTP_BASE = "http://iptv.inhe.net";
	//private static final String HTTP_BASE = "http://103.21.116.242";
    public static final String HTTP_BASE = "http://192.168.1.10";
	private static final String HTTP_PROJECT_NAME="/cars/";
	public static boolean displayDownloadAPK=false;
	public static boolean displayZT=false;
	public static boolean displayVIP=false;
	public static boolean displayMyOrders=false;
	/** 首页 */
	public static final String HTTP_HOME = HTTP_BASE + HTTP_PROJECT_NAME+"?jsonFormat=true";
	/**  下载客户端  **/
	public static final String HTTP_DOWNOLAD_PLAYER = HTTP_BASE + "/apk/download.jsp?fileName=";
	public static final String HTTP_DOWNOLAD_PLAYER_PAGE = HTTP_BASE + "/apk/player.jsp";
	/**  下载客woxuan客户端  **/
	public static final String HTTP_DOWNOLAD_WOXUAN = HTTP_BASE + "/apk/download.jsp?fileName=woxuan.apk";
	public static final String HTTP_DOWNOLAD_WOXUAN_PAGE = HTTP_BASE + HTTP_PROJECT_NAME+"download.html";
	/**  下载客woxin客户端  **/
	public static final String HTTP_DOWNOLAD_WOXIN = HTTP_BASE + "/apk/download.jsp?fileName=woxin.apk";
	public static final String HTTP_DOWNOLAD_WOXIN_PAGE = HTTP_BASE + HTTP_PROJECT_NAME+"download.html";
	/** 专区列表页  **/
	public static final String HTTP_SPECIAL_TOPIC = HTTP_BASE + HTTP_PROJECT_NAME+"special.jsp?fromClient=true";
	
	/** 直播 */
	public static final String HTTP_LIVE = HTTP_BASE + HTTP_PROJECT_NAME+"live.jsp?jsonFormat=true";
	/** 点播 */
	public static final String HTTP_VOD = HTTP_BASE + HTTP_PROJECT_NAME+"vod.jsp?jsonFormat=true";
	/** 详情页 */
	public static final String HTTP_DETAIL = HTTP_BASE + HTTP_PROJECT_NAME+"detail.jsp?jsonFormat=true&";
	/** 验证码 */
	public static final String HTTP_AUTHCODE = HTTP_BASE + "/user/user!createVerifyCode.action?";
	/** 登陆 */
	public static final String HTTP_LOGIN = HTTP_BASE + "/mobile/doLogin.jsp?";
	/** 车辆信息，基本信息 */
	public static final String HTTP_LIST_CAR = HTTP_BASE + "/mobile/car.jsp?";
	/** 车辆信息，基本信息 */
	public static final String HTTP_LIST_CONDUCTS = HTTP_BASE + "/mobile/conducts.jsp?";
	/** 列表页， 频道页 */
	public static final String HTTP_CHANNEL = HTTP_BASE + "/page/js/list.jsp?";
	/** 搜索 */
	public static final String HTTP_SEARCH = HTTP_BASE + "/page/js/list.jsp?isChannelIds=true&";
	/** 播放鉴权 */
	public static final String HTTP_PLAYAUTH = HTTP_BASE + "/user/user!checkPlayPermissions.action?";
	/** 鉴权不通过，获取订购列表 */
	public static final String HTTP_ORDERLIST = HTTP_BASE + HTTP_PROJECT_NAME+"buyList.jsp?jsonFormat=true&";
	/** 订购产品和退订产品 */
	public static final String HTTP_SUBSCRIPTION_PRODUCT = HTTP_BASE + "/user/user!operateOrder.action?";
    /*** 获取某个推荐 **/
    public static final String HTTP_RECOMMEND_CONTENTS = HTTP_BASE+"/page/js/recommends.jsp?propertyIds=PC_MEDIA_POSTER_BIG,MEDIA_NAME&ids=";
	/** 播放地址 */
	public static final String HTTP_PLAYURL = HTTP_BASE
			+ "/user/getPlayUrl.jsp?jsonFormat=true&clientType=m3u8&clipId=1&";
	/** 播放记录 */
	public static final String HTTP_HISTORY = HTTP_BASE
			+ HTTP_PROJECT_NAME+"userOptions.jsp?jsonFormat=true&command=listPlayHistory&";
	/** 增加播放记录 */
	public static final String HTTP_HISTORY_ADD = HTTP_BASE
			+ HTTP_PROJECT_NAME+"userOptions.jsp?jsonFormat=true&command=addPlayHistory&";
	/** 删除播放记录 */
	public static final String HTTP_HISTORY_DEL = HTTP_BASE
			+ HTTP_PROJECT_NAME+"userOptions.jsp?jsonFormat=true&command=removePlayHistory&";
	/** 个人收藏 */
	public static final String HTTP_FAVORITE = HTTP_BASE
			+ HTTP_PROJECT_NAME+"userOptions.jsp?jsonFormat=true&command=listFavorite&";
	/** 添加个人收藏 */
	public static final String HTTP_FAVORITE_ADD = HTTP_BASE
			+ HTTP_PROJECT_NAME+"userOptions.jsp?jsonFormat=true&command=addFavorite&";
	/** 删除个人收藏 */
	public static final String HTTP_FAVORITE_DEL = HTTP_BASE
			+ HTTP_PROJECT_NAME+"userOptions.jsp?jsonFormat=true&command=removeCollect&";

	/** 我的订阅 */
	public static final String HTTP_MY_ORDER = HTTP_BASE
			+ HTTP_PROJECT_NAME+"userOptions.jsp?command=myBilling&jsonFormat=true&";

	/** 通过IP寻找手机号码 */
	public static final String HTTP_GET_TOKEN_BY_UUID = HTTP_BASE
			+ HTTP_PROJECT_NAME+"uniLogin.jsp?jsonFormat=true&status=8000&unikey=";
	/** 启动验证 */
	public static final String HTTP_VERIFY_TOKEN = HTTP_BASE + HTTP_PROJECT_NAME+"verifyToken.jsp?fromClient=true&";
    /**搜索热词**/
    public static final String HTTP_SEARCH_HOT_WORDS=HTTP_BASE+"/page/js/searchHot.jsp";
    public static final String HTTP_SEARCH_CHANNELS=HTTP_BASE+"/page/js/channels.jsp?withSubChannels=true&id=";
	/** 获取升级信息 */
	public static final String HTTP_GET_UPDATE = HTTP_BASE + HTTP_PROJECT_NAME+"checkUpdateAndroid.jsp";

	// ------------play view mode and frompage ---------
	// IsFromPage: IsFromPage, IsFromRecommend
	/************** MediaplayActivity播放器用到的常量参数：playMode: Local , Program , schedule, Live *******************/
	public static final String PLAY_VIEW_LOCAL = "local";
	public static final String PLAY_VIEW_PROGRAM = "program";
	public static final String PLAY_VIEW_SCHEDULE = "schedule";
	public static final String PLAY_VIEW_LIVE = "live";

	public static final String PLAY_KEY_FROMPLACE = "fromPlace";
	public static final String PLAY_KEY_PLAYMODE = "playMode";
	public static final String PLAY_KEY_DETAILURL = "detailURL";
	public static final String PLAY_KEY_VIDEOURL = "videoURL";

	public static final String PLAY_BAND_LIUCHANG = "流畅";
	public static final String PLAY_BAND_QINGXI = "清晰";
	public static final String PLAY_BAND_GAOQING = "高清";
	public static final String PLAY_BAND_CHAOQING = "超清";

}