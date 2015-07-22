<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-11-11
  Time: 下午1:47
  To change this template use File | Settings | File Templates.
--%><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface" %><%@ page
        import="com.fortune.rms.business.module.model.Property" %><%@ page
        import="com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.model.ContentProperty" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.model.Content" %><%@ page
        import="com.fortune.util.*" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page import="java.util.*" %><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    ServletContext servletContext = session.getServletContext();
    final ContentPropertyLogicInterface contentPropertyLogicInterface = (ContentPropertyLogicInterface)
            SpringUtils.getBean("contentPropertyLogicInterface",servletContext);
    final PropertyLogicInterface  propertyLogicInterface = (PropertyLogicInterface)
            SpringUtils.getBean("propertyLogicInterface",servletContext);
    final  ContentLogicInterface contentLogicInterface = (ContentLogicInterface)
            SpringUtils.getBean("contentLogicInterface",servletContext);
    final String[] propertyCodes = new String[]{"Media_Url_Source","Media_Url_384k","Media_Url_768k","Media_Url_512k"};
/*
    long[] spIds = new long[]{15905690,16241840,16241843};
    final List<Content> spContents = new ArrayList<Content>();
    for(long spId:spIds){
        Content searchBean = new Content();
        searchBean.setCspId(spId);
        spContents.addAll(contentLogicInterface.search(searchBean));
    }
    String result = "一共有" +spContents.size()+
            "个媒体要处理！";
    logger.debug(result);
*/
    Map<String,List<ContentProperty>> allClips = new HashMap<String,List<ContentProperty>>();
    List<ContentProperty> sourceClips = null;
    String filePath = application.getRealPath("/logs/intValue/");
    String fileName = "intValueRepair" +StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+
            ".sql";
    int outputLines = 0,allSize=0,hasScanCount=0;
    PageBean pageBean = new PageBean(0,Integer.MAX_VALUE,"o1.contentId asc,o1.intValue","asc");
    for(String propertyCode:propertyCodes){
        Property clipProperty = propertyLogicInterface.getByCode(propertyCode);
        if(clipProperty!=null){
            ContentProperty clip = new ContentProperty();
            clip.setPropertyId(clipProperty.getId());
            clip.setContentId(-1L);
            logger.debug("准备添加片段："+clipProperty.getCode()+","+clipProperty.getName());

            List<ContentProperty> clips = contentPropertyLogicInterface.search(clip,pageBean);
            if("Media_Url_Source".equals(propertyCode)){
                sourceClips = clips;
                allSize = sourceClips.size()*3;
            }else{
                if(sourceClips!=null){
                    hasScanCount++;
                    for(ContentProperty sourceClip:sourceClips){
                        String url = sourceClip.getStringValue();
                        if(url==null||"".equals(url)){
                            continue;
                        }
                        Long contentId = sourceClip.getContentId();
                        if(contentId<=0){
                            continue;
                        }
                        int p=url.lastIndexOf(".");
                        if(p>0){
                            url = url.substring(0,p+1);
                        }
                        while(url.startsWith("/")){
                            url = url.substring(1);
                        }
                        url = "encode/"+url;
                        Long sourceIntValue = sourceClip.getIntValue();
                        if(sourceIntValue==null){
                            sourceIntValue = 1L;
                        }
                        for(int m=0,encodedSize=clips.size();m<encodedSize;m++){
                            ContentProperty encodedClip = clips.get(m);
                            if(!encodedClip.getContentId().equals(sourceClip.getContentId())){
                                continue;
                            }
                            String encodedUrl = encodedClip.getStringValue();
                            if(encodedUrl==null||"".equals(encodedUrl)){
                                continue;
                            }
                            if(378068204==encodedClip.getContentId()){
                                logger.debug("正在检查《火凤凰》");
                            }
                            while(encodedUrl.startsWith("/")){
                                encodedUrl=encodedUrl.substring(1);
                            }
                            if(encodedUrl.startsWith(url)){
                                if(!sourceIntValue.equals(encodedClip.getIntValue())){
                                    Content content = (Content) CacheUtils.get(contentId,"allContents",new DataInitWorker(){
                                       public Object init(Object key,String cacheName){
                                           Long contentId = (Long)key;
                                           try {
                                               return contentLogicInterface.get(contentId);
                                           } catch (Exception e) {
                                               return null;
                                           }
                                       }
                                    });
                                    if(content ==null){
                                        logger.error("无法找到媒体：contentId="+contentId);
                                        continue;
                                    }
                                    String sqlLine ="--影片：" +content.getName()+",集数"+
                                            "原始值：" +encodedClip.getIntValue()+",contentId="+encodedClip.getContentId()+
                                            ",类型：" +propertyCode+
                                            ",编码前连接："+sourceClip.getStringValue()+
                                            ",编码后连接：" +encodedClip.getStringValue()+
                                            "\r\n";
                                    sqlLine+= "update CONTENT_PROPERTY set INT_VALUE="+
                                           sourceIntValue+" where ID="+encodedClip.getId()+";\r\n";
                                    logger.debug("发现一个有问题的片段，尝试进行修正："+sqlLine);
                                    if(outputLines%100==0){
                                        sqlLine+="commit;\r\n";
                                    }
                                    FileUtils.writeContinue(filePath,fileName,sqlLine);
                                    outputLines++;
                                }
                            }
                        }
                        if(hasScanCount%100==0){
                            logger.debug("总进度："+hasScanCount+"/"+allSize);
                        }
                    }
                }
            }
            allClips.put(propertyCode,clips);
            logger.debug("类型" +clipProperty.getName()+","+clipProperty.getCode()+"的媒体添加完毕，这个类型的媒体有"+clips.size()+"个,"+
                    "累计添加片段"+allClips.size()+"个");
        }else{
        }
    }
    for(int i=1,l=propertyCodes.length;i<l;i++){
    }
%><%!
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.repairIntValue");
%>
<html>
<head>
    <title></title>
</head>
<body>

</body>
</html>