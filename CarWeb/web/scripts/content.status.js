var _STATUS_OFFLINE = 1;
var _STATUS_ONLINE = 2;
var _STATUS_WATING = 100;
var _STATUS_WATING_ONLINE = 3;
var _STATUS_WATING_OFFLINE = 6;
var _STATUS_SOURCE_MISSED= 10;
var _STATUS_WATING_TRANS = 200;
var _STATUS_TRANSING = 201;
var _STATUS_TRANSING_ERROR = 202;
var _STATUS_UN_AUDIT = 300;
var _STATUS_AUDIT_REJECT = 301;

function get_status_name(status){
    var s_name = "未知";
    switch(parseInt(status)){
        case _STATUS_OFFLINE: s_name = "下线";break;
        case _STATUS_ONLINE: s_name = "上线"; break;
        case _STATUS_WATING: s_name = "等待"; break;
        case _STATUS_WATING_ONLINE: s_name = "等待上线"; break;
        case _STATUS_WATING_OFFLINE: s_name = "等待下线"; break;
        case _STATUS_SOURCE_MISSED: s_name = "源文件丢失"; break;
        case _STATUS_WATING_TRANS: s_name = "等待转码"; break;
        case _STATUS_TRANSING: s_name = "正在转码"; break;
        case _STATUS_TRANSING_ERROR: s_name = "转码失败"; break;
        case _STATUS_UN_AUDIT: s_name = "待审"; break;
        case _STATUS_AUDIT_REJECT: s_name = "审核未通过"; break;
    }
    return s_name;
}
