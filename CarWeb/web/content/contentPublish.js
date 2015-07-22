var channelStore;
var columnStore;
var tagStore;
var recommendStore;
var serviceProductStore;
var winOnline;
if (!channelStore){
    channelStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort: true,
        url: "/publish/channel!searchAll.action?type=1&cspId="+spId,
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {name:'id'},
            {name:'name'}
        ]
    });
}
channelStore.setDefaultSort("id","ASC");
channelStore.load({params:{start:0, limit:1000000}});
if (!columnStore){
    columnStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort: true,
        url: "/publish/channel!searchAll.action?type=2&cspId="+spId,
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {name:'id'},
            {name:'name'}
        ]
    });
    columnStore.setDefaultSort("id","ASC");
//    columnStore.load({params:{start:0, limit:1000000}});
}
if (!tagStore){
    tagStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort: true,
        url: "/publish/channel!searchAll.action?type=3&cspId="+spId,
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {name:'id'},
            {name:'name'}
        ]
    });
    tagStore.setDefaultSort("id","ASC");
//    tagStore.load({params:{start:0, limit:1000000}});
}


if (!recommendStore){
    recommendStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort: true,
        //查询所有常规推荐
        // url: "/publish/recommend!list.action?type=1&cspId="+spId,
        //查询本频道推荐
        url: "/publish/recommend!getRecommendsByCspId.action?type=1&cspId="+spId,
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {name:'id'},
            {name:'name'}
        ]
    });
    recommendStore.load({params:{start:0, limit:1000000}});
}

if (!serviceProductStore){
    serviceProductStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort: true,
        //查询所有产品列表
        // url: "/product/serviceProduct!list.action?cspId="+spId,
        //查询本频道产品列表
        url: "/product/serviceProduct!getServiceProductsByCspId.action?cspId="+spId,
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {name:'id'},
            {name:'name'},
            {name:'type'}
        ]
    });
    serviceProductStore.setDefaultSort("type","ASC");
    serviceProductStore.load({params:{start:0, limit:1000000}});
}

function saveAll(){
    var actionUrl="/content/contentCsp";
    //获取每个Store里的值
    var channels = getCheckboxValue('channels');
    var serviceProducts= getCheckboxValue('serviceProducts');
    var recommends = getCheckboxValue('recommends');
    var contentId='';
    for(var i=0;i<checkRow;i++){
        contentId += clipStore.data.items[i].data.c_id+",";
    }
    if (contentId!=''){
        contentId = contentId.substring(0,contentId.length-1);
    }
    var uploadData = "?contentId="+contentId+"&channels="+channels
        +"&serviceProducts="+serviceProducts +"&recommends="+recommends+"&cspId="+spId;

    var remoteRequestStore = new Ext.data.JsonStore({

        method:'POST',
        url: actionUrl + "!publish.action"+uploadData+"&type="+1+"&keyIds="+keyIds
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
                    //根据参数判断页面是否需要刷新
                    if(isNeedLoad){
                        winOnline.close();
                        loadData();
                    }else{
                        winOnline.close();
                    }
                } else {
                    Ext.MessageBox.alert('发生异常','服务器返回错误信息：'+returnData.error);
                }
            }
    });
}
function addTabMax(){
    winOnline.getTopToolbar().add(new Ext.Toolbar.TextItem('已选中电影：'));
    for(i=0;i<checkRow;i++){
        keyId=clipStore.data.items[i].data.cc_id;
        contentName=clipStore.data.items[i].data.c_name;
        contentId=clipStore.data.items[i].data.c_id;
        //空格
        winOnline.getTopToolbar().add(new Ext.Toolbar.Spacer());
        //分割线
        winOnline.getTopToolbar().add(new Ext.Toolbar.Separator());
        winOnline.getTopToolbar().add(new Ext.Toolbar.TextItem(contentName));
    }
}

//定义的界面
var onlineForm;
var remoteRequestStore;
function online(){
    if (onlineForm){
        onlineForm.update('');
    }
    winOnline = new Ext.Window({
        title:"上线设置",
        y:2,
        width:945,
        height:640,
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
                width:938,
                xtype: 'panel',
                baseCls:'my-panel-no-border',
                height:532,
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
                                width:300,
                                height:532,
                                id:contentId,
                                store: channelStore,
                                loadMask:{msg:'正在加载数据，请稍侯……'},
                                iconCls:'icon-grid',
                                columns: [
                                    {header: "名称", dataIndex: 'name', width: 250, sortable: false, align:'left'},
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
                            }) ,
                            {   //产品form
                                layout:'table',
                                layoutConfig: {columns:1},
                                baseCls: 'x-plain',
                                items:[
                                    new Ext.grid.GridPanel({
                                        title:"产品",
                                        width:300,
                                        height:280,
                                        store: serviceProductStore,
                                        loadMask:{msg:'正在加载数据，请稍侯……'},
                                        iconCls:'icon-grid',
                                        columns: [
                                            {header: "选", dataIndex: '', width: 40, sortable: false, align:'center',
                                                renderer:function(val,p,row){
                                                    return '<input type="checkbox" name="serviceProducts" value="'+row.data.id+'"/>';
                                                }
                                            },
                                            {header: "名称", dataIndex: 'name', width: 200, sortable: false, align:'left'},
                                            {header: "类型", dataIndex: 'type', width: 52, sortable: false, align:'center',
                                                renderer:function(val,p,row){
                                                    switch(val){
                                                        case 1:
                                                            return '包月';
                                                            break;
                                                        case 2:
                                                            return '按次';
                                                            break;
                                                        case 3:
                                                            return '礼品包';
                                                            break;
                                                    }
                                                }
                                            }
                                        ]
                                    }) ,
                                    new Ext.grid.GridPanel( {
                                        title:"常规推荐",
                                        width:300,
                                        height:250,
                                        store: recommendStore,
                                        loadMask:{msg:'正在加载数据，请稍侯……'},
                                        iconCls:'icon-grid',
                                        columns: [
                                            {header: "选", dataIndex: '', width: 40, sortable: false, align:'center',
                                                renderer:function(val,p,row){
                                                    return '<input type="checkbox" name="recommends" value="'+row.data.id+'"/>';
                                                }
                                            },
                                            {header: "名称", dataIndex: 'name', width: 250, sortable: false, align:'left'}
                                        ]
                                    })
                                ]
                            },
                            {
                                layout:'table',
                                layoutConfig: {columns:1},
                                baseCls: 'x-plain',
                                items:[
                                    new Ext.grid.GridPanel({
                                        title:"栏目",
                                        width:310,
                                        height:280,
                                        store: columnStore,
                                        loadMask:{msg:'正在加载数据，请稍侯……'},
                                        iconCls:'icon-grid',
                                        columns: [
                                            {header: "名称", dataIndex: 'name', width: 240, sortable: false, align:'left'} ,
                                            {header: "选", dataIndex: '', width: 40, sortable: false, align:'center'
//                                                renderer:function(val,p,row){
//                                                    isLeaf = row.json.leaf;
//                                                    if(isLeaf){
//                                                        return '<input type="checkbox" name="columnStores" value="'+row.data.id+'"/>';
//                                                    }
//                                                }
                                            }
                                        ]
                                    }) ,
                                    new Ext.grid.GridPanel({
                                        title:"标签",
                                        width:310,
                                        height:250,
                                        store: tagStore,
                                        loadMask:{msg:'正在加载数据，请稍侯……'},
                                        iconCls:'icon-grid',
                                        columns: [
                                            {header: "名称", dataIndex: 'name', width: 240, sortable: false, align:'left'},
                                            {header: "选", dataIndex: '', width: 40, sortable: false, align:'center'
//                                                renderer:function(val,p,row){
//                                                    isLeaf = row.json.leaf;
//                                                    if(isLeaf){
//                                                        return '<input type="checkbox" name="tagStores" value="'+row.data.id+'"/>';
//                                                    }
//                                                }
                                            }
                                        ]
                                    })
                                ]
                            }
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
    //向tbar添加所选的电影名称
    addTabMax();
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
function changeOnlineSet(isNeedLoad){
    online();
    //得到已经选了的数据
    remoteRequestStore = new Ext.data.JsonStore({
        method:'POST',
        url: actionUrl + "!getPublish.action?content.id=" + clipStore.data.items[0].data.c_id+"&csp.id="+spId
    });
    remoteRequestStore.reload({
        callback :
            function(records,options,success){
                var returnData = this.reader.jsonData;
                var contentChannels = this.reader.jsonData.ContentChannel;
                var contentRecommends = this.reader.jsonData.ContentRecommend;
                var contentServiceProducts = this.reader.jsonData.ContentServiceProduct;

                if (contentChannels && contentChannels.length>0){
                    var str = '';
                    for (var j=0; j<contentChannels.length; j++){
                        str += ","+contentChannels[j].channelId+",";
                    }
                    setCheckboxValue('channels',str);
//                    setCheckboxValue('columnStores',str);
//                    setCheckboxValue('tagStores',str);
                }
                if (contentRecommends && contentRecommends.length>0){
                    var str1 = '';
                    for (var i=0; i<contentRecommends.length; i++){
                        {
                            str1 += ","+contentRecommends[i].recommendId+",";
                        }
                    }
                    setCheckboxValue('recommends',str1);
                }
                if (contentServiceProducts && contentServiceProducts.length>0){
                    var str = '';
                    for (var i=0; i<contentServiceProducts.length; i++){
                        str += ","+contentServiceProducts[i].serviceProductId+",";
                    }
                    setCheckboxValue('serviceProducts',str);
                }
            }
    });
}
