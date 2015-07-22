<%@ page import="com.fortune.rms.business.content.model.ContentProperty" %>
<%@ page import="com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface" %>
<%@ page import="com.fortune.util.SpringUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fortune.util.PageBean" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.fortune.util.HzUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 14-6-5
  Time: 下午4:34
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    ContentPropertyLogicInterface contentPropertyLogicInterface = (ContentPropertyLogicInterface)
            SpringUtils.getBean("contentPropertyLogicInterface",session.getServletContext());
    ContentProperty bean = new ContentProperty();
    String sql = "";
    bean.setPropertyId(676496266L);
    List<ContentProperty> clips = contentPropertyLogicInterface.search(bean,new PageBean(0,Integer.MAX_VALUE,"o1.stringValue","asc"));
    String result = "";
    String oldChannelPath = null;
    String oldContentPath = null;
    long oldContentId = -1;
    if(clips!=null&&clips.size()>0){
        Map<String,String> channelPaths = new HashMap<String,String>();
        channelPaths.put("movie","movie");
        channelPaths.put("电影","movie");
        channelPaths.put("电视剧","tv");
        channelPaths.put("风尚","fashion");
        channelPaths.put("纪实","nature");
        channelPaths.put("名栏目","variety");
        channelPaths.put("娱乐","amusement");
        boolean repairPath = !"false".equals(request.getParameter("repairPath"));
        for(ContentProperty clip:clips){
            String url = clip.getStringValue();
            while(url.startsWith("/")&&url.length()>1){
                url = url.substring(1);
            }
            int p = url.lastIndexOf("/");
            if(p>0){
                String path = url.substring(0,p);
                int p0 = path.lastIndexOf("/");
                if(p0>0){
                    String channelPath = path.substring(0,p0);
                    String newChannelPath;
                    if(repairPath){
                        newChannelPath =channelPaths.get(channelPath);
                    }else{
                        newChannelPath =channelPaths.get(oldChannelPath);
                    }

                    if(newChannelPath==null){
                        newChannelPath = HzUtils.getFullSpell(oldChannelPath);
                    }
                    if(newChannelPath==null){
                        newChannelPath = "movie^M";
                    }
                    String contentPath = path;
                    //String fileName = url.substring(p);
                    if(!contentPath.equals(oldContentPath)){
                        if(oldContentPath!=null&&oldContentId>=0){
                            if(repairPath){
                                newChannelPath =channelPaths.get(oldChannelPath);
                                result += "mv "+newChannelPath+"^M/"+oldContentId+"^M "+newChannelPath+"^M/"+oldContentId+"\n";
                            }else{
                                result += "mv " + oldContentPath + " "+oldChannelPath+"/"+oldContentId+"\n";
                            }
                        }
                        oldContentPath = contentPath;
                        oldContentId = clip.getContentId();
                    }
                    if(!channelPath.equals(oldChannelPath)){
                        if(oldChannelPath!=null){
                            if(repairPath){
                                newChannelPath =channelPaths.get(oldChannelPath);
                                result += "mv " +newChannelPath+"^M "+newChannelPath+"\n";
                            }else{
                                result += "mv " +oldChannelPath+" "+newChannelPath+"\n";
                            }
                        }
                        oldChannelPath = channelPath;
                    }
                    if(repairPath){
                        newChannelPath =channelPaths.get(channelPath);
                        result += "mv "+newChannelPath+"^M/"+clip.getContentId()+"^M/"+clip.getContentId()+"_"+clip.getIntValue()+"_"+clip.getDesp()+".mp4^M "
                                +newChannelPath+"^M/"+clip.getContentId()+"^M/"+clip.getContentId()+"_"+clip.getIntValue()+"_"+clip.getDesp()+".mp4\n";
                        sql += "update CONTENT_PROPERTY set STRING_VALUE='"+newChannelPath+"/"+
                                clip.getContentId()+"/"+clip.getContentId()+"_"+clip.getIntValue()+"_"+clip.getDesp()+".mp4' where ID=" +
                                clip.getId()+";\n";
                    }else{
                        result += "mv "+url+" "+path+"/"+clip.getContentId()+"_"+clip.getIntValue()+"_"+clip.getDesp()+".mp4\n";
                    }
                }
            }
        }
        if(oldChannelPath!=null){
            String newChannelPath = channelPaths.get(oldChannelPath);
            if(newChannelPath==null){
                newChannelPath = HzUtils.getFullSpell(oldChannelPath);
            }
            result += "mv " +oldChannelPath+" "+newChannelPath+"\n";
        }
    }
%>
<html>
<head>
    <title></title>
</head>
<body>
sql<br/>
<pre><%=sql%></pre>
rename.sh<br/>
<pre><%=result%></pre>
</body>
</html>
