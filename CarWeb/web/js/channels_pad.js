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
    leftChannelCls:'.list_item ul',
    bottomChannelCls:'.qlink ul',
    renderTopLevelChannel:function(channels){
        var areas = [this.topChannelCls,'top',this.leftChannelCls,'left',this.bottomChannelCls,'bottom'];
        for(var i= 0,l=areas.length;i<l;i+=2){
            var id = areas[i];
            var eleId = areas[i+1];
            var subChannels = $(id);
            if(subChannels==null){
                continue;
            }
            subChannels.html('');
            if(id!='.list_item ul'){
                subChannels.append('<li><a href="index_pad.html"><em>首页</em></a></li>');
            }
            $.each(channels,function(){
                var c = this;
                subChannels.append('<li id="' +eleId+
                    'Channel_' + c.id+
                    '"><a href="javascript:renderChannel(' + c.id+
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

function renderChannel(id){
    if(window.location.href.indexOf("list_pad.html")<0){
        window.location.href = "list_pad.html?parentId="+id;
        return;
    }
    var selected = $(".item_over");
    $.each(selected,function(){
        var id = $(this).tagName;
        $(this).removeClass("item_over");
    });
    $("#topChannel_"+id).closest("li").addClass("item_over");
    $("#leftChannel_"+id).closest("li").addClass("item_over");
    appendJs(serverOptions.address+'page/js/channels.jsp?id=' +id+
        '&callback=doRenderChannel');
    searchData.channelId = channelId;
    //goToPage(1);
}

function doRenderChannel(jsonData){
    var subChannels = $(".channel_list ul");
    subChannels.html('');
    subChannels.append('<label>频道</label>');
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