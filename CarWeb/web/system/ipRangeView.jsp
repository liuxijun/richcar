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
        needPermissions(actionHeader,"save","ipRangeSave");
        needPermissions(actionHeader,"list","ipRangeList");
        needPermissions(actionHeader,"view","ipRangeView");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="ipRange"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
    <script type="text/javascript">
        actionHeader = "ipRange";
        nextUrl = "ipRangeList.jsp";

        var areaId =<%=request.getParameter("obj.areaId")%>;
        var keyId =<%=request.getParameter("keyId")%>;

        function saveCallBackFunction() {
            nextUrl = "ipRangeList.jsp?obj.areaId=" + areaId;
            gotoNextUrl();
        }
   
        function initDisplay() {
            checkAllFunctions(); //检查权限
            var ipRangeViewForm = new FortuneViewForm({
                title:'<fts:text name="ipRange"/>基本信息',
                url:'ipRange!save.action',
                saveUrl:'ipRange!save.action',
                viewUrl:'ipRange!view.action',
                bodyStyle:'padding: 5px  5px 0',
                items:[
                    {
                        name:'obj.id',
                        inputType:'hidden',
                        value:'-1'
                    }                     ,
                    {
                        fieldLabel:'<fts:text name="ipRange.name"/>',
                        name:'obj.name',
                        readOnly:viewReadOnly,
                        allowBlank:false
                    }                     ,
                    {
                        fieldLabel:'<fts:text name="ipRange.ipFrom"/>',
                        name:'obj.ipFromStr',
                        readOnly:viewReadOnly,
                        allowBlank:false,
                        regex: /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/,
                        regexText: '只能输入IP地址'
                    }                     ,
                    {
                        fieldLabel:'<fts:text name="ipRange.ipTo"/>',
                        name:'obj.ipToStr',
                        readOnly:viewReadOnly,
                        allowBlank:false,
                        regex: /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/,
                        regexText: '只能输入IP地址'
                    }         ,
                    {
                        fieldLabel:'<fts:text name="ipRange.desp"/>',
                        xtype:'textarea',
                        name:'obj.desp',
                        readOnly:viewReadOnly
                    }  ,
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