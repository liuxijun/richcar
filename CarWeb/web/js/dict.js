/**
 *字典助手
 */

var dictUtils = {
    dict:[],
    nameMapping:[],
    getDict:function(id){
        var dict = dictUtils.nameMapping[id];
        if(dict==null||typeof(dict)=='undefined'){
            dict= [];
        }
        return dict;
    },
    callback:function(){

    },
    init:function(callback){
        if(callback!=null){
            dictUtils.callback = callback;
        }else{
            dictUtils.callback=function(){}
        }
        $.ajax({
            url:'../config/dict!list.action?typeId=all',
            dataType:'json',
            success:function(data){
                var dicts = data['objs'];
                if(dicts!=null){
                    dictUtils.dict = dicts;
                    dictUtils.reIndexDict(dicts,1);
                    dictUtils.callback();
                }
            }

        });
    },
    reIndexDict:function(dicts,repeatTimes){
        if(repeatTimes>100){
            return;
        }
        if(dicts!=null&&dicts.length>0){
            var idx= 0,len=dicts.length;
            for(;idx<len;idx++){
                var dict =dicts[idx];
                var items = dict['items'];
                if(items==null||items.length<=0){
                    return;
                }
                var code = getParameter(dict,'code',null);
                if(code==null||code==''){
                    code = getParameter(dict,'value',null);
                }
                if(code==null||code==''){
                    return;
                }
                dictUtils.nameMapping[code]=items;
                dictUtils.reIndexDict(items,repeatTimes+1);
            }
        }
    }
};