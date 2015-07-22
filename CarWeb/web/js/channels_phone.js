/**
 * Created by xjliu on 2014/10/16.
 */
var serverOptions = {
    address:'/'
};
var channelOptions = {
    channels:[
        {id:474431590,name:'重大活动',parentId:1}
        ,{id:474431591,name:'新闻频道',parentId:1}
        ,{id:474431592,name:'人文频道',parentId:1}
        ,{id:474431593,name:'工业频道',parentId:1}
        ,{id:474431594,name:'International',parentId:1}
        ,{id:474431595,name:'原创专区',parentId:1}
    ],
    topChannelCls:'.navarea ul',
    leftChannelCls:'.menu_list',
    bottomChannelCls:'.qlink ul',
    renderTopLevelChannel:function(channels){
        var areas = [this.leftChannelCls,'left'];
        for(var i= 0,l=areas.length;i<l;i+=2){
            var id = areas[i];
            var eleId = areas[i+1];
            var subChannels = $(id);
            if(subChannels==null){
                continue;
            }
            subChannels.html('');
            $.each(channels,function(){
                var c = this;
                subChannels.append('<li id="' +eleId+
                    'Channel_' + c.id+
                    '"><a id="' +eleId+'Channel_a' + c.id+'" href="javascript:renderChannel(' + c.id+
                    ')">' + c.name + '</a></li>');
            });
        }


    },
    selectChannel:function(channelId){

    }


};

function appendJs(url){
    var head = document.getElementsByTagName("HEAD")[0];
    var jsFileRef=document.createElement('script');
    jsFileRef.setAttribute("type","text/javascript");
    jsFileRef.setAttribute("src", url);
    head.appendChild(jsFileRef);
}

var firstRender = true;

function renderChannel(id){
    if(window.location.href.indexOf("list_phone.html")<0){
        window.location.href = "list_phone.html?parentId="+id;
        return;
    }
    var selected = $(".right_channel_on");
    $.each(selected,function(){
        var id = $(this).tagName;
        $(this).removeClass("right_channel_on");
    });
    $("#leftChannel_a"+id).addClass("right_channel_on");
    appendJs(serverOptions.address+'page/js/channels.jsp?id=' +id+
        '&callback=doRenderChannel');
    searchData.channelId = channelId;

    if(!firstRender) {
        $("#menu_list").toggle();
    }
    firstRender=false;

    //goToPage(1);
}

function doRenderChannel(jsonData){
    var subChannels = $(".channel_list ul");
    subChannels.html('');
   /* subChannels.append('<label>频道</label>');*/
    var firstChannelId = -1;
    $.each(jsonData.children,function(){
        var c = this;
        if(firstChannelId == -1){
            firstChannelId = c.id;
        }
        if(channelId == c.id){
            firstChannelId = channelId;
        }
        subChannels.append('<li class="subChannel" id="subChannel_' + c.id+
            '"><a href="javascript:searchChannel(' + c.id+
            ')">' + c.name+
            '</a></li>');
    });
    searchChannel(firstChannelId);
}

function doRenderSearchChannel() {
   var searchChannels = $("#searchChannels");
   searchChannels.html('');
   var channelIndex = 0;
   var length = parseInt(channelOptions.channels.length+1+channelOptions.channels.length/4);
   for(var i = 0;i< length;i++) {
       var channel = channelOptions.channels[channelIndex];
       if(i==0) {
           searchChannels.append('<li class="over" id="subChannel_-1"><a href="javascript:searchChannel(-1)">全部</a></li>');
       }else if(i > 0 && i%4 == 0) {
           searchChannels.append('<li></li>');
       } else {
           searchChannels.append('<li id="subChannel_'+channel.id+'"><a href="javascript:searchChannel('+channel.id+')">'+channel.name+'</a></li>');
           channelIndex++;
       }
   }
}

