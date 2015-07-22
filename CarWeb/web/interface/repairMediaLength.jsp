<%@ page import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.rms.business.module.model.Property" %><%@ page
        import="com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface" %><%@ page
        import="com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.model.Content" %><%@ page import="java.util.List" %><%@ page
        import="com.fortune.rms.business.content.model.ContentProperty" %><%@ page
        import="com.fortune.rms.business.system.model.Device" %><%@ page import="java.io.File" %><%@ page
        import="com.fortune.util.*" %><%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-5-12
  Time: 下午7:21
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    PropertyLogicInterface propertyLogicInterface = (PropertyLogicInterface) SpringUtils.getBean("propertyLogicInterface", session.getServletContext());
    String propertyCode = "Media_Url_Source";
    Property mediaSourceProperty = propertyLogicInterface.getByCode(propertyCode);
    Property mediaLengthProperty = propertyLogicInterface.getByCode("MEDIA_LENGTH");
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.repairMediaLength.jsp");
    ContentPropertyLogicInterface contentPropertyLogicInterface = (ContentPropertyLogicInterface)SpringUtils.getBean("contentPropertyLogicInterface",session.getServletContext());
    final DeviceLogicInterface deviceLogicInterface = (DeviceLogicInterface) SpringUtils.getBean("deviceLogicInterface",session.getServletContext());
    ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface",session.getServletContext());
    Long[] cspIds = new Long[]{16241843L,15905690L,16241840L};
    String offlineSql = "";
    String errorInfo = "";
    String mediaLengthSql = "";
    String contentPropertyLengthSql = "";
    String mediaLengthRestoreSql = "--恢复修改的mediaLength的脚本 "+StringUtils.date2string(new Date());
    try {
        int updatePropertyCount = 0;
        int updateMediaLengthCount = 0;
        for(Long cspId:cspIds){
            List<Content> contents = contentLogicInterface.getContent(null,null,cspId);
            int i=0,l=contents.size();
            logger.debug("cspId="+cspId+"共发现媒体文件"+l+"个！");
            for(;i<l;i++){
                Content content = contents.get(i);
                if(content==null)continue;
                if(i%100==0){
                }
                logger.debug("正在扫描媒体文件《" +content.getName()+
                        "》....("+i+"/"+l+")"+(i*100/l)+"%");
                Device contentDevice = (Device) CacheUtils.get(content.getDeviceId(),"deviceCache",new DataInitWorker(){
                  public Object init(Object key ,String cacheName){
                      return deviceLogicInterface.get((Long) key);
                  }
                });
                if(contentDevice==null){
                    continue;
                }
                String deviceUrl = contentDevice.getUrl();
                if(deviceUrl==null){
                    continue;
                }
                if(deviceUrl.contains("/live/")){
                    continue;
                }
                ContentProperty clip = new ContentProperty();
                clip.setContentId(content.getId());
                clip.setPropertyId(mediaSourceProperty.getId());
                List<ContentProperty> clips = contentPropertyLogicInterface.search(clip);
                //扫描所有的资源，获取时长，然后相加。如果缺集，就警告下线
                long mediaLength = 0;
                int lostClips = 0;
                boolean doNotCheckAllFiles=false;
                for(ContentProperty cp:clips){
                    if(doNotCheckAllFiles&&cp.getLength()>0){
                        mediaLength +=cp.getLength();
                    }else{
                        String fileName = contentDevice.getLocalPath()+"/"+cp.getStringValue();
                        File mediaFile = new File(fileName);
                        if(mediaFile.exists()&&!mediaFile.isDirectory()&&mediaFile.length()>0){
                            SimpleFileInfo fileInfo = new SimpleFileInfo(mediaFile);
                            boolean clipLosted=true;
                            if(FileUtils.setFileMediaInfo(mediaFile.getAbsolutePath(),fileInfo)){
                                int fileLength = fileInfo.getLength();
                                if(fileLength>0){
                                    clipLosted = false;
                                    mediaLength += fileInfo.getLength();
                                    contentPropertyLengthSql+="\r\n--媒体"+content.getName()+"的片段"+cp.getName()+","+cp.getIntValue()+"时长进行修复：";
                                    contentPropertyLengthSql+="\r\nupdate CONTENT_PROPERTY set LENGTH="+fileInfo.getLength()+" where id="+cp.getId()+";";
                                    updatePropertyCount++;
                                    if(updatePropertyCount%100==0){
                                        contentPropertyLengthSql+="\r\ncommit;";
                                    }
                                }
                            }
                            if(clipLosted){
                                String tempStr = content.getName()+"的第" +cp.getIntValue()+
                                        "文件存在，但不能播放："+mediaFile.getAbsolutePath();
                                errorInfo +=tempStr+"\r\n";
                                logger.error(tempStr);
                                lostClips++;
                            }
                        }else{
                            String tempStr = content.getName()+"的第" +cp.getIntValue()+"文件不存在："+mediaFile.getAbsolutePath();
                            errorInfo +=tempStr+"\r\n";
                            logger.error(tempStr);
                            lostClips++;
                        }
                    }
                }
/*
                Property firstPicProperty = propertyLogicInterface.getByCode("PC_MEDIA_POSTER_BIG");
                Property secondPicProperty = propertyLogicInterface.getByCode("PC_MEDIA_POSTER_HORIZONTAL_BIG");
                int picLost = 0;
                Property[] picIds = new Property[]{firstPicProperty,secondPicProperty};
                for(Property picId:picIds){
                    ContentProperty picCp = new ContentProperty();
                    picCp.setPropertyId(picId.getId());
                    picCp.setContentId(content.getId());
                    List<ContentProperty> pics = contentPropertyLogicInterface.search(picCp);
                    if(pics.size()>0){
                        for(ContentProperty pic:pics){
                            File picFile = new File(application.getRealPath(pic.getStringValue()));
                            if(picFile.exists()&&!picFile.isDirectory()){

                            }else{
                                String tempStr = content.getName()+"海报："+picId.getName()+"缺失！";
                                picLost ++;
                                errorInfo+=tempStr+"\r\n";
                                logger.error(tempStr);
                            }
                        }
                    }else{
                        String tempStr = content.getName()+"海报："+picId.getName()+"缺失！";
                        picLost++;
                        errorInfo+=tempStr+"\r\n";
                        logger.error(tempStr);
                    }
                }
                if(picLost>0){
                    String tempStr = "《" +content.getName()+
                            "》缺失了"+picLost+"个海报，将其下线";
                    errorInfo+=tempStr+"\r\n";
                    offlineSql+="--媒体《" +content.getName()+"》缺了" +picLost+"个海报"+
                            "\r\n";
                    offlineSql+="update content_csp set status=1 where content_id="+content.getId()+"\r\n";
                    logger.error(tempStr);
                }else{

                }
*/
                if(lostClips>0){
                    String tempStr = "《" +content.getName()+
                            "》缺失了"+lostClips+"个片段，将其下线";
                    errorInfo+=tempStr+"\r\n";
                    offlineSql+="-- "+tempStr+"\r\n";
                    offlineSql+="update content_csp set status=1 where content_id="+content.getId()+"\r\n";
                    logger.error(tempStr);
                }else{

                }

                ContentProperty searchBean = new ContentProperty();
                searchBean.setPropertyId(mediaLengthProperty.getId());
                searchBean.setContentId(content.getId());
                List<ContentProperty> lengthCps = contentPropertyLogicInterface.search(searchBean);
                if(lengthCps!=null&&lengthCps.size()>0){
                    for(ContentProperty mediaLengthCp:lengthCps){
                        int oldLength =  StringUtils.string2int(mediaLengthCp.getStringValue(),0);
                        if(mediaLength!=oldLength){
                            if(oldLength>0){
                                mediaLengthSql+="\r\n--媒体长度有问题，老的长度是："+oldLength+","+StringUtils.formatTime(oldLength)+",新的长度是："+
                                mediaLength+","+StringUtils.formatTime(mediaLength);
                            }
                            mediaLengthSql+="\r\nupdate content_property set int_value="+mediaLength+",string_value='"+mediaLength+"' where id="+mediaLengthCp.getId()+";";
                            mediaLengthRestoreSql+="\r\n-- update content_property set int_value="+mediaLengthCp.getIntValue()+",string_value='"+mediaLengthCp.getStringValue()+"' where id="+mediaLengthCp.getId()+";";
                            updateMediaLengthCount++;
                        }
                    }
//                    mediaLengthCp=lengthCps.get(0);
                }else{
                    mediaLengthSql += "\r\ninsert into content_property(id,name,int_value,string_value,content_id,property_id)" +
                            "values(FORTUNE_GLOBAL_SEQ.nextval,'"+content.getName().replaceAll("'","’").replaceAll("&","＆")+"时间长度',"+mediaLength+",'"+mediaLength+"'," +
                            ""+content.getId()+","+mediaLengthProperty.getId()+");";
                    updateMediaLengthCount++;
                }
                if(updateMediaLengthCount%100==0){
                    mediaLengthSql+="\r\ncommit;";
                    mediaLengthRestoreSql+="\r\ncommit;";
                }
//                mediaLengthCp.setIntValue(mediaLength);
//                mediaLengthCp.setStringValue(""+mediaLength);
//                contentPropertyLogicInterface.save(mediaLengthCp);
            }
        }
    } catch (Exception e) {
        errorInfo+="发生异常错误："+e.getMessage();
        logger.error("发生异常："+e.getMessage());
    }
    String errorFileName = "/tools/"+StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+"_error.log";
    FileUtils.writeNew(application.getRealPath(errorFileName),errorInfo);
    String sqlFileName = "/tools/"+StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+"_offline.sql";
    FileUtils.writeNew(application.getRealPath(sqlFileName),offlineSql);
    String lengthSqlFileName = "/tools/"+StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+"_mediaLength.sql";
    FileUtils.writeNew(application.getRealPath(lengthSqlFileName),mediaLengthSql);
    String restoreLengthSqlFileName = "/tools/"+StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+"_mediaLengthRestore.sql";
    FileUtils.writeNew(application.getRealPath(restoreLengthSqlFileName),mediaLengthRestoreSql);
    String updateContentPropertyLengthSqlFileName = "/tools/"+StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+"_updateContentPropertyLength.sql";
    FileUtils.writeNew(application.getRealPath(updateContentPropertyLengthSqlFileName),contentPropertyLengthSql);
%>
<html>
<head>
    <title>处理完毕</title>
</head>
<body>
  <a href="<%=errorFileName%>">下载错误日志</a><br/>
<a href="<%=sqlFileName%>">下载SQL文件</a>
</body>
</html><%!
    //201305181430
%>