<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"view","stbManage,stbView");
        needPermissions(actionHeader,"save","stbManage,stbSave");
    }
%><%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="device"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        actionHeader = "stb";
        nextUrl = "stbList.jsp";
        function initDisplay() {
            checkAllFunctions(); //检查权限


            var deviceViewForm = new FortuneViewForm({
                title:'机顶盒基本信息',
                url:'stb!save.action',
                saveUrl:'stb!save.action',
                viewUrl:'stb!view.action',
                 bodyStyle:'padding:5px 5px 0',
                items:[
                     {name:'obj.id',inputType:'hidden',value:'-1'}                     ,
                     {fieldLabel:'关联用户',name:'obj.userId',readOnly:modifyOldData},
                    {
                        fieldLabel:'系列号',
                        name:'obj.serialNo',
                        id:'obj.serialNo',
                        readOnly:viewReadOnly,
                        allowBlank:false      ,
                        vtype:modifyOldData ? '' : 'login',
                        invalidText: '已经存在的系列号'     ,
                        regex:/^[a-zA-Z]{1}([a-zA-Z0-9]|[._]){7,15}$/,
                        regexText:"只能输入8-16个以字母开头、可带数字、“_”、“.”的字串"

                    },
                    {
                        xtype:'radiogroup',
                        fieldLabel:'机顶盒类型',
                        name:'obj.stbType',
                        items:[
                            {boxLabel:'标清',name:'obj.stbType',inputValue:"1",checked: true },
                            {boxLabel:'高清',name:'obj.stbType',inputValue:"2"}
                        ]
                    },
                    {fieldLabel:'安装位置',name:'obj.location',readOnly:viewReadOnly}                     ,
                     new Ext.form.ComboBox({
                         fieldLabel:'状态',
                         hiddenName:'obj.status',
                         mode:'local',
                         value:1,
                         forceSelection:true,
                         triggerAction:"all",
                         editable:false,
                         store:getDictStore('stbStatus'),
                         valueField:'value',displayField:'name',
                         emptyText:'请选择...'
                     })
                ]
            });

       <%if (request.getParameter("keyId")!=null){%>
                      loadFormAjax();
            <%      }            %>
            deviceViewForm.render(displayDiv);
        }
        Ext.onReady(function() {
            Ext.QuickTips.init();
            queueFunctions([
                {
                    func:initDictStores,
                    done:false,
                    flag:'initDictStores'
                }
            ],
                   initDisplay);
        });
    </script>
</head>
<body>
<table align="center">
    <tr>
        <td>
            <div id="displayDiv"></div>
        </td>
    </tr>
</table>
</body>
</html>