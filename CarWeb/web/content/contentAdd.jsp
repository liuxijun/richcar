<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 12-7-28
  Time: 下午12:10
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp" %><%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"getCspDevice","deviceGetDeviceRegUrl");
//        needPermissions(actionHeader,"list","propertyList");
//        needPermissions(actionHeader,"list","propertySelectList");
        //      needPermissions(actionHeader,"uploadFile","contentManage,contentUploadFile");
        needPermissions(actionHeader,"save","contentManage,contentSave");
        needPermissions(actionHeader,"getFtpList","contentManage,contentGetFtpList");
        needPermissions(actionHeader,"getDeviceRegUrl","deviceGetDeviceRegUrl");
    }
    Admin contentOperator =(Admin)session.getAttribute(
                    com.fortune.common.Constants.SESSION_ADMIN);
%><%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title></title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript" src="/js/common.js"></script>
    <script type="text/javascript" src="contentViewV2.js"></script>
    <script type="text/javascript" src="../js/FtpFileSelector.js"></script>
    <script language="javascript">
        nextUrl = "contentList.jsp";
        var contentViewForm;
        var defaultPostUrl = "../images/gongneng1.jpg";
        var clipSn= 0;
        var clipCount = 0;
        var isModify = (keyId!=""&&keyId!=null&&keyId!="null"&&keyId!="-1"&&keyId!=-1);

        var contentPropertyFields = ['id','contentId','desp','extraData','extraInt','intValue','length',
            'name','propertyId','stringValue','subContentId','thumbPic','sn'];

        storeConfig.fields = contentPropertyFields;
        storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: 'contentProperty!list.action?obj.contentId='+keyId});
        storeConfig.baseParams={limit:1000};

        searchStore = new FortuneSearchStore(storeConfig);
        var systemIsBusy = true;
        if(isModify){
            searchStore.setDefaultSort("o1.intValue","asc");
            searchStore.load({callback:function(records,options,success){
                if(success){
                    tryTimes = 0;
                    reDraw();
                }
            }});
        }
        keyFieldId = "id";

        function fileSelectWinClosed(files){
            var name = "contentProperties_"+currentClipId+"_stringValue";
            var clipUrlObj = Ext.getCmp(name);
            if(clipUrlObj!=null){
                clipUrlObj.setValue(files);
            }else{
                alert("没有找到控件："+name);
            }
        }
        function fileSelectForEncodedWinClosed(fileNames){
            var name = "urls_"+currentClipInfo.intValue;
            var clipUrlGridObj = Ext.getCmp(name);
            if(clipUrlGridObj!=null){
                var urlsStore = clipUrlGridObj.getStore();
                var files = fileNames.split(";");
                var i=0,l=files.length;
                var sn=currentClipInfo.sn;
                var pl=encoderTemplateStore.getCount();
                for(;i<l;i++){
                    clipSn++;
                    //clipCount++;
                    var file = files[i];
                    var pi=0;
                    var propertyId;
                    var clipName = getCmpValue("contentProperties_"+sn+"_name");
                    for(;pi<pl;pi++){
                        var templateRec = encoderTemplateStore.getAt(pi);
                        var templateCode=templateRec.get("templateCode");
                        propertyId = templateRec.get("propertyId");
                        if(file.indexOf("."+templateCode+".")>0){
                            clipName += " "+templateRec.get("templateName");
                            break;
                        }
                    }
                    var rec = new Ext.data.Record({propertyId:propertyId,id:-1,
                        intValue:currentClipInfo.intValue,sn:clipSn,
                        stringValue:file,extraData:'',extraInt:0,
                        desp:getCmpValue("contentProperties_"+sn+"_desp"),
                        name:clipName,
                        length:getCmpValue("contentProperties_"+sn+"_length"),
                        contentId:getCmpValue("contentProperties_"+sn+"_contentId"),
                        subContentId:getCmpValue("contentProperties_"+sn+"_subContentId")
                    },clipSn);
                    urlsStore.add(rec);
                    searchStore.add(rec);
                }
            }else{
                alert("没有找到控件："+name);
            }
        }
        function addEncodedClip(clipId,clipNo,sn){
            currentClipInfo = {id:clipId,intValue:clipNo,sn:sn};  //集数
            var deviceId = getCmpValue('obj.deviceId');
            if(deviceId!=null&&deviceId!=""&&deviceId!="null"){
                showFtpSelectWindow(fileSelectForEncodedWinClosed,
                        {
                            url:"../system/device!listFiles.action?obj.id="+deviceId,
                            selectFileOnly:true,
                            singleSelect:false
                        }
                );
            }else{
                alert("请选择一个流媒体服务器！");
            }
        }
        function deleteSelectClips(clipId,clipNo,sn){
            alert("clipId="+clipId+",clipNo="+clipNo+",sn="+sn);
        }
        var currentClipId;
        var currentClipInfo;
        function selectSourceFile(clipId){
            currentClipId = clipId;
            var deviceId = getCmpValue('encoderId');
            if(deviceId!=null&&deviceId!=""&&deviceId!="null"){
                showFtpSelectWindow(fileSelectWinClosed,
                        {
                            url:"../system/device!listFiles.action?obj.id="+deviceId,
                            selectFileOnly:true,
                            singleSelect:true
                        }
                );
            }else{
                alert("请选择一个转码服务器！");
            }
        }
        var encoderTemplateStore = new Ext.data.JsonStore({
            method:'POST',
            url:"/encoder/encoderTemplate!list.action",
            baseParams:{limit:1000000},
            totalProperty:'totalCount',
            root:'objs',
            autoLoad:true,
            fields:[
                {name:'id'},
                {name:'propertyId'},
                {name:'templateCode'},
                {name:'templateName'}
            ]
        });
        var encodeDeviceStore = new Ext.data.JsonStore({
            method:'POST',
            url:"/system/device!list.action?obj.type=4",
            baseParams:{limit:1000000},
            totalProperty:'totalCount',
            root:'objs',
            autoLoad:true,
            fields:[
                {name:'id'},
                {name:'name'}
            ]
        });
        var streamDeviceStore = new Ext.data.JsonStore({
            method:'POST',
            url:"/system/device!list.action?obj.type=2",
            baseParams:{limit:1000000},
            totalProperty:'totalCount',
            root:'objs',
            autoLoad:true,
            fields:[
                {name:'id'},
                {name:'name'}
            ]
        });
        function collectionClipData(){
            var allData = contentViewForm.getForm().getValues();
            if(allData!=null){

            }
        }

        function setFormFieldValue(id,value,form){
            var field = form.findField(id);
            if(field!=null){
                field.setValue(value);
            }
        }
        var tryTimes = 0;
        function reDraw(){
            if(systemIsBusy){
                if(tryTimes<10){
                    tryTimes++;
                    window.setTimeout("reDraw()",1000);
                }else{
                    alert("系统稍稍有些小忙，请稍候刷新，谢谢。");
                }
                return;
            }
            var allData = contentViewForm.getForm().getValues();
            displayDiv.innerHTML = '';
            initDisplay(allData);
            var form = contentViewForm.getForm();
            form.setValues(allData);
            rescanClips();
//            setCmpValue("encoderId",encoderId,form);
//            setCmpValue("obj.deviceId",deviceId,form);
            for(var i=0,l=searchStore.getCount();i<l;i++){
                var clip = searchStore.getAt(i);
                var posterSrc = clip.get("thumbPic");
                if(posterSrc!=null&&posterSrc!=""&&posterSrc!="null"){
                    var posterObj = document.getElementById('clipPoster_'+i);
                    if(posterObj!=null){
                        posterObj.src = posterSrc;
                    }
                }
            }
            var mediaPosterPicObj = document.getElementById("mediaPosterPic");
            if(mediaPosterPicObj!=null){
                mediaPosterPicObj.src = getCmpValue("obj.post1Url");
            }

        }
        function newClip(){
            clipSn ++;
            clipCount++;
            //allClips.push({clipId:clipSn});
            var rec = new Ext.data.Record({propertyId:1,id:-1,intValue:clipCount,sn:clipSn},clipSn);
            searchStore.add(rec);
            tryTimes = 0;
            reDraw();
        }
        function getClip(clipSnValue){
            for(var index=0,l=searchStore.getCount();index<l;index++){
                var clip = searchStore.getAt(index);
                if(clip.get("sn")==clipSnValue){
                    return clip;
                }
            }
            return null;
        }
        function copyClip(clipId){
            alert(clipId);
        }
        function removeClip(sn){
/*
            var clipCollectionObj = Ext.getCmp("clipCollection");
            if(clipCollectionObj!=null){
                var clipObj = Ext.getCmp("clipInfo_"+clipId);
                if(clipObj!=null){
                    clipCollectionObj.remove(clipObj);
                }
            }
*/
            var clip = getClip(sn);
            if(clip!=null){
                var intValue = clip.get("intValue");//这些都删
                for(var index=0,l=searchStore.getCount()-1;index<=l;l--){
                    var checkClip = searchStore.getAt(index);
                    if(checkClip.get("intValue")==intValue){
                        searchStore.remove(checkClip);
                    }
                }
            }
            tryTimes = 0;
            reDraw();
        }
        var picNo = 0;
        var startTimeSec = 100;
        var snapServerUrl = "";
        var makeNewUrl = true;
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
                    +"c"+getCmpValue("obj.contentId")+"_"+picNo+"_";
            if(snapTime){
              result += snapTime+"_";
            }
            result += now.getHours()+"_"+now.getMinutes()+"_"+now.getSeconds()+"_"+Math.round(Math.random() * 100000) + ".jpg";
            return result;
        }
        function toCover(clipId){
            //将本片断的截图作为本影片的封面
            var posterObj = document.getElementById('clipPoster_'+clipId);
            if(posterObj!=null){
                var mediaPosterPicObj = document.getElementById("mediaPosterPic");
                if(mediaPosterPicObj!=null){
                    var imgSrc = getParameter(posterObj.src,"thumbPicUrl");
                    if(imgSrc==null||imgSrc==""||imgSrc==-1||imgSrc=="-1"){
                        imgSrc = posterObj.src;
                    }
                    mediaPosterPicObj.src = imgSrc;
                    if(imgSrc.indexOf("http://")==0){
                        //先判断一下 是否是同一个服务器上，如果是同一台服务器，就把前面的主机连接去了
                        var tempImgSrc = imgSrc.substring(8);
                        var p = tempImgSrc.indexOf("/");

                        if(p>0){
                            var imgHostUrl = imgSrc.substring(0,8+p);
                            var hostUrl = window.location.href.substring(0,8+p);
                            if(imgHostUrl == hostUrl){
                                imgSrc = tempImgSrc.substring(p);
                            }
                        }
                    }
                    setCmpValue("obj.post1Url",imgSrc);
                    makeNewUrl = true;
                }
            }
        }
        var oldSnapClipId=-1;
        function snapClip(clipId){
            if(oldSnapClipId!=clipId){
                makeNewUrl = true;
                oldSnapClipId = clipId;
            }
            var posterObj = document.getElementById('clipPoster_'+clipId);
            if(posterObj!=null){
                var deviceId=getCmpValue("encoderId");
                if(deviceId!=null&&deviceId!=""&&deviceId!="null"){
                    var snapTime = getCmpValue("snapTime");
                    if(snapTime!=""&&snapTime!=null){
                        startTimeSec = snapTime;
                    }
                    var mediaUrl = getCmpValue("contentProperties_"+clipId+"_stringValue");
                    if(mediaUrl!=null&&mediaUrl!=""){
                        mediaUrl = encodeURI(encodeURI(mediaUrl));
                        var thumbPicUrl = "";
                        var contentId = contentViewForm.getForm().findField('obj.id').getValue();
                        var clipPosterField = contentViewForm.getForm().findField("contentProperties_"+clipId+"_thumbPic");
                        if (contentId == null || contentId == "") {
                            if (clipPosterField.getValue() == "") {
                                thumbPicUrl = getNewThumbPicFullPath();
                                //alert(thumbPicUrl);
                                clipPosterField.setValue(thumbPicUrl);
                            }
                        } else {
                            if(makeNewUrl) {
                                thumbPicUrl = getNewThumbPicFullPath();
                                //alert(thumbPicUrl);
                                clipPosterField.setValue(thumbPicUrl);
                                makeNewUrl = false;
                            }
                        }
                        posterObj.src = snapServerUrl+"/interface/snapMedia.jsp?deviceId="+deviceId+"&mediaUrl="+mediaUrl+
                                "&startTime=" +
                                startTimeSec +

                                "&thumbPicUrl=" + clipPosterField.getValue() +
                                "&date=" + (new Date()).getTime();
                    }
                }
            }
        }
        function upClip(clipId){

        }
        function downClip(clipId){

        }
        function newPosterSelected(imgSrc) {
            try {
                setCmpValue("uploadFileLocal",imgSrc);
            } catch(e) {
                alert("发生错误：'" + e.description + "'，\n可能是form中没有这个field:" + 'localFileName');
            }
            var imgObj = Ext.getCmp("mediaPosterPic");
            if (imgObj) {
                //imgObj.getEl().dom.src = imgSrc;
            } else {
                alert("无法找到图片组件：mediaPosterPic");
            }
        }

        Ext.onReady(function() {
            Ext.QuickTips.init();
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
/*
            queueFunctions([
                {
                    func:initDictStores,
                    done:false,
                    flag:'initDictStores'
                }
            ],initDisplay
            );
*/
//            initDisplay({});
//            systemIsDrawing = true;
            loadFormFromServerData();
        });
        function displayThumbPic(val, metaData, record){
            if (metaData && record) {

            }
            var imgHeight = record.get("height");
            var imgWidth = record.get("width");
            if(imgHeight==0){
                imgHeight = 112;
                imgWidth = 108;
            }else{
                var rate=0.1;
                imgHeight = imgHeight*rate;
                imgWidth = imgWidth*rate;
            }
            var picUrl = encodeURI(encodeURI(val));
            return "<img height='"+imgHeight+"' width='"+imgWidth+"' src='getSmallPic.jsp?fileName="+picUrl
                    +"&width="+imgWidth+"&height="+imgHeight+"' alt='"+val+"'><br/>"+val;
        }
        function initDisplay(allData){
            systemIsBusy = true;
            var clips = [];
            clipCount=0;
            clipSn = 0;
            for(var c0=0,l=searchStore.getCount();c0<l;c0++){
                var clip = searchStore.getAt(c0);
                clip.data["sn"]=clipSn;
                var clipInfo = getClipInfo(clipSn,recordToContentPropertyArray(clip,clipSn),clip);
                if(clipInfo!=null){
                    clips.push(clipInfo);
                    clipCount++;
                }
                clipSn++;
            }
            contentViewForm = new Ext.FormPanel({
                border:true,
                title: '课件详细信息',
                //bodyStyle:'padding:5px 5px 0',
                width:650,
                fileUpload:true,
                autoHeight:true,
                labelWidth:60,
                saveUrl:"content!saveCourse.action",
                id:'BaseViewForm338547183092',
                layout:'table',
                //baseCls:'x-plain',
                layoutConfig: {columns:2},
                // applied to child components
                defaults: {frame:false, width:314, height: 200},
                items:[
                    {
                        title:'基本信息',
                        autoHeight:true,
                        xtype:'fieldset',
                        layout:'form',
                        defaults: {width: 200},
                        defaultType: 'textfield',
                        items: [
                            getBaseItem('课件名称','name',false,allData),
                            getBaseItem('课件分类','directors',false,allData),
                            getBaseItem('讲师姓名','actors',false,allData),
                            getBaseItem('关键词','property8',false,allData),
                            {
                                fieldLabel:'课件封面',
                                name:'uploadFile',
                                // inputType:'file',
                                autoCreate : {
                                    tag : "input",
                                    type : "file",
                                    size : "20",
                                    autocomplete : "off",
                                    onChange : "newPosterSelected(this.value);"
                                }
                            },getHiddenItem('id',allData),
                            getHiddenItem('creatorAdminId',allData),
                            getHiddenItem('createTime',allData),
                            getHiddenItem('cspId',allData),
                            getHiddenItem('moduleId',allData),
                            getHiddenItem('status',allData),
                            getHiddenItem('statusTime',allData),
                            getHiddenItem('contentAuditId',allData),
                            getHiddenItem('digiRightUrl',allData),
                            getHiddenItem('validStartTime',allData),
                            getHiddenItem('validEndTime',allData),
                            getHiddenItem('allVisitCount',allData),
                            getHiddenItem('monthVisitCount',allData),
                            getHiddenItem('weekVisitCount',allData),
                            getHiddenItem('post1Url',allData),
                            getHiddenItem('post2Url',allData),
                            getHiddenItem('property1',allData),
                            getHiddenItem('property2',allData),
                            getHiddenItem('property3',allData),
                            getHiddenItem('property4',allData),
                            getHiddenItem('property5',allData),
                            getHiddenItem('property6',allData),
                            getHiddenItem('property7',allData),
                            {
                                id:'uploadFileLocal',
                                name:'uploadFileLocal',
                                inputType:'hidden'
                            }
                        ]
                    },{
                        id:'mediaPoster',
                        xtype:'fieldset',
                        layout:'form',
                        border:false,
                        autoHeight:false,
                        height:196,
                        defaultType: 'textfield',
                        items:[
                            {
                                xtype:'box',
                                width : 260,
                                // 图片宽度
                                height : 196,
                                // 图片高度
                                id : 'mediaPosterPic',
                                autoEl : {
                                    tag : 'img', // 指定为img标签
                                    src : defaultPostUrl // 指定url路径
                                }

                            }
                        ]
                    },{
                        xtype:'fieldset',
                        title:'课件介绍',
                        //                            autoHeight:true,
                        layout:'fit',
                        width:610,
                        height:206,
                        labelAlign:'top',
                        colspan:2,
                        items:[
                            {
                                fieldLabel:'详情',
                                xtype:'htmleditor',
                                width:560,
                                autoHeight:false,
                                name:'obj.intro',
                                readOnly:viewReadOnly
                            }
                        ]
                    },{
                        xtype:'fieldset',
                        title:'章节片段',
                        layout:'fit',
                        width:610,
                        autoHeight:true,
                        labelAlign:'top',
                        colspan:2,
                        id:'clipCollection',
                        name:'clipCollection',
                        tbar:[
                            {text:'增加章节',handler:newClip},
                            {xtype:'label',text:'转码服务器：'},
                            new FortuneCombo({
                                fieldLabel:'服务器',
                                width:100,
                                valueField:'id',
                                allowBlank:false,
                                hiddenId:'encoderId',
                                hiddenName:'encoderId',
                                store:encodeDeviceStore,value:allData["encoderId"]
                             }),
                            {xtype:'label',text:'点播服务器：'},
                            new FortuneCombo({
                                fieldLabel:'服务器',
                                width:100,
                                valueField:'id',
                                readOnly:keyId!=null&&keyId!=-1&&keyId!=""&&keyId!="null"&&keyId!="-1",
                                hiddenId:'obj.deviceId',
                                allowBlank:false,
                                hiddenName:'obj.deviceId',
                                store:streamDeviceStore,
                                value:allData["obj.deviceId"]=="0"?"15879285":allData["obj.deviceId"]
                            })
                            ,{xtype:'label',text:'截图时间：'}
                            ,{xtype:'textfield',name:'snapTime',id:'snapTime',width:60,value:allData["snapTime"]?allData["snapTime"]:120}
                            ,{xtype:'label',text:'秒'}
                            /*new FortuneCombo({
                                valueField:'id',
                                displayField:'display',
                                width:40,
                                editable: true,
                                //id:'encoderId', \
                                value:allData["snapTime"],
                                hiddenName:'snapTime',
                                store:new Ext.data.ArrayStore({
                                    fields: ['id', 'display'],
                                    data: [['60', '60'],['120', '120'],['240', '240'],['360', '360'],
                                        ['480', '480'],['360', '360'],['480', '480'],
                                        ['240', '240'],['360', '360'],['480', '480']]
                                })
                            })*/
                            /*
                            ,{
                                xtype:'text',
                                id:'snapTime',
                                name:'snapTime'
                            }
*/
                        ],
                        items:clips
                    }
                ],
                buttons:[
                    {text:'保存数据',handler:saveData},{text:'重新加载',handler:loadData}
                ]
            });
            //loadFormFromServerData();
            contentViewForm.render("displayDiv");
            if(systemIsBusy){
                systemIsBusy = false;
            }
        }
        var allTemplates="";
        function collectEncodeTemplates(clipId) {
            var encodeTemplates = "";
            var group = Ext.getCmp('encodeTemplateSets_'+clipId);
            if (group == null) {
                alert('没有找到encodeTemplateSets_'+clipId);
                return;
            }
            group.items.each(function(item, index) {
                if (item.checked) {
                    if (encodeTemplates!="") {
                        encodeTemplates += ",";
                    }
                    encodeTemplates += item.value;
                }
            });
            var extraData = contentViewForm.getForm().findField("contentProperties_"+clipId+"_extraData");
            if(extraData!=null){
                extraData.setValue(encodeTemplates);
            }
            allTemplates+=encodeTemplates;
        }
        function saveToStore(){
            //遍历store，把propertyId=1的数据收集起来
            for(var i=0,l=searchStore.getCount();i<l;i++){
                var rec = searchStore.getAt(i);
                if(rec!=null){
                    var propertyId = rec.get("propertyId");
                    if(propertyId==1||propertyId=="1"){
                        for(var j=0,jl=contentPropertyFields.length;j<jl;j++){
                            var fieldId = contentPropertyFields[j];
                            var sn = rec.get("sn");
                            if(fieldId!="sn"){
                                rec.data[fieldId] = getCmpValue("contentProperties_"+sn+"_"+fieldId);
                            }
                        }
                    }
                }
            }
        }
        function saveData(){
            allTemplates="";
            var baseParams = {};
            for(var i=0,l=searchStore.getCount();i<l;i++){
                var rec = searchStore.getAt(i);
                if(rec!=null){
                    var propertyId = rec.get("propertyId");
                    if(propertyId==1||propertyId=="1"){
                        collectEncodeTemplates(rec.get("sn"));//收集编码配置
                        //收集源文件表单配置
                        for(var j=0,jl=contentPropertyFields.length;j<jl;j++){
                            var fieldId = contentPropertyFields[j];
                            var sn = rec.get("sn");
                            if(fieldId!="sn"){
                                rec.data[fieldId] = getCmpValue("contentProperties_"+sn+"_"+fieldId);
                            }
                        }
                    }
                    //将这个rec输出到baseParams
                    for(var m=0,ml=contentPropertyFields.length;m<ml;m++){
                        var fieldId0 = contentPropertyFields[m];
                        if(fieldId0!="sn"){
                            baseParams["contentProperties["+i+"]."+fieldId0]=rec.get(fieldId0);
                        }
                    }
                }
            }
            //contentViewForm.getForm().findField("encoderId").setValue(deviceId);
//            var allData = contentViewForm.getForm().getValues();
            if(allTemplates!=""){//如果有转码要求被选择，则必须有一个转码服务器被选择
                var encoderId = getCmpValue("encoderId");
                if(encoderId==""){
                    alert("请选择编码服务器!");
                    return;
                }
            }
            var deviceId = getCmpValue("obj.deviceId");
            if(deviceId==""||deviceId=="0"||deviceId=="null"||deviceId==null){
                alert("请选择媒体服务器!");
                return;
            }
            contentViewForm.getForm().baseParams = baseParams;
            saveFormAjax();
        }
        function loadData(){

        }
        function getHiddenItem(name,allData){
            if(name.indexOf("obj.")<0) name = "obj."+name;
            return {name:name,id:name,inputType:'hidden',value:allData[name]};
        }
        function getBaseItem(fieldLabel,propertyName,allowBlank,allData,defaultValue,inputType,xtype){
            var name="obj."+propertyName;
            var result = {name:name,id:name,fieldLabel:fieldLabel,allowBlank:allowBlank};
            if(xtype) result["xtype"]=xtype;
            if(inputType) result["inputType"]=inputType;
            if(allData){
                var value = allData[name];
                result["value"]=value?value:defaultValue;
            }
            return result;
        }

        function getItem(clipNo,fieldLabel,propertyName,inputType,allData,xtype,defaultValue,allowBlank){
            var name="contentProperties_"+clipNo+"_"+propertyName;
            var result = {name:name,id:name,fieldLabel:fieldLabel,allowBlank:allowBlank};
            if(xtype) result["xtype"]=xtype;
            if(allData){
                var value = allData[name];
                result["value"]=value?value:defaultValue;
            }
            if(inputType) result["inputType"]=inputType;

            return result;
        }
        function renderUrlControl(val,meta,rec){

        }

        function recordToContentPropertyArray(rec,index){
            var fieldHeader="contentProperties_"+index+"_";
            var result  = {};
            for(var i=0,l=contentPropertyFields.length;i<l;i++){
                var fieldName = contentPropertyFields[i];
                result[fieldHeader+fieldName]=rec.get(fieldName);
            }
            return result;
        }

        function rescanClips(){
            for(var c0=0,l=searchStore.getCount();c0<l;c0++){
                var clip = searchStore.getAt(c0);
                var propertyId = clip.get("propertyId");
                if(propertyId !=null && propertyId !=1 && propertyId!=""){
                    var intValue = clip.get("intValue");
                    var theGrid = Ext.getCmp('urls_'+intValue);
                    if(theGrid!=null){
                        var urlsStore= theGrid.getStore();
                        urlsStore.add(clip);
                    }
                }
            }
        }
        function getTemplateItem(checked){
            var i=0,l=encoderTemplateStore.getCount();
            var result = [];
            for(;i<l;i++){
                var rec=encoderTemplateStore.getAt(i);
                result.push({boxLabel:rec.get('templateName'),value:rec.get('id'),checked:checked});
            }
            return result;
        }

        function getClipInfo(clipNo,allData,rec){
            if(allData==null){
                allData={};
            }
            var field="contentProperties_"+clipNo+"_";
            var clipId = allData[field+"id"];
            var propertyId = allData[field+"propertyId"];
            if(propertyId==null||propertyId=="1"||propertyId==""){//不是源文件的属性 ，不显示 ，隐藏
            }else{
                return null;
            }
            var intValue = allData[field+"intValue"];
            if(intValue==""||intValue==null||intValue=="null"){
                intValue="0";
                allData[field+"intValue"]=intValue;
            }
            isModify = !(clipId == null || clipId == "" || clipId == -1 || clipId == "-1" || clipId == "null");
            var infoItems=[
                getItem(clipNo,"主键","id","hidden",allData,null,null,true),
                getItem(clipNo,"关联内容ID","contentId","hidden",allData,null,null,true),
                getItem(clipNo,"属性ID","propertyId","hidden",allData,null,1,true),
                getItem(clipNo,"章节扩展数据","extraInt","hidden",allData,null,"",true),
                getItem(clipNo,"章节扩展数据Str","extraData","hidden",allData,null,"",true),
                getItem(clipNo,"章节名称","name",null,allData,null,'第'+intValue+'个章节',false),
                getItem(clipNo,"章节时长","length","hidden",allData,null,0,true),
                getItem(clipNo,"第几集","intValue","hidden",allData,null,0,true),
                getItem(clipNo,"章节简介","desp",null,allData,"textarea",0,true)];
            infoItems.push([new Ext.form.SelectField({
                fieldLabel:'编码文件',
                name:field+"stringValue",
                id:field+"stringValue",
                value:allData[field+"stringValue"],
                listeners:{
                    select:function(){selectSourceFile(clipNo)}
                },
                readOnly:true
            }),
                {
                    fieldLabel:'编码选项',
                    layout:'form',
                    xtype:'checkboxgroup',
                    columns:3,
                    border:true,
                    autoWidth:true,
                    id:'encodeTemplateSets_'+clipNo,
                    name:'encodeTemplateSets_'+clipNo,
                    items:getTemplateItem(!isModify)
                }]);
            //编码后的连接作为一个列表存在
//                infoItems.push(urlsGrid);
            var urlsGrid =  {
                border:true,
                xtype:'grid',
                title:'已经编码的内容',
                width:585,
                height:160,
                colspan:2,
                id:'urls_'+intValue,//注意，不是clipNo,而是第几集 ,和数据库数据有关联
                name:'urls_'+intValue,
                sm: new Ext.grid.CheckboxSelectionModel({singleSelect : false}),
                store:new Ext.data.SimpleStore({
                    fields:contentPropertyFields
                }),
                tbar:[{text:'添加流媒体文件',handler:function(){addEncodedClip(clipId,intValue,clipNo)}}
                    ,{text:'删除所选',handler:function(){deleteSelectClips(clipId,intValue,clipNo)}}
                ],
                cm:new Ext.grid.ColumnModel([
                    sm,
                    {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'}                ,
                    {header: "名称",hidden:false, align:'left', width: 80, sortable: true,dataIndex: 'name'}                ,
                    {header: "连接",hidden:false, align:'left', width: 180, sortable: true,dataIndex: 'stringValue'}                ,
                    {header: "时长",hidden:false, align:'center', width: 100, sortable: true,dataIndex: 'length',
                        renderer:formatTime},
                    {hidden:false,align:'center',header: "信息", width: 100, sortable: true, dataIndex: 'extraData'}                ,
                    {header:'管理',align:'center',renderer:renderUrlControl}
                ])
            };
            var clipItems =[
                {
                    id:'clipPosterItem_'+clipNo,
                    xtype:'fieldset',
                    layout:'form',
                    border:false,
                    autoHeight:false,
                    height:196,
                    defaultType: 'textfield',
                    items:[
                        {
                            xtype:'box',
                            width : 260,
                            // 图片宽度
                            height : 196,
                            // 图片高度
                            id : 'clipPosterBox_'+clipNo,
                            name:'clipPosterBox_'+clipNo,
                            autoEl : {
                                id:  'clipPoster_'+clipNo,
                                tag : 'img', // 指定为img标签
                                src : defaultPostUrl // 指定url路径
                            }

                        },
                        {
                            fieldLabel:'章节海报',
                            name:field+"thumbPic",
                            id:field+"thumbPic",
                            inputType:'hidden',
                            value:allData[field+"thumbPic"]
                        }
                    ]
                },
                {
                    autoHeight:true,
                    xtype:'fieldset',
                    layout:'form',
                    title:'第'+clipNo+'个章节',
                    width:310,
                    labelAlign:'left',
                    labelWidth:60,
                    defaults: {width: 220},
                    defaultType: 'textfield',
                    items: infoItems
                }

            ];
            //原来的判断里，如果是添加，则只能是从转码服务器上找原始文件。为了通用，还是要考虑到已经编码文件
            if(isModify){
            }
            clipItems.push(urlsGrid);
            return {
                clipId:clipNo,
                layout:'table',
                autoHeight:true,
                id:'clipInfo_'+clipNo,
                name:'clipInfo_'+clipNo,
                border:true,
                layoutConfig: {columns:2},
                buttons:[
                    //{width:40,text:'复制',handler:function(){copyClip(clipNo)}},
                    //{xtype:'text',id:'snapTime'},
                    {width:80,text:'视频截图',handler:function(){snapClip(clipNo)}}
                    ,{width:80,text:'变成封面',handler:function(){toCover(clipNo)}}
                    ,{width:80,text:'删除本段',handler:function(){removeClip(clipNo)}}
                    //,{width:40,text:'上移',handler:function(){upClip(clipNo)}}
                    //,{width:40,text:'下移',handler:function(){downClip(clipNo)}}
                ],
                items:clipItems
            };

        }
        function loadFormFromServerData() {
            Ext.Ajax.request({
                url:"content!viewCourse.action?keyId=" + keyId,
                method:'POST',
                callback : function(opt, success, response) {
                    if (success) {
                        var serverResult = Ext.util.JSON.decode(response.responseText);
                        if (serverResult.success) {
                            loadFormData(serverResult.data);
                        } else {
                            Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + serverResult.error)
                        }
                    }
                }
            });
        }
        function loadFormData(serverData) {
            initDisplay(serverData);
        //    contentViewForm.getForm().setValues(serverData);
        }
    </script>
</head>
<body>
   <div align="center" id="displayDiv"></div>
   <div align="center" id="clipsDiv"></div>
</body>
</html>