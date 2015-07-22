/**
 * Created by xjliu on 2014/10/15.
 */
var searchData={
    cspId:-1,
    channelId:-1,
    isChannels:null,
    channelIds:'',
    pageNo:1,pageSize:9,pageCount:1000000,
    searchType:null,searchValue:null
};
function getParameterFromUrl(urlString,parameterName,defaultValue){
    if(urlString==null){
        urlString = document.location.search;
        if(urlString==null||urlString==""){
            urlString = document.location.href;
        }
    }
    if (urlString != null){
        var parameterBegin = ["&"+parameterName + "=","?"+parameterName+"=","_"+parameterName+"_eq_"];
        var parameterEnd = ["&","_amp_",".shtml",".html",".jsp",".xml"];
        var urlEnd = -1,i;
        for(i=0;i<parameterBegin.length;i++) {
            urlEnd = urlString.indexOf(parameterBegin[i]);
            if (urlEnd != -1) {
                var paramsUrl = urlString.substring(urlEnd + parameterBegin[i].length);
                var j;
                for (j = 0; j < parameterEnd.length; j++) {
                    var isEnd = paramsUrl.indexOf(parameterEnd[j]);
                    if (isEnd != -1) {
                        return paramsUrl.substring(0, isEnd);
                    } else {
                    }
                }
                return paramsUrl;
            } else {
            }
        }
        return defaultValue;
    }else{
        return defaultValue;
    }
}

function renderTop10(top10,containorId,systemIsReady){
    if(top10==null||!systemIsReady){
        return;
    }
    var hotOl= $(containorId);
    hotOl.html('');
    if(top10.length>0){
        if(containorId.indexOf(".list_hot")<0){
            hotOl.append('<li class="fr_p"><a href="video.html?id=' +top10[0].id+
                '"><img src="' +top10[0].poster+
                '" width="250" height="110"><span class="pl"></span><em></em></a></li>');
        }
        for(var i= 0,l=top10.length;i<l&&i<10;i++){
            var item = top10[i];
            var name = item.name;
            if(name!=null){
                if(name.length>18){
                    name = name.substring(0,18)+'...';
                }
            }else{
                name = '(нч)';
            }
            hotOl.append('<li class="fr_f"><em' + (i<5?' class="hn"':'')+
                '>' +(i+1)+
                '</em><a href="video.html?id=' +item.id+
                '" title="' +item.name+
                '">' +name+
                '</a></li>');
        }
    }
}
