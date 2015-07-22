/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-1-19
 * Time: 上午10:51
 * To change this template use File | Settings | File Templates.
 */

function loadIndexList() {

    $.ajax({
        type: "POST",
        url: "/user/frontUser!getIndexList.action",
        dataType: "text",
        data: {'channelIds':"474431622,474431627,474431638"},
        success: function(msg){
            var result =  eval('('+msg+')');
            if(result.success == 'true') {
                var objs = result.data;
                if(objs != null && objs.length > 0) {
                    loadOk = true;
                    for(var i = 0; i< objs.length; i++) {
                        var o = objs[i];
                        if(o.channelId == 474431622) {
                             renderDjdt(o.contents, o.channelId, o.subContents)
                        }

                        if(o.channelId == 474431627) {
                            renderYjzc(o.contents, o.channelId)
                        }

                        if(o.channelId == 474431638) {
                            renderXzzq(o.contents, o.channelId)
                        }
                    }
                }
            }
        }
    });
}

function renderDjdt(objs,channelId,subObjs) {
    /*if(objs != null && objs.length > 0) {
        for(var i = 0;i<objs.length&&i<3;i++) {
            var item = objs[i];
            document.getElementById(channelId+'_'+(i+1)).setAttribute('data-link',"detail.html?id="+item.id);
            document.getElementById(channelId+'_'+(i+1)).src = item.post1Url;
            document.getElementById(channelId+'_'+(i+1)).setAttribute('data-title',item.name);
        }
    }*/

    if(subObjs != null && subObjs.length > 0) {
        for(var k = 0;k<subObjs.length&&k<3;k++) {
            var subItem = subObjs[k];
            var contents = subItem.subContentList;
            if(contents != null  && contents.length > 0) {
                var indexCount = 0;
                for(var n = 0;n < contents.length;n++) {
                    var c = contents[n];
                    if(subItem.subChannelId == 474431642) {  //如果是国内要闻
                        if(n < 3) {
                            document.getElementById(channelId+'_'+(n+1)).setAttribute('data-link',"detail.html?id="+c.id);
                            document.getElementById(channelId+'_'+(n+1)).src = c.post1Url;
                            document.getElementById(channelId+'_'+(n+1)).setAttribute('data-title',c.name);
                        }


                        $("#"+ subItem.subChannelId+"_news").append(' <li><a class="article" href="detail.html?id='+ c.id+'" target="_blank">'+ c.name+'</a></li>');


                        if(indexCount > 5) {
                            break;
                        }
                    }

                    if(subItem.subChannelId == 474431644 || subItem.subChannelId == 474431643) {  //如果是陕西或者延安要闻
                        $("#"+ subItem.subChannelId+"_news").append('<li><a class="article" href="detail.html?id='+ c.id+'">'+ c.name+'</a></li>');

                        if(indexCount > 1) {
                            break;
                        }
                    }
                    indexCount++;

                }
            }
        }
    }

}


function renderYjzc(objs,channelId) {
    if(objs != null && objs.length > 0) {
        var indexCount = 0;
        for(var i = 0; i < objs.length;i++) {
            var c = objs[i];
            if(indexCount == 0) {
                document.getElementById(channelId+"_pic").src= c.post1Url;
                document.getElementById(channelId+"_pic_title").innerHTML= c.name;
                document.getElementById(channelId+"_pic_href").href= 'detail.html?id='+c.id;
            }

            if( indexCount < 18) {
                /* $("#"+channelId+"_ul_2").append('<li><a href="detail.html?id='+ c.id+'">' +
                 ' <img src="'+ c.post1Url+'" class="rpFrameMini" alt=""/>' +
                 '<img src="images/play.gif" class="rpFramePlayIco" alt=""/>' +
                 '<p class="vodTitleMini">'+ c.name +'</p></a></li> ')*/
                $("#"+channelId+"_ul_2").append('<li><a class="article" href="detail.html?id='+ c.id+'" target="_blank">'+ c.name+'</a></li>');
            }

            if(indexCount > 17 & indexCount < 21) {
                $("#"+channelId+"_ul_1").append('<li><span class="fRed">[最新]</span> <a class="article" href="detail.html?id='+ c.id+'">'+c.name+'</a></li>')
            }


            indexCount++;
        }
    }

}

function renderXzzq(objs,channelId) {
    if(objs != null && objs.length > 0) {
        var indexCount = 0;
        for(var i = 0; i < objs.length;i++) {
            var c = objs[i];
            if(indexCount == 0) {
                document.getElementById(channelId+"_pic").src= c.post1Url;
                document.getElementById(channelId+"_pic_title").innerHTML= c.name;
                document.getElementById(channelId+"_pic_href").href="javascript:goToDownLoad("+ c.id+")";
            }

            if( indexCount < 15) {
                /*$("#"+channelId+"_ul_2").append('<li><a href="javascript:goToDownLoad('+ c.id+')">' +
                 ' <img src="'+ c.post1Url+'" class="rpFrameMini" alt=""/>' +
                 '<img src="images/play.gif" class="rpFramePlayIco" alt=""/>' +
                 '<p class="vodTitleMini">'+ c.name +'</p></a></li> ')*/
                $("#"+channelId+"_ul_2").append('<li><a class="article" href="javascript:goToDownLoad('+ c.id+')" target="_blank">'+ c.name+'</a></li>');
            }

            if(indexCount > 14 && indexCount < 18) {
                $("#"+channelId+"_ul_1").append('<li><span class="fRed">[最新]</span> <a class="article" href="javascript:goToDownLoad('+ c.id+')">'+c.name+'</a></li>')
            }


            indexCount++;
        }
    }
}


var totalTicketCount=0;
var maxOption=1;
function loadVote(id, callback){
    $.ajax({
        type: "POST",
        url: "/vote/vote!voteStat.action",
        data: {"obj.id": id},
        dataType: "text",
        success: function(msg){
            var jsonData = JSON.parse(msg);
            if(jsonData !=null && jsonData !='') {
                var voteOptions = [];
                var optionList = jsonData["optionList"];
                totalTicketCount = jsonData["totalTicketCount"];
                for(var i=0; i<optionList.length; i++){
                    voteOptions.push(new Option(optionList[i].id, optionList[i].title,optionList[i].ticketCount));
                }
                g_vote = new Vote(jsonData["id"], jsonData["title"], voteOptions, jsonData["maxOption"]);
                maxOption = jsonData["maxOption"];
                if(callback){
                    callback(g_vote);
                }
            }
        }
    });
}

function  showVote(vote){
    if(!vote) return;

    $("#voteTitle").html(vote.title);
    if(parseInt(vote.maxOption) > 1){
        $("#voteTitle").append("(最多选择" + vote.maxOption + "项)");
    }

    for(var i=0; i<vote.optionList.length; i++){
        $("#tbVote").append('<tr>' +
            '<td>'+vote.optionList[i].title+'</td>' +
            '<td><div style="width: '+getPercent(vote.optionList[i].ticketCount,1)+'%;height: 8px;background-color:'+getBackgroundColor(i)+'"></div>' +
            '<span style="margin-top: 3px;display: block;">'+vote.optionList[i].ticketCount+'票 ('+getPercent(vote.optionList[i].ticketCount,2)+'%)</span></td>' +
            '</tr>');
    }

    $("#totalTicketCount").html("总投票：（"+totalTicketCount+"票）");
    document.getElementById("goVote").href='vote.html?id='+vote.id;
}

function getPercent(ticketCount,displayType) {
    var percent = ticketCount/totalTicketCount;
    if(totalTicketCount < 1) {
        percent = 0;
    }
    if(displayType == 1 && percent == 0) {
        return  1;
    }
    return Math.round(percent*1000)/10;
}

function getBackgroundColor(index) {
    switch(index%4){
        case 0:
            return "#87AF47";
        case 1:
            return "#EE722E";
        case 2:
            return "#F8B606";
        case 3:
            return "#934120";
    }
    return "#87AF47";
}

function Vote(id, title, optionList, maxOption){
    this.id = id;
    this.title = title;
    this.optionList = optionList;
    this.maxOption = maxOption;
}

function Option(id, title,ticketCount){
    this.id = id;
    this.title = title;
    this.ticketCount = ticketCount;
}


function renderNotice() {
    $.ajax({
        type: "POST",
        url: "js/list.jsp",
        dataType: "text",
        data: {'channelId':474431645},
        success: function(msg){
            var response = eval('('+msg+')');
            var notices = response.listData.objs;
            for(var i = 0; i < notices.length;i++) {
                var n  = notices[i];
                if(i == 0) {
                    $('#notice').append('<li><a class="f16 fRed" href="detail.html?id='+ n.id+'">'+ n.name+'</a></li>')
                }else if(i < 8) {
                    $('#notice').append('	<li><a class="article f14" href="detail.html?id='+ n.id+'">'+ n.name+'</a></li>')
                }

            }
        }
    });
}