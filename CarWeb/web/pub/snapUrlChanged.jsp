<%@ page import="com.fortune.util.StringUtils" %><%@ page
        import="com.fortune.rms.business.content.model.ContentFile" %><%@ page
        import="java.util.List" %><%@ page
        import="java.util.HashMap" %><%@ page
        import="java.util.Map" %><%@ page
        import="com.fortune.util.JsonUtils" %><%@ page
        import="org.apache.log4j.Logger" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/4/23
  Time: 9:23
  当截图变化后，重新整理session中的截图链接
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.snapUrlChanged.jsp");
    int index = StringUtils.string2int(request.getParameter("idx"), -1);
    boolean success = false;
    String message;
    if(index>0){
        String snapUrl = request.getParameter("snapUrl");
        if(snapUrl!=null){
            List<ContentFile> contentFiles = (List<ContentFile>) session.getAttribute("contentFiles");
            if(contentFiles!=null){
                if(contentFiles.size()>index){
                    ContentFile file = contentFiles.get(index);
                    if(file!=null){
                        file.setThumbPic(snapUrl);
                        success = true;
                        message="成功保存截图信息:index="+index+",url="+snapUrl;
                    }else{
                        message = "索引对应的媒体文件居然为空："+index;
                    }
                }else{
                    message = "没有找到索引对应的媒体文件信息："+index;
                }
            }else{
                message = "session中没有任何的媒体文件信息："+index;
            }
        }else{
            message = "截图的url输入为空，不能继续处理："+index;
        }
    }else{
        message = "没有输入任何的索引："+index;
    }
    logger.debug("设置截图url结果："+message);
    Map<String,Object> result = new HashMap<String,Object>();
    result.put("success",success);
    result.put("message",message);
    out.print(JsonUtils.getJsonString(result));
%>