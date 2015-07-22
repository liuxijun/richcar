<%@ page import="org.apache.log4j.Logger" %><%@ page
        import="org.hibernate.type.TimestampType" %><%@ page import="java.sql.Timestamp" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.model.Content" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface" %><%@ page
        import="com.fortune.rms.business.module.model.Property" %><%@ page
        import="com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface" %><%@ page
        import="com.fortune.rms.business.content.model.ContentProperty" %><%@ page
        import="com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface" %><%@ page
        import="com.fortune.rms.business.system.model.Device" %><%@ page import="com.fortune.util.*" %><%@ page import="java.io.File" %><%@ page
        import="java.awt.image.BufferedImage" %><%@ page
        import="java.util.concurrent.Executors" %><%@ page
        import="java.util.concurrent.Executor" %><%@ page import="java.util.*" %><%@ page
        import="org.hibernate.type.StringType" %><%@ page
        import="com.fortune.rms.business.system.logic.logicInterface.SystemLogLogicInterface" %><%@ page
        import="com.fortune.common.business.security.model.Admin" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-5-5
  Time: 下午7:11
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
/*
    Enumeration paramNames = request.getParameterNames();

    while(paramNames.hasMoreElements()){
        String pName = paramNames.nextElement().toString();
        String[] values = request.getParameterValues(pName);
        for(String v:values){
            logger.debug(pName+"="+v);
        }
    }
*/
    Admin admin = (Admin)session.getAttribute(com.fortune.common.Constants.SESSION_ADMIN);
    if(admin==null){
        out.println("{success:false,message:'尚未登录，无法处理指令！'}");
        return;
    }
    boolean errorOccur=false;
    String result = "";
    List<Object> parameterValue = new ArrayList<Object>();
    List<org.hibernate.type.Type> parameterType = new ArrayList<org.hibernate.type.Type>();
    String hql = "from  com.fortune.rms.business.content.model.Content c where 1=1";
    Executor executor = Executors.newFixedThreadPool(5);
    final String command = request.getParameter("command");
    if(command==null){
        //command = "NotInputCommand";
    }
    if(command!=null){
        if("true".equals(request.getParameter("createAll"))){
            long cspId = StringUtils.string2long(request.getParameter("cc_cspId"),-1);
            long cspStatus=StringUtils.string2long(request.getParameter("cc_status"),-1);
            if(cspId>0||cspStatus>=0){
                hql += " and c.id in(select cc.contentId from " +
                        "com.fortune.rms.business.content.model.ContentCsp cc" +
                        " where 1=1";
                if(cspId>0){
                    hql += " and cc.cspId = " + cspId;
                }
                if(cspStatus>=0){
                    hql += " and cc.status="+cspStatus;
                }
                hql+=")";
            }
            long channelId = StringUtils.string2long(request.getParameter("channelId"),-1);
            if(channelId>0){

                hql+=" and c.id in (select cch.contentId from " +
                        "com.fortune.rms.business.content.model.ContentChannel cch" +
                        " where cch.channelId = "+channelId;
            }
            long contentStatus = StringUtils.string2int(request.getParameter("c_status"),-1);
            if(contentStatus>=0){
                hql += " and c.status="+contentStatus;
            }
            String contentName = request.getParameter("c_name");
            if(contentName!=null&&!"".equals(contentName.trim())){
                parameterType.add(new StringType());
                parameterValue.add("%"+contentName+"%");
                hql += " and c.name like ?";
            }
            hql += appendLongParameter("c.cspId",request);
            String startDate = request.getParameter("startDate");
            if(startDate!=null&&!"".equals(startDate)){
                parameterType.add(new TimestampType());
                parameterValue.add(new Timestamp(StringUtils.string2date(startDate+" 00:00:00").getTime()));
                hql += " and c.createTime >= ?";
            }
            String endDate = request.getParameter("endDate");
            if(endDate!=null&&!"".equals(endDate)){
                parameterType.add(new TimestampType());
                parameterValue.add(new Timestamp(StringUtils.string2date(endDate+" 23:59:59").getTime()));
                hql += " and c.createTime <= ?)";
            }
        }else{
            String[] contentIds = request.getParameterValues("contentId");
            hql +=" and c.id in (";
            for(String id:contentIds){
                hql += ""+id+",";
            }
            hql = hql.substring(0,hql.length()-1)+")";
        }
        //logger.debug("hql = "+hql);
        ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface",
                session.getServletContext());
        try {
            org.hibernate.type.Type[] types = new org.hibernate.type.Type[parameterType.size()];
            for(int i=0,l=types.length;i<l;i++){
                types[i]=parameterType.get(i);
            }
            int count = HibernateUtils.findCount(contentLogicInterface.getSession(), hql,
                    parameterValue.toArray(),
                    types);
            if(count>0){
                List<Object> allRows = HibernateUtils.findList(contentLogicInterface.getSession(),
                        hql,parameterValue.toArray(),types,0,Integer.MAX_VALUE);
                String picSize = request.getParameter("picSize");
                if(picSize == null||"".equals(picSize)){
                    picSize = "450x360";
                }
                picSize = picSize.toLowerCase();
                final ContentPropertyLogicInterface contentPropertyLogicInterface = (ContentPropertyLogicInterface)
                        SpringUtils.getBean("contentPropertyLogicInterface",session.getServletContext());
                final DeviceLogicInterface deviceLogicInterface = (DeviceLogicInterface) SpringUtils.getBean("deviceLogicInterface",session.getServletContext());
                PropertyLogicInterface propertyLogicInterface = (PropertyLogicInterface) SpringUtils.getBean("propertyLogicInterface",session.getServletContext());
                String propertyCode = "Media_Url_Source";
                final Property property = propertyLogicInterface.getByCode(propertyCode);
                long picPropertyId = StringUtils.string2int(request.getParameter("propertyId"),-1);
                Property tempProperty = null;
                try {
                    if(picPropertyId>0){
                        tempProperty = propertyLogicInterface.get(picPropertyId);
                    }
                } catch (Exception e) {
                    tempProperty = null;
                }
                if(tempProperty==null){
                    String picPropertyCode = request.getParameter("picPropertyCode");
                    if(picPropertyCode==null){
                        picPropertyCode = "PC_MEDIA_POSTER_HORIZONTAL_BIG";
                        //PC_MEDIA_POSTER_HORIZONTAL_BIG,PC_MEDIA_POSTER_BIG,PC_MEDIA_POSTER_SMALL
                    }
                    tempProperty = propertyLogicInterface.getByCode(picPropertyCode);
                }
                final Property picProperty = tempProperty;
                String[] size = picSize.split("x");
                final int width = StringUtils.string2int(size[0],116);
                final int height = StringUtils.string2int(size[1],88);
                final int startTime = StringUtils.string2int(request.getParameter("startTime"),300);
                final boolean replaceOldIfExists = "true".equals(request.getParameter("replaceOldIfExists"));
                final String picUrl = request.getParameter("picUrl");
                final String webRoot = application.getRealPath("/");
                int i=0;
                File picFile = new File(webRoot+"/"+picUrl);
                if("replace".equals(command)){
                    if((!picFile.exists())||picFile.isDirectory()){
                        result = "图片文件不存在，不能继续执行替代的任务："+picFile.getAbsolutePath();
                        logger.error(result);
                        errorOccur = true;
                    }
                }
                final SimpleFileInfo fileInfo = new SimpleFileInfo(picFile);
                if(!errorOccur){
                    for(Object obj :allRows){
                        final Content content = (Content) obj;
                        final Device device =(Device)CacheUtils.get(content.getDeviceId(),"device",new DataInitWorker(){
                            public Object init(Object keyId,String cacheName){
                                return deviceLogicInterface.get((Long)keyId);
                            }
                        });
                        //logger.debug("即将处理："+content.getName());
                        Runnable task = new Runnable() {
                            public void run() {
                                //To change body of implemented methods use File | Settings | File Templates.
                                if("capture".equals(command)){
                                    capturePoster(content, width,height, startTime,contentPropertyLogicInterface,
                                            property,picProperty,device,webRoot,replaceOldIfExists);
                                }else if("replace".equals(command)){
                                    replacePoster(content, picUrl,fileInfo,width,height,contentPropertyLogicInterface,
                                            picProperty,replaceOldIfExists);
                                }
                            }
                        };
                        executor.execute(task);
                        i++;
                        if(i%10==0){
                            logger.debug("已经完成"+i+"个，"+(i*100/count)+"%");
                        }
                    }
                }
            }else{
                errorOccur = true;
                result = "发生异常：没有符合要求的记录存在！";
            }
        } catch (Exception e) {
            errorOccur = true;
            result = "发生异常："+e.getMessage();
        }
    }else{
        errorOccur = true;
        result = "不认识的命令："+command;
    }
    Map<String,Object> jsonResult = new HashMap<String,Object>();
    jsonResult.put("success",!errorOccur);
    jsonResult.put("message",result);
    SystemLogLogicInterface systemLogLogicInterface = (SystemLogLogicInterface) SpringUtils.getBean("",session.getServletContext());
    systemLogLogicInterface.saveLog(request.getRemoteAddr(),
            admin,
            "posterAutoCreator","批量海报处理："+result);

%><%=JsonUtils.getJsonString(jsonResult)%><%!
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.contentPosterAutoCreator.jsp");
    public static final int FILE_HAS_EXISTS=201;
    public static final int SUCCESS=200;
    public static final int FAIL_SOURCE_NOT_EXISTS=404;
    public int replacePoster(Content content,String picUrl,SimpleFileInfo fileInfo,int width,int height,ContentPropertyLogicInterface contentPropertyLogicInterface,
                             Property picProperty,boolean replaceOldIfExists){
        int result = 404;
        ContentProperty contentProperty = new ContentProperty();
        contentProperty.setContentId(content.getId());
        contentProperty.setPropertyId(picProperty.getId());
        List<ContentProperty> oldData = contentPropertyLogicInterface.search(contentProperty);
        if(oldData!=null&&oldData.size()>0){
            //把这些数据的contentId反转，留个备份
            for(ContentProperty pic :oldData){
                logger.warn("发现旧的海报数据，反转备份....："+content.getName()+"-"+pic.getStringValue());
                pic.setContentId(0-pic.getContentId());
                contentPropertyLogicInterface.save(pic);
            }
        }
        contentProperty.setExtraData(""+fileInfo.getWidth()+"x"+fileInfo.getHeight());
        contentProperty.setName(picProperty.getName());
        contentProperty.setStringValue(picUrl);
        contentProperty.setIntValue(0L);
        contentPropertyLogicInterface.save(contentProperty);
        return 200;
    }
    public int capturePoster(Content content,int width,int height,int startTime,
                             ContentPropertyLogicInterface contentPropertyLogicInterface,
                             Property sourceClipProperty,Property picProperty,Device device,String appRoot,boolean replaceOldIfExists){
        int result = 404;
        ContentProperty contentProperty = new ContentProperty();
        contentProperty.setContentId(content.getId());
        contentProperty.setPropertyId(sourceClipProperty.getId());
        PageBean pageBean = new PageBean(0,2,"o1.intValue","asc");//默认查找第一集
        List<ContentProperty> clips = contentPropertyLogicInterface.search(contentProperty,pageBean);
        contentProperty = new ContentProperty();
        contentProperty.setContentId(content.getId());
        contentProperty.setPropertyId(picProperty.getId());
        List<ContentProperty> oldData = contentPropertyLogicInterface.search(contentProperty);
        if(!replaceOldIfExists&&oldData!=null&&oldData.size()>0){
            File picFile = new File(appRoot+"/"+oldData.get(0).getStringValue());
            if(picFile.exists()){
                logger.debug("媒体海报已经存在，不用再生成！");
                return 201;
            }
            logger.debug("尽管数据库中有数据，但实际上却没有找到该图片文件，重新截图："+picFile.getAbsolutePath());
        }
        if(oldData!=null&&oldData.size()>0){
            //把这些数据的contentId反转，留个备份
            for(ContentProperty pic :oldData){
                logger.warn("发现旧的海报数据，反转备份....："+content.getName()+"-"+pic.getStringValue());
                pic.setContentId(0-pic.getContentId());
                contentPropertyLogicInterface.save(pic);
            }
        }
        Date now = new Date();
        if(clips!=null&&clips.size()>0){
            ContentProperty clip = clips.get(0);
            String mediaUrl = device.getLocalPath()+"/"+clip.getStringValue();
            MediaUtils mu = new MediaUtils();
            try {
                File mediaFile = new File(mediaUrl);
                SimpleFileInfo fileInfo = new SimpleFileInfo(mediaFile);
                FileUtils.setFileMediaInfo(mediaFile.getAbsolutePath(),fileInfo,FileType.video);
                if(fileInfo.getLength()<startTime){
                    startTime = 10;
                }
                String dateStr =StringUtils.date2string(now,"yyyy/MM/dd")+"/";
                //String orgFilePath = "/snap/"+dateStr+content.getId()+"_"+clip.getId()+"_"+startTime+".jpg";
                //File orgFile = new File(appRoot+"/"+orgFilePath);
                String dstFileName = "/upload/"+dateStr+content.getId()+"_"+
                        clip.getId()+"_"+startTime+"_"+width+"x"+height+".jpg";
                File dstFile = new File(appRoot+"/"+dstFileName);
                mu.snapContentPosterFromMediaFile(mediaFile.getAbsolutePath(),
                        dstFile.getAbsolutePath(),
                        width,height,startTime);
                if(dstFile.exists()){
                    //截图成功！
                    contentProperty.setExtraData(""+width+"x"+height);
                    contentProperty.setName(picProperty.getName());
                    contentProperty.setStringValue(dstFileName.replace("//", "/"));
                    contentProperty.setIntValue(0L);
                    contentPropertyLogicInterface.save(contentProperty);
                    //将海报推送到其他服务器
                    JsUtils jsUtils = new JsUtils();
                    jsUtils.saveAndPushSynFile(dstFile.getName(), dstFile.getAbsolutePath(),dstFileName,content.getCspId());
                }else{
                    logger.error("截图失败："+content.getName()+","+mediaFile.getAbsolutePath()+","+dstFile.getAbsolutePath());
                }

/*
                mu.snap(mediaUrl,orgFile.getAbsolutePath(),startTime);
                if(orgFile.exists()){
                    ImageControl imageControl = new ImageControl();
                    BufferedImage srcImg = imageControl.loadImageLocal(orgFile.getAbsolutePath());

                }
*/
            } catch (Exception e) {
                logger.error("截图时发生异常："+e.getMessage());
            }
        }
        //System.gc();
        return result;
    }
    public String appendParameter(String fieldName,ServletRequest request){
        String parameterName = fieldName.replaceFirst(".","_");
        return appendParameter(parameterName,fieldName,request);
    }
    public String appendParameter(String parameterName,String fieldName,ServletRequest request){
        String value = request.getParameter(parameterName);
        if(value!=null&&!"".equals(value.trim())){
            return " and "+fieldName+" like '%"+value+"%'";
        }
        return "";
    }
    public String appendLongParameter(String fieldName,ServletRequest request){
        String parameterName = fieldName.replaceFirst(".","_");
        return appendLongParameter(parameterName,fieldName,request);
    }
    public String appendLongParameter(String parameterName,String fieldName,ServletRequest request){
        String value = request.getParameter(parameterName);
        if(value!=null&&!"".equals(value.trim())){
            return " and "+fieldName+" = "+value;
        }
        return "";
    }

%>