var http_request = false;

function createhttp(){
		if(window.XMLHttpRequest) {
			http_request = new XMLHttpRequest();
			if (http_request.overrideMimeType) {
				http_request.overrideMimeType('text/xml');
			}
		}
		else if (window.ActiveXObject) {
			try {
				http_request = new ActiveXObject("Msxml2.http_request");
			} catch (e) {
				try {
					http_request = new ActiveXObject("Microsoft.http_request");
				} catch (e) {}
			}
		}
		if (!http_request) {
			window.alert("XMLHttpRequest instance cannot be created.");
			return false;
		}
}

function send(url, returnStr){
	createhttp();
	http_request.open("post", encodeURI(url), true);
	http_request.onreadystatechange = function (){
		if(http_request.readyState == 4){
	        if(http_request.status == 200){
	            returnStr.innerHTML = http_request.responseText;
	        }else{
	        	alert("the page you request has an exception!");
	        }
	    }
	};
	http_request.send(null);
}


    var http_request1 = false;
    function send_request(product_id,type,subject_id,property_id,service_id,sp_id) {//初始化、指定处理函数、发送请求的函数
        //alert("test");
        var bname = window.navigator.appName;
        //var subjectCake = "<rm:favoriteAdd channelSubjectId='$subjectId' type='tvbox'/>&consumerId=3"+iPanel.ioctlRead('serial');
        var subjectCake = "buysoap.jsp?product_id="+product_id+"&product_type="+type+"&subject_id="+subject_id
                +"&property_id="+property_id+"&service_id="+service_id+"&sp_id="+sp_id;
       // alert(subjectCake);
        //开始初始化XMLHttpRequest对象
        if(window.XMLHttpRequest) { //Mozilla 浏览器
            http_request1 = new XMLHttpRequest();
            if (http_request1.overrideMimeType) {//设置MiME类别
                http_request1.overrideMimeType('text/xml');
            }
        } else if (window.ActiveXObject) { // IE浏览器
            try {
                http_request1 = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (e) {
                try {
                    http_request1 = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (e) {}
            }
        } else {
            http_request1 = new XMLHttpRequest();
        }
        if (http_request1==null) { // 异常，创建对象实例失败
            window.alert("不能创建XMLHttpRequest对象实例.");
            return false;
        } else {

        }

        http_request1.onreadystatechange = processRequest;
                // 确定发送请求的方式和URL以及是否同步执行下段代码
        http_request1.open("POST", subjectCake, true);
        http_request1.send(null);
    }
    // 处理返回信息的函数
    function processRequest(id) {
        // alert(productid+"AJAX STATE:"+http_request1.readyState);
        if (http_request1.readyState == 4) { // 判断对象状态
            if (http_request1.status == 200) { // 信息已经成功返回，开始处理信息
                var tempStr=http_request1.responseText;
                alert("执行完毕"+tempStr);
                var  mediasStr=document.getElementById('product30981');
                mediasStr.innerHTML=tempStr;
            }else { //页面不正常
                alert("您所请求的页面有异常。");
            }
        }
    }