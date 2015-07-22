var wmvStr = '<object id="player" classid="CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6" type="application/x-oleobject" width="950" height="510"> <param name="autoStart" value="false"> <param name="balance" value="0"> <param name="currentPosition" value="0"> <param name="currentMarker" value="0"> <param name="enableContextMenu" value="true"> <param name="enableErrorDialogs" value="false"> <param name="enabled" value="true"> <param name="fullScreen" value="false"> <param name="invokeURLs" value="false"> <param name="mute" value="true"> <param name="playCount" value="1"> <!--<param name="AUTOSIZE" value="true">--> <param name="rate" value="1"> <param name="uiMode" value="none"> <param name="volume" value="100"> </object>';
var mp4Str = '<object id="stmpeer" codebase="WebStreamPeer.cab#version=1,0,8,90" type="application/x-oleobject" width="950" height="510" standby="loading WebStreamPeer components..." classid="CLSID:43F30492-8015-4E23-AAD9-9292A56A629E"> <param name="uiMode" value="none"> <param name="AUTOSIZE" value="true"> <param name="stretchToFit" value="true"> <param name="WindowlessVideo" value="0"> <!--<param name="url" value="rtsp://192.168.1.158/movies/fire.mp4">--> </object>';
var flvStr = '<object id="flvplayer" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0" width="950" height="510"> <param name="movie" value="style/Flvplayer.swf" /> <param name="quality" value="high" /> <param name="allowFullScreen" value="true" /> <param name="FlashVars" value="vcastr_file=http://localhost:8080/fortuneplayer/video/01.flv&LogoText=fortune－net.cn&BufferTime=3" /> <embed src="style/Flvplayer.swf" allowfullscreen="true" flashvars="vcastr_file=http://localhost:8080/fortuneplayer/video/01.flv&LogoText=fortune－net.cn" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="950" height="510"></embed> </object>';
var html5Str = '<video id="moviePlayer" width="640" height="360" controls autoplay></video>';
var moveLength = 600 - 30;
//for apple IOS
var HTML5Player = {
    setPosStart:false,
    init:function (playerId, playerInfoId, currentTimeId, totalTimeId, mediaUrl, autoStart) {
        //alert("hello");
        this.player = document.getElementById(playerId);
        this.playerInfo = document.getElementById(playerInfoId);
        this.currentTime = document.getElementById(currentTimeId);
        this.totalTime = document.getElementById(totalTimeId);
        this.player.src = mediaUrl;
    },
    play:function () {
        if (player) {
            player.play();
        }
    },
    pause:function () {
        if (player) {
            player.pause();
        }
    },
    pausePlay:function () {
        if(player!=null){
            if (!player.paused) {
                player.pause();
            }else{
                player.play();
            }
        }else{
        }
    },
    stop:function () {
        if (player) {
            player.stop();
        }
    },
    updateTime:function () {
    },
    trackLocating:function () {
    },
    setPos:function () {
    },
    setPlayerPosition:function (newTime) {
    },
    showPos:function () {
    },
    volumeLocating:function () {
    },
    setVol:function (vLen) {
    },
    showVolPos:function (vPos) {
    },
    progressDragBegin:function () {
    },
    progressDragging:function () {
    },
    progressDragDrop:function () {
    },
    volumeDragBegin:function () {
    },
    volumeDragging:function () {
    },
    volumeDragDrop:function () {
    },
    getCurrentPosition:function () {
        if (player) {
            return player.currentTime;
        }else{
            return 0;
        }
    },
    getDurationPosition:function () {
        if (player) {
            return player.duration;
        }else{
            return 0;
        }
    },
    screenSize:function (sizeValue) {
        if (player) {
            return 1;
        }else{
            return 0;
        }
    }
};

//for WMVPlayer
var WMVPlayer = {
    setPosStart:false,
    init:function (playerId, playerInfoId, currentTimeId, totalTimeId, mediaUrl, autoStart) {
        //alert("hello");
        this.player = document.getElementById(playerId);
        this.playerInfo = document.getElementById(playerInfoId);
        this.currentTime = document.getElementById(currentTimeId);
        this.totalTime = document.getElementById(totalTimeId);
        this.player.url = mediaUrl;
        this.player.settings.autoStart = autoStart;
    },
    play:function () {
        if (player.controls.isAvailable('play')) {
            player.controls.play();
            WMVPlayer.updateTimeStatus = setInterval("WMVPlayer.updateTime()", 1000);
            playerInfo.innerHTML = "播放";
        }
    },
    pause:function () {
        if (player.controls.isAvailable('pause')) {
            player.controls.pause();
            clearInterval(WMVPlayer.updateTimeStatus);
            playerInfo.innerHTML = "暂停";
        }
    },
    pausePlay:function () {
        if (player.playState == 1) {
            // startGetStatus();
            changeIcon('btnPause', "images/player/video_pause_o.jpg");
        } else { //noinspection JSUnresolvedVariable
            if (player.playState == 3) {
                //alert("hello");
                WMVPlayer.pause();
                //startGetStatus();
                changeIcon('btnPause', "images/player/video_play_o.jpg");
            } else {
                WMVPlayer.play();
                changeIcon('btnPause', "images/player/video_pause_o.jpg");
            }
        }
    },
    stop:function () {
        if (player.controls.isAvailable('stop')) {
            player.controls.stop();
            clearInterval(WMVPlayer.updateTimeStatus);
            playerInfo.innerHTML = "停止";
        }
    },
    updateTime:function () {

        currentTime.innerHTML = getTimeStr(player.controls.currentPosition);
//        alert(player.controls.currentPosition);
        //noinspection JSUnresolvedVariable
        totalTime.innerHTML = getTimeStr(player.currentMedia.duration);
        WMVPlayer.showPos();
    },
    trackLocating:function () {
        var _tracker = document.getElementById("playTracker");
        if (_tracker) {
            var trackerAbsX = getAbsLeft(_tracker);
            if (trackerAbsX < 0) return;
            var relX = event.clientX - trackerAbsX;
            var _cursor = document.getElementById("playPosCursor");
            if (_cursor) {
                // 修正直观位置和控制位置偏差
                var amendX = Math.round(relX * moveLength / _tracker.clientWidth);
                //var amendX = Math.round(relX);
                _cursor.style.pixelLeft = amendX;
                var _playTracker0 = document.getElementById("playTracker0");
                if (_playTracker0) {
                    _playTracker0.style.width = amendX + 2 + "px";
                }
                this.setPos();
            }
        }
    },
    setPos:function () {
        var _cursor = document.getElementById("playPosCursor");
        var _tracker = document.getElementById("playTracker");
        if (_cursor && _tracker) {
            var newLeft = _cursor.style.pixelLeft;
            //var dragPosition = newLeft * Math.round(player.currentMedia.duration * 1000) / _tracker.clientWidth / 1000;
            player.controls.CurrentPosition = newLeft * Math.round(player.currentMedia.duration * 1000) / moveLength / 1000;
        }
    },
    setPlayerPosition:function (newTime) {
        WMVPlayer.play();
        var _cursor = document.getElementById("playPosCursor");
        var _playTracker0 = document.getElementById("playTracker0");
        var newPos = moveLength * newTime / player.currentMedia.duration;
        if (_cursor && _playTracker0) {
            var newLeft = _cursor.style.pixelLeft;
            _cursor.style.pixelLeft = newPos;
            _playTracker0.style.width = newPos + 2 + "px";
            WMPlayer.controls.CurrentPosition = newTime;

        }
    },
    showPos:function () {
//        alert("a");
        if (WMVPlayer.setPosStart) return;

        var currentPosition;
        var duration;


        currentPosition = player.controls.currentPosition;
//        alert("currentPosition:"+currentPosition);
        duration = player.currentMedia.duration;
//        alert("duration:"+duration);

        var blockpos = moveLength * currentPosition / duration;
        if (blockpos > moveLength) {
            blockpos = moveLength;
        }
        if (blockpos < 0) {
            blockpos = 0;
        }
//        alert("blockpos:"+blockpos);
        var _cursor = document.getElementById("playPosCursor");
        if (_cursor) {
            _cursor.style.pixelLeft = blockpos;

            //alert("blockpos:"+blockpos);

        }
        var _playTracker0 = document.getElementById("playTracker0");
        if (_playTracker0) {
            _playTracker0.style.width = blockpos + 2 + "px";
//            alert(_playTracker0.style.width);
        }
    },
    volumeLocating:function () {
        var _volume = document.getElementById("volumeTracker");
        if (_volume) {
            var volumeAbsX = getAbsLeft(_volume);
            if (volumeAbsX < 0) return;
            var relX = event.clientX - volumeAbsX;
            var _cursor = document.getElementById("volumeCursor");
            if (_cursor) {
                // 修正直观位置和控制位置偏差
                var amendX = Math.round(relX * this.volLength / _volume.clientWidth);
                _cursor.style.pixelLeft = amendX;
                var _volumeTracker0 = document.getElementById("volumeTracker0");
                if (_volumeTracker0) {
                    _volumeTracker0.style.width = amendX + 1;
                }

                var _v = Math.round(_cursor.style.pixelLeft * 100 / this.volLength);
                this.setVol(_v);
            }
        }
    },
    setVol:function (vLen) {
        this.player.settings.volume = vLen;
        this.showVolPos(this.player.settings.volume);
    },
    showVolPos:function (vPos) {
        if (this.volDragging) {
            return;
        }
        var _v = Math.round(this.volLength * vPos / 100);
        _v = (_v > this.volLength) ? this.volLength : _v;
        _v = (_v < 0) ? 0 : _v;
        var _volumeCursor = document.getElementById("volumeCursor");
        if (_volumeCursor) {
            _volumeCursor.style.pixelLeft = _v;
            _volumeCursor.title = "音量:" + vPos;
            // 根据音量大小显示音量级别
            var _volumeLevel = document.getElementById("volume_level");
            if (_volumeLevel) {
//            if (vPos > vlvl_h) _volumeLevel.src = "../images/vol_lvl_h.gif";
//            if ((vPos > vlvl_m) && (vPos < vlvl_h)) _volumeLevel.src = "../images/vol_lvl_m.gif";
                if (vPos > 50) _volumeLevel.src = "images/player/video_volume_max.jpg";
                if (vPos <= 50) _volumeLevel.src = "images/player/video_volume_min.jpg";
            }
        }
        var _volumeTracker0 = document.getElementById("volumeTracker0");
        if (_volumeTracker0) {
            _volumeTracker0.style.width = vPos + "%";
        }
    },
    progressDragBegin:function () {
        //检测是否 是 IE浏览器
        if (!document.all) return false;
        //检测是否是 需要 拖动的 元素
        if (event.srcElement.className == "drag") {
            dragApproved = true;

            var _cursor = document.getElementById("playPosCursor");
            if (_cursor) {
                //获取拖动元素 当前的左偏移量
                _cursorPixelLeft = _cursor.style.pixelLeft;
                //获得当前元素与 浏览器窗口 之间的 的 x轴 距离
                _cursorClientX = event.clientX;

                //y = event.clientY;
                document.onmousemove = progressDragging;
                setPosStart = true;
                event.cancelBubble = true;
            }
        }
        return false;
    },
    progressDragging:function () {
        if (event.button == _EVENTBUTTON_LEFT && dragApproved) {
            var newPosX = _cursorPixelLeft + event.clientX - _cursorClientX;
            if ((newPosX >= 0) && (newPosX <= moveLength)) {
                var _cursor = document.getElementById("playPosCursor");
                var _tracker = document.getElementById("playTracker");
                if (_cursor && _tracker) {
                    //var amendX = Math.round(newPosX * moveLength / _tracker.clientWidth);
                    _cursor.style.pixelLeft = newPosX;
                    var _playTracker0 = document.getElementById("playTracker0");
                    if (_playTracker0) {
                        _playTracker0.style.width = newPosX + 2 + "px";
                    }
                    //dragPosition = newPosX * getDuration() / movelen;
                    event.cancelBubble = true;
                }
            }
        }
        return false;
    },
    progressDragDrop:function () {
        this.dragApproved = false;
        if (this.setPosStart)
            this.setPos();
        this.setPosStart = false;
        return false;
    },
    volumeDragBegin:function () {
        var _volumeCursor = document.getElementById("volumeCursor");
        if (!_volumeCursor) return false;

        if (event.srcElement.className == "drag") {
            this.volDragging = true;
            this._volumePixelLeft = _volumeCursor.style.pixelLeft;
            this._volumeClientX = event.clientX;
            document.onmousemove = this.volumeDragging;
        }
        return false;
    },
    volumeDragging:function () {
        if (event.button == this._EVENTBUTTON_LEFT && this.volDragging) {
            var newPosX1 = this._volumePixelLeft + event.clientX - this._volumeClientX;

            if ((newPosX1 >= 0) && (newPosX1 <= this.volLength)) {
                var _volumeCursor = document.getElementById("volumeCursor");
                if (_volumeCursor) {
                    _volumeCursor.style.pixelLeft = newPosX1;
                    var _volumeTracker0 = document.getElementById("volumeTracker0");
                    if (_volumeTracker0) {
                        _volumeTracker0.style.width = newPosX1 + 1;
                    }

                    var _v = Math.round(_volumeCursor.style.pixelLeft * 100 / this.volLength);
                    this.setVol(_v);
                }
            }
        }
        return false;
    },
    volumeDragDrop:function () {
        this.volDragging = false;
        this.showVolPos(this.player.settings.volume);
    },
    getCurrentPosition:function () {
        return player.controls.currentPosition;
    },
    getDurationPosition:function () {
        return player.currentMedia.duration;
    },
    screenSize:function (sizeValue) {

    }
};

var mediaUrl = "";                   //播放地址
var statusString = "";
var currentPosition = 0;              // 当前位置
var isPaused = 0;
var isStoped = 1;
var statusPaused = 1;
var statusPlaying = 2;
var statusStoped = -3;
var statusBuffering = 4;
var statusConnected = 5;
var statusNotFound = -6;
var statusConnectionError = -7;
var statusDisconnected = -8;

var _EVENTBUTTON_NO = 0;            //没按键
var _EVENTBUTTON_LEFT = 1;          //按左键
var _EVENTBUTTON_RIGHT = 2;         //按右键
var _EVENTBUTTON_LEFTNRIGHT = 3;   //按左右键
var _EVENTBUTTON_MIDDLE = 4;        //按中间键
var _EVENTBUTTON_LEFTNMIDDLE = 5;  //按左键和中间键
var _EVENTBUTTON_MIDDLENRIGHT = 6; //按右键和中间键
var _EVENTBUTTON_ALL = 7;           //按所有的键

var playStatus = 0;


//关于播放器进度条
var CanSetPos = true;
var SetPosStart = false;
var SetVolStart = false;

var timelen = 1000;
var orileft = 0;
var timesnum = 100;
var rm_tollen = 0;
var orix = 0;
var newLeft = 0;
var dragPosition = -1;

var moveon = false;
//var movelen = 585;//movie bar length=90,扣除block宽度

//声音控制条
var volDragging = false;
var kind = 0;
//var isMute = false;
var voriLeft = 0;
var vollen = 30;
var vorix = 0;
var vlvl_h = 66;
var vlvl_m = 10;

var dragapproved = false;
var z, x, y;


//for MP4Player
var MP4Player = {
    playStatus:0,
    init:function (playerId, playerInfoId, currentTimeId, totalTimeId, mediaUrl, autoStart) {
        //alert("hello");
        this.player = document.getElementById(playerId);
        this.player.SetTrace(0);
        this.player.MiscellaneousCall(0x09, 0x04, 0, videoshowratio_);
        this.player.MiscellaneousCall(0x09, 0x05, 0, videodispmode_);
        //stmpeer.MiscellaneousCall(0x09, 0x0a, 0, videoupdateintv_);
        this.player.MiscellaneousCall(0x09, 0x0b, 0, _SHOW_PLAYER_STATUS);
        this.player.StreamPlaySetOpt(0, 0x05, 0, 1000); // playtimer report interval
        this.playerInfo = document.getElementById(playerInfoId);
        this.currentTime = document.getElementById(currentTimeId);
        this.totalTime = document.getElementById(totalTimeId);

    },
    pausePlay:function () {
        var _btnPause = document.getElementById("btnPause");
        if (isStoped) {
            __streamplay(this.mediaUrl);
            isPaused = 0;
            isStoped = 0;
            if (_btnPause) {
                _btnPause.src = "images/player/video_pause_o.jpg";
            }

        } else if (playStatus > 0) {
            if (1 == isPaused) {
                this.absUnPause();
            } else {
                this.absPause();
            }
        }
    },
    updateTime:function () {
        this.currentTime.innerHTML = getTimeStr(this.player.controls.currentPosition);
        //noinspection JSUnresolvedVariable
        this.totalTime.innerHTML = getTimeStr(this.player.currentMedia.duration);
    },
    trackLocating:function () {
        var _tracker = document.getElementById("playTracker");
        if (_tracker) {
            var trackerAbsX = getAbsLeft(_tracker);
            if (trackerAbsX < 0) return;
            var relX = event.clientX - trackerAbsX;
            var _cursor = document.getElementById("playPosCursor");
            if (_cursor) {
                // 修正直观位置和控制位置偏差
                var amendX = Math.round(relX * moveLength / _tracker.clientWidth);
                //var amendX = Math.round(relX);
                _cursor.style.pixelLeft = amendX;
                var _playTracker0 = document.getElementById("playTracker0");
                if (_playTracker0) {
                    _playTracker0.style.width = amendX + 2 + "px";
                }
                this.setPos();
            }
        }
    },
    setPos:function () {
        var _cursor = document.getElementById("playPosCursor");
        if (_cursor) {
            newLeft = _cursor.style.pixelLeft;
            // to second
            dragPosition = newLeft * MP4Player.getDurationPosition() / moveLength / 1000;
            this.setPlayerPosition(dragPosition);
        }
    },
    setPlayerPosition:function (newPos) {
        if (!gotDuration) return;
        if ((parseInt(newPos) < 0) || (parseInt(newPos * 1000) > parseInt(movieDuration))) return;

        //newPos = Math.round(newPos*100/movieDuration);
        //displayMessage("新的播放位置："+newPos);
        var _pos = parseInt(newPos);
        __absposition(_pos);
        this.setPosStart = false;
    },
    volumeLocating:function () {
        if (isfullscreen_) return;
        var _volume = document.getElementById("volumeTracker");
        if (_volume) {
            var volumeAbsX = getAbsLeft(_volume);
            if (volumeAbsX < 0) return;
            var relX = event.clientX - volumeAbsX;
            var _cursor = document.getElementById("volumeCursor");
            if (_cursor) {
                // 修正直观位置和控制位置偏差
                var amendX = Math.round(relX * vollen / _volume.clientWidth);
                _cursor.style.pixelLeft = amendX;
                var _volumeO = document.getElementById("volumeTracker0");
                if (_volumeO) {
                    _volumeO.style.width = amendX + 1;
                }

                var _v = Math.round(_cursor.style.pixelLeft * 100 / vollen);
                this.setVol(_v);
            }
        }
        return false;
    },
    setVol:function (vLen) {
        __volume(vLen);
        MP4Player.showVolPos(__getVolume());
    },
    showVolPos:function (vPos) {
        if (isfullscreen_) return;

        if (volDragging) {
            return;
        }
        var _v = Math.round(vollen * vPos / 100);
        _v = (_v > vollen) ? vollen : _v;
        _v = (_v < 0) ? 0 : _v;
        var _volumeCursor = document.getElementById("volumeCursor");
        if (_volumeCursor) {
            _volumeCursor.style.pixelLeft = _v;
            _volumeCursor.title = "音量:" + vPos;
            // 根据音量大小显示音量级别
            var _volumeLevel = document.getElementById("volume_level");
            if (_volumeLevel) {
                if (vPos > vlvl_h) _volumeLevel.src = "images/player/video_volume_max.jpg";
                if ((vPos > vlvl_m) && (vPos < vlvl_h)) _volumeLevel.src = "images/player/video_volume_max.jpg";
                if (vPos <= vlvl_m) _volumeLevel.src = "images/player/video_volume_min.jpg";
            }
        }
        var _volumeO = document.getElementById("volumeTracker0");
        if (_volumeO) {
            _volumeO.style.width = vPos + "%";
        }
    },
    progressDragBegin:function () {
        //检测是否 是 IE浏览器
        if (!document.all) return false;
        //检测是否是 需要 拖动的 元素
        if (event.srcElement.className == "drag") {
            this.dragApproved = true;

            var _cursor = document.getElementById("playPosCursor");
            if (_cursor) {
                //获取拖动元素 当前的左偏移量
                this._cursorPixelLeft = _cursor.style.pixelLeft;
                //获得当前元素与 浏览器窗口 之间的 的 x轴 距离
                this._cursorClientX = event.clientX;

                //y = event.clientY;
                document.onmousemove = this.progressDragging;
                this.setPosStart = true;
                event.cancelBubble = true;
            }
        }
        return false;
    },
    progressDragging:function () {
        if (event.button == this._EVENTBUTTON_LEFT && this.dragApproved) {
            var newPosX = this._cursorPixelLeft + event.clientX - this._cursorClientX;
            if ((newPosX >= 0) && (newPosX <= moveLength)) {
                var _cursor = document.getElementById("playPosCursor");
                var _tracker = document.getElementById("playTracker");
                if (_cursor && _tracker) {
                    //var amendX = Math.round(newPosX * moveLength / _tracker.clientWidth);
                    _cursor.style.pixelLeft = newPosX;
                    var _playTracker0 = document.getElementById("playTracker0");
                    if (_playTracker0) {
                        _playTracker0.style.width = newPosX + 2 + "px";
                    }
                    //dragPosition = newPosX * getDuration() / movelen;
                    event.cancelBubble = true;
                }
            }
        }
        return false;
    },
    progressDragDrop:function () {
        this.dragApproved = false;
        if (this.setPosStart)
            this.setPos();
        this.setPosStart = false;
        return false;
    },
    volumeDragBegin:function () {
        var _volumeCursor = document.getElementById("volumeCursor");
        if (!_volumeCursor) return false;

        if (event.srcElement.className == "drag") {
            volDragging = true;
            voriLeft = _volumeCursor.style.pixelLeft;
            vorix = event.clientX;
            document.onmousemove = this.volumeDragging;
        }
        return false;
    },
    volumeDragging:function () {
        if (event.button == _EVENTBUTTON_LEFT && volDragging) {
            var newPosX1 = voriLeft + event.clientX - vorix;

            if ((newPosX1 >= 0) && (newPosX1 <= vollen)) {
                var _volumeCursor = document.getElementById("volumeCursor");
                if (_volumeCursor) {
                    _volumeCursor.style.pixelLeft = newPosX1;
                    var _volumeO = document.getElementById("volumeTracker0");
                    if (_volumeO) {
                        _volumeO.style.width = newPosX1 + 1 + "px";
                    }

                    var _v = Math.round(_volumeCursor.style.pixelLeft * 100 / vollen);
                    MP4Player.setVol(_v);
                }
            }
        }
        return false;
    },
    volumeDragDrop:function () {
        volDragging = false;
    },
    screenSize:function (sizeValue) {

    },
    displayPlayerPosition:function (newPos) {
        if (isfullscreen_) return;
        this.currentPosition = newPos;
//    displayPosition(""+getTimeStr(currentPosition/1000)+" / "+getTimeStr(movieDuration/1000));
        this.displayCurrentPosition(getTimeStr(this.currentPosition / 1000));
        this.displayTotalPosition(getTimeStr(movieDuration / 1000));
        this.showPos();
        this.showVolPos(__getVolume());
    },
    displayCurrentPosition:function (pos) {
        if (isfullscreen_) return;
        var _posSpan = document.getElementById("currentTime");
        if (_posSpan) {
            _posSpan.innerHTML = pos;
        }
    },
    displayTotalPosition:function (pos) {
        if (isfullscreen_) return;
        var _posSpan = document.getElementById("totalTime");
        if (_posSpan) {
            _posSpan.innerHTML = pos;
        }
    },
    showPos:function () {
        if (isfullscreen_) return;

        if (SetPosStart) {
            return;
        }
        if (!gotDuration) return;
        var blockpos = moveLength * this.currentPosition / MP4Player.getDurationPosition();
        // alert("blockpos:"+blockpos);
        if (blockpos > moveLength) {
            blockpos = moveLength;
        }
        if (blockpos < 0) {
            blockpos = 0;
        }
        var _cursor = document.getElementById("playPosCursor");
        if (_cursor) {
            _cursor.style.pixelLeft = blockpos;
        }
        var _playTracker0 = document.getElementById("playTracker0");
        if (_playTracker0) {
            _playTracker0.style.width = blockpos + 2 + "px";
        }
    },
    __streamplay:function (strurl, bufftime) {
        if (rtspref_ > 0) {
            if (strurl == strurl_)
                return;
            player.StreamPlayStop(rtspref_);
            rtspref_ = 0;
            isplaying_ = 0;
            //__showwating(0);
        }
        //判断播放url是否为空
        if (strurl != "") {
            strurl_ = strurl;
            player.StreamPlaySetOpt(0, 0x01, 0, bufftime);
            //开始播放
            rtspref_ = player.StreamPlayOpen(strurl, 0, videodispmode_, player);
            //__showwating(1);
        }
        __onExp();
    },
    __absposition:function (pos) {
        if (rtspref_ == 0)
            return;
        var p = parseInt(pos);
        player.StreamPlaySetOpt(rtspref_, 0x20, 0, p); // this pos is time based by seconds
    },
    __streampause:function (pause) {
        player.StreamPlaySetOpt(rtspref_, 0x21, 0, pause);
    },
    __volume:function (v) {
        player.StreamPlaySetOpt(rtspref_, 0x06, 0, v);
    },
    __getVolume:function () {
        return player.StreamPlayGetOpt(rtspref_, 0x06, 0, 0);
    },
    __duration:function () {
        return (player.StreamPlayGetOpt(rtspref_, 0x22, 0, 0));
    },
    __onExp:function () {
        videoshowratio_ = videoshowratio_ == 1 ? 8 : 1;
        player.MiscellaneousCall(0x09, 0x04, 0, videoshowratio_);
    },
    preUnload:function () {
        if (rtspref_ > 0) {
            player.StreamPlayStop(rtspref_);
            rtspref_ = 0;
        }
    },
    absPause:function () {
        var _btnPause = document.getElementById("btnPause");
        isPaused = 1;
        __streampause(1);
        this.playStatusChanged(statusPaused);
        if (_btnPause && !isfullscreen_) {
            _btnPause.src = (_btnPause.mouseover) ? "/page/3Screen/images/player/video_play_o.jpg" : "/page/3Screen/images/player/video_play_n.jpg";
        }
    },
    absUnPause:function () {
        if (isfullscreen_) return;

        if ((playStatus <= 0) || (isPaused == 0)) return;
        var _btnPause = document.getElementById("btnPause");
        isPaused = 0;
        __streampause(0);
        //playStatusChanged(statusPaused);
        if (_btnPause) {
            _btnPause.src = (_btnPause.mouseover) ? "/page/3Screen/images/player/video_pause_o.jpg" : "/page/3Screen/images/player/video_pause_n.jpg";
        }
    },
    stopPlay:function () {
        if (isfullscreen_) return;

        __streamplay('', 0);
        //playStatusChanged(statusStoped);
        isStoped = 1;
        isPaused = 0;
        //btnPlay.src="../images/mp4_stop_o.gif";
        var _btnPause = document.getElementById("btnPause");
        if (_btnPause) {
            _btnPause.src = "images/player/video_play_n.jpg";
        }
    },
    playStatusChanged:function (newStatus) {
        if (isfullscreen_) return;
        playStatus = newStatus;
        try {
            if (typeof(playStatusChangedCallBack) == 'function') {
                playStatusChangedCallBack(newStatus);
            }
        } catch (e) {

        }
        switch (parseInt(playStatus)) {
            case statusPaused:
                statusString = ("暂停");
                break;
            case statusPlaying:
                statusString = ("播放");
                break;
            case statusStoped:
                statusString = ("已停止");
                this.playerStopped();
                this.displayPlayerPosition(0);
                break;
            case statusBuffering:
                statusString = ("正在缓冲");
                changePausePlayIcon(true);
                break;
            case statusConnected:
                statusString = ("播放");
                break;
            case statusNotFound:
                statusString = ("无法连接媒体文件");
                break;
            case statusConnectionError:
                statusString = ("连接失败");
                break;
            case statusDisconnected:
                statusString = ("连接已断开");
                //标记为停止状态，重新开始
                this.stopPlay();
                this.playerStopped();
                this.displayPlayerPosition(0);
        }
        this.displayStatus(statusString);
    },
    fullScreen:function () {
        __onExp();
    },
    playerStopped:function () {
        if (isfullscreen_) return;
        // 位置设为头上
        var _cursor = document.getElementById("playPosCursor");
        if (_cursor) {
            _cursor.style.pixelLeft = 0;
        }
        var _playTracker0 = document.getElementById("playTracker0");
        if (_playTracker0) {
            _playTracker0.style.width = 2;
        }
        //playStatusChanged(statusStoped);
    },
    displayStatus:function () {

    },
    getCurrentPosition:function () {
        return currentPosition;
    },
    getDurationPosition:function () {
        return movieDuration;
    }
};

//for flv
var FLVPlayer = {
    init:function (playerId) {
        this.player = document.getElementById(playerId);
    },
    getCurrentPosition:function () {
        return currentPosition;
    },
    getDurationPosition:function () {
        return movieDuration;
    }
};


//for playerControl
var playerControl = {
    player:null,
    playerInfo:null,
    currentTime:null,
    totalTime:null,
    updateTimeStatus:null,
    playerId:'player',
    playerInfoId:'playerInfo',
    currentTimeId:'currentTime',
    totalTimeId:'totalTime',
    mediaUrl:'rtsp://192.168.1.158/movies/亚瑟700.wmv',
    autoStart:'false',
    playerType:'WMV',
    moveLength:600 - 16,
    volLength:28,
    volDragging:false,
    _EVENTBUTTON_LEFT:1,
    setPosStart:false,
    dragApproved:false,
    _cursorPixelLeft:-1,
    _cursorClientX:-1,
    _volumePixelLeft:-1,
    _volumeClientX:-1,
    initPlayer:function () {
        var _playerTD2;
        var _playerTD = document.getElementById("playerTD");
        _playerTD.innerHTML = "";
        if (this.playerType == 'WMV') {
            _playerTD.innerHTML = wmvStr;
            playerControl = deepCopy(WMVPlayer, playerControl);
            playerControl.init(playerControl.playerId, playerControl.playerInfoId, playerControl.currentTimeId, playerControl.totalTimeId, playerControl.mediaUrl, playerControl.autoStart);
            _playerTD2 = document.getElementById("playerTD2");
            _playerTD2.style.display = "block";
        } else if (this.playerType == 'MP4') {
            _playerTD.innerHTML = mp4Str;
            playerControl = deepCopy(MP4Player, playerControl);
            playerControl.init(playerControl.playerId, playerControl.playerInfoId, playerControl.currentTimeId, playerControl.totalTimeId, playerControl.mediaUrl, playerControl.autoStart);
            _playerTD2 = document.getElementById("playerTD2");
            _playerTD2.style.display = "block";
        } else if (this.playerType == 'Safari') {
            _playerTD.innerHTML = html5Str;
            playerControl = deepCopy(HTML5Player, playerControl);
            playerControl.init(playerControl.playerId, playerControl.playerInfoId, playerControl.currentTimeId, playerControl.totalTimeId, playerControl.mediaUrl, playerControl.autoStart);
            _playerTD2 = document.getElementById("playerTD2");
            _playerTD2.style.display = "block";
        } else if (this.playerType == 'FLV') {
            _playerTD.innerHTML = flvStr;
            playerControl = deepCopy(FLVPlayer, playerControl);
            playerControl.init(playerControl.playerId);
            _playerTD2 = document.getElementById("playerTD2");
            _playerTD2.style.display = "none";
        }
    }
};
function deepCopy(p, c) {
    var c = c || {};
    for (var i in p) {
        if (typeof p[i] === 'object') {
            c[i] = (p[i].constructor === Array) ? [] : {};
            deepCopy(p[i], c[i]);
        } else {
            c[i] = p[i];
        }

    }
    return c;
};

//格式化 时间
function getTimeStr(ts) {
    ts = parseInt(ts);
    var sec = (ts % 60);

    ts -= sec;
    var tmp = (ts % 3600);  //# of seconds in the total # of minutes
    ts -= tmp;              //# of seconds in the total # of hours

    var strSec = new String(sec);

    if (strSec.length < 2) {
        strSec = "0" + strSec;
    }

    var hour = (ts / 3600);
    if ((ts % 3600) != 0)
        hour = 0;
    var min = (tmp / 60);
    if ((tmp % 60) != 0)
        min = 0;

    if ((new String(min)).length < 2)
        min = "0" + min;

    return hour + ":" + min + ":" + strSec;
}


//返回当前在浏览器中绝对位置
function getAbsLeft(e) {
    if (!e) return -1;
    var x = e.offsetLeft;
    while (e = e.offsetParent) {
        x += e.offsetLeft;
    }
    return x;
}

function changeIcon(objId, imgSrc) {
    var ele = document.getElementById(objId);
    if (ele) {
        ele.src = imgSrc;
    }
}

function changeIconOver(imgEle) {
    var oldSrc = imgEle.src + "";
    oldSrc = oldSrc.replace("_n", "_o");
    imgEle.src = oldSrc;
}

function changeIconOut(imgEle) {
    var oldSrc = imgEle.src + "";
    oldSrc = oldSrc.replace("_o", "_n");
    imgEle.src = oldSrc;
}

function fullScreen() {
    __onExp();
}

function displayStatus(s) {
    if (isfullscreen_) return;
    var _statusSpan = document.getElementById("playStatus");
    if (_statusSpan) {
        _statusSpan.innerHTML = s;
        //alert(s);
    }
}
function changePausePlayIcon(displayPause) {
    var _btnPause = document.getElementById("btnPause");
    if (displayPause) {
        if (_btnPause) _btnPause.src = "/page/3Screen/images/player/video_pause_n.jpg";
    } else {
        if (_btnPause) _btnPause.src = "/page/3Screen/images/player/video_play_n.jpg";
    }
}


function getAbsLeft(e) {
    if (!e) return -1;
    var x = e.offsetLeft;
    while (e = e.offsetParent) {
        x += e.offsetLeft;
    }
    return x;
}

//根据url传入的key获得value
function request(paras){
    var url = location.href;
    var paraString = url.substring(url.indexOf("?")+1,url.length).split("&");
    var paraObj = {};
    for (i=0; j=paraString[i]; i++){
        paraObj[j.substring(0,j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf
            ("=")+1,j.length);
    }
    var returnValue = paraObj[paras.toLowerCase()];
    if(typeof(returnValue)=="undefined"){
        return "";
    }else{
        return returnValue;
    }
}


