<%@ page import="java.io.UnsupportedEncodingException" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page
        contentType="text/html;charset=UTF-8" language="java" %>
<%@include
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
    String newFiles = request.getParameter("newFiles");
    String contentId=request.getParameter("contentId");
    String contentName="";
    try {
        contentName = java.net.URLDecoder.decode(request.getParameter("contentName"), "UTF-8");//处理中文文件名的问题
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title>上传海报信息</title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
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
    .x-form-file {
            height: 22px;
            opacity: 0;
            position: absolute;
            right: 0;
            z-index: 2;
           margin-left: -30px;
        }
    </style>
    <script type="text/javascript" src="../js/FtpFileSelector.js"></script>
    <script type="text/javascript" src="../js/ExtFortuneSelectField.js"></script>
    <script type="text/javascript">
        var actionUrl = "/content/content";
        function renderStringValue(val,meta,rec){
            return val;
        }
        function setPosterImageSrc(newSrc,imageSizeInfoStr){
            var imageObj = document.getElementById("posterPicture");
            if(imageObj!=null){
               imageObj.src=newSrc;
                if(imageSizeInfoStr!=null){
                    var WxH = imageSizeInfoStr.split("x");
                    if(WxH.length>=2){
                        var w = parseInt(WxH[0]);
                        var h = parseInt(WxH[1]);
                        if(typeof(w)!="undefined"&&typeof(h)!="undefined"){
                        //固定宽度，调整高度
                            var imgWidth = imageObj.width;
                            var imgHeight = imgWidth*h/w;
                            if(imgHeight>0){
                                imageObj.height = imgHeight;
                            }
                        }
                    }
                }
            }
        }
        Ext.onReady(function () {
            Ext.QuickTips.init();
            var bd = Ext.getBody();
            storeConfig.fields =['id','contentId','stringValue','propertyId','name','extraData','desp','length'];
            storeConfig.proxy = new Ext.data.HttpProxy({method:'POST', url:'/content/contentProperty!list.action?dataType=11&obj.contentId='+<%=contentId%> });
            storeConfig.baseParams = {limit:defaultPageSize};
            storeConfig.autoLoad=true;
            searchStore = new FortuneSearchStore(storeConfig);

            var colModel = new Ext.grid.ColumnModel([
                {id:'id', header:"影片名", width:160, sortable:true, locked:false, dataIndex:'name'},
                {header:"图片信息", width:220, sortable:true, dataIndex:'stringValue',renderer:renderStringValue},
                {width:200, sortable:true, dataIndex:'id',hidden:true},
                {width:200, sortable:true, dataIndex:'propertyId',hidden:true}
            ]);
            var gridForm = new Ext.FormPanel({
                id:'company-form',
                frame:true,
                fileUpload:true,
                labelAlign:'left',
                title:'《<%=contentName%>》海报上传更新',
                bodyStyle:'padding:5px',
                width:750,
                layout:'column', // Specifies that the items will now be arranged in columns
                items:[
                    {
                        columnWidth:0.55,
                        layout:'fit',
                        items:{
                            xtype:'grid',
                            cm:colModel,
                            store:searchStore,
                            sm:new Ext.grid.RowSelectionModel({
                                singleSelect:true,
                                listeners:{
                                    rowselect:function (sm, row, rec) {
                                        Ext.getCmp("company-form").getForm().loadRecord(rec);
                                        setPosterImageSrc(rec.data.stringValue,rec.data.extraData);//'http://61.55.144.81/'
                                        setCmpValue("propertyId",rec.data.propertyId);
                                        setCmpValue("contentId",rec.data.contentId);
                                        setCmpValue("id",rec.data.id);
                                        setCmpValue("uploadFile","");
                                    }
                                }
                            }),
                            //autoExpandColumn:'company',
                            height:500,
                            title:'海报信息',
                            border:true,
                            listeners:{
                                viewready:function (g) {
                                    g.getSelectionModel().selectRow(0);
                                } // Allow rows to be rendered.
                            }
                        }
                    },
                    {
                        columnWidth:0.45,
                        xtype:'fieldset',
                        defaults:{border:false}, // Default config options for child items
                        defaultType:'textfield',
                        autoHeight:true,
                        height:600,
                        bodyStyle:Ext.isIE ? 'padding:0 0 5px 5px;' : 'padding:5px 5px;',
                        border:false,
                        style:{
                            "margin-left":"10px",
                            "margin-top":"20px",
                            "margin-right":Ext.isIE6 ? (Ext.isStrict ? "0px" : "0px") : "0"
                        },
                        items:[
                            {
                                fieldLabel:'图片类型',
                                cls:'nameStyle',
                                width:230,
                                id:'name',
                                name:'name'
                            },
                            {
                                fieldLabel:'海报路径',
                                cls:'valueStyle',
                                width:230,
                                id:'stringValue',
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
                                buttonText:'选择图片'
                            },
                            {xtype:'textfield',inputType:'hidden',hidden:true,name:'obj.id',id:'id'},
                            {xtype:'textfield',inputType:'hidden',hidden:true,name:'obj.propertyId',id:'propertyId'},
                            {xtype:'textfield',inputType:'hidden',hidden:true,name:'obj.contentId',id:'contentId'},
                            {
                                xtype:'button',
                                text:'保存海报',
                                id:'uploadFileBtn',
                                cls:'uploadFile',
                                width:100,
                                listeners:{
                                    "click":function () {
                                        setCmpValue("localFileName", getCmpValue("uploadFile"));
                                        if(getCmpValue("uploadFile")==""||getCmpValue("name")==""||getCmpValue("uploadFile")=="请选择") {
                                                alert("请选择上传文件，或者选择上传类型！");
                                        }else{
                                        gridForm.getForm().submit({
                                            url:"/content/contentProperty!uploadPoster.action?saveToDatabase=1&amp;localFileName=" + getCmpValue("localFileName"),
                                            method:'post',
                                            waitMsg:'正在处理数据，请稍后……',
                                            success:function (form, response) {
                                                var serverData = Ext.util.JSON.decode(response.response.responseText);
                                                var newSrc = serverData.data["obj.stringValue"];
                                                setCmpValue("stringValue", newSrc);
                                                setPosterImageSrc(newSrc);
                                                searchStore.load();
                                            }
                                        });
                                      }
                                    }
                                }
                            },new Ext.Panel({
                                border:1,
                                id:"image_info",
                                cls:'imagePosition',
                                html:'<img id="posterPicture" width="310" height="330"/>'

                            })
                        ]
                    }
                ],
                renderTo:bd
            });
        });
    </script>
</head>
<body class="bodyCls">
<table align="center" width="760">
    <tr>
        <td>
            <div id="display"></div>
        </td>
    </tr>
</table>
</body>
</html>