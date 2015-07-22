<%@ page import="com.fortune.common.business.security.dao.daoInterface.AdminDaoInterface" %><%@ page
        import="java.util.List" %><%@ page
        import="java.util.Set" %><%@ page
        import="java.util.Collection" %><%@ page
        import="java.lang.reflect.Field" %><%@ page
        import="com.fortune.util.*" %><%@ page
        import="java.util.Date" %>
<%@ page
        contentType="text/html;charset=UTF-8" %><%
    String hqlStr = request.getParameter("hqlStr");
    StringBuffer outStr = new StringBuffer();
    if (hqlStr == null) {
        hqlStr = "";
    }else{
        hqlStr = hqlStr.trim();
    }
    if (!"".equals(hqlStr)) {
        AdminDaoInterface daoInterface = (AdminDaoInterface) SpringUtils.getBean("operatorDaoInterface", session.getServletContext());
        PageBean pageBean = new PageBean();
        pageBean.setPageSize(10);
        try {
            SearchResult result = daoInterface.getObjects(hqlStr, new Object[]{}, pageBean);
            pageBean.setRowCount(result.getRowCount());
            List list = result.getRows();
            if (list != null) {
                for (Object obj : list) {
                    outStr.append(getObjectToString(obj, null));
                }
            }
        } catch (Exception e) {
            outStr.append("\r\n<error>").append(e.getMessage()).append("</error>\r\n");
        }
    }
%><%!
    public String getObjectToString(Object obj, String objName) {
        StringBuffer result = new StringBuffer("");
        if (obj != null) {
            if (objName == null || "".equals(objName)) {
                objName = obj.getClass().getSimpleName();
            }
            result.append("&lt;").append(objName).append("&gt;");
            if (obj instanceof Object[]) {
                Object[] objArray = (Object[]) obj;
                for (Object o : objArray) {
                    result.append(getObjectToString(o, null));
                }
            } else if (obj instanceof List) {
                List objArray = (List) obj;
                for (Object o : objArray) {
                    result.append(getObjectToString(o, null));
                }
            } else if (obj instanceof Set) {
                Set objArray = (Set) obj;
                for (Object o : objArray) {
                    result.append(getObjectToString(o, null));
                }
            } else if (obj instanceof Collection) {
                Collection objArray = (Collection) obj;
                for (Object o : objArray) {
                    result.append(getObjectToString(o, null));
                }
            } else if (obj instanceof Integer) {
                result.append(obj.toString());
            } else if (obj instanceof Long) {
                result.append(obj.toString());
            } else if (obj instanceof Date) {
                result.append(obj.toString());
            } else if (obj instanceof String) {
                result.append(obj.toString());
            } else {
                System.out.println("toString.....:" + objName);
                //obj.getClass().getAnnotations()
                for (Field field : obj.getClass().getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object fieldValue = BeanUtils.getProperty(obj, fieldName);
                    result.append(getObjectToString(fieldValue, fieldName));
/*
                    System.out.println(" " + fieldName+
                            ":"+fieldValue);
                   result.append("\t<").append(fieldName).append(">");
                   if(fieldValue!=null){
                       result.append(fieldValue.toString());
                   }
                   result.append("</").append(fieldName).append(">\r\n");
*/
                }
            }
            result.append("&lt;/").append(objName).append("&gt;<br>");

        } else {
            result.append("\r\n&lt;").append(objName).append("&gt;null&lt;/").append(objName).append("&gt;<br>");
        }
        return result.toString();
    }
%><html>
<head>
    <title>hql运行调试</title>
</head>
<body>
<form action="hqlPlus.jsp" method="post">
    <pre><%=(outStr.toString())%></pre>
    <textarea name="hqlStr" rows="hqlStr" style="width:500px;height:400px"><%=hqlStr%></textarea><br/>
    <input type="submit" value="提交数据"/>&nbsp;<input type="reset" value="恢复数据"/>
</form>
</body>
</html>