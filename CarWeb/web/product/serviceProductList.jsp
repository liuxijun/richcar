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
        needPermissions(actionHeader, "listOfSelf", "spServiceProductManage,serviceProductListOfSelf");
//        needPermissions(actionHeader, "save", "spServiceProductManage,serviceProductSave");
        needPermissions(actionHeader, "serviceProductView", "spServiceProductManage,serviceProductView");
        needPermissions(actionHeader, "deleteSelected", "spServiceProductManage,serviceProductDeleteSelected");
        needPermissions(actionHeader, "giftServiceProductBin", "spServiceProductManage");
        needPermissions(actionHeader, "add", "spServiceProductManage");
        needPermissions(actionHeader, "selectChannels", "spServiceProductManage");
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
nextUrl = "serviceProductList.jsp";
actionHeader = "serviceProduct";


checkAllFunctions();  // 检查用户权限
function gotoAddFunc() {
    window.location.href = "serviceProductView.jsp";
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
    },
    {
        text:'绑定频道',
        handler:selectChannels,
        action:'selectChannels'
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


defaultPageSize = 12;

function initDisplay() {
    storeConfig.fields = ['id', 'name', 'type', 'cspId', 'productName', 'validLength', 'lengthUnit', 'status', 'autoPay', 'searchExtra', 'discount', 'discountStartTime', 'discountEndTime', 'isFree', 'freeStartTime', 'freeEndTime', 'isDisplayMsg', 'msg', 'msgStartTime', 'msgEndTime', 'isDisplayGift'];
    storeConfig.proxy = new Ext.data.HttpProxy({method:'POST', url:appendParameter(getActionUrl('listOfSelf'), "keyId", cspId)});
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
            width:185,
            sortable:true,
            dataIndex:'name'
        }                ,
        {
            header:"<fts:text name="serviceProduct.type"/>",
            hidden:false,
            align:'center',
            width:55,
            sortable:true,
            dataIndex:'type', renderer:displayType
        }                ,
        {
            header:"<fts:text name="serviceProduct.cspId"/>",
            hidden:false,
            align:'center',
            width:65,
            sortable:true,
            dataIndex:'cspId'
        }                ,
        {
            header:"<fts:text name="serviceProduct.productId"/>",
            hidden:false,
            align:'center',
            width:110,
            sortable:true,
            dataIndex:'productName'
        }                ,
        {
            header:"<fts:text name="serviceProduct.validLength"/>",
            hidden:true,
            align:'center',
            width:60,
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
            width:70,
            sortable:true,
            dataIndex:'status', renderer:displayStatus
        }                ,
        {
            header:"<fts:text name="serviceProduct.autoPay"/>",
            hidden:true,
            align:'center',
            width:80,
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
            width:80,
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
            width:50,
            sortable:false,
            items:defaultManageLinks,
            dataIndex:'id',
            renderer:function (val, p, row) {
                if(canDoThisAction('serviceProductView')){
                return '<a href=\'serviceProductView.jsp?keyId=' + val + '\'>查看</a>&nbsp;';
            }                  }
        },
        {
            header:"赠送服务产品",
            align:'center',
            width:90,
            sortable:false,
            dataIndex:'id',
            renderer:function (val, p, row) {
                // alert(row.data.type);
                if (row.data.type == 1 || row.data.type == 3) {
                    if (canDoThisAction('giftServiceProductBin')) {
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
        cm:columns,
        width:730,
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



  var  channelStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort: true,
        url: "/publish/channel!searchAll.action?type=1&cspId="+cspId,
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {name:'id'},
            {name:'name'}
        ]
    });
channelStore.setDefaultSort("id","ASC");
channelStore.load({params:{start:0, limit:1000000}});


var remoteRequestStore;

function saveAll(){
    var actionUrl="/product/serviceProductChannel";
    //获取每个Store里的值
    var channels = getCheckboxValue('channels');

    var serviceProductId='';
    for(var i=0;i<checkRow;i++){
        serviceProductId += serviceProductStore.data.items[i].id+",";
    }
    if (serviceProductId!=''){
        serviceProductId = serviceProductId.substring(0,serviceProductId.length-1);
    }
    var uploadData = "?serviceProductIds="+serviceProductId+"&channelIds="+channels+"&cspId="+cspId;

    remoteRequestStore = new Ext.data.JsonStore({

        method:'POST',
        url: actionUrl + "!changeSelectedChannel.action"+uploadData+"&type="+1
    });
    Ext.MessageBox.show({
        msg: '正在处理,请稍候...',
        progressText: '正在处理,请稍候...',
        width:300,
        wait:true,
        waitConfig: {interval:200}
    });
    remoteRequestStore.reload({
        callback :
                function(records,options,success){
                    Ext.MessageBox.hide();
                    var returnData = this.reader.jsonData;
                    if (returnData.success){
                        Ext.MessageBox.alert('提示','操作成功');
                            winOnline.close();
                    } else {
                        Ext.MessageBox.alert('发生异常','服务器返回错误信息：'+returnData.error);
                    }
                }
    });
}


var serviceProductStore=new Ext.data.SimpleStore();
var checkRow;

function selectChannels(){
    var row = defaultGrid.getSelectionModel().getSelections();
    if (row.length == 0) {
        Ext.Msg.alert("提示", "未选择记录！");
        return;
    }else{
        //关闭后存储的数据清空
        serviceProductStore=new Ext.data.SimpleStore();
        for(var i= 0,j=row.length;i<j;i++){
            serviceProductStore.add(row[i]);
            checkRow=j;
        }
        changeChannel();
    }


}


function changeChannel() {
    showChannelWin();
    //得到已经选了的数据
    remoteRequestStore = new Ext.data.JsonStore({
        method:'POST',
        url: "/product/serviceProductChannel!getSelectedChannel.action?serviceProductId=" + serviceProductStore.data.items[0].id+"&csp.id="+cspId
    });

    remoteRequestStore.reload({
        callback :
                function(records,options,success){
                    var serviceProductChannels = this.reader.jsonData.serviceProductChannel;

                    if (serviceProductChannels && serviceProductChannels.length>0){
                        var str = '';
                        for (var j=0; j<serviceProductChannels.length; j++){
                            str += ","+serviceProductChannels[j].channelId+",";
                        }
                        setCheckboxValue('channels',str);
                    }
                }
    });
}

var  winOnline;
function showChannelWin() {
        winOnline = new Ext.Window({
        title:"绑定频道",
        y:2,
        width:425,
        height:630,
        autoHeight: true,
        //当点关闭时窗体的状态,hide为隐藏，close为关闭。
        closeAction:"close",
        tbar:new Ext.Toolbar({
            style: {'margin-left':'0px','margin-top': '0px','margin-right':'5px','margin-bottom':'0px'},
            height:40,
            id:'sss',
            //label溢出时添加滚动条
            autoScroll : true,
            //自动适应高度
            autoHeight: true,
            items:[
//                '-'
            ]
        }),
        items:[
            {
                width:418,
                xtype: 'panel',
                baseCls:'my-panel-no-border',
                height:522,
                items:[
                    {
                        style: {'margin-left':'5px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                        id:'onlineForm',
                        width: '100%',
                        layout:'table',
                        layoutConfig: {columns:3},
                        baseCls: 'x-plain',
                        waitMsgTarget: true,
                        loadMask:{msg:'正在加载数据，请稍侯……'},
                        items: [
                            //频道推荐Form
                            new Ext.grid.GridPanel({
                                title:"频道",
                                width:400,
                                height:522,
                               // id:contentId,
                                store: channelStore,
                                loadMask:{msg:'正在加载数据，请稍侯……'},
                                iconCls:'icon-grid',
                                columns: [
                                    {header: "名称", dataIndex: 'name', width: 330, sortable: false, align:'left'},
                                    {header: "", dataIndex: 'id', width: 40, sortable: false, align:'center',
                                        renderer:function(val,p,row){
                                            //判断leaf,如果是true，则可选，如果是false，则不可选
//                                            isLeaf = row.json.leaf;
//                                            if(isLeaf){
                                            return '<input type="checkbox" name="channels"  value="'+row.data.id+'"/>';
//                                            }
                                        }
                                    }
                                ]
                            })
                        ]
                    }
                ]
            },
            {
                layout:'table',
                layoutConfig: {columns:3},
                baseCls: 'x-plain',
                style: {'margin-left':'5px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                items:[
                    {
                        text: '保　存',
                        xtype:'button',
                        minWidth:80,
                        handler:function(){
                            Ext .Msg.confirm('信息','确定要修改所有信息？',function(btn) {
                                if(btn == 'yes') {
                                   saveAll();
                                }
                            });
                        }
                    },
                    {
                        text: '关闭窗口',
                        xtype:'button',
                        minWidth:80,
                        listeners:{
                            "click":function()
                            {
                                winOnline.close();
                            }
                        }
                    }
                ]
            }
        ]

    });

    winOnline.show();
}

function getCheckboxValue(cbName){
    var checkBoxs = document.getElementsByName( cbName );
    if (checkBoxs==null){
        return '';
    }
    if (checkBoxs.length==null){
        if (checkBoxs.checked){
            return checkBoxs.value;
        }
    }else{
        var result = '';
        for (var i=0;i<checkBoxs.length;i++){
            if (checkBoxs[i].checked){
                result += checkBoxs[i].value+",";
            }
        }
        if (result!=''){
            result = result.substring(0,result.length-1);
        }
        return result;
    }
    return '';
}
function setCheckboxValue(cbName,values){
    var checkBoxs = document.getElementsByName( cbName );
    if (checkBoxs==null){
        return '';
    }
    if (checkBoxs.length==null){
        if (values.indexOf(","+checkBoxs.value+",")>-1){
            checkBoxs.checked = true;
        }
    }else{
        var result = '';
        for (var i=0;i<checkBoxs.length;i++){
            if (values.indexOf(","+checkBoxs[i].value+",")>-1){
                checkBoxs[i].checked = true;
            }
        }
    }
}

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