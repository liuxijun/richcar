<%@ page import="com.fortune.rms.business.content.model.Content" %><%@ page
        import="java.util.List" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentRecommendLogicInterface" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.rms.business.publish.model.Channel" %><%@ page
        import="java.util.Map" %><%@ page
        import="java.util.HashMap" %><%@ page import="com.fortune.common.Constants" %><%@ page
        import="com.fortune.rms.business.frontuser.model.FrontUser" %><%@ page
        import="com.fortune.rms.business.publish.logic.logicInterface.RecommendLogicInterface" %><%@ page
        import="com.fortune.rms.business.publish.model.Recommend" %><%@ page import="com.fortune.util.*" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%
//    ChannelLogicInterface channelLogicInterface = (ChannelLogicInterface) SpringUtils.getBean("channelLogicInterface", session.getServletContext());
//    ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface", session.getServletContext());
    ContentRecommendLogicInterface contentRecommendLogicInterface = (ContentRecommendLogicInterface) SpringUtils.getBean("contentRecommendLogicInterface", session.getServletContext());
    final RecommendLogicInterface recommendLogicInterface = (RecommendLogicInterface) SpringUtils.getBean("recommendLogicInterface", session.getServletContext());
//    System.out.println("\n"+createSql());
 //   final List<Channel> channels = getRootChannels(channelLogicInterface);
//       List<Content> sliderContents = getRecommendContentsOfSlider(contentRecommendLogicInterface);
    // added by mlwang @2014-11-10，前台用户可以观看的频道列表
    List<Long> idList = null;
    if( session.getAttribute(Constants.SESSION_FRONT_USER_CHANNEL) != null){
        idList = (List)session.getAttribute(Constants.SESSION_FRONT_USER_CHANNEL);
    }
    final List<Long> channelIdList = idList;
    FrontUser user = null;
    if( session.getAttribute(Constants.SESSION_FRONT_USER) != null){
        user = (FrontUser)session.getAttribute(Constants.SESSION_FRONT_USER);
    }
    final FrontUser frontUser = user;
    // ===================================

    String callback = request.getParameter("callback");
    if(callback==null){
        out.println("var serverData=");
    }else{
        out.println("\r\n"+callback+"(");
    }
    String recommends = request.getParameter("ids");
    if(recommends == null){
        recommends = "djdt,yjzc,yast,xzzq,474431621_ch1,474431621_ch2,474431621_ch3,474431621_ch4,474431626_ch1," +
                "474431626_ch2,474431626_ch3,474431626_ch4,474431631_ch1,474431631_ch2,474431631_ch3,474431631_ch4,474431637_ch1," +
                "474431637_ch2,474431637_ch3,474431637_ch4";
    }else if("all".equals(recommends)){
        recommends = (String)CacheUtils.get("allRecommendCodes","allRecommendCodes",new DataInitWorker(){
           public Object init(Object key,String cacheName){
               List<Recommend> recommendList = recommendLogicInterface.search(new Recommend(),new PageBean(1,100,"o1.isSystem","asc"));
               String result = "";
               for(Recommend recommend:recommendList){
                   Long isSystem = recommend.getIsSystem();
                   if(!RecommendLogicInterface.NOT_SYSTEM_RECOMMEND.equals(isSystem)){
                       if(!"".equals(result)){
                           result+=",";
                       }
                       result += recommend.getCode();
                   }
               }
               return result;
           }
        });
    }
    indexLogger.debug("请求获取："+recommends);
    //Map<String,List<Map<String,Object>>> results= getRecommendContentsOfChannels(recommends.split(","),contentRecommendLogicInterface);
    Map<String,List<Map<String,Object>>> results= getRecommendContentsOfChannels(recommends.split(","),contentRecommendLogicInterface, channelIdList, frontUser);
    out.print(JsonUtils.getJsonString(results));
    if(callback==null){
        out.println(";");
    }else{
        out.println(");");
    }
    indexLogger.debug("获取完毕："+recommends);
%><%!
    Logger indexLogger = Logger.getLogger("com.fortune.rms.jsp.js.index.jsp");
    //public Map<String,List<Map<String,Object>>> getRecommendContentsOfChannels(String[] channelCodes, ContentRecommendLogicInterface contentRecommendLogicInterface){
    public Map<String,List<Map<String,Object>>> getRecommendContentsOfChannels(String[] channelCodes,
                       ContentRecommendLogicInterface contentRecommendLogicInterface,
                       List channelIdList, FrontUser user){
        Map<String,List<Map<String,Object>>> result = new HashMap<String,List<Map<String,Object>>>(channelCodes.length+1);
        List<Map<String,Object>> recommends = new ArrayList<Map<String, Object>>(channelCodes.length);
        for(String code:channelCodes){
            recommends.add(getRecommend(code,contentRecommendLogicInterface));
            List<Map<String,Object>> contents = getRecommendContentsOfChannel(code,contentRecommendLogicInterface, channelIdList, user);
            result.put(code,contents);
        }
        result.put("allRecommends",recommends);
        return result;
    }
    @SuppressWarnings("unchecked")
    public Map<String,Object> getRecommend(String recommendCode,final ContentRecommendLogicInterface contentRecommendLogicInterface){
        return (Map<String,Object>) CacheUtils.get(recommendCode,"recommendCache",new DataInitWorker(){
           public Object init(Object key,String cacheName){
               Map<String,Object> result = new HashMap<String,Object>();
               Recommend recommend = contentRecommendLogicInterface.getRecommendByCode(key.toString());
               if(recommend!=null){
                   result.put("id",recommend.getId());
                   result.put("name",recommend.getName());
                   result.put("code",recommend.getCode());
                   Long channelId = recommend.getChannelId();
                   if(channelId!=null&&channelId>0){
                       Channel channel =(Channel) TreeUtils.getInstance().getObject(Channel.class,channelId);
                       if(channel!=null){
                           result.put("channelName",channel.getName());
                           result.put("channelCode",channel.getCode());
                           result.put("channelPoster",channel.getPoster1());
                       }
                   }
                   result.put("channelId",recommend.getChannelId());
               }
               return result;
           }
        });
    }
    public List<Map<String,Object>> getRecommendContentsOfChannel(Channel channel, final ContentRecommendLogicInterface contentRecommendLogicInterface, final List channelIdList, final FrontUser user) {
        final String channelCode = channel.getCode();
        return getRecommendContentsOfChannel(channelCode,contentRecommendLogicInterface, channelIdList, user);
    }

    public List<Map<String,Object>> getRecommendContentsOfSlider(final ContentRecommendLogicInterface contentRecommendLogicInterface, final List channelIdList, final FrontUser user) {
        indexLogger.debug("准备获取大的横版推荐内容。。。。。");
        return getRecommendContentsOfChannel("slider",contentRecommendLogicInterface, channelIdList, user);
    }
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getRecommendContentsOfChannel(final String channelCode, final ContentRecommendLogicInterface contentRecommendLogicInterface,
                                                                  final List channelIdList, final FrontUser user) {
        String recommendKey = channelCode;
        if( user != null){
            recommendKey += "_" + user.getOrganizationId() + "_" + user.getTypeId();
        }
        return (List<Map<String,Object>>) CacheUtils.get(recommendKey, "channelRecommendContents", new DataInitWorker() {
            public Object init(Object key, String cacheName) {
                //indexLogger.debug("准备获取推荐："+key);

                List<Content> contents = contentRecommendLogicInterface.getContents(channelCode, channelIdList, user);
                List<Map<String,Object>> result = new ArrayList<Map<String,Object>>(contents.size());
                for(Content c:contents){
                    result.add(getSimpleContentMap(c,null));
                }
                indexLogger.debug("推荐[" + key +                       "]获得了" + result.size() + "个影片！");
                return result;
            }
        });
    }

%><%@include file="/page/js/jsUtils.jsp"%>