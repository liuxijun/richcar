<%@ page import="com.fortune.util.AppConfigurator" %><%@ page
        import="com.fortune.rms.business.content.model.Content,com.fortune.rms.business.publish.model.Channel" %><%@ page
        import="java.util.Map" %><%@ page
        import="java.util.List" %><%@ page
        import="java.util.ArrayList" %><%@ page
        import="java.util.HashMap" %><%@ page
        import="com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface" %><%@ page
        import="com.fortune.rms.business.module.model.Property" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-8-15
  Time: 下午5:30
  To change this template use File | Settings | File Templates.
--%><%!
    public Map<String,Object> getChannelMap(Channel channel,String[] splits){
        Map<String,Object> channelMap = new HashMap<String,Object>();
        channelMap.put("id",channel.getId());
        String channelName =channel.getName();
        if(channelName!=null){
            if(splits!=null){
                for(String split:splits){
                    int p = channelName.indexOf(split);
                    if(p>0){
                        channelName = channelName.substring(0,p);
                    }
                }
            }
        }
        channelMap.put("name",channelName);
        channelMap.put("code",channel.getCode());
        channelMap.put("poster",channel.getPoster1());
        channelMap.put("parentId",channel.getParentId());
        return channelMap;
    }
    public Map<String,Object> getContentMap(Content content,String[] propertyIds,String serverIp,int serverPort,PropertyLogicInterface propertyLogic){
        Map<String,Object> result = new HashMap<String,Object>();
        Map<String,Object> properties = content.getProperties();
        String picIds = ",MEDIA_PIC_RECOM2," +
                "PHONE_MEDIA_POSTER_SMALL," +
                "PAD_MEDIA_POSTER_HORIZONTAL_BIG," +
                "PAD_MEDIA_POSTER_BIG," +
                "PC_MEDIA_POSTER_BIG," +
                "PC_MEDIA_POSTER_HORIZONTAL_BIG,";
        result.put("id",content.getId());
        String host = AppConfigurator.getInstance().getConfig("system.default.imageHost","http://itv.inhe.net/");
        if("serverIP".equals(host)&&(serverIp!=null&&!"".equals(serverIp))){
            host = "http://"+serverIp;
            if(serverPort!=80){
                host+=":"+serverPort;
            }
        }else{
            //host = "";
        }
        if(propertyLogic==null){
            try {
                propertyLogic = (PropertyLogicInterface)SpringUtils.getBean("propertyLogicInterface");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        for(String propertyId:propertyIds){
            if("".equals(propertyId)||"id".equals(propertyId)||null==propertyId){
                continue;
            }
            Property property = propertyLogic.getByCode(propertyId);
            Byte dataType = null;
            if(property!=null){
                dataType = property.getDataType();
            }
            Object value = properties.get(propertyId);
            if(value!=null){
                if(value instanceof String && "".equals(value.toString())){
                    //基于页面某些考虑，空数据也要进行输出
                    result.put(propertyId," ");
                }else{
                    String picId = ","+propertyId+",";
                    boolean isPicture=false;
                    if(property!=null){
                        if(PropertyLogicInterface.DATA_TYPE_FILE_PIC.equals(dataType)){
                            isPicture = true;
                        }
                    }else{
                        if(picIds.contains(picId)||(propertyId.contains("_PIC_")||propertyId.contains("_POSTER"))){
                            isPicture = true;
                        }
                    }
                    if(isPicture){
                        if(value instanceof ArrayList){
                            //猜测一下，如果是海报，则将其数组形态改成字符串形态
                            ArrayList list = (ArrayList) value;
                            if(list.size()>0){
                                value = list.get(0);
                            }
                        }
                        if(value instanceof String){
                            String valueStr = value.toString();
                            //value.toString();
                            //下面的代码是为了和手机的接口配合添加的。手机必须取得完整的图片链接
                            if(valueStr.startsWith("/")){
                                valueStr = host+valueStr;
                            }else{
                                valueStr =host+"/page/hbMobile/"+valueStr;
                            }
                            value = valueStr;
                        }
                    }else if("MEDIA_INTRO".equals(propertyId)){
                    }
                    if(value instanceof List){
                        //只有少数数据允许是数据，大多数都不是
                        if(PropertyLogicInterface.DATA_TYPE_FLV.equals(dataType)||
                                PropertyLogicInterface.DATA_TYPE_MP4.equals(dataType)){
                        }else{
                            List listValue = (List)value;
                            if(listValue.size()>0){
                                value = listValue.get(0);
                            }
                        }
                    }
                    result.put(propertyId,value);
                }
            }else{
                //基于页面某些考虑，空数据也要进行输出
                value = " ";
                if("MEDIA_LENGTH".equals(propertyId)
                        ||"STARS".equals(propertyId)){
                    value="0";
                }
                result.put(propertyId,value);
            }
        }
        Channel channel =(Channel) properties.get("channel");
        if(channel!=null){
            Map<String,Object> channelMap = new HashMap<String,Object>();
            channelMap.put("id",channel.getId());
            channelMap.put("name",channel.getName());
            channelMap.put("parentId",channel.getParentId());
            result.put("channel",channelMap);
        }
        return result;
    }
    public Map<String,Object> getContentMap(Content content,String contentPropertyIds){
        return getContentMap(content,contentPropertyIds,null,80,null);
    }
    public Map<String,Object> getContentMap(Content content,String contentPropertyIds,String serverIp,int serverPort,PropertyLogicInterface propertyLogicInterface){
        return getContentMap(content, contentPropertyIds.split(","),serverIp,serverPort,propertyLogicInterface);
    }
    public List<Map<String,Object>> getContentList(List<Content> contents,String propertyIds){
        return getContentList(contents,propertyIds,null,80);
    }
    public List<Map<String,Object>> getContentList(List<Content> contents,String propertyIds,String serverIp,int serverPort){
        if(contents==null){
            return new ArrayList<Map<String,Object>>(0);
        }
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>(contents.size());
        if(propertyIds==null||"".equals(propertyIds)){
            propertyIds = AppConfigurator.getInstance().getConfig("publish.default.simplePropertyIds",
                    "MEDIA_NAME,ID,MEDIA_ACTORS,MEDIA_LENGTH,MEDIA_DIRECTORS,MEDIA_INTRO," +
                            "MEDIA_PIC_RECOM2," +
                            "PHONE_MEDIA_POSTER_SMALL," +
                            "PAD_MEDIA_POSTER_HORIZONTAL_BIG," +
                            "PAD_MEDIA_POSTER_BIG," +
                            "PC_MEDIA_POSTER_BIG," +
                            "PC_MEDIA_POSTER_HORIZONTAL_BIG," +
                            "MEDIA_PIC_SEARCH,MEDIA_HOMETOWN," +
                            "MEDIA_AGE,STARS," +
                            "channelId");
        }
        try {
            PropertyLogicInterface propertyLogicInterface = (PropertyLogicInterface)SpringUtils.getBean("propertyLogicInterface");
            for(Content content:contents){
                result.add(getContentMap(content,propertyIds,serverIp,serverPort,propertyLogicInterface));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public List<Map<String,Object>> getContentList(List<Content> contents){
        return getContentList(contents,null,80);
    }
    public List<Map<String,Object>> getContentList(List<Content> contents,String serverIp,int serverPort){
        if(contents==null||contents.size()==0){
            return new ArrayList<Map<String,Object>>(0);
        }
        return getContentList(contents,null,serverIp,serverPort);
    }
%>