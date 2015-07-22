package com.fortune.threeScreen.common;


public class Constants {
    //用户类型
    public static final int USER_TYPE_MOBILE = 1;
    public static final int USER_TYPE_PC = 2;
    public static final int USER_TYPE_IPTV = 3;

    //返回状态结果码
    public static final int RESULT_CODE_NORMAL = 1;
    public static final int RESULT_CODE_EXCEPTION = 2;

    //内容应用属性
    public static final int SERVICE_TYPE_WAP_VIDEO = 1;
    public static final int SERVICE_TYPE_BROADBAND_VIDEO = 2;
    public static final int SERVICE_TYPE_IPTV_VIDEO = 3;
    public static final int SERVICE_TYPE_IPONE_VIDEO = 4;
    public static final int SERVICE_TYPE_ANDROID_VIDEO = 5;
    public static final int SERVICE_TYPE_WAP_AND_ANDROID_VIDEO = 6;

    //标签类型
    public static final int BOOKMARK_TYPE_DEMAND = 1;
    public static final int BOOKMARK_TYPE_LIVE = 2;
    public static final int BOOKMARK_TYPE_READ = 3;

    //子内容类型
    public static final int SUBCONTENT_TYPE_POSITIVE = 1;
    public static final int SUBCONTENT_TYPE_SERIES = 2;
    public static final int SUBCONTENT_TYPE_VIDEOS = 3;
    public static final int SUBCONTENT_TYPE_TRAILERS = 4;
    public static final int SUBCONTENT_TYPE_HIGHLIGHTS = 5;
    public static final int SUBCONTENT_TYPE_POSTERS = 6;
    public static final int SUBCONTENT_TYPE_STILLS = 7;
    public static final int SUBCONTENT_TYPE_OTHERS = 99;

    //推荐模式
    public static final int MODE_ALL_FRIENDS = 1;
    public static final int MODE_FRIEND_LIST = 2;

    //推荐信息状态
    public static final int STATUS_UNREAD = 1;
    public static final int STATUS_READ = 2;

    //用户关联状态结果码
    public static final int USER_BINDING_RESULT_CODE_RELATED = 1;
    public static final int USER_BINDING_RESULT_CODE_UNRELATED = 2;
    public static final int USER_BINDING_RESULT_CODE_EXCEPTION = 3;

    //操作推荐信息类型
    public static final int OPERATION_TYPE_LIST_DEL = 1;
    public static final int OPERATION_TYPE_ALL = 2;
    public static final int OPERATION_TYPE_LIST_STATUS = 3;
}
