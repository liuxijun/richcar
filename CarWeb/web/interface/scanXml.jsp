<%@ page import="java.io.File" %>
<%@ page
        import="java.util.List" %>
<%@ page
        import="org.dom4j.Node" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page
        import="com.fortune.rms.business.content.model.ContentProperty" %>
<%@ page
        import="java.util.Date" %>
<%@ page import="com.fortune.rms.business.content.model.Content" %>
<%@ page import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %>
<%@ page import="com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface" %>
<%@ page
        import="com.fortune.rms.business.module.model.Property" %>
<%@ page import="com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface" %>
<%@ page import="com.fortune.util.*" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-4-15
  Time: 下午3:29
  扫描某个目录下的所有xml文件，修复导入是错误的集数信息
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String xmlPath = request.getParameter("path");
    if (xmlPath == null) {
        xmlPath = "xml";
    }
    File path = new File(application.getRealPath(xmlPath));
    String allSQL = "--启动\r\n";
    PropertyLogicInterface propertyLogicInterface = (PropertyLogicInterface) SpringUtils.getBean("propertyLogicInterface", session.getServletContext());
    String propertyCode = "Media_Url_Source";
    Property property = propertyLogicInterface.getByCode(propertyCode);
    ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface", session.getServletContext());
    ContentPropertyLogicInterface contentPropertyLogicInterface = (ContentPropertyLogicInterface) SpringUtils.getBean("contentPropertyLogicInterface", session.getServletContext());

    List<File> xmlFiles = FileUtils.listDir(path.getAbsolutePath(), "*.xml", true);
    for (File xmlFile : xmlFiles) {
/*
        String xmlFileName = xmlFile.getAbsolutePath();
        if(!(xmlFileName.contains("20130416094847843.xml")||xmlFileName.contains("20130425160745250.xml"))){
            continue;
        }
*/
        logger.debug("开始处理：" + xmlFile.getAbsolutePath());
        allSQL += "\r\n--以下数据来自文件：" + xmlFile.getName() + "\r\n";
        org.dom4j.Element root = XmlUtils.getRootFromXmlFile(xmlFile.getAbsolutePath());
        if (root != null) {
            List allContentNodes = root.selectNodes("content");
            int i = 0, nodeCount = allContentNodes.size();
            long cspId = XmlUtils.getLongValue(root, "@CPCode", -1);
            for (Object node : allContentNodes) {
                i++;
                if (i % 50 == 0) {
                    logger.debug("处理了" + i + "/" + nodeCount + ",百分比" + Math.round(i * 100.0 / nodeCount) + "%...");
                }
                Node contentNode = (Node) node;
                String contentName = XmlUtils.getValue(contentNode, "@MediaName", null);
                String contentId = XmlUtils.getValue(contentNode, "@ContentID", null);
                List<Content> contents = contentLogicInterface.getContent(null, contentId, cspId);
                if (contents == null || contents.size() == 0) {
                    continue;
                }
                Content content = contents.get(0);
                int episodes = 1;
                List subcontents = contentNode.selectNodes("subcontents/subcontent");
                int l = subcontents.size();
                ContentProperty cp = new ContentProperty();
                cp.setContentId(content.getId());
                cp.setPropertyId(property.getId());
                //所有的源连接
                List<ContentProperty> clips = contentPropertyLogicInterface.search(cp, new PageBean(0, 1000, "o1.intValue", "asc"));
                /**
                 * 先检查数据库中记录是否正常
                 */
                long lastIntValue = -1;
                String lastStringValue = "";
                int  clipsCount = clips.size();
                try {
                    if(clipsCount>0){
                        for (int index = 0; index < clipsCount; index++) {
                            ContentProperty clip = clips.get(index);
                            Long intValue = (clip.getIntValue());
                            String result = null;
                            if (intValue == null) {
                                if(clipsCount>1){
                                    result = content.getName() + "的第" + index + "个资源集数为空："+clip.getStringValue();
                                }
                                intValue = -1L;
                            }else{
                                if (index == 0) {
                                    if (intValue != 1&&clipsCount>1) {
                                        result = content.getName() + "的第一集从" + intValue +
                                                "开始。";
                                    }
                                } else {
                                    long sub = intValue-lastIntValue;
                                    if (sub == 0) {
                                        result = content.getName() + "的第" + (intValue) + "集重复:" + lastStringValue + " vs " + clip.getStringValue();
                                    }else if(sub>1){
                                        if(sub>2){
                                            result = content.getName() + "缺集，从第" + (lastIntValue+1) + "集开始，到第" +(intValue-1)+"集";
                                        }else{
                                            result = content.getName() + "缺集：第" + (intValue-1) + "集";
                                        }
                                    }
                                }
                            }
                            lastStringValue = clip.getStringValue();
                            lastIntValue = intValue;
                            if (result != null) {
                                logger.debug(result);
                                allSQL +=content.getId()+"\t"+clip.getId()+"\t"+result + "\r\n";
                            }
                        }
                    }else{
                        String result = content.getName()+"没有影片片段！";
                        logger.debug(result);
                        allSQL += result + "\r\n";
                    }
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
/*
                for (Object subN : subcontents) {
                    Node subContentNode = (Node) subN;
                    episodes = XmlUtils.getIntValue(subContentNode, "@Episodes",episodes+1);
                    String mediaUrl = XmlUtils.getValue(subContentNode,"@MediaUrlSource","");
                    if(mediaUrl==null||"".equals(mediaUrl)){
                        continue;
                    }
                    boolean found = false;
                    int count = clips.size();
                    for(int idx=0;idx<count;idx++){
                        ContentProperty clip = clips.get(idx);
                        String oldUrl =clip.getStringValue();
                        if(mediaUrl.equals(oldUrl)||oldUrl.contains(mediaUrl)){
                            found = true;
                            clips.remove(idx);
                            //data = clip;
                            break;
                        }
                        Long intValue= clip.getIntValue();
                        if(intValue==null){
                            intValue = 0L;
                        }
                        String fileName = FileUtils.extractFileName(mediaUrl,"/");
                        String oldFileName = FileUtils.extractFileName(oldUrl,"/");
                        //某些恶心的数据，是会文件名重复，但目录不同而已
                        if(oldFileName.contains(fileName)&&episodes==intValue.intValue()){
                            found = true;
                            clips.remove(idx);
                            //data = clip;
                            break;
                        }
                    }
                    if(found){
                        continue;
                    }
                    if(!mediaUrl.endsWith(".mp4")){
                        mediaUrl +=".mp4";
                    }
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
                    String subContentId = XmlUtils.getValue(subContentNode, "@SubContentID", "");
                    ContentProperty data = new ContentProperty();
                    data.setName(subContentName);
                    data.setContentId(content.getId());
                    data.setStringValue(mediaUrl);
                    data.setSubContentId(subContentId);
                    data.setIntValue(new Long(episodes));
                    data.setPropertyId(property.getId());
                    data = contentPropertyLogicInterface.save(data);
                    String sql = "insert into content_property(id,int_value,name,subcontent_id,content_id,string_value,property_id)" +
                            " values(FORTUNE_GLOBAL_SEQ.nextval,"+episodes+",'"+subContentName+
                            "','"+XmlUtils.getValue(subContentNode,"@SubContentID","")+"'," + content.getId()+","+
                            "'"+mediaUrl+"'," +property.getId()+");";
                    logger.debug(sql);
                    allSQL +=sql+"\r\n";
                }
*/
            }
        }
        allSQL += "\r\n--以上数据来自文件：" + xmlFile.getName() + "\r\n";
    }
    allSQL += "--结束";
    logger.debug("处理完毕");
    String sqlFileName = "/xml/repair" + StringUtils.date2string(new Date(), "yyyyMMddHHmmss") + ".sql";
    FileUtils.writeNew(application.getRealPath(sqlFileName), allSQL);
%>
<html>
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