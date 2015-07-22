                var ShouldPlayMovie = true;
                var tryTimes = 0;



                var imgPlaySrc;
                var imgStopSrc;
                var imgPauseSrc;
                var imgPath = "../../images/player/";
                var WidthBorder=134;
                var HeightBorder=101;
                var diaplaySizeChanged = false;
                var hasBeenStarted = false;
                var errorMsg = "您目前无法正常播放此节目，可能是以下原因造成的：\n"+
                                " 1.请确认您终端的媒体播放器安装配置是否正确，目前平台\n"+
                                "   需要安装MEDIA PLAYER 9.0版本播放器。\n"+
                                " 2.请检查您本机或网络中的防火墙是否组织正常访问流媒体服务。\n"+
                                " 3.目前服务器允许访问免费节目的连接数已经达到最大上限，请稍后再试。\n"+
                                " 4.请检查你的网络连接是否正常通畅。\n"+
                                "谢谢！！";
                function getUrlCookieName(Url){
                   var splitStr = "media_id=";
                   var posOfDot = Url.indexOf(splitStr);
                   if(posOfDot>=0){
                      Url = Url.substring(posOfDot+splitStr.length);
                   }
                   splitStr = "mu=";
                   posOfDot = Url.indexOf(splitStr);
                   if(posOfDot>=0){
                      Url = Url.substring(0, posOfDot);
                   }
                   var Result = "";
                   for(var i=0;i<Url.length;i++){
                      var ch = Url.charAt(i);
                      switch(ch)
                      {
                         case '=':
                         case '.':
                         case '&':
                         case '/':
                         case '\\':
                         case '?':
                         case ':':
                         case '%':
                         case ' ':
                             Result = Result + '_';
                             break;
                         default:
                             if(ch>='0'&&ch<='9'){
                                Result = Result + ch;
                             }
                             break;
                      }
                   }
                   return Result;
                }
                function CalculateTime(SecondNumber,simpleMethod){
                  var l,j=0,i = parseInt(SecondNumber);
                  //alert(i);
                  var Result = "";
                  var TimeSpe = new Array;
                  TimeSpe[0]=60;
                  TimeSpe[1]=60;
                  TimeSpe[2]=24;
                  TimeSpe[3]=30;
                  TimeSpe[4]=12;
                  var TimeDot = new Array;
                  if(simpleMethod){
                      TimeDot[0]="";
                      TimeDot[1]=":";
                      TimeDot[2]=":";
                      TimeDot[3]="d ";
                      TimeDot[4]="m";
                  }else{
                      TimeDot[0]="秒";
                      TimeDot[1]="分";
                      TimeDot[2]="时";
                      TimeDot[3]="天";
                      TimeDot[4]="月";
                  }
                  while(i>=1){
                     l = i % TimeSpe[j];
                     i = i / TimeSpe[j];
                     Result = parseInt(l)+TimeDot[j]+Result;
                     j ++;
                  }
                  return Result;
                }
                function refreshPage(){

                }
                function expand() {
                    window.moveTo(0,0);
                    window.resizeTo(516, 381);
                    changeDisplaySize(1);
                    var mediaUrl = getPlayerUrl();
                    var lastTime = getCookie(getUrlCookieName(mediaUrl));
                    if(lastTime){
                        if(confirm("您上次观看到"+CalculateTime(parseInt(lastTime))+"，您想继续上次的时间观看吗？")){
                             //gotoTime = lastTime;
                             //program.controls.currentPosition = lastTime;
                             setPlayerPosition(lastTime);
                        }
                    }
                    playMedia();
                }
                function resized(){
                   if(videoDisplay.height){
                     videoDisplay.height = document.body.offsetHeight - 50;
                   }
                }
                //关于播放器进度条
                var CanSetPos = true;
                var SetPosStart = false;
                var SetVolStart = false;

                var timelen=1000;
                var orileft=0;
                var timesnum=100;
                var rm_tollen=0;
                var orix=0;


                var moveon=false;
                var movelen=183-30;//movie bar length=90,扣除block宽度
                function moves()//ok
                {
                    if(CanSetPos&&event.button!=2)
                    {
//                        outStates("move");
//                        DoPlayPause();
                        orix=event.x;
                        //orileft=trackball.style.pixelLeft;
                        if(moveon)
                        {
//                            clearTimeout(mfollow);
                        }
                        SetPosStart=true;
                    }
                    else
                    {
                        return false;
                    }
                }
                function movego()
                {
                    //outStates("move_go");
                    if(SetPosStart)
                    {
                        newx=event.x;
                        disx=newx-orix;
                        newleft=orileft+disx;
                        if((newleft>-1)&&(newleft<movelen))
                        {
                            //trackball.style.left=newleft;
                            rcp=newleft * getDuration() / movelen;
                            outStates(CalculateTime(rcp,true) + "/" + CalculateTime(getDuration(),true));
                        }else{
//                           outStates("Can't move");
                        }
                    }
                }
                function Total_Up()//ok
                {
                    //outStates("In Total_Up");
                      if(SetPosStart)	setpos();
                //        if(SetVolStart) setvol();
                }
                function setpos()
                {
                    SetPosStart=false
                    newleft=100;
                    //newleft=trackball.style.pixelLeft
                    //alert(newleft);
                    rcp=newleft * getDuration() / movelen;
                    //alert(rcp);
                    setPlayerPosition( rcp );
                }
                function showPos(){
                   if(SetPosStart){
                      return;
                   }
//                   alert("will show pos?");
                   if(null == trackball){
                      return;
                   }
                   var blockpos=movelen* getCurrentPosition()/getDuration();
                   //trackball.style.pixelLeft=blockpos;
                }

                function show_o(aImg){
                aImg.src = getOverImgSrc(aImg.src);
                }
                function show_n(aImg){
                aImg.src = getNormalImgSrc(aImg.src);
                }
                function getOverImgSrc(fileName){
                    //是不是已经是Over图片了？
                    if(isOverPic(fileName)){
                      return fileName;
                    }
                    posOfDot = fileName.indexOf(".gif");
                    if(posOfDot<0){
                      posOfDot = fileName.indexOf(".jpg");
                    }
                    if(posOfDot<0){
                       return fileName;
                    }
                    result = fileName.substring(0,posOfDot);
                    result = result + "_o"+fileName.substring(posOfDot);
                    return result;
                }
                function getNormalImgSrc(fileName){
                  if(!isOverPic(fileName)){
                     return fileName;
                  }
                  var posOfDot = -1;
                  var posOfDot = fileName.indexOf("_o.gif");
                  if(posOfDot<0){
                      posOfDot = fileName.indexOf("_o.jpg");
                  }
                  if(posOfDot<0){
                     return fileName;
                  }
                  var result = fileName.substring(0,posOfDot);
                  result = result + fileName.substring(posOfDot+2);
                  return result;
                }
                function isOverPic(fileName){
                    var posOfDot = fileName.indexOf("_o.gif");
                    if(posOfDot<0){
                       posOfDot = fileName.indexOf("_o.jpg");
                    }
                    if(posOfDot >= 0){
                       return true;
                    }
                    return false;
                }
