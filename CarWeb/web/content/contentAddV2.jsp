<%@ page import="java.io.UnsupportedEncodingException" %><%@ page contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp" %>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader, "upload", "synFileManage,synFileUpload");
        needPermissions(actionHeader, "isExistFile", "synFileManage,synFileIsExistFile");
        needPermissions(actionHeader, "listFiles", "templateListFiles");

    }
    Admin contentOperator =(Admin)session.getAttribute(
            com.fortune.common.Constants.SESSION_ADMIN);
    String moduleId = request.getParameter("moduleId");
    String contentId=request.getParameter("contentId");
    String deviceId=request.getParameter("deviceId");
    String contentName="";
    try {
        contentName = request.getParameter("contentName");
        if(contentName!=null){
            contentName = java.net.URLDecoder.decode(contentName, "UTF-8");//处理中文文件名的问题
        }else{
            contentName = "";
        }
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
%><%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
<meta http-equiv=content-type content="text/html; charset=UTF-8">
<title></title>
<%@include file="../inc/jsCssLib.jsp"%>
<%@include file="../inc/extBase.jsp"%>
<style type="text/css">
    .imageWidth {
        width: 500px;
    }
    .bodyCls{
        margin-left: 150px;
    }
    .uploadFile{
        margin-left: 205px;
        margin-top: 10px;
    }
    .imagePosition{
        margin-top: 30px;
        height:400;
        width:320;
    }
    .nameStyle{
        margin-left: -30px;
    }
    .valueStyle{
        margin-left: -30px;
    }
    .fileStyle{
        margin-left: -30px;
    }
    .x-btn {
        margin-right: 10px;
        cursor: pointer;
        white-space: nowrap;
    }
</style>
<script type="text/javascript" src="contentAddV2.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript" src="../js/FtpFileSelector.js"></script>
<script type="text/javascript" src="../js/ExtFortuneSelectField.js"></script>
<script language="javascript">
nextUrl = "contentList.jsp";
var actionUrl = "/content/content";
var sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
var contentId =<%=(contentId==null)?-1:contentId%>;
var contentName="<%=contentName%>";
var moduleId=<%=(moduleId==null)?5000:moduleId%>;
var deviceId=<%=deviceId%>;
var clipStore=[];
var baseStore=[];
var picSrc="../images/gongneng1.jpg";
var contentPropertyId=0;
var propertyStore=new Ext.data.JsonStore({
    method:'POST',
    url:"/module/property!getPropertiesOfModule.action?obj.moduleId="+moduleId,
    baseParams:{limit:1000000},
    totalProperty:'totalCount',
    root:'objs',
    fields:[
        {name:'id'},
        {name:'code'},
        {name:'name'},
        {name:'dataType'},
        {name:'status'},
        {name:'moduleId'},
        {name:'stringValue'} ,
        {name:'isMain'} ,
        {name:'columnName'}
    ]
});
var contentPropertyStore=new Ext.data.JsonStore({
    method:'POST',
    url:"/content/contentProperty!list.action?obj.contentId="+contentId+"&moduleId="+moduleId,
    baseParams:{limit:1000000},
    totalProperty:'totalCount',
    root:'objs',
    fields:[
        {name:'id'},
        {name:'name'},
        {name:'contentId'} ,
        {name:'propertyId'},
        {name:'intValue'},
        {name:'stringValue'},
        {name:'desp'}       ,
        {name:'extraData'} ,
        {name:'extraInt'} ,
        {name:'length'},
        {name:'thumbPic'}
    ]
});


var cspDeviceStore = new Ext.data.JsonStore({
    method:'POST',
    url:"/csp/cspDevice!getDevicesOfCsp.action",
    root:'objs',
    fields:[
        {name:'id'},
        {name:'name'}
    ]
});
cspDeviceStore.load();

var propertySelectStore=new Ext.data.JsonStore({
    method:'POST',
    url:"/module/propertySelect!list.action",
    baseParams:{limit:1000000},
    totalProperty:'totalCount',
    root:'objs',
    fields:[
        {name:'id'},
        {name:'propertyId'},
        {name:'name'}
    ]
});
var contentAuditStore=new Ext.data.JsonStore({
    method:'POST',
    url:"/content/contentAudit!list.action?obj.contentId="+contentId,
    baseParams:{limit:1000000},
    totalProperty:'totalCount',
    root:'objs',
    fields:[
        {name:'id'},
        {name:'contentId'},
        {name:'status'}
    ]
});



var propertyStoreReady=false;
var contentPropertyStoreReady=false;
var propertySelectStoreReady=false;
var contentAuditStoreReady=false;

function getNewContentPropertyStore(){
    var contentPropertyDataStore=new Ext.data.SimpleStore({
        fields:[
            {name:'id'},
            {name:'name'},
            {name:'contentId'} ,
            {name:'propertyId'},
            {name:'intValue'},
            {name:'stringValue'},
            {name:'desp'}       ,
            {name:'extraData'} ,
            {name:'extraInt'} ,
            {name:'length'},
            {name:'thumbPic'}
        ]
    });
    contentPropertyDataStore.setDefaultSort("intValue","asc");
    return contentPropertyDataStore;
}

var picStore= getNewContentPropertyStore();


function allStoreLoad(){

    propertyStore.load({
        callback :function(records, options, success) {
            if (success) {
                propertyStoreReady=true;
                if(propertyStoreReady&&contentPropertyStoreReady&&propertySelectStoreReady&&contentAuditStoreReady){
                    functionDone('allStoreLoad');
                }
            }

        }
    });
    propertySelectStore.load({
        callback :function(records, options, success) {
            if (success) {
                propertySelectStoreReady=true;
                if(contentPropertyStoreReady&&propertyStoreReady&&propertySelectStoreReady&&contentAuditStoreReady){
                    functionDone('allStoreLoad');
                }
            }
        }
    });
    contentPropertyStore.load({
        callback :function(records, options, success) {
            if (success) {
                contentPropertyLoaded();
            }
        }
    });
    if(contentId=="-1"||contentId==-1||contentId==null||contentId=="null"){
        contentAduitStoreLoaded();
    }else{
        contentAuditStore.load({
            callback :function(records, options, success) {
                if (success) {
                    contentAduitStoreLoaded();
                }
            }
        });
    }


}
function contentPropertyLoaded(){
    contentPropertyStoreReady=true;
    if(contentPropertyStoreReady&&propertyStoreReady&&propertySelectStoreReady&&contentAuditStoreReady){
        functionDone('allStoreLoad');
    }
}
function contentAduitStoreLoaded(){
    contentAuditStoreReady=true;
    if(contentPropertyStoreReady&&propertyStoreReady&&propertySelectStoreReady&&contentAuditStoreReady){
        functionDone('allStoreLoad');
    }
}
function isNumber(str){
    if(""==str){
        return false;
    }
    var reg = /\D/;
    return str.match(reg)==null;
}

var picNo=0;
var startTimeSec = 10;
var makeNewUrl = true;
var snapServerUrl = "";
var oldSnapClipId=-1;
function dateDirectory() {
    var temp = new Date();
    var directory = temp.getFullYear() + "/";
    var month = temp.getMonth()+1;
    if(month < 10) {
        month = "0"+month;
    }
    var date = temp.getDate();
    if(date<10){
        date = "0"+date;
    }
    directory = directory + month + "/" + date +"/";
    return directory;
}
function getNewThumbPicFullPath(snapTime){
    picNo ++;
    var now = new Date();
    var result =  "/post/"+dateDirectory()+"<%=contentOperator.getLogin()%>_"+
            +"c"+contentId+"_"+picNo+"_";
    if(snapTime){
        result += snapTime+"_";
    }
    result += now.getHours()+"_"+now.getMinutes()+"_"+now.getSeconds()+"_"+Math.round(Math.random() * 100000) + ".jpg";
    return result;
}

function snapClip(v){
    if(oldSnapClipId!=v){
        makeNewUrl = true;
        oldSnapClipId = v;
    }
    var posterObj = document.getElementById('clipPoster'+v);
    if(posterObj!=null){
        var deviceId=getCmpValue("deviceId");
        if(deviceId!=null&&deviceId!=""&&deviceId!="null"){
            var snapTime = getCmpValue("snapTime"+v);
            if(snapTime!=""&&snapTime!=null){
                startTimeSec = snapTime;
            }
            var mediaUrl = getCmpValue("stringValue"+v);
            if(mediaUrl!=null&&mediaUrl!=""){
                mediaUrl = encodeURI(encodeURI(mediaUrl));
                var thumbPicUrl = "";
                var clipPosterField =getCmpValue('thumbPic'+v); //contentViewForm.getForm().findField("contentProperties_"+clipId+"_thumbPic");
                if (contentId == null || contentId == "") {
                    if (clipPosterField == "") {
                        thumbPicUrl = getNewThumbPicFullPath();
                       setCmpValue('thumbPic'+v,thumbPicUrl);
                    }
                } else {
                    if(makeNewUrl) {
                        thumbPicUrl = getNewThumbPicFullPath();
                        setCmpValue('thumbPic'+v,thumbPicUrl);
                        makeNewUrl = false;
                    }
                }
                posterObj.src = snapServerUrl+"/interface/snapMedia.jsp?deviceId="+deviceId+"&mediaUrl="+mediaUrl+
                        "&startTime=" +
                        startTimeSec +
                        "&thumbPicUrl=" + getCmpValue('thumbPic'+v)+
                        "&date=" + (new Date()).getTime();
            }

        }
    }
}

var store1;

var tabsMax= new Ext.TabPanel({
    resizeTabs:true,
    minTabWidth: 100,
    tabWidth:100,
    enableTabScroll:true,
    width:700,
    height:500,
    activeTab:0,
    plugins: new Ext.ux.TabCloseMenu()
});
var tabsMin= new Ext.TabPanel({
    resizeTabs:true,
    minTabWidth: 135,
    tabWidth:135,
    enableTabScroll:true,
    width:700,
    height:450,
    activeTab:0,
    plugins: new Ext.ux.TabCloseMenu()
});
var indexMax=0;
var indexMin=0
function mediumInfo(){
    while(indexMax <2){
        addTabMax(indexMax);
    }
}
function scanSourceLength(videoStore){
    var allLength = 0;
    if(videoStore==null){
        for(var cIdx= 0,cLen=clipStore.length;cIdx<cLen;cIdx++){
            var clipData = clipStore[cIdx];
            if(clipData.propertyCode == "Media_Url_Source"){
                videoStore = clipData.store;
                break;
            }
        }
    }
    if(videoStore!=null){
        for(var vIdx= 0,vLen = videoStore.getCount();vIdx<vLen;vIdx++){
            var lengthData = videoStore.getAt(vIdx).data.length;
            if(lengthData!=null){
                allLength += parseInt(lengthData);
            }
        }
        setContentPropertyValue("MEDIA_LENGTH",allLength,allLength);
    }else{
        setContentPropertyValue("MEDIA_LENGTH",60,60);
    }
}
function addTabMax(v){
    var medio="";
    if(v==0){
        medio="海报信息";
    }else if(v==1){
        medio="片段信息";
    }
    if(v==1){
        tabsMax.add({
            title: medio,
          //  baseCls: 'x-plain',
            width:700,
            autoHeight:true,
            baseCls:'my-panel-no-border',
            items:[
                {
                    baseCls: 'x-plain',
                    layout: 'form',
                    baseCls:'my-panel-no-border',
                    style:{'margin-left':'392px', 'margin-top':'2px', 'margin-right':'0px', 'margin-bottom':'0px'},
                    width:360,
                    labelAlign:"right",
                    items: [
                        {
                            hiddenName:'name',
                            xtype: 'combo',
                            labelWidth: 50,
                            id:'deviceId',
                            fieldLabel: '服务器地址',
                            width:200,
                            triggerAction: 'all',
                            emptyText:'请选择...',
                            store:cspDeviceStore,
                            valueField: 'id',
                            displayField: 'name',
                            mode:'local',
                            loadingText:'加载中...',
                            readOnly:viewReadOnly,
                            selectOnFocus:true,
                            editable: false,
                            typeAheadDelay:1000,
                            forceSection: true,
                            typeAhead: false,
                            value:deviceId,
                            listeners:{
                                select: function(combo, record, index) {
                                    //alert(index +":"+ record.get("channelId") +":"+ record.get("name"));
                                }
                            }

                        }
                    ]
                }
                        ,
                        tabsMin
            ]
        }).show();
    }else if(v==0){
        tabsMax.add({
            title: medio,
         //   baseCls: 'x-plain',
            baseCls:'my-panel-no-border',
            width:700,
            autoHeight:true,
            items:[
                {
                    width:700,
                    xtype: 'panel',
                    baseCls:'my-panel-no-border',
//                    bodyStyle: 'border-width:1px 1px 1px 1px',
                    height:500,
                    style:{'margin-left':'0px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                    items:[
                        gridForm
                    ]
                }
            ]
        }).show();
    }
    indexMax++;
}

function setData(dataArray,startIndex,row){
    var propertyId=row.data.propertyId;
    var id = row.data.id;
    if(propertyId == null||propertyId==""){
        propertyId = row.data.id;
        id = "";
    }
    dataArray["contentProperties["+startIndex+"].id"]=row.data.id;
    dataArray["contentProperties["+startIndex+"].name"]=row.data.name;
    dataArray["contentProperties["+startIndex+"].stringValue"]=row.data.stringValue;
    dataArray["contentProperties["+startIndex+"].intValue"]=row.data.intValue;
    dataArray["contentProperties["+startIndex+"].propertyId"]=propertyId;
    dataArray["contentProperties["+startIndex+"].desp"]=row.data.desp;
    dataArray["contentProperties["+startIndex+"].thumbPic"]=row.data.thumbPic;
    dataArray["contentProperties["+startIndex+"].extraData"]=row.data.extraData;
    dataArray["contentProperties["+startIndex+"].subContentId"]=row.data.subContentId;
    dataArray["contentProperties["+startIndex+"].extraInt"]=row.data.extraInt;
    dataArray["contentProperties["+startIndex+"].length"]=row.data.length;
    dataArray["contentProperties["+startIndex+"].contentId"]=row.data.contentId;
}
function collectData(dataArray,startIndex){
    //循环得到所有要片段信息
    for(var j= 0,a=tabsMin.items.getCount();j<a;j++){
        var store=tabsMin.items.items[j].items.items[0].initialConfig.items[0].store;
        for(var i= 0,b=store.getCount();i<b;i++){
            var row = store.data.items[i];
            setData(dataArray,startIndex,row);
            startIndex++;
        }
   }
    //循环得到所有海报信息
    var picGridStore=Ext.getCmp('picGrid').store;
    for(var i=0,a=picGridStore.getCount();i<a;i++){
        var row = picGridStore.data.items[i];
        if(row.data.stringValue!=""){
            setData(dataArray,startIndex,row);
            startIndex++;
        }
    }

    //循环得到基本信息
    for(var i= 0,j=baseStore.length;i<j;i++){
        var isMainType=baseStore[i].property.isMain;
        var type=baseStore[i].property.dataType;
            if(type==1||type==2||type==3||type==4||type==5||type==6||type==7){
                dataArray["contentProperties["+startIndex+"].id"]=getCmpValue("contentProperties["+i+"].id");
                dataArray["contentProperties["+startIndex+"].contentId"]=contentId;
                dataArray["contentProperties["+startIndex+"].desp"]=getCmpValue("contentProperties["+i+"].desp");
                dataArray["contentProperties["+startIndex+"].extraData"]=getCmpValue("contentProperties["+i+"].extraData");
                dataArray["contentProperties["+startIndex+"].extraInt"]=getCmpValue("contentProperties["+i+"].extraInt");
                var intValue =getCmpValue("contentProperties["+i+"].intValue");
                if(typeof(intValue)=="undefined"){
                    intValue = 0;
                }else if(intValue==null){
                    intValue = 0;
                }else if((""+intValue)=="NaN"){
                    intValue = 0;
                }
                try {
                    intValue = parseInt(intValue);
                } catch (e) {
                    intValue = 0;
                }
                dataArray["contentProperties["+startIndex+"].intValue"]=intValue;
                dataArray["contentProperties["+startIndex+"].length"]=getCmpValue("contentProperties["+i+"].length");
                dataArray["contentProperties["+startIndex+"].name"]=getCmpValue("contentProperties["+i+"].name");
                dataArray["contentProperties["+startIndex+"].propertyId"]=getCmpValue("contentProperties["+i+"].propertyId");
                dataArray["contentProperties["+startIndex+"].subContentId"]=getCmpValue("contentProperties["+i+"].subContentId");
                dataArray["contentProperties["+startIndex+"].thumbPic"]=getCmpValue("contentProperties["+i+"].thumbPic");
                if(type==6){
                    if(getCmpValue("contentProperties["+i+"].stringValue")!=""&&getCmpValue("contentProperties["+i+"].stringValue")!=null){
                        dataArray["contentProperties["+startIndex+"].stringValue"]=getCmpValue("contentProperties["+i+"].stringValue").inputValue;
                    }else{
                        dataArray["contentProperties["+startIndex+"].stringValue"]="";
                    }

                }else  if(type==7){
                    var cpSelect=getCmpValue("contentProperties["+i+"].stringValue");
                    var propertySelectId=";";
                    for(var a= 0,b=cpSelect.length;a<b;a++){
                        if(cpSelect!=""|cpSelect!=null){
                            propertySelectId=propertySelectId+cpSelect[a].inputValue+";";
                        }
                    }
                    dataArray["contentProperties["+startIndex+"].stringValue"]=propertySelectId;
                }else{
                    dataArray["contentProperties["+startIndex+"].stringValue"]=getCmpValue("contentProperties["+i+"].stringValue");
                }
                startIndex++;
            }
    }
    return startIndex;
}
var deleteArray={};
var medioGrid;
var handlePicFile;
function delContentProperty(){
    var tabId=tabsMin.getActiveTab();
    var selectRow=-1;
    if(tabId.get(0).items.items[0].getSelectionModel().last!=null){
        selectRow =tabId.get(0).items.items[0].getSelectionModel().last;
    }
    var n =tabId.get(0).items.items[0].store;
    if(selectRow>=0){
        if(viewReadOnly==false){
            Ext .Msg.confirm('信息','确定要删除？',function(btn) {
                if(btn == 'yes') {
                    var record = n.getAt(selectRow);
                    n.remove(record);
                }
            });
        }
    }
}
var  isReplace=true;
function saveAll(){
    var dataArray={};
    var startIndex = 0;
    startIndex=collectData(dataArray,startIndex)
    dataArray["obj.contentId"]=contentId;
    dataArray["content.id"]=contentId;
    dataArray["content.moduleId"]=moduleId;
    dataArray["content.deviceId"]=getCmpValue("deviceId");
        Ext.Ajax.request({
//            url:'/content/contentProperty!saveAll.action',
            url:'/content/contentProperty!saveAll.action?handleMethod='+handleMethod+'&handlePicFile='+handlePicFile,
            params:dataArray,
            callback : function(opt, success, response) {
                if (success) {
                    alert('修改成功');
                    isReplace=true;
                    window.location="../content/cpContentList.jsp";
                }
            }
        });
}

function newWindow(v){
    if(getCmpValue("deviceId")==""||getCmpValue("deviceId")==null){
        alert("请选择服务器");
    }else{
        var tabId=tabsMin.getActiveTab();
        var videoStore=tabId.get(0).items.items[0].store;//得到当前grid中的store
        var row = tabId.get(0).items.items[0].getSelectionModel().last;//得到当前选中的行号
        contentPropertyId=videoStore.data.items[row].data.id;
        if(videoStore.data.items[row].data.thumbPic!=null&&videoStore.data.items[row].data.thumbPic!=""){
            picSrc=videoStore.data.items[row].data.thumbPic;
        }
        var viewWin = new Ext.Window({
           // baseCls:'x-plain',
//            baseCls:'my-panel-no-border',
            closeAction:"hide",
            closable:false,
            modal:true,
            resizable:false,
            plain:true,
            title:'视频详细信息',
            items:[
                {
                    layout:'form',
                    baseCls:'x-plain',
                    style:{'margin-left':'5px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                    labelAlign:"left",
                    items: [
                        {
                            layout:'table',
                            layoutConfig: {columns:2} ,
                            baseCls: 'x-plain',
                           // baseCls:'my-panel-no-boder',
                            labelAlign:"right",
                            items: [
                                {
                                    layout:'form',
                                    baseCls:'x-plain',
                                   // style:{'margin-left':'5px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                                    labelAlign:"left",
                                    items: [
                                        {
                                            layout:'table',
                                            layoutConfig: {columns:4} ,
                                            width:317,
                                        //    height:270,
                                            baseCls: 'x-plain',
                                         //   baseCls:'my-panel-no-boder',
                                            labelAlign:"right",
                                            items: [
                                                {
                                                    layout:'table',
                                               //     baseCls:'x-plain',
                                                    xtype:'panel',
                                                    style:{'margin-left':'-10px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                                                    colspan:4,
                                                    width:310,
                                                    height:270,
                                                    items: [
                                                        {
/*
                                                            xtype: 'box',
                                                            width: 300,
                                                            height: 270,
                                                            autoEl: {
                                                                id:'clipPlayer'+v,
                                                                xtype: 'flash',
                                                                src:""//flvStr   //指定url路径
                                                            }
*/
                                                            id:'flashPlayerObj'+v,
                                                            xtype:'panel'
                                                            ,
                                                            html:getFlvStr(getCmpValue('deviceId'),"")//"<div style='width:320px;height:240px;border:1px solid red'>hello</div>"
                                                        }
                                                    ]
                                                },
                                                {
                                                    xtype:'button',
                                                    baseCls: 'x-plain',
                                                    style:{'margin-left':'100px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                                                    width:50,
                                                    text: "播放",
                                                    disabled:viewReadOnly,
                                                    handler: function() {
                                                        getplayUrl(v);

                                                        getMediaDuration("flashPlayer2","length"+v);

                                                       /* var duration = getMediaDuration("flashPlayer2");
                                                        if(duration <=0) {
                                                           duration = getMediaDuration("flashPlayer1");
                                                        }*/
                                                    }
                                                },{
                                                    xtype:'label',
                                                    text:'时长(秒)：'
                                                },{
                                                    xtype:'textfield',
                                                    name:'length',
                                                    id:'length'+v,
                                                    value:videoStore.data.items[row].data.length,
                                                    width:50
                                                }
                                            ]
                                        },
                                        {
                                            layout:'table',
                                            layoutConfig: {columns:2} ,
                                            baseCls: 'x-plain',
                                      ////      baseCls:'my-panel-no-boder',
                                            labelAlign:"right",
                                            items: [
                                                {
                                                    layout:'form',
                                                    baseCls:'x-plain',
                                                     style:{'margin-left':'1px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                                                    labelAlign:"left",
                                                    items: [
                                                        {
                                                            xtype: 'textfield',
                                                            id:'name'+v,
                                                            style:{'margin-left':'-70px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                                                            readOnly:viewReadOnly,
                                                            fieldLabel:'名称',
                                                            name:'name' ,
                                                            width:   148,
                                                            value:videoStore.data.items[row].data.name,
                                                            allowBlank:  true
                                                        }
                                                    ]

                                                } ,    {
                                                    layout:'form',
                                                    baseCls:'x-plain',
                                                    style:{'margin-left':'25px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                                                    labelAlign:"left",
                                                    items: [
                                                        {
                                                            xtype: 'textfield',
                                                            style:{'margin-left':'-70px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                                                            id:'intValue'+v,
                                                            fieldLabel:'集数',
                                                            value:videoStore.data.items[row].data.intValue,
                                                            name:'intValue' ,
                                                            width:   50,
                                                            allowBlank:  true ,
                                                            readOnly:viewReadOnly
                                                        }
                                                    ]
                                                }


                                            ]
                                        },
                                        {
                                            layout:'table',
                                            layoutConfig: {columns:2} ,
                                            width:300,
                                            baseCls: 'x-plain',
                                         //   baseCls:'my-panel-no-boder',
                                            labelAlign:"right",
                                            items: [
                                                {
                                                    layout:'form',
                                                    baseCls: 'x-plain',
                                               //     baseCls:'my-panel-no-boder',
                                                    items: [
                                                        {
                                                            xtype: 'textfield',
                                                            style:{'margin-left':'-70px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                                                            id:'stringValue'+v,
                                                            fieldLabel:'链接',
                                                            name:'stringValue' ,
                                                            value:videoStore.data.items[row].data.stringValue,
                                                            width:   212,
                                                            readOnly:viewReadOnly,
                                                            allowBlank:  true
                                                        }
                                                    ]
                                                },
                                                {
                                                    xtype:'button',
                                                    baseCls: 'x-plain',
                                                    style:{'margin-left':'0px', 'margin-top':'-5px', 'margin-right':'0px', 'margin-bottom':'0px'},
                                                    width:50,
                                                    text: "文件...",
                                                    disabled:viewReadOnly,
                                                    handler: function() {
                                                        getFtpList(v);
                                                    }
                                                }

                                            ]
                                        } ,
                                        {
                                            xtype: 'textarea',
                                            style:{'margin-left':'-70px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                                            id:'extraData'+v,
                                            fieldLabel:'描述',
                                            readOnly:viewReadOnly,
                                            name:'extraData' ,
                                            width:   265,
                                            heigth:250,
                                            value:videoStore.data.items[row].data.extraData,
                                            allowBlank:  true
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls:'x-plain',
                                   // style:{'margin-left':'5px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                                    labelAlign:"left",
                                    items: [
                                        {
                                            layout:'table',
                                            layoutConfig: {columns:4} ,
                                            width:400,
                                            height:400,
                                            baseCls:'my-panel-no-boder',
                                            labelAlign:"right",
                                            items: [
                                                {
                                                    layout:'table',
                                                    baseCls:'x-plain',
                                                    xtype:'panel',
                                                    style:{'margin-left':'0px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                                                    colspan:4,
                                                    width:400,
                                                    height:360,
                                                    items: [
                                                        {
                                                            xtype: 'box', //或者xtype: 'component',
                                                            width: 395, //图片宽度
                                                            height: 350, //图片高度
                                                            autoEl: {
                                                                id:'clipPoster'+v,
                                                                tag: 'img',    //指定为img标签
                                                                src: picSrc   //指定url路径
                                                            }
                                                        }  ,

                                                        {
                                                            //fieldLabel:'章节海报',
                                                            name:'thumbPic',
                                                            xtype:'textfield',
                                                            readOnly:viewReadOnly,
                                                            id:'thumbPic'+v,
                                                            inputType:'hidden',
                                                            value:picSrc
                                                        }
                                                    ]
                                                },
                                                {
                                                    layout:'table',
                                                    layoutConfig: {columns:4} ,
                                                    width:350,
                                                    baseCls:'my-panel-no-boder',
                                                    labelAlign:"right",
                                                    items: [
                                                        {
                                                            xtype:'button',
                                                            //baseCls: 'x-plain',
                                                            style:{'margin-left':'80px'},
                                                            width:50,
                                                            text: "视频截图",
                                                            disabled:viewReadOnly,
                                                            handler: function() {
                                                                snapClip(v);
                                                            }
                                                        },
                                                        {
                                                            xtype: 'label',
                                                     //       style:{'margin-left':'-80px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                                                            text:'截图时间：'

                                                        }   ,
                                                        {
                                                            xtype: 'numberfield',
                                                            //  style:{'margin-left':'-60px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                                                            readOnly:viewReadOnly,
                                                            //  fieldLabel:'截图时间:',
                                                            name:'snapTime',
                                                            id:'snapTime'+v,
                                                            width:   50,
                                                            allowBlank:  true  ,
                                                            value:10
                                                        }    ,
                                                        {
                                                            xtype: 'label',
                                                            //  style:{'margin-left':'-10px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                                                            text:'秒'
                                                        }
                                                    ]
                                                }

                                            ]
                                        }
                                    ]
                                }
                            ]
                        },



                        {
                            layout:'table',
                            layoutConfig: {columns:2} ,
                            baseCls:'my-panel-no-boder',
                            style:{'margin-left':'70px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                            labelAlign:"right",
                            items: [
                                {
                                    xtype:'button',
                                    baseCls: 'x-plain',
                                    width:80,
                                    text: "确定",
                                    disabled:viewReadOnly,
                                    handler: function() {
//                                            if(getCmpValue('stringValue'+v)!=null&&getCmpValue('stringValue'+v)!=""){
//                                               if(getCmpValue('intValue'+v)!=null&&getCmpValue('intValue'+v)!=""){
//                                                   if(isNumber(getCmpValue('intValue'+v))){
//                                                        Ext .Msg.confirm('信息','确定要保存当前信息',function(btn) {
//                                                            if(btn == 'yes') {
                                        var tabId=tabsMin.getActiveTab();
                                        var row = tabId.get(0).items.items[0].getSelectionModel().last;//得到当前选中的行号
                                        var rec = tabId.get(0).items.items[0].store.getAt(row);
                                        rec.set("thumbPic" , getCmpValue('thumbPic'+v));
                                        rec.set("name" , getCmpValue('name'+v));
                                        rec.set("extraData" , getCmpValue('extraData'+v));
                                        rec.set("stringValue" , getCmpValue('stringValue'+v));
                                        rec.set("intValue" , getCmpValue('intValue'+v));
                                        rec.set("length" , getCmpValue('length'+v));
                                        var propertyId = rec.data.propertyId;
                                        rec.commit();
                                        viewWin.close();
                                        var videoStore=tabId.get(0).items.items[0].store;//得到当前grid中的store
                                        for(var pIdx= 0,pLen = propertyStore.getCount();pIdx<pLen;pIdx++){
                                            var property = propertyStore.getAt(pIdx);
                                            if(propertyId == property.data.id){
                                                //只有是Media_Url_Source才修改时间
                                                if("Media_Url_Source"==property.data.code){
                                                    scanSourceLength(videoStore);
                                                }
                                            }
                                        }


//                                                                }
//                                                        });
//                                               }else{
//                                                   alert('请输入正确的集数');
//                                               }
//                                            }  else{
//                                                    alert('集数不能为空');
//                                            }
//
//                                        }else{
//                                                alert('链接不能为空');
//                                        }

                                    }
                                },
                                {
                                    xtype:'button',
                                    baseCls: 'x-plain',
                                    width:80,
                                    text: "放弃",
                                    handler: function() {
                                        viewWin.close();
//                                        Ext .Msg.confirm('信息','确定要放弃当前修改信息？',function(btn) {
//                                            if(btn == 'yes') {
//                                                viewWin.close();
//                                            }
//                                        });
                                    }
                                }
                            ]
                        }
                    ]
                }
            ]
        });
        viewWin.show();
    }

}

//选择服务器上的视频来源
var ftpUrl = "/";
function getFtpList(v) {
    var pageSize = 20;
    var dataFtpStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort: true,
        url:"/content/content!getFtpList.action",
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {
                name:'name'
            },
            {
                name:'type'
            },
            {
                name:'size'
            },
            {
                name:'date'
            }
        ]
    });

    var winFtp = new Ext.Window({
        title:"文件列表",
        width:514,
        height:483,
        closeAction:"hide",
        closable:true,
        resizable:false,
        modal : true,
        //html:'<h1>Hello,easyjf open source</h1>',
        items:[
            new Ext.grid.GridPanel({
                title:"位置: " + ftpUrl ,
                width:500,
                height:450,
                store: dataFtpStore,
                loadMask:{msg:'正在加载数据，请稍侯……'},
                iconCls:'icon-grid',
                //viewConfig: { forceFit: true },

                listeners:{
                    "cellclick":function(grid, rowIndex, columnIndex)
                    {
                        if(columnIndex){

                        }
                        //alert(grid.getStore().getAt(rowIndex).get('name'));
                        if (grid.getStore().getAt(rowIndex).get('type') == 1) {

                            if (grid.getStore().getAt(rowIndex).get('name') == ".") {
                                ftpUrl = "/";
                            } else if (grid.getStore().getAt(rowIndex).get('name') == "..") {
                                ftpUrl = ftpUrl.substring(0, ftpUrl.lastIndexOf("/", ftpUrl.length - 2) + 1);
                            } else {
                                ftpUrl += grid.getStore().getAt(rowIndex).get('name') + "/";
                            }

                            grid.setTitle("位置: " + ftpUrl);

                            dataFtpStore.baseParams = {
                                url: ftpUrl,
                                deviceId: deviceId
                            };
                            dataFtpStore.load({params:{start:0, limit:pageSize }});
                        } else {
                            var fileUrl = ftpUrl + grid.getStore().getAt(rowIndex).get("name");
                            setCmpValue("stringValue"+v,fileUrl);
                            //getClipLength(fileUrl,"length"+v);
                            winFtp.hide();
                        }
                    }
                },

                columns: [
                    {
                        header: "文件名",
                        dataIndex: 'name',
                        width: 185,
                        sortable: true,
                        align:'left'
                    },
                    {
                        header: "类型",
                        dataIndex: 'type',
                        width: 75,
                        sortable: true,
                        align:'center',
                        renderer:function(val, p, row) {
                            if(p&&row){}
                            if (val == '1') {
                                return '&lt;DIR&gt;'
                            } else {
                                return '&lt;FILE&gt;'
                            }

                        }
                    },
                    {
                        header: "大小",
                        dataIndex: 'size',
                        width: 60,
                        sortable: true,
                        align:'center'
                    },
                    {
                        header: "日期",
                        dataIndex: 'date',
                        width: 130,
                        sortable: true,
                        align:'center'
                    }
                ],

                tbar:[
                    new Ext.Toolbar.TextItem('工具栏：'),//文本
                    {
                        pressed:true,
                        text:'根目录',
                        handler:
                                function() {
                                    ftpUrl = "/";

                                    var grid = winFtp.items.items[0];
                                    grid.setTitle("位置: " + ftpUrl);

                                    dataFtpStore.baseParams = {
                                        url: ftpUrl,
                                        deviceId: deviceId
                                    } ;
                                    dataFtpStore.load({params:{start:0, limit:pageSize }});
                                    //alert(1);
                                }
                    },
                    {
                        xtype:"tbseparator"
                    },//加上这句，后面的就显示到右边去了
                    {
                        pressed:true,
                        text:'上级目录',
                        handler:
                                function() {

                                    ftpUrl = ftpUrl.substring(0, ftpUrl.lastIndexOf("/", ftpUrl.length - 2) + 1);

                                    var grid = winFtp.items.items[0];
                                    grid.setTitle("位置: " + ftpUrl);

                                    dataFtpStore.baseParams = {
                                        url: ftpUrl,
                                        deviceId: deviceId
                                    };
                                    dataFtpStore.load({params:{start:0, limit:pageSize }});
                                    //alert(1);
                                }
                    }

                    //{xtype:"tbfill"},
                ] ,

                bbar:new Ext.PagingToolbar({
                    pageSize: pageSize,
                    store: dataFtpStore,
                    displayInfo: true,
                    displayMsg: '结果数据 {0} - {1} of {2}',
                    emptyMsg: "没有数据"
                })

            })
        ]

    });

var  deviceId =getCmpValue("deviceId");
    dataFtpStore.baseParams = {
        url: ftpUrl,
        deviceId:deviceId
    };
    dataFtpStore.load({params:{start:0, limit:pageSize }});
    winFtp.show();

}
function renderStringValue(val,meta,rec){
    return val;
}
function setPosterImageSrc(newSrc){

    document.getElementById("posterPicture").src=newSrc;
}
var imageStoreName = new Ext.data.ArrayStore({
    fields: ['id', 'name'],
    data: []
});
var userWay;
var  handleMethod;
var imageInfoPanel = {
    xtype:'fieldset',
    collapsible: true,
    autoHeight:true,
    defaultType: 'textfield',
    layout:'table',
    width:1150,
    cls:'xmlStyle',
    title:'海报处理方式',
    layoutConfig:{columns:7}    ,
    items:[
        {xtype:'label',text:'选择处理方式: '},
        {
            id:'userWay',
            xtype: 'combo',
            labelWidth: 100,
            width:130,
            triggerAction: 'all',
            emptyText:'不自动处理',
            store:  new Ext.data.ArrayStore({
                fields: ['id', 'name'],
                data: [ ['0', '自动处理'], ['1', '不自动处理']]
            }),
            listeners:{
                //判断是自动处理还是手动
                'select': function(){
                    var imageNum=0;
                    imageStoreName.removeAll();
                    userWay=Ext.getCmp('userWay').getRawValue();
                    if(userWay=="自动处理"){
                        handleMethod="auto_handle";
                        //循环得到所有海报信息
                        var picGridStore=Ext.getCmp('picGrid').store;
                        for(var i=0,a=picGridStore.getCount();i<a;i++){
                            var row = picGridStore.data.items[i];
                            //如果内容不为空说明有海报
                            if(row.data.stringValue!=""){
                                //得到有海报的名字（向Store 你面加东西）
                                var rec = new Ext.data.Record({id:row.data.stringValue,name:row.data.name});
                                imageStoreName.add(rec)
                                imageNum=imageNum+1;
                            }
                        }
                        if(imageNum==0){
                            alert("暂时没有处理的海报来源，请先选择保存海报！");
                        }
                    }else{
                        handleMethod="handle";
                    }

                }
            } ,
            valueField: 'id',
            displayField: 'name',
            mode:'local',
            loadingText:'加载中...',
            selectOnFocus:true,
            editable: false,
            typeAheadDelay:1000,
            forceSection: true,
            typeAhead: false
        },{xtype:'label',text:"选择处理海报"+':'},
        {
            id:'imageName',
            xtype: 'combo',
            labelWidth: 100,
            width:130,
            triggerAction: 'all',
            emptyText:'请选择海报...',
            store: imageStoreName,
            listeners:{
                'select': function(){
                    handlePicFile=Ext.getCmp("imageName").value;
                }
            } ,
            valueField: 'id',
            displayField: 'name',
            mode:'local',
            loadingText:'加载中...',
            selectOnFocus:true,
//            disabled:true,
            editable: false,
            typeAheadDelay:1000,
            forceSection: true,
            typeAhead: false
        }
    ]
};
var selectRow=0;//当前点击的行
var colModel = new Ext.grid.ColumnModel([
    {id:'id', header:"影片名", width:160, sortable:false, locked:false, dataIndex:'name'},
    {header:"图片信息", width:220, sortable:false, dataIndex:'stringValue',renderer:renderStringValue},
    {width:200, sortable:false, dataIndex:'id',hidden:true}
]);
var gridForm = new Ext.FormPanel({
    id:'company-form',
    frame:true,
    fileUpload:true,
    baseCls:'my-panel-no-border',
    labelAlign:'left',
    bodyStyle:'padding:5px',
    width:700,
    height:473,
    layout:'column',
    items:[
        {
            columnWidth:0.99,
            xtype:'fieldset',
            defaults:{border:false}, // Default config options for child items
            defaultType:'textfield',
            autoHeight:true,
            Cls: 'style:magin-left:30px;',
            height:20,
            items:[
                imageInfoPanel
            ]
        },
        {
            columnWidth:0.55,
            layout:'fit',
            items:{
                xtype:'grid',
                cm:colModel,
                id:'picGrid',
                store:picStore,
                baseCls:'my-panel-no-border',
                sm:new Ext.grid.RowSelectionModel({
                    singleSelect:true,
                    listeners:{
                        rowselect:function (sm, row, rec) {
                            selectRow=row;
                            Ext.getCmp("company-form").getForm().loadRecord(rec);
                            setPosterImageSrc(rec.data.stringValue);//'http://61.55.144.81/'
                            setCmpValue("propertyId",rec.data.id);
                            setCmpValue("contentId",contentId);
                            setCmpValue("id",rec.data.id);
                            setCmpValue("uploadFile","");
                        }
                    }
                }),
                height:455,
                border:true,
                listeners:{
                    viewready:function (g) {
                        g.getSelectionModel().selectRow(0);
                    }
                }
            }
        },
        {
            columnWidth:0.45,
            xtype:'fieldset',
            defaults:{border:false}, // Default config options for child items
            defaultType:'textfield',
            autoHeight:true,
            height:430,
            bodyStyle:Ext.isIE ? 'padding:0 0 0px 0px;' : 'padding:0px 0px;',
            border:false,
            labelAlign:"left",
            style:{
                "margin-left":"5px",
                "margin-top":"20px",
                "margin-right":Ext.isIE6 ? (Ext.isStrict ? "0px" : "0px") : "0"
            },
            items:[
                {
                    fieldLabel:'图片类型',
                    cls:'nameStyle',
                    width:218,
                    id:'name',
                    readOnly:viewReadOnly,
                    name:'name'
                },
                {
                    fieldLabel:'海报路径',
                    cls:'valueStyle',
                    width:218,
                    id:'stringValue',
                    readOnly:viewReadOnly,
                    name:'stringValue'
                },
                {
                    xtype:'textfield',
                    inputType:'hidden',
                    name:'localFileName',
                    id:'localFileName',
                    hidden:true
                },
                {
                    xtype:'fileuploadfield',
                    width:200,
                    cls:'fileStyle',
                    id:'uploadFile',
                    emptyText:'请选择',
                    fieldLabel:'上传文件',
                    name:'uploadFile',
                    disabled:viewReadOnly,
                    buttonText:'选择图片'
                },
                {xtype:'textfield',inputType:'hidden',hidden:true,name:'obj.id',id:'id'},
                {xtype:'textfield',inputType:'hidden',hidden:true,name:'obj.propertyId',id:'propertyId'},
                {xtype:'textfield',inputType:'hidden',hidden:true,name:'obj.contentId',id:'contentId'},
                {
                    xtype:'button',
                    text:'保存海报',
                    id:'uploadFile',
                    disabled:viewReadOnly,
                    cls:'uploadFile',
                    width:90,
                    listeners:{
                        "click":function () {
                            setCmpValue("localFileName", getCmpValue("uploadFile"));
                            if(getCmpValue("uploadFile")==""||getCmpValue("name")==""||getCmpValue("uploadFile")=="请选择") {
                                alert("请选择上传文件，或者选择上传类型！");
                            }else{
                                gridForm.getForm().submit({
                                    url:"/content/contentProperty!updatePoster.action?localFileName=" + getCmpValue("localFileName"),
                                    method:'post',
                                    waitMsg:'正在处理数据，请稍后……',
                                    success:function (form, response) {
                                        var serverData = Ext.util.JSON.decode(response.response.responseText);
                                        var newSrc = serverData.data["obj.stringValue"];
                                        setCmpValue("stringValue", newSrc);
                                        setPosterImageSrc(newSrc);
                                        var picGridStore=Ext.getCmp('picGrid');
                                        picGridStore.store.data.items[selectRow].data.stringValue=newSrc;
                                        picGridStore.store.data.items[selectRow].data.name=getCmpValue("name");
                                        picGridStore.store.getAt(selectRow).commit();
//                                        Ext.getCmp('userWay').value="不自动处理";
                                    }
                                });
                            }
                        }
                    }
                },new Ext.Panel({
                    border:1,
                    id:"image_info",
                    cls:'imagePosition',
                    html:'<img id="posterPicture" width="300" height="300"/>'

                })
            ]
        }
    ]
});

function addTabMin(){
        for(var j=0;j<clipStore.length;j++){
            /*if(clipStore[j].store.data.items[0].data.id<=0){
                clipStore[j].store=getNewContentPropertyStore();
            }*/
            var theStore= clipStore[j].store;
            if(""+theStore=="undefined" || theStore==null){
                theStore = getNewContentPropertyStore();
                clipStore[j].store=theStore;
            }
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id', hidden:true, align:'center', header:"序号", width:60, sortable:false,dataIndex:'id'}                ,

                {header:"影片名称", hidden:false, align:'center', width:150, sortable:true,dataIndex:'name'
                   , editor : new Ext.form.TextField({     transform:'ylfw',        triggerAction: 'all',        hiddenName:'ylfw',        lazyRender:true,        readOnly : viewReadOnly })
                },
                {header:"属性ID", hidden:true, align:'center', width:50, sortable:false,dataIndex:'propertyId'},
                {header:"集数", hidden:false, align:'center', width:50, sortable:false,dataIndex:'intValue'
                    , editor :new Ext.form.NumberField({     transform:'ylfw',        triggerAction: 'all',        hiddenName:'ylfw',        lazyRender:true,        readOnly : viewReadOnly })
                } ,
                {header:"影片信息", hidden:false, align:'center', width:140, sortable:false,dataIndex:'extraData'},
                {header:"媒体ID", hidden:true, align:'center', width:50, sortable:false,dataIndex:'contentId'},
                {header:"地址", hidden:false, align:'center', width:200, sortable:false,dataIndex:'stringValue',
                    editor :new Ext.form.TextField({     transform:'ylfw',        triggerAction: 'all',        hiddenName:'ylfw',        lazyRender:true,        readOnly : viewReadOnly })
//                    {
//                        allowBlank : true
//                    }
                },
                {header:"时长", hidden:false, align:'center', width:50, sortable:false,dataIndex:'length',
                    editor :new Ext.form.NumberField({     transform:'ylfw',        triggerAction: 'all',        hiddenName:'ylfw',        lazyRender:true,        readOnly : viewReadOnly })},
                {header:"图片地址", hidden:true, align:'center', width:50, sortable:false,dataIndex:'thumbPic'},
                {header:"操作", hidden:false, align:'center', width:95, sortable:false,dataIndex:'id',
                    renderer:
                            function (val,p,row){
                                    return "<a href='javascript:void(0);' onclick='delContentProperty();'>删除</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href='javascript:newWindow("+val+");'>详细</a>";
                            }
                }
            ]) ;
            var Record = new Ext .data.Record.create([
                {name:'id',type:'int'},
                {name:'name',type:'string'},
                {name:'contentId',type:'int'},
                {name:'propertyId',type:'int'},
                {name:'intValue',type:'string'},
                {name:'stringValue',type:'string'},
                {name:'desp',type:'string'},
                {name:'extraData',type:'string'}
            ]);


             medioGrid= new Ext.grid.EditorGridPanel({
                width:700,
                height:423,
                sm : new Ext.grid.CheckboxSelectionModel({singleSelect:false}),
                cm:columns,
                frame: true,
                store:clipStore[j].store,
                name:clipStore[j].propertyId,
                disableSelection:true,
                tbar: [
                    {
                        text: '添加',
                        disabled:viewReadOnly,
                        handler : function(){// 点击按钮执行的操作
                            var tabId=tabsMin.getActiveTab();
                            var n =tabId.get(0).items.items[0].store;
                            var storeCount= n.getCount();
                            var p = new Record({
                                id:0,
                                name:tabId.initialConfig.title,
                                contentId:contentId,
                                propertyId:tabId.get(0).items.items[0].name,
                                intValue:0,
                                stringValue :'',
                                desp :'',
                                extraData:''
                            });
                            medioGrid.stopEditing();
                            n.insert(storeCount,p);
                            medioGrid.startEditing(0, 0);
                        }
                    }   ,
                    {
                        text: '删除所选',
                        disabled:viewReadOnly,
                        handler : function(){// 点击按钮执行的操作
                            var tabId=tabsMin.getActiveTab();
                            var row = tabId.get(0).items.items[0].getSelectionModel().getSelections();
                            if (row.length ==0){
                                Ext.Msg.alert("提示","未选择记录！");
                                return;
                            }else{
                                var n =tabId.get(0).items.items[0].store;
                                Ext .Msg.confirm('信息','确定要删除所选？',function(btn) {
                                    if(btn == 'yes') {
                                        for(var i= 0,j=row.length;i<j;i++){
                                            n.remove(row[i]);
                                        }
                                    }
                                });

                            }

                        }
                    }

                ]

            })
            tabsMin.add({
                title:clipStore[j].propertyName,
                baseCls: 'x-plain',
                width:700,
                height:400,
                baseCls:'my-panel-no-border',
                items:[
                    {
                        width:700,
                        xtype: 'panel',
                       baseCls:'my-panel-no-border',
                       // bodyStyle: 'border-width:1px 1px 1px 1px',
                        height:417,
                        //style:{'margin-left':'10px', 'margin-top':'5px', 'margin-right':'0px', 'margin-bottom':'0px'},
                        items:[
                              medioGrid
                        ]
                    }
                ]
            }).show();
    }
}

var baseForm;
function addTextFile(){
    for(var i= 0,j=baseStore.length;i<j;i++){
        var text;
        var type=baseStore[i].property.dataType;
        var contentProperty= baseStore[i].contentProperty[0];
        baseStore[i].property.name=baseStore[i].property.name.replace("[media]","");
        baseStore[i].property.name=baseStore[i].property.name.replace("[real]","");
        if(type==1){
            text =new Ext.form.TextField(
                    {
                        width:570,
                        labelWidth:100,
                        fieldLabel:baseStore[i].property.name,
                        name:"contentProperties["+i+"].stringValue",
                        id:"contentProperties["+i+"].stringValue",
                        readOnly:viewReadOnly,
                        value:contentProperty.stringValue
                    });
        }else if(type==2){
            text =new Ext.form.TextArea(
                    {
                        width:570,
                        labelWidth:100,
                        fieldLabel:baseStore[i].property.name,
                        name:"contentProperties["+i+"].stringValue",
                        id:"contentProperties["+i+"].stringValue",
                        readOnly:viewReadOnly,
                        value:contentProperty.stringValue
                    });
        }else if(type==3){
            text =new Ext.form.NumberField(
                    {
                        width:570,
                        labelWidth:100,
                        fieldLabel:baseStore[i].property.name,
                        name:"contentProperties["+i+"].stringValue",
                        id:"contentProperties["+i+"].stringValue",
                        readOnly:viewReadOnly,
                        value:contentProperty.stringValue
                    });
        }else if(type==4){
            text=new Ext.form.DateField(
                    {
                        width:570,
                        labelWidth:100,
                        fieldLabel:baseStore[i].property.name,
                        name:"contentProperties["+i+"].stringValue"  ,
                        id:"contentProperties["+i+"].stringValue"   ,
                        readOnly:viewReadOnly,
                        format:'Y-m-d H:i:s',
                        value:contentProperty.stringValue
                    });
        }else if(type==5){
            text=new Ext.form.ComboBox({
                    id:"contentProperties["+i+"].stringValue" ,
                    fieldLabel:baseStore[i].property.name,
                    readOnly:viewReadOnly,
                    hiddenName:'obj.id',
                    labelWidth:100,
                    width:570,
                    store: baseStore[i].propertySelectStore,
                    valueField:'id',
                    displayField:'name',
                    mode:'local',
                    triggerAction: 'all',
                    loadingText:'加载中...',
                    emptyText:'请选择...',
                    selectOnFocus:true,
                    editable: false,
                    typeAheadDelay:1000,
                    forceSection: true,
                    typeAhead: false ,
                    value:contentProperty.stringValue

            });
        }
        else if(type==6){
            var radioText =[];
            for(var a= 0,b=baseStore[i].propertySelectStore.getCount();a<b;a++){
                var psStore=baseStore[i].propertySelectStore.data.items[a].data;
                var checkSelect=psStore.id+";";
                radioText.push(
                        {
                            boxLabel:psStore.name,
                            name:'obj.name1'+i,
                            inputValue:psStore.id,
                            disabled:viewReadOnly,
                            checked:contentProperty.stringValue==psStore.id
                        }
                );

            }
            text= new Ext.form.RadioGroup({
                id:"contentProperties["+i+"].stringValue" ,
                fieldLabel:baseStore[i].property.name,
                name:"contentProperties["+i+"].stringValue",
             //   readOnly:false,
                autoHeight:true,
                columns:10,
                items :radioText
               });
      }
    else if(type==7){
            var checkText =[];
            for(var a= 0,b=baseStore[i].propertySelectStore.getCount();a<b;a++){
                var psStore=baseStore[i].propertySelectStore.data.items[a].data;
                var checkSelect=psStore.id+";";
                checkText.push(
                        {
                            boxLabel:psStore.name,
                            label:100,
                            name:'obj.name2'+i,
                            disabled:viewReadOnly,
                            inputValue:psStore.id,
                            checked:contentProperty.stringValue.indexOf(checkSelect)>=0
                        }
                );

            }
            text=new Ext.form.CheckboxGroup({
                id:"contentProperties["+i+"].stringValue" ,
                fieldLabel:baseStore[i].property.name,
                layout:'fit',
                name:"contentProperties["+i+"].stringValue",
            //   readOnly:true,
                autoHeight:true,
                columns: 6,
                items :checkText
            });
     }
        var h1,h2,h3,h4,h5,h6,h7,h8,h9,h10,h11;
        if(type==1||type==2||type==3||type==4||type==5||type==6||type==7){
            h1=  new Ext.form.TextField(
                    {
                        width:570,
                        labelWidth:100,
                        hidden:true,
                        name:"contentProperties["+i+"].id",
                        id:"contentProperties["+i+"].id",
                        value:contentProperty.id
                    });
            h2=   new Ext.form.TextField(
                    {
                        width:570,
                        labelWidth:100,
                        hidden:true,
                        name:"contentProperties["+i+"].contentId",
                        id:"contentProperties["+i+"].contentId",
                        value:contentProperty.contentId
                    });
            h3=  new Ext.form.TextField(
                    {
                        width:570,
                        labelWidth:100,
                        hidden:true,
                        name:"contentProperties["+i+"].desp",
                        id:"contentProperties["+i+"].desp",
                        value:contentProperty.desp
                    });
            h4=  new Ext.form.TextField(
                    {
                        width:570,
                        labelWidth:100,
                        hidden:true,
                        name:"contentProperties["+i+"].extraData",
                        id:"contentProperties["+i+"].extraData",
                        value:contentProperty.extraData
                    });
            h5=  new Ext.form.TextField(
                    {
                        width:570,
                        labelWidth:100,
                        hidden:true,
                        name:"contentProperties["+i+"].extraInt",
                        id:"contentProperties["+i+"].extraInt",
                        value:contentProperty.extraInt
                    });
            h6=  new Ext.form.TextField(
                    {
                        width:570,
                        labelWidth:100,
                        hidden:true,
                        name:"contentProperties["+i+"].intValue",
                        id:"contentProperties["+i+"].intValue",
                        value:contentProperty.intValue
                    });
            h7=  new Ext.form.TextField(
                    {
                        width:570,
                        labelWidth:100,
                        hidden:true,
                        name:"contentProperties["+i+"].length",
                        id:"contentProperties["+i+"].length",
                        value:contentProperty.length
                    });
            h8= new Ext.form.TextField(
                    {
                        width:570,
                        labelWidth:100,
                        hidden:true,
                        name:"contentProperties["+i+"].name",
                        id:"contentProperties["+i+"].name",
                        value:contentProperty.name
                    });
            h9= new Ext.form.TextField(
                    {
                        width:570,
                        labelWidth:100,
                        hidden:true,
                        name:"contentProperties["+i+"].propertyId",
                        id:"contentProperties["+i+"].propertyId",
                        value:contentProperty.propertyId
                    });
            h10= new Ext.form.TextField(
                    {
                        width:570,
                        labelWidth:100,
                        hidden:true,
                        name:"contentProperties["+i+"].subContentId",
                        id:"contentProperties["+i+"].subContentId",
                        value:contentProperty.subContentId
                    });
            h11= new Ext.form.TextField(
                    {
                        width:570,
                        labelWidth:100,
                        hidden:true,
                        name:"contentProperties["+i+"].thumbPic",
                        id:"contentProperties["+i+"].thumbPic",
                        value:contentProperty.thumbPic
                    });
        }

       if(type==6||type==7){
           baseForm.add(text);
        }else{
           baseForm.items.add(baseForm.items.getCount(),text);
       }
        baseForm.items.add(baseForm.items.getCount(),h1,h2,h3,h4,h5,h6,h7,h8,h9,h10,h11);
          baseForm.doLayout();
    }

}

function getAllClipLengthOfStore(clipStore){
    if(clipStore==null){
        return;
    }
    var clipDeviceId = getCmpValue("deviceId");
    if(clipDeviceId!=""&&clipDeviceId!="-1"&&clipDeviceId!=-1){
        var queryUrl = "/interface/getMovieLength.jsp?deviceId="+deviceId;
        var i= 0,l=clipStore.getCount();
        var urls = "";
        for(;i<l;i++){
           if(i!=0){
               urls+=";";
           }
            var row = clipStore.getAt(i);
            var url = row.data.stringValue;
            if(url!=null){
                urls += encodeURI(encodeURI(url));
            }
        }
        queryUrl+="&urls="+urls;
        Ext.Ajax.request({
            url:queryUrl,
            callback : function(opt, success, response) {
                if (success) {
                    var data = Ext.JSON.decode(response.text);
                    setContentPropertyValue("MEDIA_LENGTH",data.resultLength,data.resultLength);
                    var clipInfo = data.files;
                    if(clipInfo!=null){
                        for(var idx= 0,len=clipInfo.length;idx<len;idx++){

                        }
                    }
                }
            }
        });
    }
}

function getClipLength(clipUrl,elementId){
    var clipDeviceId = getCmpValue("deviceId");
    if(clipDeviceId!=""&&clipDeviceId!="-1"&&clipDeviceId!=-1){
        var queryUrl = "/interface/getMovieLength.jsp?deviceId="+clipDeviceId;
        var  urls = encodeURI(encodeURI(clipUrl));
        queryUrl+="&urls="+urls;
        Ext.Ajax.request({
            url:queryUrl,
            callback : function(opt, success, response) {
                if (success) {
                    var data = Ext.util.JSON.decode(response.responseText);
                    setCmpValue(elementId,data.resultLength);
                    var clipInfo = data.files;
                    if(clipInfo!=null){
                    }
                }
            }
        });
    }
}
function setContentPropertyValue(propertyCode,stringValue,intValue){
    var idx = 0,len = baseStore.length;
    for(;idx<len;idx++){
        var row = baseStore[idx];
        var property = row.property;
        if(property.code == propertyCode){
/*
            var contentProperties = row.contentProperty;
            if(contentProperties!=null){

            }
*/
            if(stringValue!=null&&stringValue!=""){
                setCmpValue("contentProperties["+idx+"].stringValue",stringValue);
            }
            if(intValue!=null&&intValue!=""){
                setCmpValue("contentProperties["+idx+"].intValue",intValue);
            }
        }
    }
}
function initDisplay() {
    baseForm= new Ext.FormPanel(
            {
                layout:'form',
                baseCls:'x-plain',
                // renderTo:'displayDiv',
                bodyStyle: 'border-width:1px 1px 1px 1px',
                width:700,
                autoHeight:true,
                id:'baseForm',
                style:{'margin-left':'0px', 'margin-top':'5px', 'margin-right':'0px', 'margin-bottom':'0px'},
                labelAlign:"right"   ,
                items:[

                ]
            }
    );
    var mainPanel = new Ext.Panel({
        title: contentName,
        id:'basePanel',
        style:{'margin-left':'200px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
        items:[
            baseForm  ,
            tabsMax,
            {
                layout:'table',
                layoutConfig: {columns:2} ,
                baseCls:'my-panel-no-boder',
                style:{'margin-left':'510px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                labelAlign:"right",
                items: [
                    {
                        xtype:'button',
                        baseCls: 'x-plain',
                        width:80,
                        text: "重新加载",
                        handler: function() {
                            Ext .Msg.confirm('信息','重新加载会导致修改过的数据还原',function(btn) {
                                if(btn == 'yes') {
                                    window.location.reload();
                                }
                            });
                        }
                    },
                    {
                        xtype:'button',
                    //    style:{'margin-left':'600px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                        baseCls: 'x-plain',
                        width:80,
                        text: "保存",
                        disabled:viewReadOnly,
                        handler: function() {
                            if(getCmpValue("deviceId")==""||getCmpValue("deviceId")==null){
                                alert("请选择服务器");
                            }else{
                                if(isReplace){
                                    Ext .Msg.confirm('信息','确定要修改所有信息？',function(btn) {
                                        if(btn == 'yes') {
                                            isReplace=false;
                                            saveAll();
                                        }
                                    });
                                } else{
                                    alert("保存信息还没处理完，请等待！");
                            }
                            }
                        }
                    }
                ]
            }

        ]
    });
        for(i=0;i<propertyStore.getCount();i++){
            var data = propertyStore.data.items[i].data;
            var dataType= data.dataType;
            if(dataType==8||dataType==9||dataType==10){
                //媒体片段信息
                var store = getNewContentPropertyStore();
                clipStore.push({propertyId:data.id,propertyCode:data.code,propertyName:data.name,store:store});
            }else if(dataType==11){
              //海报信息
                var newRec = new Ext.data.Record({id:-1,propertyId:data.id,name:data.name,stringValue:'',extraData:'',
                extraInt:0,intValue:0,thumbPic:'',desp:'',length:0,subcontentId:'0',contentId:-1});
                picStore.add(newRec);
            }else{
              //基本信息
                var propertyData = propertyStore.data.items[i].data;
                if(propertyData.code!="DEVICE"){
                    baseStore.push({propertyId:propertyData.id,property:propertyData,contentProperty:[],propertySelectStore:new Ext.data.SimpleStore()});
                }
            }
    }
    var cpStore=[];
    for(i=0;i<contentPropertyStore.getCount();i++){
        var cpData = contentPropertyStore.data.items[i].data;
        var propertyId=cpData.propertyId;
        for(j=0;j<clipStore.length;j++){
            if(clipStore[j].propertyId==propertyId){
                clipStore[j].store.add(contentPropertyStore.data.items[i]);
            }
        }
        for(j=0;j<picStore.getCount();j++){
            var picData = picStore.data.items[j].data;
            if(picData.propertyId==propertyId){
                for(var prop in cpData){
                    picData[prop] = cpData[prop];
                }
/*
                picData.stringValue=cpData.stringValue;
                picStore.data.items[j].data.name=contentPropertyStore.data.items[i].data.name;
*/
            }
        }
    }
    for(j=0;j<clipStore.length;j++){
        clipStore[j].store.sort("intValue","asc");
    }
    for(j=0;j<baseStore.length;j++){
        for(i=0;i<contentPropertyStore.getCount();i++){
            var propertyId=contentPropertyStore.data.items[i].data.propertyId;
            if(baseStore[j].propertyId==propertyId){
                baseStore[j].contentProperty.push(contentPropertyStore.data.items[i].data);
            }
        }
    }
    for(i=0;i<propertySelectStore.getCount();i++){
        var propertyId=propertySelectStore.data.items[i].data.propertyId;
        for(var j=0;j<baseStore.length;j++){
            if(baseStore[j].propertyId==propertyId){
                baseStore[j].propertySelectStore.add(propertySelectStore.data.items[i]);
            }
        }
    }
    addTabMin();
    addTextFile();

    try {
        displayDiv.innerHTML = "";
        mainPanel.render("displayDiv");
    } catch (e) {
       // alert("发生异常！");
    }
}




Ext.onReady(function() {
    Ext.QuickTips.init();
    mediumInfo();
    queueFunctions([
        {
            func:allStoreLoad,
            done:false,
            flag:'allStoreLoad'
        }
    ],

            initDisplay);

});



</script>
<body>
<table id="viewMainTable">
    <tr>
        <td>
            <div id="displayDiv" class="viewMainDiv"><div  style="background-color: #FEFEFE;text-align:center;padding-left: 200px;padding-top: 200px;width:600px;height:400px;font-size: 50px;"><img src="../admin/images/loading.gif"/>正在加载。。。</div></div>
        </td>
    </tr>
</table>
</body>
</html>