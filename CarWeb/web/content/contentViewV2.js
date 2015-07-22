var propertyStore;
var propertySelectStore;
var contentPropertyStore;
var propertyInited = false;
var propertySelectInited =false;
var dataTypeTextSingle = 1;
var dataTypeTextArea = 2;
var dataTypeTextNumber = 3;
var DataTypeTextDate = 4;
var dataTypeDropDown = 5;
var dataTypeRadioBox = 6;
var dataTypeCheckBox = 7;
var dataTypeMediaWmv = 8;
var dataTypeMediaFlash= 9;
var dataTypeMediaRTSP = 10;
var dataTypeFilePic = 11;
var dataTypeFilePage = 12;
var dataTypeFileZip = 13;

function initSystem(contentId,moduleId){
    initPropertyStores(contentId,moduleId);
}
function initPropertyStores(contentId,moduleId){
    propertyStore     = new Ext.data.JsonStore({
        method:'POST',
        url:"/module/property!list.action?obj.moduleId="+moduleId,
        baseParams:{limit:1000000},
        totalProperty:'totalCount',
        root:'objs',
        fields:[
            {name:'id'},
            {name:'name'},
            {name:'code'},
            {name:'dataType'},
            {name:'isMultiLine'},
            {name:'isMerge'},
            {name:'isNull'},
            {name:'isMain'},
            {name:'maxSize'},
            {name:'columnName'},
            {name:'relatedTable'},
            {name:'status'},
            {name:'displayOrder'}
        ]
    });
    propertyStore.setDefaultSort('displayOrder','asc');
    propertyStore.load({params:{start:0,limit:10000000},callback :
        function(records,options,success){
            if(success){
                propertyInited = true;
                initContentProperties(contentId);
            }
        }});
    propertySelectStore = new Ext.data.JsonStore({
        method:'POST',
        url:"/module/propertySelect!list.action",
        baseParams:{limit:1000000},
        totalProperty:'totalCount',
        root:'objs',
        fields:[
            {name:'id'},
            {name:'name'},
            {name:'code'},
            {name:'status'},
            {name:'propertyId'},
            {name:'displayOrder'}
        ]
    });
    propertySelectStore.setDefaultSort('displayOrder','asc');
    propertySelectStore.load({params:{start:0,limit:10000000},callback :
        function(records,options,success){
            if(success){
                propertySelectInited=true;
                initContentProperties(contentId);
            }
        }});
}

function initContentProperties(contentId){
    if(propertyInited && propertySelectInited){
        contentPropertyStore = new Ext.data.JsonStore({
            method:'POST',
            remoteSort: true,
            url: "contentProperty!search.action?obj.contentId="+contentId,
            totalProperty:"totalCount",
            root:'objs',
            fields:[
                {name:'id'},
                {name:'contentId'},
                {name:'name'},
                {name:'desp'},
                {name:'intValue'},
                {name:'stringValue'},
                {name:'propertyId'},
                {name:'subcontentId'},
                {name:'extraData'}
            ]
        });
        if(contentId&&contentId!=""&&contentId!="-1"&&contentId!="null"){
            contentPropertyStore.load({params:{start:0,limit:10000000},callback :
                function(records,options,success){
                    if(success){
                        displayProperties(contentId);
                    }
                }});
        }else{
            displayProperties(contentId);
        }
    }
}

function displayProperties(contentId){
    var dataItems=[];
    for(var i=0,l=propertyStore.getCount();i<l;i++){
        var rec = propertyStore.getAt(i).data;
        if(rec){
            var dataFound=false;
            var tempData={};
            for(var j=0,cl=contentPropertyStore.getCount()-1;j<=cl;cl--){
                var cpRec = contentPropertyStore.getAt(cl);
                if(cpRec&&cpRec.data.propertyId==rec.id){
                    tempData = cpRec.data;
                    dataFound=true;
                    contentPropertyStore.remove(cpRec);
                }
            }
            if(!dataFound){
                tempData = {id:-1,contentId:contentId,desp:'',extraData:'',intValue:0,name:'',
                    propertyId:rec.id,stringValue:'',subcontentId:''
                };
            }
            tempData["property"]=rec.data;
            dataItems.push(tempData);
        }
    }
    var displayItems = [];
    var dataId = 1;
    for(i=0,l=dataItems.length;i<l;i++){
        var dataItem = dataItems[i];
        var dataType = dataItem.property.dataType;
        switch(dataType){
            case dataTypeTextSingle:
                break;
            case dataTypeTextArea:
                break;
            case dataTypeTextNumber:
                break;
            case DataTypeTextDate:
                break;
            case dataTypeDropDown:
                break;
            case dataTypeRadioBox:
                break;
            case dataTypeCheckBox:
                break;
            case dataTypeMediaWmv:
                break;
            case dataTypeMediaFlash:
                break;
            case dataTypeMediaRTSP:
                break;
            case dataTypeFilePic:
                break;
            case dataTypeFilePage:
                break;
            case dataTypeFileZip:
                break;
            default:
                break;
        }
    }
}
