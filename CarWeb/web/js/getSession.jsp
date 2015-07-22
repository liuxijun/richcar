<%@ page import="com.fortune.common.Constants" %><%@ page
        import="com.fortune.rms.business.publish.model.Channel" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.rms.business.frontuser.model.FrontUser" %><%@ page
        import="com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface" %><%@ page
        import="com.fortune.util.*" %><%@ page
        import="com.fortune.wsdl.zjlib.ZjLibAuth" %><%@ page
        import="com.fortune.rms.business.user.model.UserLogin" %><%@ page
        import="java.util.*" %><%@ page
        import="java.security.NoSuchAlgorithmException" %><%@ page
        import="com.fortune.rms.business.frontuser.logic.logicInterface.FrontUserLogicInterface" %><%@ page
        import="com.fortune.rms.business.user.logic.logicInterface.UserLoginLogicInterface" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%
    FrontUser user = (FrontUser)(session.getAttribute(Constants.SESSION_FRONT_USER));
    if(user==null){
        Cookie[] cookies = request.getCookies();
        String userLoginId = null;
        String userLoginPwd = null;
        for(Cookie cookie:cookies){
            if(cookie!=null){
                if("userLoginId".equals(cookie.getName())){
                    userLoginId = cookie.getValue();
                }else if("userLoginPwd".equals(cookie.getName())){
                    userLoginPwd = cookie.getValue();
                }
            }
        }
        if(userLoginId!=null&&!userLoginId.isEmpty()){
            logger.debug("cookie中找到了以前登录的账号口令，所以尝试一下登录："+userLoginId+","+userLoginPwd);
            if(userLoginPwd!=null&&!userLoginPwd.isEmpty()){
                user = frontUserLogon(userLoginId,userLoginPwd,request);
            }
        }
    }
    String result = null;
    if(user!=null){
        result = "{userId:'"+user.getUserId()+"',name:'" +user.getName()+"'}";
    }
    out.println("var user ="+result+";");
    //logger.debug("userId="+user+",queryString="+request.getQueryString());
    List<Channel> topLevelChannel = (List<Channel>) session.getAttribute(Constants.SESSION_FRONT_USER_TOP_CHANNEL);
    String topChannel;
    if(topLevelChannel==null){
        final ChannelLogicInterface channelLogicInterface = (ChannelLogicInterface) SpringUtils.getBean("channelLogicInterface",session.getServletContext());
        topLevelChannel =(List<Channel>) CacheUtils.get("topLevelChannels","channel",new DataInitWorker(){
            public Object init(Object key,String cacheName){
                long systemChannelId = channelLogicInterface.getSystemChannelId();
                logger.debug("将搜索所有parentId="+systemChannelId+"的频道作为首页默认频道列表");
                return channelLogicInterface.getChannelList(systemChannelId);
            }
        });
    }
    if(topLevelChannel != null && topLevelChannel.size() > 0) {
        List<Map<String,Object>> channels = new ArrayList<Map<String, Object>>();
        for(Channel channel:topLevelChannel){
            Map<String,Object> cMap = new HashMap<String, Object>();
            cMap.put("id",channel.getId());
            cMap.put("name",channel.getName());
            cMap.put("parentId",channel.getParentId());
            channels.add(cMap);
        }
         topChannel = JsonUtils.getJsonString(channels);
         out.print("var channels ="+topChannel+";");
    } else {
         out.print("var channels ='';");
    }
    out.print("var innerWebStat=" + AppConfigurator.getInstance().getBoolConfig("system.weichai.innerWebStat",false)+
            ";");
%><%!
    private Logger logger = Logger.getLogger("com.fortune.rms.jsp.getSession.jsp");
    public FrontUser frontUserLogon(String userId,String pwd,HttpServletRequest request){
        boolean userLogined = false;
        boolean errorMessageAdded =false;
        FrontUser u = new FrontUser();
        String ip= request.getRemoteAddr();
        HttpSession session = request.getSession();
        AppConfigurator config=AppConfigurator.getInstance();
        FrontUserLogicInterface frontUserLogicInterface = (FrontUserLogicInterface) SpringUtils.getBean("frontUserLogicInterface",request.getServletContext());
        UserLoginLogicInterface userLoginLogicInterface = (UserLoginLogicInterface) SpringUtils.getBean("userLoginLogicInterface",request.getServletContext());
        if(userId== null || userId.isEmpty() ||
                pwd == null ||pwd.isEmpty()){
            logger.error("登录信息为空或不完整！");
            return null;
        }else{
            if("zjlib".equals(config.getConfig("system.frontUser.loginType", "local"))){
                logger.debug("认证方式是浙图，将调用相应接口.....");
                u = ZjLibAuth.auth(userId, pwd, ip);
                if(u==null){
                    logger.warn("认证结果失败："+ip+","+userId+",pwd="+pwd);
                    UserLogin loginLog = new UserLogin(-1, u.getUserId(), new Date(),
                            -1, ip, u.getName(),
                            "用户通过浙图SOAP接口登录失败",-1);
                    userLoginLogicInterface.save(loginLog);
                }else{
                    userLogined = true;
                    userLoginLogicInterface.save(new UserLogin(-1, u.getUserId(), new Date(),
                            u.getTypeId(), ip, u.getName(),
                            "用户通过SOAP接口登录",u.getOrganizationId()));
                }
            }else{
                u.setUserId(userId);
                List<FrontUser> l = frontUserLogicInterface.search(u);
                if( l == null || l.size() == 0){
                    userLogined=false;
                }else{
                    // 比对密码
                    if(pwd.length()<32){
                        //浙图是明码传送，所以如果不是浙图，可能要看一下是不是32位字符长度的MD5字符串
                        try {
                            pwd = MD5Utils.getMD5String(pwd);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    }
                    for(FrontUser user:l){
                        u=user;
                        if( u.getPassword().equals(pwd)&&u.getUserId().equalsIgnoreCase(userId)){
                            // 检查是不是被锁定
                            if( FrontUser.USER_STATUS_NORMAL.equals(u.getStatus())){
                                // 登录
                                u = user;
                                userLogined = true;
                                userLoginLogicInterface.save(new UserLogin(-1, u.getUserId(), new Date(),
                                        UserLoginLogicInterface.LOGIN_STATUS_FROM_WAP_HEADER, ip, u.getName(),
                                        "用户通过登录",u.getOrganizationId()));
                                break;
                            }else{
                                userLogined = (false);
                                if(  FrontUser.USER_STATUS_LOCKED.equals(u.getStatus())) {
                                    logger.debug("账号被锁定:" + u.getName());
                                }else if(FrontUser.USER_STATUS_UN_AUDITED.equals(u.getStatus())){
                                    logger.debug("账号尚未通过审核:" + u.getName());
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
        if(!userLogined){
            logger.warn("用户登录失败：" + userId + "," + pwd);
            //
            return null;
        } else {
            session.setAttribute(Constants.SESSION_FRONT_USER, u);
            // 解析用户可以观看的所有频道，保存在Session中
            session.setAttribute(Constants.SESSION_FRONT_USER_CHANNEL, frontUserLogicInterface.getUserAuthorizedChannel( u) );
            //解析用户可以观看的一级频道,保存在Session中
            List<Channel> visibleChannelList = frontUserLogicInterface.
                    getTopLevelChannel(frontUserLogicInterface.getUserAuthorizedChannel( u));
            session.setAttribute(Constants.SESSION_FRONT_USER_TOP_CHANNEL,visibleChannelList);
            logger.debug("用户“" +u.getName()+","+u.getUserId()+"”登录成功");
            if(u.getTypeId()==null){
                u.setTypeId(5L);
            }
            if(u.getOrganizationId()==null){
                u.setOrganizationId(0L);
            }
        }
        return u;
    }

%>