/**
 *
 *
 */
var selectedFunc=null;
var desertWin;
function openSelectDesertWin(parameters){
    selectedFunc = parameters.selectedFunc;
    initDisplayWindows(parameters.url);
}
function onSelected(id,name){
    if(selectedFunc!=null){
        selectedFunc.call(this,id,name);
    }
    desertWin.close();

}
function displaySelectOption(val,meta,rec){
    return "<a href='#' onclick='onSelected("+val+",\""+rec.get("name")+"\")' title='选中该目标'>选中</a>";
}

function initDisplayWindows(url){
    var width=320;
    var desertStoreConfig = {
        fields : ['id','name'],
        root: 'objs',
        totalProperty: 'totalCount',
        remoteSort: true,
        proxy : new Ext.data.HttpProxy({method: 'POST',
            url: url}
        )
    };
    var desertSearchStore = new Ext.data.JsonStore(desertStoreConfig);
    var columns = new Ext.grid.ColumnModel([
        new Ext.grid.CheckboxSelectionModel({singleSelect:true}),
        {id:'Id',hidden:true,align:'center',header: "序号", width: 30, sortable: true, dataIndex: 'id'},
        {id:'name',hidden:false,align:'center',header: "名字", width:width-110, sortable: true, dataIndex: 'name'},
        {header: "选中", align:'center', width: 100, sortable: false,renderer:displaySelectOption,dataIndex: 'id'}
    ]);
    var desertGrid = new Ext.grid.GridPanel({
        title:'目标列表',
        width:width,
        height:300,
        sm : sm,
        cm:columns,
        store:desertSearchStore,
        tbar:new Ext.Toolbar({items:[
            {text:'搜索: '}, {text:'名称'},
            new Ext.ux.form.SearchField({
                store: desertSearchStore,
                paramName:'obj.name',
                width:width-100
            })
        ]}),
        bbar:new Ext.PagingToolbar({
            pageSize: defaultPageSize,
            store: desertSearchStore,
            displayInfo: true,
            displayMsg: '结果数据 {0} - {1} of {2}',
            emptyMsg: "没有数据"
        })
    });
    desertWin = new Ext.Window({
        width:width,
        height:375,
        closable: true,
        resizable: true,
        plain: true,
        border: false,
        title:'选择线路',
        autoHeight:true,
        items: [desertGrid]
    });
    desertWin.show();
    desertSearchStore.load({params:{start:0, limit:defaultPageSize}});
}