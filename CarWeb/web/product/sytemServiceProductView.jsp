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
        needPermissions(actionHeader,"view", "serviceProductView,systemServiceProductManage");
        needPermissions(actionHeader,"save", "systemServiceProductManage,serviceProductSave");
   //     needPermissions(actionHeader,"list", "systemServiceProductManage,serviceProductSearch");
   //     needPermissions(actionHeader,"listProductType","productListProductType");
    }

%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
<meta http-equiv=content-type content="text/html; charset=UTF-8">
<title><fts:text name="serviceProduct"/>管理</title>
<%@include file="../inc/jsCssLib.jsp" %>
<%@include file="../inc/extBase.jsp" %>
<script type="text/javascript">
<%
    long cspId = 0;
    if (admin != null && admin.getCspId()!=null){
        cspId = admin.getCspId();
    }
%>
var cspId =<%=cspId%>;
actionHeader = "serviceProduct";
nextUrl = "systemServiceProductList.jsp";
var win = null;

var keyId =<%= request.getParameter("keyId") %>;

var count;

Ext.override(Ext.form.RadioGroup, {
    getItems:function() {
        return this.items;
    },
    setItems:function(data) {
        this.items = data;
    }
});

var productNameSelected;
var myRadioGroup;


var serviceProductStore = new Ext.data.JsonStore({
    method:'POST',
    url:"serviceProduct!view.action",
    root:'obj'
});

var discountCom;
var lengthUnitCom;
var productCom;
var typeCom;
var type;


var productStore = new Ext.data.JsonStore({
    autoLoad:false,
    method:'POST',
    url:"/product/product!listProductType.action?limit=10000",
    root:'objs',
    totalProperty:'totalCount',
    fields:['id','name'],
    readOnly:viewReadOnly
});

formOptions.afterLoad = function() {  //赋上初始值
    var dis = discountCom.getValue();

    if (dis == 0 || dis == null) {
        discountCom.setValue();
    }
    var lu = lengthUnitCom.getValue();
    if (lu == 0) {
        lengthUnitCom.setValue();
    }
    type = Ext.getCmp("type").getValue();
    var productId = Ext.getCmp("productId").getValue();
    productStore.load({params:{'obj.type':type},
        callback: function(r, options, success) {
            if (success == true) {
                for (var i = 0; i < productStore.getCount(); i++) {
                    if (productId == productStore.getAt(i).id) {
                        productCom.setValue(productStore.getAt(i).data.id);
                        break;
                    } else {

                    }
                }
            }

        }
    });


}


function initDisplay() {
    checkAllFunctions(); //检查权限

    var sytemServiceProductViewForm = new Ext.FormPanel({
        title:'<fts:text name="serviceProduct"/>基本信息',
        url:'serviceProduct!save.action',
        saveUrl:'serviceProduct!save.action',
        viewUrl:'serviceProduct!view.action',
        //  bodyStyle:'padding:5px 5px 0',
        frame:true,
        width: 630,
        layout:'column',
        id:'BaseViewForm338547183092',
        labelWidth: 100,
        labelAlign:'right',
        buttons:defaultViewFormButtons,
        buttonAlign:'center',
        items:[
            {
                columnWidth:.5,
                layout: 'form',
                defaultLabelAlign:'right',
                defaults: {width: 190},
                defaultType: 'textfield',
                items:[
                    {
                        name:'obj.id',
                        inputType:'hidden',
                        value:'-1'
                    }                     ,
                    {
                        fieldLabel:'<fts:text name="serviceProduct.name"/>',
                        name:'obj.name',
                        readOnly:viewReadOnly,
                        allowBlank:false
                    }                     ,
                    typeCom = new FortuneCombo({
                        fieldLabel:'<fts:text name="serviceProduct.type"/>',
                        hiddenName:'obj.type',
                        id:'type',
                        store:getDictStore("productType"),
                        readOnly:viewReadOnly ,
                        listeners:{
                            select: function(combo, record, index) {
                                productCom.clearValue();   //清空原来的数据
                                var type = record.get("value");
                                productStore.load({params:{'obj.type':type}});
                            }
                        }
                    })  ,
                    productCom = new FortuneCombo({
                        id:"productId",
                        fieldLabel:'<fts:text name="serviceProduct.productId"/>',
                        hiddenName:'obj.productId',
                        store:productStore,
//                        allowBlank:false,
                        valueField:'id',
                        displayField:'name'
                    }),
                    {
                        fieldLabel:'<fts:text name="serviceProduct.validLength"/>',
                        name:'obj.validLength',
                        vtype:'number',
                        allowBlank:true,
                        readOnly:viewReadOnly
                    },
                    lengthUnitCom = new FortuneCombo({
                        fieldLabel:'<fts:text name="serviceProduct.lengthUnit"/>',
                        hiddenName:'obj.lengthUnit',
                        store:getDictStore("productLengthUnit"),
//                        allowBlank : false,//不允许为空
                        readOnly:viewReadOnly
                    }),
                    new FortuneCombo({
                        fieldLabel:'<fts:text name="serviceProduct.status"/>',
                        hiddenName:'obj.status',
                        store:getDictStore("productStatus"),
                        readOnly:viewReadOnly
                    })
                    ,
                    {
                        xtype:'radiogroup',
                        fieldLabel:'<fts:text name="product.autoPay"/>',
                        name:'obj.autoPay',
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
                    },
                    {
                        xtype:'radiogroup',
                        fieldLabel:'<fts:text name="serviceProduct.searchExtra"/>',
                        name:'obj.searchExtra',
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
                    },
                    {
                        xtype:'radiogroup',
                        fieldLabel:'<fts:text name="serviceProduct.isDisplayGift"/>',
                        name:'obj.isDisplayGift',
                        items:[
                            {
                                boxLabel:'是',
                                name:'obj.isDisplayGift',
                                inputValue:"1",
                                checked: true
                            },
                            {
                                boxLabel:'否',
                                name:'obj.isDisplayGift',
                                inputValue:"2"
                            }
                        ]
                    }                   ,
                    {
                        xtype:'radiogroup',
                        fieldLabel:'<fts:text name="serviceProduct.isDisplayMsg"/>',
                        name:'obj.isDisplayMsg',
                        items:[
                            {
                                boxLabel:'是',
                                name:'obj.isDisplayMsg',
                                inputValue:"1",
                                checked: true
                            },
                            {
                                boxLabel:'否',
                                name:'obj.isDisplayMsg',
                                inputValue:"2"
                            }
                        ]
                    }
                ]
            },
            {
                columnWidth:.5,
                layout: 'form',
                defaultLabelAlign:'right',
                defaults: {width: 190},
                defaultType: 'textfield',
                items:[
                    discountCom = new FortuneCombo({
                        fieldLabel:'<fts:text name="serviceProduct.discount"/>',
                        hiddenName:'obj.discount',
                        store:getDictStore("productDiscount"),
                        readOnly:viewReadOnly
                    }),
                    {
                        fieldLabel:'<fts:text name="serviceProduct.discountStartTime"/>',
                        name:'obj.discountStartTime',
                        xtype:'datefield',
                        format:'Y-m-d H:i:s',
                        readOnly:viewReadOnly
                    }                     ,
                    {
                        fieldLabel:'<fts:text name="serviceProduct.discountEndTime"/>',
                        name:'obj.discountEndTime',
                        xtype:'datefield',
                        format:'Y-m-d H:i:s',
                        readOnly:viewReadOnly
                    }                    ,
                    {
                        xtype:'radiogroup',
                        fieldLabel:'<fts:text name="serviceProduct.isFree"/>',
                        name:'obj.isFree',
                        items:[
                            {
                                boxLabel:'是',
                                name:'obj.isFree',
                                inputValue:"1",
                                checked: true
                            },
                            {
                                boxLabel:'否',
                                name:'obj.isFree',
                                inputValue:"2"
                            }
                        ]
                    }                     ,
                    {
                        fieldLabel:'<fts:text name="serviceProduct.freeStartTime"/>',
                        name:'obj.freeStartTime',
                        xtype:'datefield',
                        format:'Y-m-d H:i:s',
                        readOnly:viewReadOnly
                    }                     ,
                    {
                        fieldLabel:'<fts:text name="serviceProduct.freeEndTime"/>',
                        name:'obj.freeEndTime',
                        xtype:'datefield',
                        format:'Y-m-d H:i:s',
                        readOnly:viewReadOnly
                    }      ,
                    {
                        fieldLabel:'<fts:text name="serviceProduct.msg"/>',
                        name:'obj.msg',
                        readOnly:viewReadOnly
                    }                     ,
                    {
                        fieldLabel:'<fts:text name="serviceProduct.cspId"/>',
                        name:'obj.cspId',
                        //   hidden:false,
                        xtype:'hidden',
                        value:cspId,
                        readOnly:viewReadOnly
                    },
                    {
                        fieldLabel:'<fts:text name="serviceProduct.msgStartTime"/>',
                        name:'obj.msgStartTime',
                        xtype:'datefield',
                        format:'Y-m-d H:i:s',
                        readOnly:viewReadOnly
                    }                     ,
                    {
                        fieldLabel:'<fts:text name="serviceProduct.msgEndTime"/>',
                        name:'obj.msgEndTime',
                        xtype:'datefield',
                        format:'Y-m-d H:i:s',
                        readOnly:viewReadOnly
                    }
                ]
            }
        ]
    });


<%
if (request.getParameter("keyId")!=null){%>
    loadFormAjax();

//    sytemServiceProductViewForm.load();
    //  alert(Ext.get('obj.productId'));
    //    Ext.getCmp("productBtn").setText( Ext.getCmp("obj.productId").getValue());
<%      }            %>

    sytemServiceProductViewForm.render(displayDiv);

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

        }
);


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

