                function outStates(outMsg){
                   if(statsInfo){
                       statsInfo.innerHTML = '<font color="white" style="font-size:9pt">'+ outMsg + '</font>';
                   }
                }
                function play(){
                  program.controls.play();
                //  setButtonImg(3);
                }
                function pause(){
                program.controls.pause();
               // setButtonImg(2);
                }
                function fastback(){
                program.controls.fastReverse()

                }
                function fastforward(){
                program.controls.fastForward()

                }
                function stop(){
                program.controls.stop();
               // setButtonImg(1);

                }
                function lowwer(){
                  program.settings.volume = program.settings.volume - 5;
                  outStates('音量：'+program.settings.volume);
                }
                function upper(){
                  program.settings.volume = program.settings.volume + 5;
                  outStates('音量：'+program.settings.volume);
                }
                var repeatTimesByCookies = 0;
                function playMedia(){
                    //window.status = program.playState;
                    if(program.playState==3){
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
                             setCookie(getUrlCookieName(program.URL),
                                       parseInt(program.controls.currentPosition),
                                       now);
                             //alert("cooki seted:"+getUrlCookieName(program.URL)+":"+program.controls.currentPosition);
                         }
                         if(SetPosStart && trackball){
                             newleft=trackball.style.pixelLeft
                             //alert(newleft);
                             rcp=newleft * program.currentMedia.duration / movelen;
                             outStates(CalculateTime(rcp,true) + "/" + program.currentMedia.durationString);
                         }else{
                             showPos();
                             outStates(program.controls.currentPositionString + "/" + program.currentMedia.durationString);
                         }
                    }else if(program.playState == 6){ //buffering
                         hasBeenStarted = true;
                         showPos();
                         var msg = program.status;
                         outStates(msg);
                    }else{
                         var msg = program.status;
	                     outStates(msg);
                    }
                    if(! ShouldPlayMovie){
                        return;
                    }
                    if(program.playState==10 && (!hasBeenStarted)){
                        if(tryTimes >=4){
                            outStates('服务器忙');

                            alert('服务器忙，请稍后再试！');
                            window.setTimeout("playMedia()",5000);
                            refreshPage();
                            ShouldPlayMovie = false;
                            return;
                        }else if(tryTimes==2){
                            var nowMediaUrl = program.URL;//.currentMedia.sourceUrl;
                            program.URL = nowMediaUrl +"&hardcache=1&time="+(new Date()).getTime();
                            program.controls.play();
                            tryTimes ++;
                        }else{
                            program.controls.play();
                            tryTimes ++;
                        }
                    }
                    window.setTimeout("playMedia()",1000);
                }
                function changeDisplaySize(newSize){
                    if(program.currentMedia == null){
                       alert("只有在播放影片状态下才能修改显示模式！");
                       return;
                    }
                    if((program.currentMedia.imageSourceHeight)||(program.currentMedia.imageSourceWidth)){
                        switch(newSize){
                           case 1:
                               newHeight = program.currentMedia.imageSourceHeight+HeightBorder;
                               newWidth  = program.currentMedia.imageSourceWidth+WidthBorder;
                               window.resizeTo(newWidth,newHeight);
                               break;
                           case 2:
                               newHeight = program.currentMedia.imageSourceHeight*2+HeightBorder;
                               newWidth  = program.currentMedia.imageSourceWidth*2+WidthBorder;
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
                    if(program.playState != 3){
                       alert("只有在播放影片状态下才能修改显示模式！");
                       return;
                    }
                    program.fullScreen = true;
                }
                function btnChangeMode_onclick(newMode){

                }
                function setPlayerPosition(lastTime){
                    program.controls.currentPosition = lastTime;
                }
                function getPlayerUrl(){
                    return program.URL;
                }
                function getCurrentPosition(){
                    return program.controls.currentPosition;
                }
                function getDuration(){
                    return program.currentMedia.duration;
                }