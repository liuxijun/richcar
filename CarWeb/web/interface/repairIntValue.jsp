<%@ page import="java.io.File" %><%@ page import="com.fortune.util.FileUtils" %><%@ page import="java.util.List" %><%@ page
        import="com.fortune.util.XmlUtils" %><%@ page import="org.dom4j.Node" %><%@ page import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.rms.business.content.model.ContentProperty" %><%@ page import="com.fortune.util.StringUtils" %><%@
        page import="java.util.Date" %><%@ page import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %>
<%@ page import="com.fortune.util.SpringUtils" %>
<%@ page import="com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface" %>
<%@ page import="com.fortune.rms.business.content.model.Content" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-4-15
  Time: 下午3:29
  扫描某个目录下的所有xml文件，修复导入是错误的集数信息
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String xmlPath = request.getParameter("path");
    if(xmlPath==null){
        xmlPath = "xml";
    }
    File path = new File(application.getRealPath(xmlPath));
    String allSQL = "--启动\r\n";
    List<File> xmlFiles = FileUtils.listDir(path.getAbsolutePath(),"*.xml",true);
    for(File xmlFile:xmlFiles){
        logger.debug("开始处理："+xmlFile.getAbsolutePath());
        allSQL+="\r\n--以下数据来自文件："+xmlFile.getName()+"\r\n";
        org.dom4j.Element root = XmlUtils.getRootFromXmlFile(xmlFile.getAbsolutePath());
        if(root!=null){
            List allContentNodes = root.selectNodes("content");
            int i=0,nodeCount = allContentNodes.size();
            for (Object node : allContentNodes) {
                i++;
                if(i%200==0){
                    logger.debug("处理了"+i+"/"+nodeCount+",百分比"+Math.round(i*100.0/nodeCount)+"%...");
                    allSQL+="\r\ncommit;\r\n";
                }
                Node contentNode = (Node) node;
                String contentName = XmlUtils.getValue(contentNode,"@MediaName",null);
                String contentId = XmlUtils.getValue(contentNode,"@ContentID",null);
                int episodes=1;
                List subcontents = contentNode.selectNodes("subcontents/subcontent");
                int l= subcontents.size();
                for (Object subN : subcontents) {
                    Node subContentNode = (Node) subN;
                    episodes = XmlUtils.getIntValue(subContentNode, "@Episodes",episodes+1);
                    String subContentName = XmlUtils.getValue(subContentNode, "@SubContentName", contentName).trim();
                    if(StringUtils.string2int(subContentName,-1)>=0){
                        //如果是数字，就把详细一点的数据复制进去
                        subContentName = contentName;
                        if(l>1){
                            subContentName += " 第" + episodes + "集";
                        }else{
                            subContentName +=" 全集";
                        }
                    }
                    subContentName = subContentName.replaceAll("&","");
                    subContentName = subContentName.replaceAll("|","");
                    subContentName = subContentName.replaceAll("'","");
                    subContentName = subContentName.replaceAll("\\+","");
                    if(subContentName.length()>90){
                        subContentName = subContentName.substring(0,90);
                    }
                    String sql = "update content_property set int_value="+episodes+",name = '"+subContentName;
                    sql += "',subcontent_id = '"+XmlUtils.getValue(subContentNode,"@SubContentID","")+"' where " +
                            " string_value='"+XmlUtils.getValue(subContentNode,"@MediaUrlSource","")+"';";
                    allSQL +=sql+"\r\n";
                }
            }
        }
        allSQL+="\r\ncommit;\r\n";
        allSQL+="\r\n--以上数据来自文件："+xmlFile.getName()+"\r\n";
    }
    allSQL+="--结束";
    logger.debug("处理完毕");
    String sqlFileName = "/xml/repair"+ StringUtils.date2string(new Date(), "yyyyMMddHHmmss")+".sql";
    FileUtils.writeNew(application.getRealPath(sqlFileName),allSQL);
%><html>
<head>
    <title></title>
</head>
<body>
<p>处理完毕！<a href="<%=sqlFileName%>">下载</a></p>
</body>
</html>
<%!
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.repairIntValue.jsp");
%>