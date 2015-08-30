/**
 * 助手类
 */
function getParameter(parameters,name,defaultVal){
    var result = defaultVal;
    if(parameters!=null&&typeof(parameters)!='undefined'){
        result = parameters[name];
    }
    return checkParameters(result,defaultVal);
}
function checkParameters(parameterValue,defaultValue){
    if(parameterValue==null||typeof(parameterValue)=='undefined'||parameterValue==''){
        parameterValue = defaultValue;
    }
    return parameterValue;
}
var utils = {
    openDialog: function (options) {
        var href = options.href || "about:blank";
        var transition = options.transition || "none";
        $('body').append("<a id='tPushDialog' href='" + options.href + "' data-rel=\"dialog\" data-transition=\"" + options.transition + "\" style='display:none;'>Open dialog</a> ");
        $("#tPushDialog").trigger('click');
        $('body').find('#tPushDialog').remove();
        $("#" + options.dialog).live('pageshow', function (event) {
            if (typeof options.callback == 'function')
                options.callback();
        });

    },
    confirm:function(messages,yesFunc,noFunc,extraParam){

    },
    getStyles:function(header,fields){
        var result = '';
        var translates = {align:'text-align',size:'font-size'};
        for(var i= 0,l=fields.length;i<l;i++){
            var field = fields[i];
            var v = getParameter(header,field,null);
            if(v!=null){
                var type = typeof(v);
                if(type=='number'){
                    v = v+'px';
                }
                if(result!=''){
                    result+=';';
                }

                result+=getParameter(translates,field,field)+':'+v;
            }
        }
        if(result!=''){
            return ' style="'+result+'"';
        }else{
            return '';
        }
    },
    buildRow:function(headers,items,rowId,rowClassName,colClassNameHeader){
        rowClassName = checkParameters(rowClassName,"ui-grid-b");
        colClassNameHeader = checkParameters(colClassNameHeader,'ui-block');
        rowId = checkParameters(rowId,null);
        if(rowId!=null){
            rowId = ' id="'+rowId+'"';
        }
        var result = '';//'\t\t<div class="'+rowClassName+'"' +rowId+'>';
        var aCharCode = 'a'.charCodeAt(0);
        for(var i= 0,l=headers.length;i<l;i++){
            var header = headers[i];
            var name = getParameter(header,'name',null);
            var value = '';
            if(name!=null&&items!=null){
                value = getParameter(items,name,'');
            }else{
                if(items!=null){
                    if(i<items.length){
                        value = items[i];
                    }else{
                        value = '';
                    }
                }else{
                    value = getParameter(header,'title',name);
                }
            }
            var colClassName = colClassNameHeader+'-'+String.fromCharCode(aCharCode+i);
            result +='\t\t\t<div class="'+colClassName+'"'+utils.getStyles(header,['align','width','height','color'])+'>'+value+'</div>\n';
        }
        //result+='</div>\n';
        return result;
    },
    comId:1,
    grid:function(parameters){
        var header = getParameter(parameters,'header',null);
        var clsName = getParameter(parameters,"clsName","ui-grid-b");
        var rowClsHeader=getParameter(parameters,"rowClsHeader","ui-block");
        var gridId =  getParameter(parameters,"id","grid_"+utils.comId++);
        var result = '<div id="'+gridId+'" class="' +clsName+
            '">\n';
        if(header!=null){
            result +=utils.buildRow(header,null,'gridHeader_'+utils.comId++,clsName,rowClsHeader);
        }
        var store = getParameter(parameters,"store",{data:[]});
        for(var i= 0,l=store.data.length;i<l;i++){
            var data = store.data[i];
            result+=utils.buildRow(header,data,'row_'+i,rowClsHeader,'ui-block');
        }
        result+='</div>';
        var renderTo = getParameter(parameters,'renderTo','mainBody');
        var obj = document.getElementById(renderTo);
        if(obj!=null){
            obj.innerHTML = result;
        }else{
            alert("无法找到组件进行渲染："+renderTo);
        }
    }
};