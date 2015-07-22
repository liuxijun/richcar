/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-1-19
 * Time: 上午10:51
 * To change this template use File | Settings | File Templates.
 */

function loadZSZT(){
    $.ajax({
        type:"POST",
        url:"/content/content!getContentsOfChannelIds.action",
        dataType:"text",
        data:{'channelIds':"474431647,474431648,474431649,474431650,474431651,474431652"} ,
        success:function(msg){
            var result = eval('('+msg+')');
            if(result.success == 'true'){
                var objs = result.data;

                var jcsp = objs['474431647'];
                var qgyj_jcsp = $("#jcsp")
                if(jcsp != null)  {
                    for(var i=0;i<jcsp.length;i++){
                        var  c = jcsp[i];
                        qgyj_jcsp.append('<li><a href="'+ c.property5+'" target="_blank">'+
                                '<img src="'+ c.post1Url+'" alt=""/>'+
                                '<h3 class="clipText">'+c.name+'</h3>'+
                                '<span class="fGray">'+c.intro.substring(0,30)+'</span>'+
                                '</a></li>');
                    }

                }
                var zbsp = objs['474431648'];
                var qgyj_zbsp = $("#qgyj_zbsp")
                if(zbsp != null)  {
                    for(var i=0;i<zbsp.length;i++){
                        var  c = zbsp[i];
                        qgyj_zbsp.append('<li><a href="'+ c.property5+'" target="_blank">'+
                            '<img src="'+ c.post1Url+'" alt=""/>'+
                            '<h3 class="clipText">'+c.name+'</h3>'+
                            '<span class="fGray">'+c.intro.substring(0,30)+'</span>'+
                            '</a></li>');
                    }

                }
                var jpkj = objs['474431649'];
                var qgyj_jpkj = $("#sxyj_jpkj")
                if(jpkj != null)  {
                    for(var i=0;i<jpkj.length;i++){
                        var  c = jpkj[i];
                        qgyj_jpkj.append('<li><a href="'+ c.property5+'" target="_blank">'+
                            '<img src="'+ c.post1Url+'" alt=""/>'+
                            '<h3 class="clipText">'+c.name+'</h3>'+
                            '<span class="fGray">'+c.intro.substring(0,30)+'</span>'+
                            '</a></li>');
                    }

                }

                var gcd = objs['474431650'];
                var gcd_dslm = $("#gcd_dslm")
                if(gcd != null)  {
                    for(var i=0;i<gcd.length;i++){
                        var  c = gcd[i];
                        gcd_dslm.append('<li><a href="'+ c.property5+'" target="_blank">'+
                            '<img src="'+ c.post1Url+'" alt=""/>'+
                            '<h3 class="clipText">'+c.name+'</h3>'+
                            '<span class="fGray">'+c.intro.substring(0,30)+'</span>'+
                            '</a></li>');
                    }

                }
                var stya = objs['474431651'];
                var yayj_stya = $("#yayj_stya")
                if(stya != null)  {
                    for(var i=0;i<stya.length;i++){
                        var  c = stya[i];
                        yayj_stya.append('<li><a href="'+ c.property5+'" target="_blank">'+
                            '<img src="'+ c.post1Url+'" alt=""/>'+
                            '<h3 class="clipText">'+c.name+'</h3>'+
                            '<span class="fGray">'+c.intro.substring(0,30)+'</span>'+
                            '</a></li>');
                    }

                }

                var jdysj = objs['474431652'];
                var jdysj_UI = $("#jdysj")
                if(jdysj!= null)  {
                    for(var i=0;i<jdysj.length;i++){
                        var  c = jdysj[i];
                        jdysj_UI.append('<li><a href="'+ c.property5+'" target="_blank">'+
                            '<img src="'+ c.post1Url+'" alt=""/>'+
                            '<h3 class="clipText">'+c.name+'</h3>'+
                            '<span class="fGray">'+c.intro.substring(0,30)+'</span>'+
                            '</a></li>');
                    }

                }
            }
        }
    });
}