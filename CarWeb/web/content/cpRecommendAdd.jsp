<%--
  Created by IntelliJ IDEA.
  User: wang
  Date: 13-11-19
  Time: 上午10:22
  展现当前用户上传影片的推荐信息
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp" %>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
//         needPermissions(actionHeader,"list","synTaskManage");
        needPermissions(actionHeader,"upload","synFileManage,synFileUpload");
        needPermissions(actionHeader,"isExistFile","synFileManage,synFileIsExistFile");
        needPermissions(actionHeader,"listFiles","templateListFiles");
    }
%>
<%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
<meta http-equiv=content-type content="text/html; charset=UTF-8">
<title></title>
<%@include file="../inc/jsCssLib.jsp" %>
<%@include file="../inc/extBase.jsp" %>
<script language="javascript">

Ext.onReady(function () {
    <%
        long spId = 0;
        if (admin != null && admin.getCspId()!=null){
            spId = admin.getCspId();
        }
//        String recommendId = request.getParameter("recommendId");
    %>
    var spId =<%=spId%>;

    <%--var recommendId = <%=recommendId%>;暂时测用id  注册掉--%>
    var recommendId = 123;

    var gridForm = new Ext.FormPanel({
        id:'company-form',
        frame:true,
        fileUpload:true,
        labelAlign:'left',
        title:'添加专题',
        bodyStyle:'padding:5px',
        style: {'margin-left':'15px','margin-top': '10px','margin-right':'0px','margin-bottom':'12px'},
        width:400,
        height:200,
        layout:'column', // Specifies that the items will now be arranged in columns
        items:[
            {
                xtype:'fieldset',
                defaults:{border:false}, // Default config options for child items
                defaultType:'textfield',
                autoHeight:true,
                bodyStyle:Ext.isIE ? 'padding:0 0 5px 5px;' : 'padding:5px 5px;',
                border:false,
                style:{
                    "margin-left":"10px",
                    "margin-top":"20px",
                    "margin-right":Ext.isIE6 ? (Ext.isStrict ? "0px" : "0px") : "0"
                },
                items:[
                    {
                        fieldLabel:'资源名称',
                        cls:'nameStyle',
                        width:230,
                        id:'name',
                        name:'name'
                    },
                    {
                        xtype:'textfield',
                        inputType:'hidden',
                        name:'uploadZipFileNames',
                        id:'uploadZipFileNames',
                        hidden:true
                    },
                    {
                        fieldLabel:'主要看点',
                        cls:'nameStyle',
                        width:230,
                        id:'mainFocus',
                        name:'mainFocus'
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
                    {
                        xtype:'textfield',
                        inputType:'hidden',
                        name:'uploadZipFileNames',
                        id:'uploadZipFileNames',
                        hidden:true
                    },
                    {
                        xtype:'fileuploadfield',
                        width:200,
                        cls:'fileStyle',
                        id:'uploadZipFile',
                        emptyText:'请选择压缩包',
                        fieldLabel:'上传压缩包',
                        name:'uploadZipFile',
                        buttonText:'选择文件'
                    },
                    {xtype:'textfield',inputType:'hidden',hidden:true,name:'obj.id',id:'id'},
                    {xtype:'textfield',inputType:'hidden',hidden:true,name:'obj.propertyId',id:'propertyId'},
                    {xtype:'textfield',inputType:'hidden',hidden:true,name:'obj.contentId',id:'contentId'},
                    {
                        xtype:'button',
                        text:'添加',
                        id:'uploadFile',
                        cls:'uploadFile',
                        width:100,
                        listeners:{
                            "click":function () {
                                setCmpValue("uploadZipFileNames", getCmpValue("uploadZipFile"));
                                setCmpValue("localFileName", getCmpValue("uploadFile"));
                                if(getCmpValue("uploadFile")==""||getCmpValue("name")==""||getCmpValue("uploadFile")=="请选择") {
                                    alert("请选择上传文件，或者选择上传类型！");
                                }else{
                                    gridForm.getForm().submit({
                                        url:"/content/contentProperty!saveZip.action?saveToDatabase=1&amp;localFileName=" + getCmpValue("localFileName")+"&name="+ getCmpValue("name")+"&uploadZipFileNames="+getCmpValue("uploadZipFileNames")+"&url="+getCmpValue("stringValue")+"&recommendId="+recommendId+"&mainFocus="+getCmpValue("mainFocus"),
                                        method:'post',
                                        waitMsg:'正在处理数据，请稍后……',
                                        success:function (form, response) {
                                            var serverData = Ext.util.JSON.decode(response.response.responseText);
                                            if (serverData.success) {
                                                Ext.MessageBox.alert("提示", "操作成功",function showResult(){
                                                   parent.viewWinZT.close();
                                                });
                                            } else {
                                                Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + serverData.error);
                                            }
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
        ]
    });


    gridForm.render('display');
    //listGrid.render(Ext.getBody());

    //这样定义函数, 就可以在extjs外调用了
    this.viewObj = function (action, id, moduleId) {
        document.location = '/content/contentViewOnly.jsp?action=' + action + '&id=' + id + '&moduleId=' + moduleId;
    }
})
</script>
</head>
<body>
<table align="center" width="760">
    <tr>
        <td>
            <div id="display"></div>
        </td>
    </tr>
</table>
</body>
</html>