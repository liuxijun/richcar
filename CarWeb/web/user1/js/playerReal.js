                function outStates(outMsg){
                   //statsInfo.innerHTML = '<font color="white" style="font-size:9pt">'+ outMsg + '</font>';
                }
                function play(){
                  realImage.DoPlay();
                //  setButtonImg(3);
                }
                function pause(){
                  realImage.DoPause();
               // setButtonImg(2);
                }
                function fastback(){
                //realImage.fastReverse()

                }
                function fastforward(){
                //realImage.fastForward()

                }
                function stop(){
                  realImage.DoStop();
               // setButtonImg(1);

                }
                function lowwer(){
                  realImage.SetVolume(realImage.GetVolume() - 5);
                  outStates('音量：'+realImage.GetVolume());
                }
                function upper(){
                  realImage.SetVolume(realImage.GetVolume() + 5);
                  outStates('音量：'+realImage.GetVolume());
                }
                var repeatTimesByCookies = 0;
                function playMedia(){
                    //window.status = program.playState;
                    //window.status = realImage.getPlayState();
                    if(realImage.GetPlayState()==3){
                         hasBeenStarted = true;
                         if(!diaplaySizeChanged){
                            diaplaySizeChanged = true;
                            changeDisplaySize(1);
                         }
                         repeatTimesByCookies ++;
                         if(repeatTimesByCookies >=5){
                             repeatTimesByCookies = 0;
                             var now = new Date()
                             fixDate(now)
                             now.setTime(now.getTime() + 365 * 24 * 60 * 60 * 1000)
                             setCookie(getUrlCookieName(getPlayerUrl()),
                                       parseInt(getCurrentPosition()),
                                       now);
                         }
                         if(SetPosStart && trackball ){
                             newleft=trackball.style.pixelLeft
                             //alert(newleft);
                             rcp=newleft * getDuration() / movelen;
                             outStates(CalculateTime(rcp,true) + "/" + CalculateTime(getDuration(),true));
                         }else{
                             showPos();
                             outStates(CalculateTime(getCurrentPosition(),true) + "/" + CalculateTime(getDuration(),true));
                         }
                    }else if(realImage.GetPlayState() == 6){ //buffering
                         hasBeenStarted = true;
                         showPos();
                         var msg = realImage.GetImageStatus();
                         outStates(msg);
                    }else{
                         var msg = realImage.GetImageStatus();
                         outStates(msg);
                    }
                    if(! ShouldPlayMovie){
                        return;
                    }
                    if(realImage.GetPlayState()==10 && (!hasBeenStarted)){
                        if(tryTimes >=3){
                            outStates('服务器忙');
                            alert('服务器忙，请稍后再试！');
                            window.setTimeout("playMedia()",5000);
                            refreshPage();
                            ShouldPlayMovie = false;
                            return;
                        }else{
                            realImage.DoPlay();
                            tryTimes ++;
                        }
                    }
                    window.setTimeout("playMedia()",1000);
                }
                function changeDisplaySize(newSize){
                    if(realImage.GetPlayState()!=3){
//                       alert("只有在播放影片状态下才能修改显示模式！");
                       return;
                    }
                    if((realImage.GetClipWidth())||(realImage.GetClipHeight())){
                        switch(newSize){
                           case 1:
                               newHeight = realImage.GetClipHeight()+HeightBorder;
                               newWidth  = realImage.GetClipWidth()+WidthBorder;
                               window.resizeTo(newWidth,newHeight);
                               break;
                           case 2:
                               newHeight = realImage.GetClipHeight()*2+HeightBorder;
                               newWidth  = realImage.GetClipWidth()*2+WidthBorder;
                               window.resizeTo(newWidth,newHeight);
                               break;
                           case 3:
                               btnfullscr_onclick();
                               break;
                           default:
                               alert("看不懂的参数，请输入我能理解的参数:1(100%),2(200%),3(全屏)");
                               break;
                        }
                    }
                }
                function btnfullscr_onclick() {
                    //program.DisplaySize=3;
                    if(realImage.GetPlayState()!=3){
                       alert("只有在播放影片状态下才能修改显示模式！");
                       return;
                    }
                    realImage.SetFullScreen();

                }
                function btnChangeMode_onclick(newMode){

                }
                function setPlayerPosition(lastTime){
                    realImage.SetPosition( lastTime * 1000 );
                }
                function getPlayerUrl(){
                    return realImage.Source;
                }
                function getCurrentPosition(){
                    return realImage.GetPosition()/1000;
                }
                function getDuration(){
                    return realImage.GetLength()/1000;
                }
