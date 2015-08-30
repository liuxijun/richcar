package com.fortune.util;

/**
 * 各种类型值
 * 
 * @author dongfang
 * 
 */
public class Types {
	// ----终端类型--------------------------------------------------------------------------------------
	public static final String CLIENTTYPE_TV = "1";
	public static final String CLIENTTYPE_PC = "2";
	public static final String CLIENTTYPE_ANDROIDMOBILE = "3";
	public static final String CLIENTTYPE_ANDROIDPAD = "4";
	public static final String CLIENTTYPE_IPHONE = "5";
	public static final String CLIENTTYPE_IPAD = "6";

	// ----账户绑定类型--------------------------------------------------------------------------------------
	public static final String ACCOUNT_TYPE_EMAIL = "7";
	public static final String ACCOUNT_TYPE_PHONE = "9";

	// ----播放类型，节目类型--------------------------------------------------------------------------------------
	/** 节目类型 : 点播节目 */
	public static final String PROGRAMTYPE_VOD = "1";
	/** 节目类型 : 回看 */
	public static final String PROGRAMTYPE_TVOY = "2";
	/** 节目类型 : 直播频道 */
	public static final String PROGRAMTYPE_LIVE = "3";
	/** 节目类型 : 剧集 */
	public static final String PROGRAMTYPE_TVPLAY = "4";
	/** 播放类型 : 点播节目 */
	public static final String PLAYTYPE_VOD = "1";
	/** 节目类型 : 回看 */
	public static final String PLAYTYPE_TVOY = "2";
	/** 播放类型 : 直播频道 */
	public static final String PLAYTYPE_LIVE = "3";
	/** 节目类型 : 剧集 */
	public static final String PLAYTYPE_TVPLAY = "4";
	/** 节目类型 : 无效类型 */
	public static final String PLAYTYPE_UNVAILD = "5";

	// ----下载类型--------------------------------------------------------------------------------------
	public static final int DOWNLOAD_TYPE_VIDEO = 0; // 视频
	public static final int DOWNLOAD_TYPE_IMAGE = 1; // 图片
	public static final int DOWNLOAD_TYPE_TEXT = 2; // 文件
	public static final int DOWNLOAD_TYPE_APP = 3; // 应用
	public static final int DOWNLOAD_TYPE_AUDIO = 4; // 音频
	public static final int DOWNLOAD_TYPE_OTHER = 5; // 其他

	// //
	// ----产品编号--------------------------------------------------------------------------------------
	// /** 产品编号 : 直播客户端 */
	// public static final String PRODUCTTYPE_LIVECLIENT = "1";
	// /** 产品编号 : 互联网电视 */
	// public static final String PRODUCTTYPE_INTERNETTV = "2";
	// /** 产品编号 : 互联网电视海外版 */
	// public static final String PRODUCTTYPE_INTERNETTV_ABROAD = "3";
	// /** 产品编号 : 天翼视讯4.0客户端 */
	// public static final String PRODUCTTYPE_VIDEOCLIENT_4 = "4";
	// /** 产品编号 : 全客户端 */
	// public static final String PRODUCTTYPE_VIDEOCLIENT = "4";

	// ----删除类型--------------------------------------------------------------------------------------
	/** 删除类型 : 单个删除 */
	public static final String DELETETYPE_ONE = "1";
	/** 删除类型 : 全部删除 */
	public static final String DELETETYPE_ALL = "2";

	// ----是否升级--------------------------------------------------------------------------------------
	/** 是否升级 : 0：不需要升级 */
	public static final int UPDATETYPE_NO = 0;
	/** 是否升级 : 1：强制升级 */
	public static final int UPDATETYPE_FORCE = 1;
	/** 是否升级 : 2：可选升级 */
	public static final int UPDATETYPE_OPTIONAL = 2;
	/** 是否升级 : 3：手动触发升级 */
	public static final int UPDATETYPE_MANUAL = 3;

	// ----客户端语种--------------------------------------------------------------------------------------
	/** 客户端语种 : 1: 简体中文 */
	public static final String LANGUAGETYPE_ZH_CN = "1";
	/** 客户端语种 : 2: 繁体中文 */
	public static final String LANGUAGETYPE_ZH_TW = "2";
	/** 客户端语种 : 3: 英文 */
	public static final String LANGUAGETYPE_EN = "3";

	// ----排序依据--------------------------------------------------------------------------------------
	/** 排序依据 : 1. 时间 */
	public static final String SORETYPE_TIME = "1";
	/** 排序依据 : 2. 点播数 */
	public static final String SORETYPE_VOD = "2";
	/** 排序依据 : 3. 评论数 */
	public static final String SORETYPE_COMMENT = "3";
	/** 排序依据 : 4. 收藏数 */
	public static final String SORETYPE_FAVORITE = "4";
	/** 排序依据 : 5. 评分数 */
	public static final String SORETYPE_GRADE = "5";
	/** 排序依据 : 1. 时间 */
	public static final String ORDERTYPE_TIME = SORETYPE_TIME;
	/** 排序依据 : 2. 点播数 */
	public static final String ORDERTYPE_VOD = SORETYPE_VOD;
	/** 排序依据 : 3. 评论数 */
	public static final String ORDERTYPE_COMMENT = SORETYPE_COMMENT;
	/** 排序依据 : 4. 收藏数 */
	public static final String ORDERTYPE_FAVORITE = SORETYPE_FAVORITE;
	/** 排序依据 : 5. 评分数 */
	public static final String ORDERTYPE_GRADE = SORETYPE_GRADE;

	// ----用户订购--------------------------------------------------------------------------------------
	/** 按月已经订购 */
	public static final int ORDER_BY_MONTH_ORDERED = 0;
	/** 按月已退订 */
	public static final int ORDER_BY_MONTH_UNORDER = 1;
	/** 手机支付 */
	public static final int ORDER_TYPE_PHONE = 0;
	/** 支付宝支付 */
	public static final int ORDER_TYPE_ALIPAY = 1;

	// ----消息类型--------------------------------------------------------------------------------------
	/** 订阅消息 */
	public static final String MESSAGE_TYPE_SUBSCRIPTION = "0";
	/** 系统公告 */
	public static final String MESSAGE_TYPE_SYSTEM = "1";
	/** 直播提醒 */
	public static final String MESSAGE_TYPE_LIVE = "2";
	/** 活动消息 */
	public static final String MESSAGE_TYPE_INTERACTIVE = "3";
	/** 积分消息 */
	public static final String MESSAGE_TYPE_POINT = "4";
	/** 下载消息 */
	public static final String MESSAGE_TYPE_DOWNLOAD = "6";
	/** 第三方应用消息 */
	public static final String MESSAGE_TYPE_APP = "8";
	/** 影视频道首页消息 */
	public static final String MESSAGE_TYPE_VIDEO = "10";
	/** 资讯频道首页消息 */
	public static final String MESSAGE_TYPE_INFO = "11";

	// ----订阅类型--------------------------------------------------------------------------------------
	/** 已经订阅 */
	public static final int SUBSCRIPTION_TYPE_SUBSCRIBED = 0;
	/** 未订阅 */
	public static final int SUBSCRIPTION_TYPE_UNSUBSCRIBE = 1;
	// ----ListView显示类型------------------------------------------------------------------------------
	/** 浏览模式 */
	public static final int MODE_VIEW = 0;
	/** 编辑模式 */
	public static final int MODE_EDIT = 1;
	// ----积分事件类型----------------------------------------------------------------------------------
	/** 按次订购 */
	public static final String INTEGRAL_ACTION_ORDER_BY_TIME = "EC001";
	/** 按月订购 */
	public static final String INTEGRAL_ACTION_ORDER_BY_MONTH = "EC002";
	/** 邮箱注册 */
	public static final String INTEGRAL_ACTION_REGISTER_BY_EMAIL = "EC003";
	/** 手机注册 */
	public static final String INTEGRAL_ACTION_REGISTER_BY_PHONE = "EC004";
	/** 邮箱激活 */
	public static final String INTEGRAL_ACTION_ACTIVE_EMAIL = "EC005";
	/** 修改头像 */
	public static final String INTEGRAL_ACTION_MODIFY_HEADIMG = "EC006";
	/** 绑定手机 */
	public static final String INTEGRAL_ACTION_BIND_PHONE = "EC007";
	/** 每日签到 */
	public static final String INTEGRAL_ACTION_LOGIN_EVERYDAY = "EC008";
	/** 分享 */
	public static final String INTEGRAL_ACTION_SHARE = "EC009";
	/** 评分 */
	public static final String INTEGRAL_ACTION_MARK = "EC010";
	/** 评论 */
	public static final String INTEGRAL_ACTION_COMMENT = "EC011";
	/** 邀请好友注册成功 */
	public static final String INTEGRAL_ACTION_INVITE_FRIENDS = "EC012";

	// ----获取静态数据类型-------------------------------------------------------------------------------
	/** OMS数据 */
	public static final String STATIC_TYPE_OMS = "omspath";
	/** 平台数据 */
	public static final String STATIC_TYPE_JSON = "jsonpath";

	// ----网络提醒类型-----------------------------------------------------------------------------------
	/** 登陆提醒 */
	public static final int NETWARNING_TYPE_LOADING = 1;
	/** 登陆提醒 */
	public static final int NETWARNING_TYPE_PLAY = 2;
	/** 下载提醒 */
	public static final int NETWARNING_TYPE_DOWNLOAD = 3;

	// ----播放码率QualityId类型-------------------------------------------------------------------------------------
	/** 点播节目 流畅 */
	public static final String VOD_QUALITYID_SMOOTH = "1";
	/** 点播节目 标清 */
	public static final String VOD_QUALITYID_STANDARD_H264 = "2";
	/** 点播节目 标清 */
	public static final String VOD_QUALITYID_STANDARD_MPEG = "4";
	/** 点播节目 高清 */
	public static final String VOD_QUALITYID_HD = "8";
	/** 点播节目 超清 */
	public static final String VOD_QUALITYID_SUPER_H264 = "16";
	/** 点播节目 超清 */
	public static final String VOD_QUALITYID_SUPER_MP4 = "512";
	/** 点播节目 原画 */
	public static final String VOD_QUALITYID_ORIGINAL = "4096";
	/** 直播回看节目 流畅 */
	public static final String LIVE_TVOY_QUALITYID_SMOOTH_MPEG = "1";
	/** 直播回看节目 流畅 */
	public static final String LIVE_TVOY_QUALITYID_SMOOTH_H264 = "2";
	/** 直播回看节目 标清 */
	public static final String LIVE_TVOY_QUALITYID_STANDARD_MPEG = "4";
	/** 直播回看节目 标清 */
	public static final String LIVE_TVOY_QUALITYID_STANDARD_H264 = "8";
	/** 直播回看节目 标清 */
	public static final String LIVE_TVOY_QUALITYID_STANDARD_MP4 = "512";
	/** 直播回看节目 高清 */
	public static final String LIVE_TVOY_QUALITYID_HD = "1024";
	/** 直播回看节目 超清 */
	public static final String LIVE_TVOY_QUALITYID_SUPER = "2048";

}