<%@ page import="com.fortune.rms.business.publish.model.Channel" %><%@ page
        import="java.util.Map" %><%@ page
        import="java.util.HashMap" %>
<%@ page
        import="com.fortune.rms.business.content.model.Content" %><%@ page
        import="com.fortune.rms.business.module.model.Module" %><%@ page
        import="java.util.ArrayList" %><%@ page
        import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2014/10/15
  Time: 21:36
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%!
    public Map<String,Object> getChannelMap(Channel channel){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("id",channel.getId());
        result.put("name",channel.getName());
        result.put("parentId",channel.getParentId());
        result.put("parentName",channel.getParentName());
        return result;
    }
    public List<Map<String,Object>> getChannels(List<Channel> channels){
        if(channels==null){
            return new ArrayList<Map<String,Object>>(0);
        }
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>(channels.size());
        for(Channel c:channels){
            result.add(getChannelMap(c));
        }
        return result;
    }
    public String getChannelJson(Channel channel,boolean simpleFormat){
        if(simpleFormat){
            return JsonUtils.getJsonString(getChannelMap(channel));
        }else{
            return channel.toString();
        }

    }
    public Map<String,Object> getSimpleContentMap(Content content,Module module){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("id",content.getId());
        result.put("name",content.getName());
        result.put("poster",content.getPost1Url());
        result.put("posterBig",content.getPost2Url());
        result.put("date",content.getProperty3());
        result.put("intro",content.getIntro());
        String type = "unknown:"+content.getModuleId();
        if(module!=null){
            type = module.getCode();
        }
        result.put("type",type);
        Long count = content.getAllVisitCount();
        if(count==null){
            count = 0L;
        }
        result.put("allVisitCount",count);
        return result;
    }
    public String getContentJson(Content content,boolean simpleFormat,Module module){
        if(simpleFormat){
            return JsonUtils.getJsonString(getSimpleContentMap(content,module));
        }else{
            return content.toString();
        }
    }
    public String getContentsJSON(List<Content> contents,boolean simpleFormat,Module module){
        if(simpleFormat){
            List<Map<String,Object>> result = new ArrayList<Map<String,Object>>(contents.size());
            for(Content content:contents){
                result.add(getSimpleContentMap(content,module));
            }
            return JsonUtils.getJsonString(result);
        }else{
            JsonUtils json=new JsonUtils();
            return json.getListJson(contents);
        }
    }
%>