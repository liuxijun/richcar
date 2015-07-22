/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-1-19
 * Time: 上午10:51
 * To change this template use File | Settings | File Templates.
 */

function renderColumnList(parentId) {
    $.ajax({
        type: "POST",
        url: "/user/frontUser!getColumnList.action",
        dataType: "text",
        data: {'channelId':parentId},
        success: function(msg){
            var result =  eval('('+msg+')');
            if(result.success == 'true') {
                $("#parentName").html(result.channelName);
                var objs = result.data;
                if(objs != null && objs.length > 0) {
                    for(var i = 0; i< objs.length; i++) {
                        var o = objs[i];
                        $("#listDiv").append('<div class="mainSec" id="'+ o.channelId+'_div">' +
                            '<div class="colHeader">' +
                            '<a href="subColumn.html?parentId='+parentId+'&channelId='+ o.channelId+'&index='+(i+1)+'" class="moreSign">更多 >></a>' +
                            '<img src="images/channel_'+ o.channelId+'.png" alt=""/>' +
                            '</div>');

                        if(i == 0) {
                            $("#"+o.channelId+"_div").append('<div class="rightColumnSec" id="'+ o.channelId+'_rightColumn"></div>');
                            if(o.channelId == 474431622) {//今日头条有子栏目，要分开处理
                                var jrttContents = o.subContents;
                                if(jrttContents != null  && jrttContents.length > 0) {
                                    for(var k = 0; k < jrttContents.length&&k<3;k++) {
                                        var content = jrttContents[k];
                                        if(content.subChannelId == 474431642) { //国内要闻
                                            var subContentList = content.subContentList;

                                            for(var x = 2; x< subContentList.length;x++) {
                                                var content_ = subContentList[x];
                                                if(x == 2) {
                                                    $("#"+ o.channelId+"_rightColumn").append('<div><h2 class="clipText" style="float: left;"><a href="subColumn.html?parentId='+o.channelId+'&channelId='+content.subChannelId +'" class="f16 fRed fHoverUnderline">国内要闻</a></h2><a href="subColumn.html?parentId=474431622&channelId=474431642" style="margin-left: 410px">更多</a></div>');
                                                    $("#"+ o.channelId+"_rightColumn").append('<ul class="UlArticles" id="'+content.subChannelId+'_right_ul"></ul>');
                                                }

                                                if(x>2) {
                                                    $("#"+content.subChannelId+"_right_ul").append('<li><a class="article" href="detail.html?id='+content_.id+'">'+content_.name+'</a></li>');
                                                }

                                            }

                                            $("#"+o.channelId+"_div").append('<div class="slideContainer" id="slideContainer_'+ o.channelId+'">' +
                                                '<div id="'+o.channelId+'_slide" class="slideShow"></div></div>');

                                            for(var n = 0; n < subContentList.length;n++) {
                                                var subContent =  subContentList[n];
                                                if(n < 3) {
                                                    var slideClass = "slideImg";
                                                     if(n == 0) {
                                                         slideClass = "slideShow";
                                                     }
                                                    $("#"+o.channelId+"_slide").append('<img class="'+slideClass+'" src="'+subContent.post1Url+'" data-link="detail.html?id='+subContent.id+'"' +
                                                        'data-title="'+subContent.name+'"/>')
                                                }
                                            }

                                            $("#slideContainer_"+ o.channelId).append('<div class="slideText"></div>' +
                                                '<div class="slideSign"></div>');

                                        }

                                        if(content.subChannelId == 474431643) {
                                            $("#"+o.channelId+"_div").append('<div class="columnHalf left" id="columnHalf"></div>');
                                            $("#columnHalf").append('<div>' +
                                                '<h2 style="float: left;"><a href="subColumn.html?parentId=474431622&channelId=474431643" class="f16 fRed fHoverUnderline">陕西要闻</a></h2>' +
                                                '<a style="margin-left: 300px" href="subColumn.html?parentId=474431622&channelId=474431643">更多</a>' +
                                                '</div>');
                                            $("#columnHalf").append('<ul class="UlArticles" id="'+content.subChannelId+'_sx"></ul>');
                                            var sxContents = content.subContentList;
                                            if(sxContents != null  && sxContents.length>0) {
                                                for(var s = 0;s<sxContents.length;s++) {
                                                    var sx = sxContents[s];
                                                    $("#"+content.subChannelId+"_sx").append('<li><a class="article" href="detail.html?id='+sx.id+'">'+sx.name+'</a></li>');
                                                }

                                            }
                                        }

                                        if(content.subChannelId == 474431644) {
                                            $("#"+o.channelId+"_div").append('<div class="columnHalf2 right" id="columnHalf2"></div>');
                                            $("#columnHalf2").append('<div>' +
                                                '<h2 style="float: left;"><a href="subColumn.html?parentId=474431622&channelId=474431644" class="f16 fRed fHoverUnderline">延安要闻</a></h2>' +
                                                '<a style="margin-left: 410px" href="subColumn.html?parentId=474431622&channelId=474431644">更多</a>' +
                                                '</div>');
                                            $("#columnHalf2").append('<ul class="UlArticles" id="'+content.subChannelId+'_ya"></ul>');
                                            var yaContents = content.subContentList;
                                            if(yaContents != null  && yaContents.length>0) {
                                                for(var y = 0;y<yaContents.length;y++) {
                                                    var ya = yaContents[y];
                                                    $("#"+content.subChannelId+"_ya").append('<li><a class="article" href="detail.html?id='+ya.id+'">'+ya.name+'</a></li>');
                                                }

                                            }
                                        }
                                    }
                                }
                            } else {
                                var contents = o.contents;
                                if(contents != null && contents.length > 0) {
                                        for(var t = 2;t<contents.length;t++) {
                                            var c = contents[t];
                                            if(t == 2) {
                                                var intro_2 = subStringIntro(c.intro);
                                                $("#"+ o.channelId+"_rightColumn").append('<h2 class="clipText"><a href="detail.html?id='+ c.id+'" class="f16 fRed fHoverUnderline">'+ c.name+'</a></h2>');
                                                $("#"+ o.channelId+"_rightColumn").append('<div class="fGray f12" style="margin-top: 10px">'+ intro_2+'</div>');
                                                $("#"+ o.channelId+"_rightColumn").append('<ul class="UlArticles" id="'+ o.channelId+'_ul"></ul>');
                                            }

                                            if(t > 2) {
                                                $("#"+ o.channelId+"_ul").append('<li><a class="article" href="detail.html?id='+c.id+'">'+c.name+'</a></li>');
                                            }
                                        }

                                    $("#"+o.channelId+"_div").append('<div class="slideContainer" id="slideContainer_'+ o.channelId+'">' +
                                        '<div id="'+o.channelId+'_slide" class="slideShow"></div></div>');

                                        for(var l = 0; l < contents.length&&l<3;l++) {
                                            var slide_c = contents[l];
                                            var slide2Class = "slideImg";
                                            if(l == 0) {
                                                slide2Class = "slideShow";
                                            }

                                            $("#"+o.channelId+"_slide").append('<img class="'+slide2Class+'" src="'+slide_c.post1Url+'" data-link="detail.html?id='+slide_c.id+'"' +
                                                'data-title="'+slide_c.name+'"/>')
                                        }

                                    $("#slideContainer_"+ o.channelId).append('<div class="slideText"></div>' +
                                        '<div class="slideSign"></div>');
                                    }
                                }
                            }

                            if(i == 1) {
                                $("#"+o.channelId+"_div").append('<div class="rightColumnSec" id="'+ o.channelId+'_rightColumn" style="min-height: 350px"></div>');
                                var contents_1 = o.contents;
                                if(contents_1 != null && contents_1.length > 0) {
                                    for(var p = 6;p < contents_1.length&&p<9;p++) {
                                        var c_1 = contents_1[p];
                                        var intro_1 = subStringIntro(c_1.intro)
                                        $("#"+ o.channelId+"_rightColumn").append('<h2 class="clipText"><a href="detail.html?id='+c_1.id+'" class="f16 fRed fHoverUnderline">'+c_1.name+'</a></h2>');
                                        $("#"+ o.channelId+"_rightColumn").append('<div class="fGray f12" style="margin-top: 10px">'+intro_1+'</div>');
                                        if(p < 8)  {
                                            $("#"+ o.channelId+"_rightColumn").append('	<hr class="grayDash"/>');
                                        }
                                    }

                                    $("#"+o.channelId+"_div").append('<div class="slideContainer" id="slideContainer_'+ o.channelId+'">' +
                                        '<div id="'+o.channelId+'_slide" class="slideShow"></div></div>');

                                    for(var a = 0; a < contents_1.length&&a < 3;a++) {
                                        var slide_a = contents_1[a];
                                        var slide3Class = "slideImg";
                                        if(l == 0) {
                                            slide3Class = "slideShow";
                                        }

                                        $("#"+o.channelId+"_slide").append('<img class="'+slide3Class+'" src="'+slide_a.post1Url+'" data-link="detail.html?id='+slide_a.id+'"' +
                                            'data-title="'+slide_a.name+'"/>')
                                    }

                                    $("#slideContainer_"+ o.channelId).append('<div class="slideText"></div>' +
                                        '<div class="slideSign"></div>');


                                    $("#"+o.channelId+"_div").append('<ul style="max-width: 400px" class="UlArticles" id="UlArticles_'+ o.channelId+'">');
                                    for(var b =3;b< contents_1.length&&b < 6;b++) {
                                        var cb = contents_1[b];
                                        $("#UlArticles_"+ o.channelId).append('<li><a class="f14 article" href="detail.html.id='+cb.id+'">'+cb.name+'</a></li>');
                                    }
                                }
                            }

                            if(i == 2) {
                                $("#"+o.channelId+"_div").append('<div class="rightColumnSec" id="'+ o.channelId+'_rightColumn"></div>');
                                var contents_2 = o.contents;
                                if(contents_2 != null && contents_2.length>0) {
                                    for(var d =3;d < contents_2.length;d++) {
                                        var cd = contents_2[d];
                                        if(d == 3) {
                                            var intro_3 = subStringIntro(cd.intro);
                                            $("#"+ o.channelId+"_rightColumn").append('<h2 class="clipText"><a href="detail.html?id='+cd.id+'" class="f16 fRed fHoverUnderline">'+cd.name+'</a></h2>');
                                            $("#"+ o.channelId+"_rightColumn").append('<div class="fGray f12" style="margin-top: 10px">'+intro_3+'</div>');
                                            $("#"+ o.channelId+"_rightColumn").append('<ul class="UlArticles" id="UlArticles_'+ o.channelId+'"></ul>');
                                        }

                                        if(d>3) {
                                            $("#UlArticles_"+ o.channelId).append('<li><a class="article" href="detail.html.id='+cd.id+'">'+cd.name+'</a></li>');
                                        }
                                    }

                                    $("#"+o.channelId+"_div").append('<div class="slideContainer" id="slideContainer_'+ o.channelId+'">' +
                                        '<div id="'+o.channelId+'_slide" class="slideShow"></div></div>');
                                    for(var e = 0; e < contents_2.length&&e<3;e++) {
                                        var slide_e = contents_2[e];
                                        var slide4Class = "slideImg";
                                        if(l == 0) {
                                            slide4Class = "slideShow";
                                        }

                                        $("#"+o.channelId+"_slide").append('<img class="'+slide4Class+'" src="'+slide_e.post1Url+'" data-link="detail.html?id='+slide_e.id+'"' +
                                            'data-title="'+slide_e.name+'"/>')
                                    }

                                    $("#slideContainer_"+ o.channelId).append('<div class="slideText"></div>' +
                                        '<div class="slideSign"></div>');
                                }
                            }

                            if(i == 3) {
                                $("#"+o.channelId+"_div").append('<div class="rightColumnSec" style="min-height: 280px" id="'+ o.channelId+'_rightColumn"></div>');
                                var contents_3 = o.contents;
                                if(contents_3 != null && contents_3.length > 0) {
                                    for(var f = 3;f<contents_3.length&&f<5;f++) {
                                        var cf = contents_3[f];
                                        var intro_4 = subStringIntro(cf.intro);
                                        $("#"+ o.channelId+"_rightColumn").append('<h2 class="clipText"><a href="detail.html?id='+cf.id+'" class="f16 fRed fHoverUnderline">'+cf.name+'</a></h2>');
                                        $("#"+ o.channelId+"_rightColumn").append('<div class="fGray f12" style="margin-top: 10px">'+intro_4+'</div>');
                                        if(f < 4) {
                                            $("#"+ o.channelId+"_rightColumn").append('<hr class="grayDash"/>');
                                        }
                                    }

                                    $("#"+o.channelId+"_div").append('<div class="slideContainer" id="slideContainer_'+ o.channelId+'">' +
                                        '<div id="'+o.channelId+'_slide" class="slideShow"></div></div>');
                                    for(var g =0;g<contents_3.length&&g<3;g++) {
                                        var slide_g = contents_3[g];
                                        var slide5Class = "slideImg";
                                        if(l == 0) {
                                            slide5Class = "slideShow";
                                        }

                                        $("#"+o.channelId+"_slide").append('<img class="'+slide5Class+'" src="'+slide_g.post1Url+'" data-link="detail.html?id='+slide_g.id+'"' +
                                            'data-title="'+slide_g.name+'"/>')
                                    }

                                    $("#slideContainer_"+ o.channelId).append('<div class="slideText"></div>' +
                                        '<div class="slideSign"></div>');

                                }
                            }


                            if(i == 4) {
                                $("#"+o.channelId+"_div").append('<div class="rightColumnSec" id="'+ o.channelId+'_rightColumn"></div>');
                                var contents_4 = o.contents;
                                if(contents_4 != null && contents_4.length>0) {
                                    for(var h =3;h < contents_4.length;h++) {
                                        var ch = contents_4[h];
                                        if(h == 3) {
                                            var intro_5 = subStringIntro(ch.intro);
                                            $("#"+ o.channelId+"_rightColumn").append('<h2 class="clipText"><a href="detail.html?id='+ch.id+'" class="f16 fRed fHoverUnderline">'+ch.name+'</a></h2>');
                                            $("#"+ o.channelId+"_rightColumn").append('<div class="fGray f12" style="margin-top: 10px">'+intro_5+'</div>');
                                            $("#"+ o.channelId+"_rightColumn").append('<ul class="UlArticles" id="UlArticles_'+ o.channelId+'"></ul>');
                                        }

                                        if(h>3) {
                                            $("#UlArticles_"+ o.channelId).append('<li><a class="article" href="detail.html.id='+ch.id+'">'+ch.name+'</a></li>');
                                        }
                                    }

                                    $("#"+o.channelId+"_div").append('<div class="slideContainer" id="slideContainer_'+ o.channelId+'">' +
                                        '<div id="'+o.channelId+'_slide" class="slideShow"></div></div>');
                                    for(var j = 0; e < contents_4.length&&j<3;j++) {
                                        var slide_j = contents_4[j];
                                        var slide6Class = "slideImg";
                                        if(l == 0) {
                                            slide6Class = "slideShow";
                                        }

                                        $("#"+o.channelId+"_slide").append('<img class="'+slide6Class+'" src="'+slide_j.post1Url+'" data-link="detail.html?id='+slide_j.id+'"' +
                                            'data-title="'+slide_j.name+'"/>')
                                    }

                                    $("#slideContainer_"+ o.channelId).append('<div class="slideText"></div>' +
                                        '<div class="slideSign"></div>');
                                }
                            }
                            $("#"+ o.channelId+"_div").append('<div class="clear"></div>');

                        }
                    }
                }
                loadSlide();
            }
    });
}


function subStringIntro(str) {
    if(str.length > 100) {
        str = str.substring(0,97)+"...";
    }
    return str;
}


