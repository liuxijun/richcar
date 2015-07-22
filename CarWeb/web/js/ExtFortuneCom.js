
Ext.ns('FortuneBaseViewForm');
FortuneCombo = Ext.extend(Ext.form.ComboBox, {
    initComponent : function() {
        FortuneCombo.superclass.initComponent.call(this);
    },
    mode:'local',
    forceSelection:true,
    triggerAction:"all",editable:false,
    valueField:'value',displayField:'name',
    hiddenName: name,
    emptyText:'请选择...'
});

 FortuneViewForm = Ext.extend(Ext.FormPanel, {
    initComponent : function() {
        FortuneViewForm.superclass.initComponent.call(this);
        this.on('specialkey', function(f, e) {
            if (e.getKey() == e.ENTER) {
                this.onSubmitClick();
            }
        }, this);
    },
    defaultLabelAlign:'right',
    id:'BaseViewForm338547183092',
    labelWidth: 75, // label settings here cascade unless overridden
    labelAlign:'right',
    title: '查看',
    //bodyStyle:'padding:5px 5px 0',
    width: 350,
    defaults: {width: 230},
    defaultType: 'textfield',
    buttons:defaultViewFormButtons,
    onSubmitClick:function() {
        saveFormAjax();
    }
});

var storeConfig = {
    root: 'objs',totalProperty: 'totalCount',
    fields:[],
    proxy: new Ext.data.HttpProxy({method: 'POST',url: ''})
};


Ext.ns("FortuneSearchStore");
FortuneSearchStore = Ext.extend(Ext.data.JsonStore, {
    root: 'objs',
    totalProperty: 'totalCount',
    remoteSort: true,
    fields:[],
    baseParams:{limit:defaultPageSize},
    sm : new Ext.grid.CheckboxSelectionModel({singleSelect : false}),
    proxy: new Ext.data.HttpProxy({
        method: 'POST',
        url: actionHeader + "!list.action"
    })
});

var searchStore = new FortuneSearchStore();
Ext.ns("FortuneSearchListGrid");

///*
var topToolBar = new Ext.Toolbar({items:[
    {
        text:'搜索：'
    },
    {
        text:'名称'
    },
    new Ext.ux.form.SearchField({
        store: searchStore,
        paramName:searchParameterName,
        width:320
    })
]});

var bottomToolBar = new Ext.PagingToolbar({
    pageSize: defaultPageSize,
    store: searchStore,
    displayInfo: true,
    displayMsg: '数据记录 {0} - {1} of {2}',
    emptyMsg: "没有数据",
    items:[]
});//fortuneSearchStore.on("beforeload", function() {});
//  */

FortuneSearchListGrid = Ext.extend(Ext.grid.GridPanel, {
    initComponent : function() {
        FortuneSearchListGrid.superclass.initComponent.call(this);
    },
    width:650,
    height:410,
    title:'列表',
    store:searchStore,
    trackMouseOver:false,
    disableSelection:true,
    loadMask: true,
    columns:[]}
        );

FortuneSearchFormPanel = Ext.extend(Ext.FormPanel, {

});

FortuneFileSearchStore = Ext.extend(Ext.data.JsonStore,{
                root: 'objs',
                totalProperty: 'totalCount',
                remoteSort: true,
                fields:['name','size','timestamp','directory'],
                baseParams:{limit:defaultPageSize},
                sm:sm
            });

///*

var fileInfoColumns = new Ext.grid.ColumnModel([
    sm,
    {
        header: "文件名",
        hidden:false,
        align:'left',
        width: 130,
        sortable: true,
        renderer:displayFileName,
        dataIndex: 'name'
    },
    {
        header: "大小",
        hidden:false,
        align:'left',
        width: 60,
        sortable: true,
        renderer:displayFileSize,
        dataIndex: 'size'
    },
    {
        header: "修改日期",
        hidden:false,
        align:'center',
        width: 120,
        sortable: true,
        dataIndex: 'timestamp'
    }
]);

FortuneFileListGrid = Ext.extend(Ext.grid.GridPanel,{
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
});
