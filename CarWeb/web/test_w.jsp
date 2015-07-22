<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

    <style type="text/css">

        body {  font-family: "宋体"; font-size: 9pt; margin-top: 0px; margin-left: 0px; margin-right: 0px}
        A { COLOR: black; FONT-SIZE: 9pt; FONT-WEIGHT: 400; TEXT-DECORATION: none }
        A:hover { COLOR: red; FONT-SIZE: 9pt; FONT-WEIGHT: 400; TEXT-DECORATION: underline }
        a:active     { font: 9pt "宋体"; cursor: hand; color: #FF0033 }

    </style>
</head>
<body>
<%=removeParameters("hello.jpg?propertyId=123456&hello=true&propertyName=测试&name=12345&propertyCode=CODE_PROPERTY&propertyType=199",
        new String[]{"propertyId","propertyName","propertyCode","propertyType"})%>
</body>
</html><%!
    public String removeParameters(String url,String[] parameters){
        String[] begins = new String[]{"&","?","&amp;"};
        for(String parameter:parameters){
            for(String begin:begins){
                String fullParameter = begin+parameter+"=";
                int p=url.indexOf(fullParameter);
                if(p>=0){
                    int p1=p+parameter.length()+1;
                    int l=url.length();
                    while(p1<l){
                        char ch = url.charAt(p1);
                        if(ch=='&'){
                            break;
                        }
                        p1++;
                    }
                    String temp = url;
                    if("?".equals(begin)){
                        p++;
                    }
                    url = temp.substring(0,p);
                    if(p1>=l){
                        continue;
                    }
                    url = url+temp.substring(p1);
                }
            }
        }
        return url.replace("?&","?");
    }
%>