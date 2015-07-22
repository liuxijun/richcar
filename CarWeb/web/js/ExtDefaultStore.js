var constEncodeTypeLive=2;
var constEncodeTypeRecord=4;
var constEncodeTypeEncode=5;

var dictFields = ['name','value'];
var dictAction = "/config/dict!list.action?typeId=";
var dictRoot = 'objs';

function initStores(stores){
    for(var i=0;i<stores.length;i++){
        var aStore = stores[i];
        aStore.load();
    }
}
function initDeviceStore(){

}

var areaStore;
function initAreaStore(){
    Ext.Ajax.request({
        waitTitle:'请稍后',
        waitMsg:'正在刷新...',
       url:"../system/area!getAreas.action?obj.id=-1",
        callback : function(opt, success, response) {
            if(success){

                var serverData = Ext.util.JSON.decode(response.responseText);
                if(serverData){
                    try {
                        var areas = serverData.objs;
                        if (areas) {
                            var aStoreItems = [];
                            for (var j = 0; j < areas.length; j++) {
                                var item = [areas[j].name,areas[j].id];
                                aStoreItems.push(item);
                            }
                            areaStore = new Ext.data.SimpleStore({
                                fields:['name','value'],
                                data:aStoreItems
                            });

                        }
                    } catch(e) {
                    }
                }else{
                    Ext.MessageBox.alert('发生异常','服务器返回错误信息：'+serverData.error)
                }
            }
            functionDone('initAreaStore');
        }
    });
}


function initDictStores(){
    Ext.Ajax.request({
        waitTitle:'请稍后',
        waitMsg:'正在刷新...',
       url:dictAction+"all",
        callback : function(opt, success, response) {
            if(success){
                var serverData = Ext.util.JSON.decode(response.responseText);
                if(serverData){
                    initDictJsonStores(serverData);
                }else{
                    Ext.MessageBox.alert('发生异常','服务器返回错误信息：'+serverData.error)
                }
            }
            functionDone('initDictStores');
        }
    });
}
var allDictStores=[];
function initDictJsonStores(serverData){
    var stores = serverData.objs;
    if(stores&&stores.length>0){
        allDictStores = [];
        for(var i=0;i<stores.length;i++){
            var aStoreData = stores[i];
            if(aStoreData){
                aStoreItems = [];
                var storeName = aStoreData.dictName;
                var dataItems = aStoreData.dictItems;
                for(var j=0;j<dataItems.length;j++){
                    var item = [dataItems[j].name,dataItems[j].value];
                    aStoreItems.push(item);
                }
                var aStore = new Ext.data.SimpleStore({
                    fields:dictFields,
                    data:aStoreItems
                });
                if(i==1){
                   //alert('storeName:'+storeName+',items:'+aStoreItems);
                }
                if(storeName == "encodeType"){
                   //alert('storeName:'+storeName+',items:'+aStoreItems);
                }
                allDictStores.push({name:storeName,store:aStore});
            }
        }
    }
}

function getDictStore(name,insertFirstNull){
    var names = "";
    for(var s=0;s<allDictStores.length;s++){
        var aStore = allDictStores[s];
        names += ","+aStore.name;
        if((s%5==0)&&s>0){
            names+="\n";
        }
        if(aStore.name == name){
            var result = aStore.store;
            if(insertFirstNull){
                result.insert(0,new Ext.data.Record({value:'',name:'请选择..'}));
            }
            return result;
        }
    }
    //alert("没有找到：'"+name+"' in ["+names+"]");
    return new Ext.data.SimpleStore({
        fields:dictFields,
        data:[['没有找到“' +name+
            '”','-1']]
    });
}