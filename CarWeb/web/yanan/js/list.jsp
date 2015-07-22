<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2011-6-13
  Time: 10:32:00
  频道影片列表
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%@page
        import="com.fortune.util.*,
        com.fortune.rms.business.content.logic.logicInterface.*,
        com.fortune.rms.business.module.logic.logicInterface.*,
        com.fortune.rms.business.content.model.Content" %><%@ page
        import="com.fortune.rms.business.content.model.ContentProperty,
        com.fortune.rms.business.publish.model.Channel" %><%@ page
        import="com.fortune.rms.business.module.model.Property" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface" %><%@ page
        import="com.fortune.rms.business.frontuser.model.FrontUser" %><%@ page import="com.fortune.common.Constants" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %><%
    int start = StringUtils.string2int(request.getParameter("start"),0);
    int limit = StringUtils.string2int(request.getParameter("limit"),10);
    if(limit<=0){
        limit = 10;
    }

    // added by mlwang @2014-11-10，前台用户可以观看的频道列表
    List<Long> channelIdList = null;
    /*if( session.getAttribute(Constants.SESSION_FRONT_USER_CHANNEL) != null){
        channelIdList = (List)session.getAttribute(Constants.SESSION_FRONT_USER_CHANNEL);
    }*/

    FrontUser user = null;
    /*if( session.getAttribute(Constants.SESSION_FRONT_USER) != null){
        user = (FrontUser)session.getAttribute(Constants.SESSION_FRONT_USER);
    }*/
    // ===================================

    PageBean pageBean = new PageBean((start+limit)/limit,limit,request.getParameter("orderBy"),request.getParameter("orderDir"));
    if(pageBean.getOrderBy()==null){
        pageBean.setOrderBy("c.id");
        pageBean.setOrderDir("DESC");
    }
    String[] splits = new String[]{"(","（"};
    long sortNum= StringUtils.string2long(request.getParameter("sortNum"),0);
    long channelId = StringUtils.string2long(request.getParameter("channelId"),-1);
    String channelIds = request.getParameter("channelIds");
    boolean isChannels = StringUtils.string2bool(request.getParameter("isChannelIds"));
    long cspId =  StringUtils.string2long(request.getParameter("cspId"),-1);
    Logger logger = Logger.getLogger("com.fortune.rms.List.jsp");
    String searchValue = request.getParameter("searchValue");
//    searchValue = URLDecoder.decode(searchValue,"UTF-8");
    if(searchValue==null){
        request.getParameter("searchContent");
    }
    String searchType= request.getParameter("searchType");
    int repeatTimes = 0;
    if(searchValue!=null){
        while(searchValue.contains("%")){
            //logger.debug(i+"次转码searchValue="+searchValue);
            searchValue = java.net.URLDecoder.decode(searchValue,"UTF-8");
            repeatTimes++;
            if(repeatTimes>5){
                break;
            }
        }
        //logger.debug("searchValue="+searchValue);
        if("MEDIA_TIME".equals(searchType)){
            if(!searchValue.contains("%")){
                searchValue="%"+searchValue+"%";
            }
        }
    }

    String contentName = null;
    String directors = null;
    String actors = null;

    List<Content> contents = new ArrayList<Content>();
    Channel channel = new Channel();
    List<ContentProperty> searchValues = new ArrayList<ContentProperty>();
    if(channelId<0){
    }
    ContentLogicInterface contentLogic = (ContentLogicInterface) SpringUtils.getBean(
            "contentLogicInterface",session.getServletContext()
    );
    long propertyId;
    String searchDesc = "普通";
    if("MEDIA_NAME".equals(searchType)||"name".equals(searchType)){
        searchDesc = "影片名‘"+searchValue+"’";
         contentName = searchValue;
    }else if("MEDIA_ACTORS".equals(searchType)||"actors".equals(searchType)){
        searchDesc = "主演‘"+searchValue+"’";
         actors = searchValue;
    }else if("MEDIA_DIRECTORS".equals(searchType)||"directors".equals(searchType)){
         searchDesc = "导演‘"+searchValue+"’";
         directors = searchValue;
    }else if("BLUR".equals(searchType)){
         searchDesc = "模糊搜索‘"+searchValue+"’";
    }else if(searchType!=null&&!"".equals(searchType)&&!"null".equals(searchType)){
        String[] searchTypes = searchType.split("\\*");
        String[] values = searchValue.split("\\*");
        for(int i=0,typeCount=searchTypes.length,valueCount=values.length;i<typeCount;i++){
            if(i<valueCount){
                String value = values[i];
                String type = searchTypes[i];
                Property p = contentLogic.getPropertyByCode(type);
                if(p!=null){
                    String pName = p.getName();
                    int pos=pName.indexOf("]");
                    if(pos>=0){
                        pName = pName.substring(pos+1);
                    }
                    if(StringUtils.string2int(value,-1)<0){
                        searchDesc += pName + "‘"+searchValue+"’";
                    }
                    propertyId=p.getId();
                    Byte dataType = p.getDataType();
                    logger.debug("将会对数据进行搜索:"+p.getName()+"("+p.getCode()+")="+value);
                    if(PropertyLogicInterface.DATA_TYPE_COMBO.equals(dataType)||PropertyLogicInterface.DATA_TYPE_RADIO.equals(dataType)
                            ||PropertyLogicInterface.DATA_TYPE_CHECKBOX.equals(dataType)){

                        value = contentLogic.getPropertySelectValue(propertyId,value);
                        logger.debug("检查翻译字典，寻找searchValue实际对应id:"+value);
                    }
                    searchValues.add(new ContentProperty(-1,-1L,propertyId,null,value,null,null));
                }
            }
        }
    }
/*
    System.out.println("Search:name="+contentName+",actors="+actors+",directors="+directors+",type="
            +searchType+"(" +propertyId+
            "),value="+searchValue);
*/
    //System.out.println("channelId="+channelId+",cspId="+cspId);
    try {
        if(channelId>0){
            channel = contentLogic.getContentBindChannel(channelId,-1L);
        }else{
            channel.setCspId(cspId);
            if(searchValue!=null){
                channel.setName(searchDesc+"搜索结果：");
            }else{
                channel.setName("搜索结果");
            }
        }
        if("BLUR".equals(searchType)){
            //contents.addAll(contentLogic.list(cspId,2L,channelId,searchValue,pageBean));
            contents.addAll(contentLogic.list(cspId,2L,channelId,searchValue,pageBean, channelIdList, user));
        }else{
            if(isChannels){
                if(channelIds==null||"".equals(channelIds)){
                    if(channelId>0){
                        channelIds=""+channelId;
                    }
                }
                //contents.addAll(contentLogic.list(cspId,2L,channelIds,contentName,directors,actors,searchValues,pageBean));
                contents.addAll(contentLogic.list(cspId,2L,channelIds,contentName,directors,actors,searchValues,pageBean, channelIdList, user));
            }else{
                switch((int)sortNum){
                    case 4:
                        pageBean.setOrderBy("c.id");
                        break;
                    case 1:
                        pageBean.setOrderBy("c.weekVisitCount");
                        break;
                    case 2:
                        pageBean.setOrderBy("c.monthVisitCount");
                        break;
                    case 3:
                        pageBean.setOrderBy("c.allVisitCount");
                        break;
                }
                if(pageBean.getOrderDir()==null){
                    pageBean.setOrderBy("desc");
                }
                //contents.addAll(contentLogic.list(cspId,ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED,
                //        channelId,contentName,directors,actors,searchValues,pageBean));
                contents.addAll(contentLogic.list(cspId,ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED,
                        channelId,contentName,directors,actors,searchValues,pageBean, channelIdList, user));
            }

        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    Channel parentChannel = null;
    if(channel!=null){
        Long parentId = channel.getParentId();
        if(parentId!=null&&parentId>0){
           parentChannel = (Channel)TreeUtils.getInstance().getObject(Channel.class, parentId);
        }
    }
    if(parentChannel==null){
        parentChannel = new Channel();
    }
    String callBackFunction = request.getParameter("callback");
    String listData;
    String subChannels = "";

    listData = JsonUtils.getListJsonString("objs",getContentList(contents,request.getParameter("propertyIds"),request.getServerName(),request.getServerPort()),"total",pageBean.getRowCount());
    if(callBackFunction!=null){
        out.println("\r\n"+callBackFunction+"(");
    }else{
        response.setContentType("application/json");
//        listData = JsonUtils.getJsonString(getContentList(contents));
        ChannelLogicInterface channelLogicInterface = (ChannelLogicInterface)SpringUtils.getBean("channelLogicInterface",
                session.getServletContext());
        //List<Channel> channels = channelLogicInterface.getChannelList(""+channelId);
        // mod by mlwang @2014-11-10，增加频道过滤
        List<Channel> channels;
        if(AppConfigurator.getInstance().getBoolConfig("system.compactMode",false)){
            channels = channelLogicInterface.getChannelList(channelId, channelIdList);
        }else{
            channels = channelLogicInterface.getChannelList(channelId);
        }
        // =======

        List<Map<String,Object>> channelList = new ArrayList<Map<String,Object>>();
        if(channels!=null&&channels.size()>0){
            for(Channel ch:channels){
                channelList.add(getChannelMap(ch,splits));
            }
        }
        subChannels = ",\n  \"subChannels\":"+JsonUtils.getJsonString(channelList);
    }
%>{
  "channel":<%=JsonUtils.getJsonString(getChannelMap(channel,splits))%>,
  "parentChannel":<%=JsonUtils.getJsonString(getChannelMap(parentChannel,splits))%>,
  "listData":<%=listData%><%=subChannels%>
}<%
    if(callBackFunction!=null){
        out.print(");");
    }
%><%@include file="contentList.jsp"%>