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
    function send_request(product_id,type,subject_id,property_id,service_id,sp_id) {//��ʼ����ָ������������������ĺ���
        //alert("test");
        var bname = window.navigator.appName;
        //var subjectCake = "<rm:favoriteAdd channelSubjectId='$subjectId' type='tvbox'/>&consumerId=3"+iPanel.ioctlRead('serial');
        var subjectCake = "buysoap.jsp?product_id="+product_id+"&product_type="+type+"&subject_id="+subject_id
                +"&property_id="+property_id+"&service_id="+service_id+"&sp_id="+sp_id;
       // alert(subjectCake);
        //��ʼ��ʼ��XMLHttpRequest����
        if(window.XMLHttpRequest) { //Mozilla �����
            http_request1 = new XMLHttpRequest();
            if (http_request1.overrideMimeType) {//����MiME���
                http_request1.overrideMimeType('text/xml');
            }
        } else if (window.ActiveXObject) { // IE�����
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
        if (http_request1==null) { // �쳣����������ʵ��ʧ��
            window.alert("���ܴ���XMLHttpRequest����ʵ��.");
            return false;
        } else {

        }

        http_request1.onreadystatechange = processRequest;
                // ȷ����������ķ�ʽ��URL�Լ��Ƿ�ͬ��ִ���¶δ���
        http_request1.open("POST", subjectCake, true);
        http_request1.send(null);
    }
    // ��������Ϣ�ĺ���
    function processRequest(id) {
        // alert(productid+"AJAX STATE:"+http_request1.readyState);
        if (http_request1.readyState == 4) { // �ж϶���״̬
            if (http_request1.status == 200) { // ��Ϣ�Ѿ��ɹ����أ���ʼ������Ϣ
                var tempStr=http_request1.responseText;
                alert("ִ�����"+tempStr);
                var  mediasStr=document.getElementById('product30981');
                mediasStr.innerHTML=tempStr;
            }else { //ҳ�治����
                alert("���������ҳ�����쳣��");
            }
        }
    }