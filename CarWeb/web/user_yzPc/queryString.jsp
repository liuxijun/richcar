<%
    long spId = getLongParameter(request,"spId",-1);
    long cpId = getLongParameter(request,"cpId",-1);
    long channelId = getLongParameter(request,"channelId",-1);
    long contentId = getLongParameter(request,"contentId",-1);
    long contentPropertyId = getLongParameter(request,"contentPropertyId",-1);
    String productIdss = getParameter(request,"productIdss",null);
    String ipmFlag = getParameter(request,"ipmFlag",null);
    String fileID = getParameter(request,"fileID",null);
    String rootCategoryID = getParameter(request,"rootCategoryID",null);
    String sender = getParameter(request,"sender","");
    String queryStr = "sender="+sender+"&spId="+spId+"&cpId="+cpId+"&channelId="+channelId
                    +"&contentId="+contentId+"&contentPropertyId="+contentPropertyId+"&ipmFlag="+ipmFlag+"&fileID="+fileID+"&rootCategoryID="+rootCategoryID;

    long serviceProductId = getLongParameter(request,"serviceProductId",-1);
    long productId = getLongParameter(request,"productId",-1);
    long productType = getLongParameter(request,"productType",-1);
    String buyQueryStr = "spId="+spId+"&cpId="+cpId+"&channelId="+channelId
                    +"&contentId="+contentId+"&contentPropertyId="+contentPropertyId
                    +"&serviceProductId="+serviceProductId+"&productId="+productId+"&productType="+productType+"&ipmFlag="+ipmFlag+"&fileID="+fileID+"&rootCategoryID="+rootCategoryID;

%><%!
    public static String getParameter(HttpServletRequest request,String param,String defaultValue){
        String value = request.getParameter(param);
        if (value==null) return defaultValue;
        else return value;
    }

    public static long getLongParameter(HttpServletRequest request,String param,long defaultValue){
        String svalue = request.getParameter(param);
        if(svalue!=null){
            try {
                long value = Long.parseLong(svalue);
                return value;
            }
            catch (NumberFormatException ex) {
                return defaultValue;
            }
        } else return defaultValue;

    }
%>