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
        needPermissions(actionHeader,"save","phoneRangeSave");
        needPermissions(actionHeader,"list","phoneRangeList");
        needPermissions(actionHeader,"view","phoneRangeView");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="phoneRange"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
    <script type="text/javascript">
        actionHeader = "phoneRange";
        nextUrl = "phoneRangeList.jsp";

        var areaId =<%=request.getParameter("obj.areaId")%>;
        var keyId =<%=request.getParameter("keyId")%>;

        //点击保存以后返回的页面
        function saveCallBackFunction() {
            nextUrl = "phoneRangeList.jsp?obj.areaId=" + areaId;
            gotoNextUrl();
        }

        function initDisplay() {
            checkAllFunctions(); //检查权限
            var ipRangeViewForm = new FortuneViewForm({
                title:'<fts:text name="phoneRange"/>基本信息',
                url:'phoneRange!save.action',
                saveUrl:'phoneRange!save.action',
                viewUrl:'phoneRange!view.action',
                bodyStyle:'padding: 5px  5px 0',
                items:[
                    {
                        name:'obj.id',
                        inputType:'hidden',
                        value:'-1'
                    }
                    ,
                    {
                        fieldLabel:'<fts:text name="名称"/>',
                        name:'obj.name',
                        readOnly:viewReadOnly,
                        allowBlank:false
                    }
                    ,
                    {
                        fieldLabel:'<fts:text name="起始号段"/>',
                        name:'obj.phoneFrom',
                        readOnly:viewReadOnly,
                        allowBlank:false,
                        regex:/^[0-9]{11}/,
                        regexText:'只能输入手机号'
//                        regex: /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/,
//                        regexText: '只能输入IP地址'
                    }
                    ,
                    {
                        fieldLabel:'<fts:text name="结束号段"/>',
                        name:'obj.phoneTo',
                        readOnly:viewReadOnly,
                        allowBlank:false,
                        regex:/^[0-9]{11}/,
                        regexText:'只能输入手机号'
//                        regex: /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/,
//                        regexText: '只能输入IP地址'
                    }
                    ,
                    {
                        fieldLabel:'<fts:text name="描述"/>',
                        xtype:'textarea',
                        name:'obj.desp',
                        readOnly:viewReadOnly
                    }
                    ,
                    {
                        fieldLabel:'区域',
                        name:'obj.areaId',
                        xtype: 'hidden',
                        value: areaId
                    }
                ]
            });
            if (keyId != "-1") {
                loadFormAjax();
            } else {

            }
            ipRangeViewForm.render(displayDiv);
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