<%@ page import="java.util.Properties" %><%@ page import="java.io.InputStream" %><%@ page import="java.io.FileNotFoundException" %><%@ page import="java.io.IOException" %><%@ page import="org.apache.log4j.Logger" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/3/13
  Time: 10:39
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
%>
<html>
<head>
    <title>生成数据</title>
</head>
<body>
<pre><%=initConfigFile(null)%></pre>
</body>
</html>
<%!
    private Logger logger = Logger.getLogger("com.fortune.rms.jsp.initConfig.jsp");
    public String initConfigFile(String configFileName){
        Properties properties = new Properties();
        if(configFileName == null || "".equals(configFileName)){
            configFileName = "/fortune_application.properties";
        }
        StringBuilder sb=  new StringBuilder("delete from config;\r\n");
        try {
            InputStream is;
            is = getClass().getResourceAsStream(configFileName);
            if(is!=null){
                properties.load(is);
                is.close();
                for(String name:properties.stringPropertyNames()){
                    String value = properties.getProperty(name);
                    sb.append("INSERT INTO config(name,value,description) values('").append(name).append("','").append(value).append("','").append(name).append("');\r\n");
                }
            }else{
                logger.error("读取属性文件--->错误！- 原因：getResourceAsStream初始化失败" + configFileName);
            }
        } catch (FileNotFoundException ex) {
            logger.error("读取属性文件--->失败！- 原因：文件路径错误或者文件不存在" + configFileName);
            //ex.printStackTrace();
        } catch (IOException ex) {
            logger.error("装载文件--->失败:" + ex.getMessage());
            //ex.printStackTrace();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }
%>