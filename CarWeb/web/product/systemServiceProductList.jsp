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
    String actionHeader = "serviceProduct";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader, "deleteSelected", "systemServiceProductManage,serviceProductDeleteSelected");
      //  needPermissions(actionHeader, "save", "systemServiceProductManage,serviceProductSave");
        needPermissions(actionHeader, "add", "systemServiceProductManage");
        needPermissions(actionHeader, "list", "systemServiceProductManage,serviceProductSearch");
        needPermissions(actionHeader, "view", "serviceProductView,systemServiceProductManage");
        needPermissions(actionHeader, "serviceProductGift", "systemServiceProductManage");
        needPermissions(actionHeader,"serviceProduceView","systemServiceProductManage");
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
nextUrl = "systemServiceProductList.jsp";
actionHeader = "serviceProduct";
checkAllFunctions();  // 检查用户权限

defaultPageSize = 17;
function gotoAddFunc() {
    window.location.href = "sytemServiceProductView.jsp";
}
var defaultBottomButtons = [
    {
        text:'添加记录',
        handler:gotoAddFunc,
        action:'add'
    },
    {
        text:'删除所选',
        handler:deleteSelectedRows,
        action:'deleteSelected'
    }
];

function displayDiscount(val) {
    return getDictStoreText("productDiscount", val)
}

function displayStatus(val) {
    return getDictStoreText("productStatus", val);
}

function displayType(val) {
    return getDictStoreText("productType", val);
}
function displayLengthUnit(val) {
    return getDictStoreText("productLengthUnit", val);
}
function initDisplay() {
    storeConfig.fields = ['id', 'name', 'type', 'cspId', 'productName', 'validLength', 'lengthUnit', 'status', 'autoPay', 'searchExtra', 'discount', 'discountStartTime', 'discountEndTime', 'isFree', 'freeStartTime', 'freeEndTime', 'isDisplayMsg', 'msg', 'msgStartTime', 'msgEndTime', 'isDisplayGift'];
    storeConfig.proxy = new Ext.data.HttpProxy({method:'POST', url:getActionUrl('list')});
    storeConfig.baseParams = {limit:defaultPageSize};
    searchStore = new FortuneSearchStore(storeConfig);
    keyFieldId = "id";
    var columns = new Ext.grid.ColumnModel([
        sm,
        {
            id:'Id',
            hidden:true,
            align:'center',
            header:"序号",
            width:60,
            sortable:true,
            dataIndex:'id'
        }                ,
        {
            header:"<fts:text name="serviceProduct.name"/>",
            hidden:false,
            align:'center',
            width:190,
            sortable:true,
            dataIndex:'name'
        }                ,
        {
            header:"<fts:text name="serviceProduct.type"/>",
            hidden:false,
            align:'center',
            width:50,
            sortable:true,
            dataIndex:'type', renderer:displayType
        }                ,
        {
            header:"<fts:text name="serviceProduct.cspId"/>",
            hidden:false,
            align:'center',
            width:70,
            sortable:true,
            dataIndex:'cspId'
        }                ,
        {
            header:"<fts:text name="serviceProduct.productId"/>",
            hidden:false,
            align:'center',
            width:90,
            sortable:true,
            dataIndex:'productName'
        }                ,
        {
            header:"<fts:text name="serviceProduct.validLength"/>",
            hidden:true,
            align:'center',
            width:50,
            sortable:true,
            dataIndex:'validLength'
        }                ,
        {
            header:"<fts:text name="serviceProduct.lengthUnit"/>",
            hidden:true,
            align:'center',
            width:60,
            sortable:true,
            dataIndex:'lengthUnit', renderer:displayLengthUnit
        }                ,
        {
            header:"<fts:text name="serviceProduct.status"/>",
            hidden:false,
            align:'center',
            width:63,
            sortable:true,
            dataIndex:'status', renderer:displayStatus
        }                ,
        {
            header:"<fts:text name="serviceProduct.autoPay"/>",
            hidden:true,
            align:'center',
            width:60,
            sortable:true,
            dataIndex:'autoPay', renderer:function (val, p, row) {
            switch (val) {
                case 1:
                    return '是';
                case 2:
                    return '否';
            }
        }}
        ,
        {
            header:"<fts:text name="serviceProduct.searchExtra"/>",
            hidden:true,
            align:'center',
            width:60,
            sortable:true,
            dataIndex:'searchExtra', renderer:function (val, p, row) {
            switch (val) {
                case 1:
                    return '是';
                case 2:
                    return '否';
            }
        }
        }                ,
        {
            header:"<fts:text name="serviceProduct.discount"/>",
            hidden:true,
            align:'center',
            width:60,
            sortable:true,
            dataIndex:'discount', renderer:displayDiscount
        }                ,
        {
            header:"<fts:text name="serviceProduct.discountStartTime"/>",
            hidden:true,
            align:'center',
            width:60,
            sortable:true,
            dataIndex:'discountStartTime'
        }                ,
        {
            header:"<fts:text name="serviceProduct.discountEndTime"/>",
            hidden:true,
            align:'center',
            width:60,
            sortable:true,
            dataIndex:'discountEndTime'
        }                ,
        {
            header:"<fts:text name="serviceProduct.isFree"/>",
            hidden:false,
            align:'center',
            width:70,
            sortable:true,
            dataIndex:'isFree', renderer:function (val, p, row) {
            switch (val) {
                case 1:
                    return '是';
                case 2:
                    return '否';
            }
        }
        }                ,
        {
            header:"<fts:text name="serviceProduct.freeStartTime"/>",
            hidden:true,
            align:'center',
            width:60,
            sortable:true,
            dataIndex:'freeStartTime'
        }                ,
        {
            header:"<fts:text name="serviceProduct.freeEndTime"/>",
            hidden:true,
            align:'center',
            width:60,
            sortable:true,
            dataIndex:'freeEndTime'
        }                ,
        {
            header:"<fts:text name="serviceProduct.isDisplayMsg"/>",
            hidden:true,
            align:'center',
            width:70,
            sortable:true,
            dataIndex:'isDisplayMsg', renderer:function (val, p, row) {
            switch (val) {
                case 1:
                    return '是';
                case 2:
                    return '否';
            }
        }
        }                ,
        {
            header:"<fts:text name="serviceProduct.msg"/>",
            hidden:true,
            align:'center',
            width:60,
            sortable:true,
            dataIndex:'msg'
        }                ,
        {
            header:"<fts:text name="serviceProduct.msgStartTime"/>",
            hidden:true,
            align:'center',
            width:60,
            sortable:true,
            dataIndex:'msgStartTime'
        }                ,
        {
            header:"<fts:text name="serviceProduct.msgEndTime"/>",
            hidden:true,
            align:'center',
            width:60,
            sortable:true,
            dataIndex:'msgEndTime'
        }                ,
        {
            header:"<fts:text name="serviceProduct.isDisplayGift"/>",
            hidden:false,
            align:'center',
            width:90,
            sortable:true,
            dataIndex:'isDisplayGift', renderer:function (val, p, row) {
            switch (val) {
                case 1:
                    return '是';
                case 2:
                    return '否';
            }
        }
        },
        {
            header:"管理",
            align:'center',
            width:55,
            sortable:false,
            items:defaultManageLinks,
            dataIndex:'id',
            renderer:function (val, p, row) {
                if(canDoThisAction('serviceProduceView')){
                return '<a href=\'sytemServiceProductView.jsp?keyId=' + val + '\'>查看</a>&nbsp;';
            }      }
        },
        {
            header:"赠送服务产品",
            align:'center',
            width:100,
            sortable:false,
            dataIndex:'id',
            renderer:function (val, p, row) {
                // alert(row.data.type);
                if (canDoThisAction('serviceProductGift')) {
                    if (row.data.type == 1 || row.data.type == 3) {
                        return '<a href=\'giftServiceProductBinding.jsp?keyId=' + val + '\'>管理</a>&nbsp;';
                    }

                }
            }
        }
    ]);
    //除了基本操作（删除，查看），还要添加的链接
    //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
    //工具栏中的按钮，还需要添加的
    //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
    checkAllFunctions();  // 检查用户权限
    var serviceProductGrid = new FortuneSearchListGrid({
        title:'<fts:text name="serviceProduct"/>列表',
        sm:sm,
        width:805,
        height:505,
        cm:columns,
        store:searchStore,
        tbar:new Ext.Toolbar({items:[
            {
                text:'搜索: '
            },
            {
                text:'名称'
            },
            new Ext.ux.form.SearchField({
                store:searchStore,
                paramName:'obj.name',
                width:320
            })
        ]}),
        bbar:new Ext.PagingToolbar({
            pageSize:defaultPageSize,
            store:searchStore,
            displayInfo:true,
            displayMsg:'结果数据 {0} - {1} of {2}',
            emptyMsg:"没有数据",
            items:defaultBottomButtons
        })
    });
    serviceProductGrid.render(displayDiv);
    defaultGrid = serviceProductGrid;
    searchStore.load({params:{start:0, limit:defaultPageSize}});
}
Ext.onReady(function () {
    Ext.QuickTips.init();
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
<table align="center" width="660">
    <tr>
        <td>
            <div id="displayDiv"></div>
        </td>
    </tr>
</table>
</body>
</html>