/**
 * 在界面中常用的一些常量
 */
var nextUrl = null;
var actionHeader = null;
var defaultPageSize = 10;
var keyFieldId = "id";
var viewReadOnly = false;
var canLoadWhenNew = true;
var allStores = [];
//如果添加按钮被点击,则先检查这个addViewPageUrl是否为空，如果不是，则访问这个URL
var addViewPageUrl = null;

var keyId = getParameter(window.location.href, "keyId");
var queryString = getQueryString(window.location.href);
var modifyOldData = !(keyId == "" || keyId == null || keyId == "-1");

var canUseFunctions = ['delete', 'view', 'list', 'add', 'save', 'deleteSelected', 'lock'];
var isIE9 = false;

try {
    //alert(navigator.appVersion);
    isIE9 = navigator.appVersion.indexOf("MSIE 9.0") >= 0;
} catch (e) {

}

var defaultManageLinks = [
    {
        text:'删除',
        action:'delete',
        type:'onclick',
        messageInfo:''
    },
    {
        text:'查看',
        action:'view',
        type:'href',
        messageInfo:''
    }
];
var defaultViewFormButtons = [
    {
        text:'保存数据',
        handler:saveFormAjax,
        action:'save'
    },
    {
        text:'重新加载',
        handler:loadFormAjax,
        action:'view'
    }
];
var defaultBottomButtons = [
    {
        text:'添加记录',
        handler:addRecord,
        action:'add'
    },
    {
        text:'删除所选',
        handler:deleteSelectedRows,
        action:'deleteSelected'
    }
];

var formOptions = {
    formDataLoaded:false,
    beforePost:function () {
            return true;
        },
    afterPost:function () {
        },
    afterLoad:function () {
        },
    afterDelete:function(){
            gotoNextUrl()
        },
    afterSave:function(){
            gotoNextUrl();
        },
    loadForm:function(form){
           return false;
        }
};

var defaultGrid = null;

var sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});

var searchParameterName = "obj.name";

Ext.form.Field.prototype.msgTarget = 'side';

function getAreaStoreText(storeData, val) {
    try {
        return getStoreText(storeData, val, 'value', 'name');
    } catch (e) {
        return val;
    }
}
function getDictStoreText(storeId, val) {
    try {
        return getStoreText(getDictStore(storeId), val, 'value', 'name');
    } catch (e) {
        return val;
    }
}
function getStoreText(storeData, val, valueField, displayField) {
    //    alert("val:"+val+",valF:"+valueField+",disp:"+displayField);
    if (storeData && storeData.data) {
        //        alert("行数："+storeData.data.length);
        for (var i = 0; i < storeData.data.length; i++) {
            if (storeData.getAt(i).get(valueField) == val) {
                return storeData.getAt(i).get(displayField);
            }
        }
    } else {
        outDebugMsg("storeData为空");
    }
    return  val;
}
function getNextUrl() {
    if (nextUrl) {
        //如果是list，就必须去掉keyId这个参数
        return appendParameter(nextUrl, "dateTimeNow", new Date().getTime());
    } else {
        var theUrl = window.location.href;
        var p = theUrl.indexOf("View.jsp");
        if (p > 0) {
            return theUrl.substring(0, p) + "List.jsp";
        }
    }
    return null;
}

function getLinkUrlByActionId(actionId) {
    return actionHeader + actionId.substring(0, 1).toUpperCase() + actionId.substring(1) + ".jsp";
}

function getActionUrl(actionId, parameters) {
    var queryString = getQueryString(window.location.href);
    if (queryString != null) {
        //把url后面的#去掉
        while (queryString.charAt(queryString.length - 1) == '#') {
            // alert("发现#,准备去掉！");
            queryString = queryString.substring(0, queryString.length - 1);
            //alert(queryString);
            if (queryString.length == 0) {
                break;
            }
        }
    }
    if (parameters != null) {
        //把url后面的#去掉
        while (parameters.charAt(parameters.length - 1) == '#') {
            parameters = parameters.substring(0, parameters.length - 1);
            if (parameters.length == 0) {
                break;
            }
        }
    }
    //alert("合并前，queryString="+queryString+"\nparameters="+parameters);
    var overwrite = true;
    var urlParameters = Ext.urlDecode(queryString, overwrite);
    var newParameters = Ext.urlDecode(parameters, overwrite);
    var result = actionHeader + "!" + actionId + ".action";
    urlParameters = Ext.apply(urlParameters, newParameters);
    queryString = Ext.urlEncode(urlParameters);
    if (null != queryString && queryString != "") {
        result += "?" + queryString;
    }
    //alert("合并后，queryString="+queryString);
    outDebugMsg('viewUrl=' + result);
    return result;
}

function gotoNextUrl() {

    var nextUrlResult = getNextUrl();
    if (nextUrlResult != null) {
        window.location.href = nextUrlResult;
    } else {
        if (searchStore) {
            searchStore.load();
        }
    }
}

function saveFormAjax(needConfirm) {
    try {
        var formPanel = Ext.getCmp('BaseViewForm338547183092');
        var dataForm = formPanel.getForm();
        if (dataForm == null) {
            alert("form为空！不能继续！");
        } else {
            if (dataForm.saveUrl) {
                dataForm.url = dataForm.saveUrl;
            }
            if (dataForm.isValid()) {
                //alert(dataForm.getValues(true));
                ///*
                if(typeof(needConfirm)=='undefined'){
                    needConfirm = true;
                }
                if(needConfirm&&!confirm("确认要提交并保存本页数据吗？")){
                    return;
                }
                if (formOptions.beforePost != null) {
                    if(!formOptions.beforePost(dataForm)){
                        return ;
                    }
                }

                //*/
                // alert(dataForm.url);
                dataForm.submit({
                    waitMsg:'保存中,请稍后...',
                    waitTitle:'正在保存',
                    clientValidation:false,
                    url:dataForm.url,
                    success:function (form, action) {
                        var textTitle = dataForm.title;
                        var nameValue = getCmpValue("objName");
                        if(null==nameValue){
                            nameValue = getCmpValue("obj.name");
                        }
                        if (nameValue != null) {
                            textTitle = nameValue;
                        }
                        Ext.Msg.alert("成功", "数据'" + textTitle +
                            "'已经保存！", function () {
                            try {
                                if (typeof(saveCallBackFunction) == 'function') {
                                    saveCallBackFunction();
                                } else {
                                    formOptions.afterSave(form);
                                }
                            } catch (e) {
                                alert("发生异常：");
                                formOptions.afterSave(form);
                            }
                        });
                    },
                    failure:function (form, action) {
                        switch (action.failureType) {
                            case Ext.form.Action.CLIENT_INVALID:
                                Ext.Msg.alert('Failure', 'Form fields may not be submitted with invalid values');
                                break;
                            case Ext.form.Action.CONNECT_FAILURE:
                                Ext.Msg.alert('Failure', '连接通讯失败！可能服务器已经关闭或者网络出现问题！');
                                break;
                            case Ext.form.Action.SERVER_INVALID:
                                var errorMsg = action.result.msg;
                                if (action.result.fieldErrors) {
                                    errorMsg += "<br/>" + action.result.fieldErrors;
                                }
                                Ext.Msg.alert('Failure', errorMsg);
                        }
                    }

                })
            } else {
                var unValidFields = "";
                dataForm.items.each(function (item) {
                    if (!item.validate()) {
                        if (unValidFields != "") {
                            unValidFields += "，";
                        }
                        for (il in item.fieldLabel) {
                            //unValidFields+=","+il;
                        }
                        unValidFields += "'" + item.fieldLabel + "'";

                    }
                });
                //alert(unValidFields);
                Ext.Msg.alert("校验错误", "有校验项目不对，请检查有红色标识的字段:<br><div style='color:#ff2634'>"
                    + unValidFields + "</div><br>如果有分页，请到各个页检查！谢谢！");
            }
        }
    } catch (exceptionE) {
        Ext.Msg.alert("有问题", "准备提交'" + formPanel.title + "'数据时发生异常：" + exceptionE.description);
    }

}
function loadForm(dataForm, dataUrl) {
    if (dataForm != null && dataUrl != null && dataUrl != '') {
        //alert(dataUrl);
        dataForm.url = dataUrl;
        dataForm.load({
            method:'GET',
            success:function (form, action) {
                formOptions.formDataLoaded=true;
                if (form && action) {
                }
                formOptions.afterLoad(form);
            },
            failure:function (re, v) {
                Ext.Msg.alert("错误信息", "发生错误:" + getErrorMsg(v.response));      //返回失败
            }
        });
    } else {
        alert("参数不够");
    }
}

function loadFormAjax() {
    if (typeof(loadFormFromServerData) == 'function') {
        loadFormFromServerData();
        return false;
    }
    var formPanel = Ext.getCmp('BaseViewForm338547183092');
    var dataForm = formPanel.getForm();
    if(formOptions.loadForm(dataForm)){
        return false;
    }
    if(formOptions.formDataLoaded&&!confirm("您确认放弃修改重新加载服务器端数据吗？")){
        return false;
    }

    if (keyId == "" || keyId == "-1" || keyId == null || typeof(keyId) == 'undefiend') {
        keyId = "-1";
    }
    if (keyId == "-1") {
        try {
            dataForm.reset();
        } catch (e) {
            //alert("dataForm.reset时抛出异常："+ e.description);
        }
    }
    if (keyId != "-1") {
        loadForm(dataForm, getActionUrl('view', "keyId=" + keyId));
        return true;
    }
    return false;
}


function getErrorMsg(response) {
    var result = "";
    try {
        var serverData = Ext.util.JSON.decode(response.responseText);
        if (serverData.error) {
            result = serverData.error;
        } else if (serverData.actionError) {
            result = serverData.actionError;
        } else if (serverData.msg) {
            result = serverData.msg;
        }
        if (result == "java.lang.NullPointerException") {
            return "服务器数据异常，可能是没有登录或登录已过期！";
        } else {
            return result;
        }
    } catch (e) {
        alert(e.description);
    }
    return response.responseText;
}

function requestActionAjax(actionUrl, parameters) {
    Ext.Ajax.request({
        url:actionUrl,
        params:parameters,
        success:function (response, options) {
            if (response && options) {
            }
            var serverData = Ext.util.JSON.decode(response.responseText);
            var messageInfo = '数据处理成功完成！';
            var serverMessageInfo = serverData.message;
            if (serverMessageInfo != null && serverMessageInfo != "") {
                messageInfo += "\r\n" + serverMessageInfo;
            }
            Ext.MessageBox.alert(' 提示', messageInfo, function () {
                try {
                    if (typeof(deleteCallBackFunction) == 'function') {
                        deleteCallBackFunction();
                    } else {
                       formOptions.afterDelete();
                    }
                } catch (e) {
                    alert("发生异常：");
                    gotoNextUrl();
                }

            });
        },
        failure:function (response, options) {
            if (options) {
            }
            Ext.MessageBox.alert(' 提示', '数据处理过程中出现问题:' + getErrorMsg(response));
        }
    });

}

function getQueryString(urlStr) {
    var p = urlStr.indexOf("?");
    if (p > 0) {
        return urlStr.substring(p + 1);
    }
    return "";
}

function getParameter(urlStr, parameterName) {
    var parameterValue = null;
    if (urlStr && parameterName) {
        var p = urlStr.indexOf("&" + parameterName + "=");
        if (p < 0) {
            p = urlStr.indexOf("?" + parameterName + "=");
        }
        if (p >= 0) {
            p += parameterName.length + 2;
            parameterValue = "";
            //alert(parameterValue);
            while (p < urlStr.length) {
                var ch = urlStr.charAt(p);
                p++;
                if (ch != '&' && ch != '?' && ch != '#') {
                    parameterValue += ch;
                } else {
                    break;
                }
            }
        } else {
            parameterValue = "-1";
        }
    }
    return parameterValue;
}


function setCmpValue(eleId, eleValue) {
    try {
        var extCmp = Ext.getCmp(eleId);
        if (extCmp == null) {
            extCmp = Ext.get(eleId);
            if (extCmp == null) {
                extCmp = Ext.get(document.getElementById(eleId));
                if (extCmp == null) {
                    extCmp = Ext.get(document.getElementsByName(eleId));
                }
            }
        }
        if(extCmp!=null){
            try {
                extCmp.setValue(eleValue);
            } catch (e) {
                extCmp.dom.value = eleValue;
            }
            return true;
        }
    } catch (e) {
        alert("发生异常："+e.description);
    }
    return false;
}

function setCmpVisible(cmpId,showIt){
    try {
        var extCmp = Ext.getCmp(cmpId);
        if (extCmp == null) {
            extCmp = Ext.get(cmpId);
            if (extCmp == null) {
                extCmp = Ext.get(document.getElementById(cmpId));
                if (extCmp == null) {
                    extCmp = Ext.get(document.getElementsByName(cmpId));
                }
            }
        }
        if (extCmp != null) {
            try {
                if(showIt){
                    extCmp.show();
                }else{
                    extCmp.hide();
                }
                return true;
            } catch (e) {
                if(showIt){
                    extCmp.style.visible = 'block';
                }else{
                    extCmp.style.visible = 'none';
                }
            }
        }
    } catch (e) {
    }
    return false;
}
function getCmpValue(eleId) {
    try {
        var extCmp = Ext.getCmp(eleId);
        if (extCmp == null) {
            extCmp = Ext.get(eleId);
            if (extCmp == null) {
                extCmp = Ext.get(document.getElementById(eleId));
                if (extCmp == null) {
                    extCmp = Ext.get(document.getElementsByName(eleId));
                }
            }
        }
        if (extCmp != null) {
            try {
                return extCmp.getValue();
            } catch (e) {
                return extCmp.value;
            }
        }
    } catch (e) {
    }
    return null;
}

function displayName(val, meta, record) {
    var result = "";
    var viewLink = false;
    if (canDoThisAction('view')) {
        var id = record.get("id");
        if (id == null) {
            id = record.get("deviceId");
        }
        if (id == null) {
            id = record.get("operatorid");
        }
        if (id == null) {
            id = record.get("Id");
        }
        if (id != null) {
            viewLink = true;
            result = "<a href='" + appendParameter(actionHeader + "View.jsp", "keyId", id) + "'>";
        }
    }
    result += val;
    if (viewLink) {
        result += "</a>"
    }
    return result;
}
function displayStatus(val) {
    var result = "";
    if (val == '1') {
        result = "正常";
    } else if (val == '2') {
        result = "锁定";
    } else if (val == '3') {
        result = "待审";
    } else {
        result = "异常(" + val + ")";
    }
    return result;
}
function appendParameter(linkUrl, parameterName, value) {
    if (linkUrl) {
        if (linkUrl.indexOf("?") > 0) {
            linkUrl = linkUrl + "&";
        } else {
            linkUrl = linkUrl + "?";
        }
        return linkUrl + parameterName + "=" + value;
    }
    return "?" + parameterName + "=" + value;
}
function displayControl(val, p, rec, controlLinks) {
    if (p && rec) {
        //return val;
    }
    var result = "";
    var i = 0;
    //alert(controlLinks.length);
    for (i = 0; i < controlLinks.length; i++) {
        var item = controlLinks[i];
        //alert(item[0].text);
        if (canDoThisAction(item.action)) {
            if (item.type == 'onclick') {
                var clickEventString = 'confirmAction(\'' + val + '\',\'' + item.text + '\',\'' + item.action + '\',null,\'' +
                    item.messageInfo + '\')';
                result += '<a href="#" onclick="' + clickEventString + '">' + item.text + '</a>&nbsp;';
            } else {
                if (item.href) {
                    if (item.dataIndex) {
                        try {
                            val = rec.get(item.dataIndex);
                        } catch (e) {
                        }
                    }
                    result += '<a href="' + appendParameter(item.href, item.parameter, val) + '">' + item.text + "</a>&nbsp;";
                } else {
                    result += '<a href="' + getLinkUrlByActionId(item.action) + '?keyId=' + val + '">' + item.text + "</a>&nbsp;";
                }
            }
        } else {
            //result += (item.text) + "&nbsp;";
        }
    }
    //alert(result);
    return  result;
}
function displayManage(val, p, rec) { //d
    return displayControl(val, p, rec, defaultManageLinks);
}
///*
function confirmDoAction(confirmMsg, actionUrl, parameters) {
    try {
        Ext.MessageBox.confirm('请您确认操作', confirmMsg, function (btn) {
            if (btn == 'yes') {
                try {
                    if (typeof(deleteAdminIdFunction) == 'function') {
                        deleteAdminIdFunction(actionUrl,parameters);
                    } else {
                        requestActionAjax(actionUrl, parameters);
                    }
                } catch (e) {
                    alert("发生异常：");
                }
//                requestActionAjax(actionUrl, parameters);
            }
        });
    } catch (e) {
        if (window.confirm('请您确认操作:\n' + confirmMsg)) {
            requestActionAjax(actionUrl, parameters);
        }
    }
}

function confirmDelete(objKey, recordTitle) {
    var confirmText = "您确认要删除这条数据吗？";
    if (recordTitle) {
        confirmText = "您确认要删除'" + recordTitle + "'这条数据吗？";
    }
    confirmDoAction(confirmText, getActionUrl('delete'), {keyId:objKey});
}

function confirmAction(objKey, actionStr, actionId, recordTitle, messageInfo) {
    var confirmText = "您确认要" + actionStr + "这条数据吗？";
    if (recordTitle && recordTitle != '') {
        confirmText = "您确认" + actionStr + "'" + recordTitle + "'这条数据吗？";
    }
    if (messageInfo && messageInfo != '') {
        confirmText = messageInfo;
    }
    confirmDoAction(confirmText, getActionUrl(actionId, "keyId=" + objKey), {});
}

function confirmSelected(actionText, actionId, objKeys) {
    if (!actionText) {
        actionText = "如此处理";
    }
    var confirmText = "您确认要" + actionText + "数据吗？";
    //alert(objKeys);
    confirmDoAction(confirmText, getActionUrl(actionId), objKeys);
}
function doAction(varActionId, varActionText, varObjKey) {
    var actionId;
    var actionText;
    var objKey;
    if (varActionId && varActionText && varObjKey) {
        actionId = varActionId;
        actionText = varActionText;
        objKey = varObjKey;
    } else {
        actionId = this.action;
        actionText = this.text;
        objKey = keyId;
    }
    confirmAction(keyId, actionText, actionId);
}
function doSelected() {
    doSelectedActionForIdAndText(this.action, this.text);
}
function doSelectedActionForIdAndText(varActionId, varActionText) {
    var actionId;
    var actionText;
    if (varActionId && varActionText) {
        actionId = varActionId;
        actionText = varActionText;
    } else {
        actionId = this.action;
        actionText = this.text;
    }
    if (defaultGrid != null) {
        try {
            var row = defaultGrid.getSelectionModel().getSelections();
            var jsonData = "keyIds=";
            var i = 0;
            var len = 0;
            for (i = 0, len = row.length; i < len; i++) {
                var ss = row[i].get(keyFieldId);
                if (i == 0) {
                    jsonData = jsonData + ss;
                } else {
                    jsonData = jsonData + "," + ss;
                }
            }
            if (i > 0) {
                confirmSelected(actionText, actionId, jsonData);
            } else {
                Ext.MessageBox.alert("提示", "请注意：\n    没有选择数据！");
            }
        } catch (e) {
            Ext.MessageBox.alert("异常", "尝试删除选择的数据时，发生意外：" + e.description);
        }
    } else {
        Ext.MessageBox.alert("异常", "没有合适的数据容器！");
    }
}

function deleteSelectedRows() {
    return doSelectedActionForIdAndText('deleteSelected', '删除所选');
}

function addRecord() {
    var urlStr;
    if (addViewPageUrl) {
        urlStr = addViewPageUrl;
    } else {
        urlStr = actionHeader + "View.jsp";
    }
    if (queryString != null && "" != queryString) {
        var parameters = Ext.urlDecode(queryString);
        if (parameters["keyId"] != "") {
            parameters["keyId"] = "-1";
        }
        urlStr += "?" + Ext.urlEncode(parameters);
    }
    window.location.href = urlStr;
}
function cloneRecordData(rec){
    var result = {};
    if(typeof(rec)!='undefined'&& rec!=null){
       var data = rec.data;
       if(typeof(data)!='undefined'&&data!=null){
           for(var d in data){
               if(data.hasOwnProperty(d)){
                   result[d] = rec.data[d];
               }
           }
       }
    }
    return result;
}
function canDoThisAction(actionId) {
    var i = 0;
    //var debugMsg = "";
    for (i = 0; i < canUseFunctions.length; i++) {
        //debugMsg += canUseFunctions[i]+"=="+actionId+"?\n";
        if (actionId == canUseFunctions[i]) {
            return true;
        }
    }
    //alert(actionId+"没有权限执行！"+debugMsg);
    return false;
}

function checkAllFunctions() {
    defaultBottomButtons = checkFunctions(defaultBottomButtons);
    defaultViewFormButtons = checkFunctions(defaultViewFormButtons);
    defaultManageLinks = checkFunctions(defaultManageLinks);
    viewReadOnly = !canDoThisAction('save');
}

function removeFunction(functions, removeActionId) {
    var resultArray = [];
    var resultStr = "";
    for (var i = 0; i < functions.length; i++) {
        var item = functions[i];
        if (removeActionId != (item.action)) {
            resultArray.push(item);
        } else {
            resultStr += ":" + item.text;
        }
    }
    return resultArray;
}

function checkFunctions(functions) {
    var resultArray = [];
    var resultStr = "";
    for (var i = 0; i < functions.length; i++) {
        var item = functions[i];
        if (canDoThisAction(item.action)) {
            resultArray.push(item);
        } else {
            resultStr += ":" + item.text;
        }
    }
    if (resultArray.length != functions.length) {
        //alert("有被删除的项目："+resultStr);
    }
    return resultArray;
}
//*/

function outDebugMsg(debugMsg) {
    var resultMsg = new Date + " - " + debugMsg;
    if (resultMsg) {

    }
    window.status = resultMsg;
    //Ext.example.msg(resultMsg);
    //alert(resultMsg);
}

function reloadFormDataFromJSON(data) {
    if (data) {

    }
}

function disableExtObjects(extObjectNames) {
    try {
        Ext.each(extObjectNames, function (objName) {
            var obj = Ext.getCmp(objName);
            if (obj) {
                obj.disable();
            }
        })
    } catch (e) {
        alert("隐藏控件时发生异常：" + e.description);
    }
}

function enableExtObjects(extObjectNames) {
    try {
        Ext.each(extObjectNames, function (objName) {
            var obj = Ext.getCmp(objName);
            if (obj) {
                obj.enable();
            }
        })
    } catch (e) {
        alert("显示控件时发生异常：" + e.description);
    }
}

/**
 * 显示文件大小，进行M,G等单位的换算
 * @param val
 * @param metaData
 * @param record
 */
function displayFileSize(val, metaData, record) {
    var isDir = record.get("directory");
    if (isDir) {
        return "";
    }
    if (val > 1024) {//超过1KB就用KB方式显示
        val = Math.round(val / 1024);
        if (val > 1024 * 10) {  //超过10M就用GB方式显示
            val = Math.round(val / 1024);
            if (val > 1024) {//超过1
                return Math.round(val / 1024) + " GB";
            } else {
                return val + " MB";
            }
        } else {
            return val + " KB";
        }
    } else {
        return val + " Byte";
    }
}
/**
 * 显示目录链接以及图标
 * @param val
 * @param metaData
 * @param rowRecord
 */
function displayFileName(val, metaData, rowRecord) {
    //alert(Ext.isArray(rec));
    var isDir = rowRecord.get("directory");
    var iconImgStr = "";//<img src="../images/icon/dir.gif" border="0">
    //alert("rowIndex="+rowIndex+",colIndex="+colIndex+",isDir="+isDir);
    if (isDir) {
        var clickScript = "onclick='gotoDirFromCurrentDir(\"" + val + "\")'";
        var result = '<a href="#" ' + clickScript + '>' + iconImgStr + val + '</a>';
        if (result) {
        }
        //alert(result);
        return result;
    } else {
        return val;
    }
}

function setLabelText(name, value) {
    var cmp = Ext.getCmp(name);
    if (cmp != null) {
        cmp.setText(value);
    }
}

function setDefaultSelectCmp(eleId, clearIt) {
    var cmp = Ext.getCmp(eleId);
    if (cmp != null) {
        try {
            if (clearIt) {
                cmp.clearValue();
            } else {
                var store = cmp.store;
                if (store.getCount() > 0) {
                    var record = store.getAt(0);
                    var val = record.get(cmp.valueField);
                    if (val) {
                        cmp.setValue(val);
                    } else {
                        cmp.clearValue();
                    }
                }
            }
        } catch (e) {
        }
    }
}

/*
function setCmpValue(eleId, eleValue) {
    try {
        var extCmp = Ext.getCmp(eleId);
        if (extCmp != null) {
            extCmp.setValue(eleValue);
            return true;
        }
    } catch (e) {
    }
    return false;
}
*/

function getSelectedFilesToParams(baseParams, row, fileNameField) {
    try {
        if (row != null) {
            //fileListGrid.getSelectionModel().getSelections();
            var i = 0;
            var len = 0;
            //var data=[];
            var nowCount = baseParams.fileCount;
            for (i = 0, len = row.getCount(); i < len; i++) {
                var record = row.getAt(i);
                if (record.get("directory")) {
                    //alert("是目录，不能加入！");
                    continue;
                }
                //baseParams["fileNames"] = row[i].get("name");
                if (baseParams == null) {
                    baseParams = {};
                }
                baseParams["fileNames[" + nowCount + "]"] = record.get(fileNameField);
                nowCount++;
            }
            baseParams.fileCount = nowCount;
        }
    } catch (e) {
        alert("文件名获取时发生异常：" + e.description);
    }
    return baseParams;
}


function getStoreFiles(fileStore, nameField, files) {
    try {
        for (i = 0, len = fileStore.getCount(); i < len; i++) {
            var record = fileStore.getAt(i);
            if (record != null) {
                var fileName = record.get(nameField);
                var fileSize = record.get("size");
                var fileModifyDate = record.get("modifyDate");
                var fileType = "image";
                var dotPos = fileName.indexOf(".");
                if (dotPos >= 0) {
                    var extName = fileName.substring(dotPos);
                    if (extName.indexOf(".mp4") >= 0 || extName.indexOf(".wmv") >= 0 || extName.indexOf(".mpg") >= 0 || extName.indexOf(".avi") >= 0) {
                        fileType = "video";
                    }
                }
                files.push({name:fileName, size:fileSize, modifyDate:fileModifyDate, selected:true,
                    type:fileType, directory:false});
            }
        }
    } catch (e) {
    }
    return files;
}
function checkAllStoreLoaded(index){
    var i= 0,l=allStores.length;
    if(index<l&&index>=0){
        var item = allStores[index];
        if(typeof(item.onLoad)=='function'){
            item.onLoad();
        }
        item["status"]=true;
    }
    for(;i<l;i++){
        var storeItem = allStores[i];
        if(typeof(storeItem.status)=='undefined'){
            return;
        }
        if(!storeItem.status){
            return;
        }
    }
    functionDone('initAllStores');
}

function initAllStores(){
    var i= 0,l=allStores.length;
    for(;i<l;i++){
        var storeItem = allStores[i];
        storeItem["status"]=false;
        storeItem.store.load({
            params:{index:i},
            callback:function(records,options/*,success*/){
                var idx = options.params.index;
                if(idx!=null){
                    checkAllStoreLoaded(idx);
                }
            }
        });
    }
}

function checkValidField(data,field,value){
    if(typeof(value)!='undefined'&&value!=null){
        data[field] = value;
    }
    return data;
}

function getHiddenField(name,value){
    var result = {xtype:'hidden',name:name,id:name};
    if(typeof(value)!='undefined'){
        result["value"]=value;
    }
    return result;
}
function getTextArea(name,label,allowBlank,value,height){
    var result = {xtype:'textarea',name:name,id:name,fieldLabel:label};
    result = checkValidField(result,'allowBlank',allowBlank);
    result = checkValidField(result,'value',value);
    result = checkValidField(result,'height',height);
    return result;

}
function getField(name,label,allowBlank,vtype,value){
    var result = {xtype:'textfield',name:name,id:name,fieldLabel:label};
    result = checkValidField(result,'allowBlank',allowBlank);
    result = checkValidField(result,'vtype',vtype);
    result = checkValidField(result,'value',value);
    return result;
}
function getCheckField(name,label,items){
    return getSelectField('checkgroup',name,label,items);
}
function getRadioField(name,label,items){
    return getSelectField('radiogroup',name,label,items);
}
function getSelectField(xtype,name,label,items){
    var radioItems=[];
    for(var i= 0,l=items.length;i<l;i++){
        var item = items[i];
        if(item!=null){
            radioItems.push({boxLabel:item.label,name:name,inputValue:item.value,checked:item.checked});
        }
    }
    return {
        name:name,
        xtype:xtype,
        fieldLabel:label,
        items:radioItems
    };
}
function getCombo(name,label,store,value,displayField,valueField){
    var result = {
        fieldLabel:label,
        hiddenId:name,
        hiddenName:name,
        store:store
    };
    result = checkValidField(result,'value',value);
    result = checkValidField(result,'displayField',displayField);
    result = checkValidField(result,'valueField',valueField);
    return new FortuneCombo(result);
    //
}

function fortuneAlert(title,msg,fn){
    try {
        if (useWindowMessageBox) {
            window.alert(title + "\n" + msg);
            if(fn){
                fn.call();
            }
        } else {
            Ext.MessageBox.alert(title, msg, function() {
                if(fn){
                    fn.call();
                }
            });
        }
    } catch(e) {

    }
}

if ((typeof Range !== "undefined") && !Range.prototype.createContextualFragment)
{
    Range.prototype.createContextualFragment = function(html)
    {
        var frag = document.createDocumentFragment(),
            div = document.createElement("div");
        frag.appendChild(div);
        div.outerHTML = html;
        return frag;
    };
}
