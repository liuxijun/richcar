<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page
        contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader,"view","productView,productManage");
//        needPermissions(actionHeader,"list","productSearch,productManage");
        needPermissions(actionHeader,"save","productSave,productManage");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="product"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
    <script type="text/javascript">
        actionHeader = "product";
        nextUrl = "productList.jsp";
        function initDisplay() {
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            checkAllFunctions(); //检查权限
            var productViewForm = new FortuneViewForm({
                title:'<fts:text name="product"/>基本信息',
                url:'product!save.action',
                saveUrl:'product!save.action',
                viewUrl:'product!view.action',
                bodyStyle:'padding:5px 5px 0',
                items:[
                    //{name:'keyId',value:keyId,inputType:'hidden'},
                    {
                        name:'obj.id',
                        inputType:'hidden',
                        value:'-1'
                    }                     ,
                    {
                        fieldLabel:'<fts:text name="product.name"/>',
                        name:'obj.name',
                        readOnly:viewReadOnly,
                        allowBlank:false
                    }                     ,
                    {
                        fieldLabel:'<fts:text name="product.payProductNo"/>',
                        name:'obj.payProductNo',
                        readOnly:viewReadOnly
                    }                     ,
                    {
                        fieldLabel:'<fts:text name="product.price"/>',
                        name:'obj.price',
                        readOnly:viewReadOnly
                    }                     ,
                    // {fieldLabel:'<fts:text name="product.type"/>',name:'obj.type',readOnly:viewReadOnly}                     ,
                    new FortuneCombo({
                        fieldLabel:'<fts:text name="product.type"/>',
                        hiddenName:'obj.type',
                        store:getDictStore("productType"),
                        readOnly:viewReadOnly
                    }),
                    {
                        fieldLabel:'<fts:text name="product.validLength"/>',
                        name:'obj.validLength',
                        readOnly:viewReadOnly
                    }                     ,

                    //   {fieldLabel:'<fts:text name="product.lengthUnit"/>',name:'obj.lengthUnit',readOnly:viewReadOnly}                     ,
                    new FortuneCombo({
                        fieldLabel:'<fts:text name="product.lengthUnit"/>',
                        hiddenName:'obj.lengthUnit',
                        store:getDictStore("productLengthUnit"),
                        readOnly:viewReadOnly
                    }),
                    {
                        /* fieldLabel:'<fts:text name="product.autoPay"/>',
                     name:'obj.autoPay',
                     readOnly:viewReadOnly*/
                        xtype:'radiogroup',
                        fieldLabel:'<fts:text name="product.autoPay"/>',
                        name:'obj.autoPay',
                         width:180,
                        items:[
                            {
                                boxLabel:'是',
                                name:'obj.autoPay',
                                inputValue:"1",
                                width:50,
                                checked:true
                            },
                            {
                                boxLabel:'否',
                                name:'obj.autoPay',
                                inputValue:"2"
                            }
                        ]
                    }                     ,
                    {
                        /*  fieldLabel:'<fts:text name="product.searchExtra"/>',
                     name:'obj.searchExtra',
                     readOnly:viewReadOnly*/
                        xtype:'radiogroup',
                        fieldLabel:'<fts:text name="product.searchExtra"/>',
                        name:'obj.searchExtra',
                         width:180,
                        items:[
                            {
                                boxLabel:'是',
                                name:'obj.searchExtra',
                                inputValue:"1",
                                checked: true
                            },
                            {
                                boxLabel:'否',
                                name:'obj.searchExtra',
                                inputValue:"2"
                            }
                        ]
                    }                     ,
                    //   {fieldLabel:'<fts:text name="product.status"/>',name:'obj.status',readOnly:viewReadOnly}
                   {
                            name:'obj.status',
                            xtype:'radiogroup',
                            fieldLabel:'当前状态',
                            width:260,
                       columns:2,
                            items:[
                                {boxLabel:'有效，可订可退',name:'obj.status',inputValue:"1",checked: true },
                                {boxLabel:'失效，不可订退',name:'obj.status',inputValue:"0"},
                                {boxLabel:'可订，不可退',name:'obj.status',inputValue:"11"},
                                {boxLabel:'可退，不可定',name:'obj.status',inputValue:"10"},
                                {boxLabel:'已定显示，未定隐藏',name:'obj.status',inputValue:"12"},
                                {boxLabel:'隐藏，只查不看',name:'obj.status',inputValue:"13"}
                            ]
                        },
                    {
                        name:'obj.costType',
                        xtype:'radiogroup',
                        fieldLabel:'扣费类型',
                        width:180,
                        items:[
                            {boxLabel:'人民币',name:'obj.costType',inputValue:"1",checked: true },
                            {boxLabel:'M值',name:'obj.costType',inputValue:"2"}
                        ]
                    }  ,
                    {
                        name:'obj.mobileProduct',
                        xtype:'radiogroup',
                        fieldLabel:'手机产品',
                        width:180,
                        items:[
                            {boxLabel:'是',name:'obj.mobileProduct',inputValue:"1",checked: true },
                            {boxLabel:'否',name:'obj.mobileProduct',inputValue:"2"}
                        ]
                    },{
                        fieldLabel:'产品描述',
                        name:'obj.description',
                        xtype:'textarea',
                        //width:260,
                        height:150
                    }
                ]
            });
           <%if (request.getParameter("keyId")!=null){%>
            loadFormAjax();
            <%      }            %>
            productViewForm.render(displayDiv);
        }
        Ext.onReady(function() {
            Ext.QuickTips.init();
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            queueFunctions([
                {
                    func:initDictStores,
                    done:false,
                    flag:'initDictStores'
                }/*,{
                 func:initAreaStore,
                 done:false,
                 flag:'initAreaStore'
                 }*/
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