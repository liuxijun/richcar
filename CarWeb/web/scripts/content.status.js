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
    var s_name = "δ֪";
    switch(parseInt(status)){
        case _STATUS_OFFLINE: s_name = "����";break;
        case _STATUS_ONLINE: s_name = "����"; break;
        case _STATUS_WATING: s_name = "�ȴ�"; break;
        case _STATUS_WATING_ONLINE: s_name = "�ȴ�����"; break;
        case _STATUS_WATING_OFFLINE: s_name = "�ȴ�����"; break;
        case _STATUS_SOURCE_MISSED: s_name = "Դ�ļ���ʧ"; break;
        case _STATUS_WATING_TRANS: s_name = "�ȴ�ת��"; break;
        case _STATUS_TRANSING: s_name = "����ת��"; break;
        case _STATUS_TRANSING_ERROR: s_name = "ת��ʧ��"; break;
        case _STATUS_UN_AUDIT: s_name = "����"; break;
        case _STATUS_AUDIT_REJECT: s_name = "���δͨ��"; break;
    }
    return s_name;
}
