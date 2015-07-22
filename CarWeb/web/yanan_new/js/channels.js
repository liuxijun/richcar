var serverOptions = {
    address:'/'
};

var channelOptions = {
    renderSonChannels:function(parentId){
        appendJs(serverOptions.address+'page/js/channels.jsp?id=' +parentId+
            '&callback=doRenderChannel');
    }
};

function appendJs(url){
    var head = document.getElementsByTagName("HEAD")[0];
    var jsFileRef=document.createElement('script');
    jsFileRef.setAttribute("type","text/javascript");
    jsFileRef.setAttribute("src", url);
    head.appendChild(jsFileRef);
}

function doRenderChannel(jsonData) {
    $("#parentName").html(jsonData.channel.name);
    var sonChannels = jsonData.children;
    if(sonChannels != null && sonChannels.length > 0) {
        if(channelId < 0) {
            channelId = sonChannels[0].id;
            $("#channelName").html( sonChannels[0].name);
            $("#subColHead").html( sonChannels[0].name);
        }

        var subChannels = $(".ulSubColName");
        subChannels.html('');
        var i = 0;
        $.each(jsonData.children,function(){
            i++;
            var c = this;
            var liStr = '<li id="subChannel_'+c.id+'" name="ch'+i+'"><a id="subChannel_'+c.id+'_a" href="javascript:searchChannel('+c.id+')">';
            if(c.id == channelId)  {
                liStr =  '<li class="fRed" id="subChannel_'+c.id+'" name="ch'+i+'"><a id="subChannel_'+c.id+'_a" href="javascript:searchChannel('+c.id+')" style="color: #c80000">';
                $("#channelName").html(c.name);
                $("#subColHead").html(c.name);
            }
            subChannels.append(liStr+c.name+'</a></li>');
        });

        //二级页面更多按钮的链接赋值，
        var subChannelLinks = document.getElementsByClassName('moreSign');
        if(subChannelLinks && subChannelLinks.length > 0) {
            getSubChannelLinks(sonChannels);
        }

        searchChannel(channelId);
    }


    function getSubChannelLinks(subChannels) {
        for(var i = 0;i<subChannels.length;i++) {
           if(i < 4) {
               subChannelLinks[i].href = "subColumn.html?parentId="+parentId+"&channelId="+subChannels[i].id+"&index="+(i+1);
           }
        }
    }



}