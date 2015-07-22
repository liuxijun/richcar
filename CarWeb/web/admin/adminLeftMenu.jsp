<%@ page
        import="com.fortune.common.business.security.model.Menu,
        com.fortune.util.*,com.fortune.common.business.security.model.Permission,com.fortune.rms.business.csp.model.AdminCsp,
        com.fortune.rms.business.csp.logic.logicInterface.AdminCspLogicInterface,
        com.fortune.common.business.security.logic.logicInterface.PermissionLogicInterface,
        com.fortune.common.Constants,
        com.opensymphony.xwork2.ActionContext,
        com.fortune.common.business.security.model.Admin" %><%@ page import="java.util.*" %><%@ page import="com.fortune.rms.business.csp.model.Csp" %><%@ page import="com.fortune.rms.business.csp.logic.logicInterface.CspLogicInterface" %><%@ page import="org.apache.log4j.Logger" %><%@ page
        contentType="text/html;charset=UTF-8" %><%@ taglib
        prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@include
        file="adminMenu.jsp"%><%
    Admin admin = (Admin) session.getAttribute(Constants.SESSION_ADMIN);
    if(admin==null){
%><html>
<body>已经超时</body>
</html><%        return;
    }

    Logger logger = Logger.getLogger("com.fortune.rms.jsp.adminLeftMenu.jsp");
    boolean isSystemAdmin = false;
    //判断是否是系统管理员
    if(admin.getIsSystem()!=null&&admin.getIsSystem()==1){
        isSystemAdmin = true;
    }

    //获得 可以操作 adminCsp 的接口 对象
    AdminCspLogicInterface adminCspLogic = (AdminCspLogicInterface)SpringUtils.getBean("adminCspLogicInterface",session.getServletContext());
    //查询当前用户绑定的csp
    List<AdminCsp> bindCsp =  adminCspLogic.search(new AdminCsp(-1,admin.getId(),-1,-1));
    //获得从前台传入的cspId
    logger.debug("发现绑定的csp数量是："+bindCsp.size());
    int cspSelected = StringUtils.string2int(request.getParameter("cspId"),-1);
    //当前台没没有传入cspId时。
    if(cspSelected<0){
        //如果为管理员权限，则赋予默认值1
        if (isSystemAdmin){
            cspSelected=0;
        }else{
            //否则，
            for(AdminCsp ac:bindCsp){
                if(cspSelected<0){
                    cspSelected = ac.getCspId();
                }else{
                    if(ac.getIsDefaultCsp()!=null&&ac.getIsDefaultCsp()==1){
                        cspSelected = ac.getCspId();
                    }
                }
            }
        }
    }

    if(cspSelected>0){
        admin.setCspId(cspSelected);
        CspLogicInterface cspLogicInterface = (CspLogicInterface) SpringUtils.getBean("cspLogicInterface");
        try {
            Csp loginCsp = cspLogicInterface.get(cspSelected);
            session.setAttribute(Constants.SESSION_ADMIN_CSP,loginCsp);
        } catch (Exception e) {
            if(cspSelected==1){
                Csp loginCsp = new Csp();
                loginCsp.setAlias("/");
                session.setAttribute(Constants.SESSION_ADMIN_CSP,loginCsp);
            }
        }
    }else if(cspSelected==0){
        Csp loginCsp = new Csp();
        admin.setCspId(1);
        loginCsp.setId(1L);
        cspSelected = admin.getCspId();
        loginCsp.setAlias("/");
        session.setAttribute(Constants.SESSION_ADMIN_CSP,loginCsp);
    }
    {
        //获得操作权限的接口对象
        PermissionLogicInterface permissionLogic = (PermissionLogicInterface) SpringUtils.getBean("permissionLogicInterface",
                session.getServletContext());
        Integer adminId = admin.getId();
        if(admin.getIsRoot()!=null&&admin.getIsRoot()==1){
            //adminId = -1;
        }
        //获得当前用户绑定csp的权限
        List<Permission> permissions = permissionLogic.getPermissionOfOperator(adminId,cspSelected);
        //定义一个用于封装操作权限的map集合
        Map<String, Permission> pmaps = new java.util.HashMap<String, Permission>();
        for(Permission p:permissions){
            //将权限的引用和权限对象保存到map集合中
            pmaps.put(p.getTarget(),p);
        }
        //将用户权限保存到session中
        session.setAttribute(Constants.SESSION_ADMIN_PERMISSION,pmaps);
    }
%>
<html>
<head>
    <title>系统管理左边菜单栏管理系统</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link href="CSS/manage.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript">
        function gotoCsp(cspId){
            //
            window.location.href="adminLeftMenu.jsp?cspId="+cspId+"&date="+new Date().getTime();
        }
        function onLoad(){
            var mainWin = window.parent.frames["main"];//
            if(mainWin!=null){
                mainWin.location.href="../system/noticeList.jsp?date="+new Date().getTime();
            }
        }
    </script>
</head>
<body style="overflow-y: auto; overflow-x: hidden;" onload="onLoad()">
<table width="311" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td colspan="2"><img alt="后台菜单" src="../images/2-1-0-_07.jpg" width="311"></td>
    </tr>
    <tr>
        <td width="45" background="../images/2-1-0-_08.jpg">&nbsp;</td>
        <td width="266">

            <table align="center" width="100%">
                <tr>
                    <td>
                        您好，${sessionOperator.realname}，请选择：
                    </td>
                </tr>
                <tr>
                    <td>
                        <select name="cspId" style="width: 150px" onchange="gotoCsp(this.value)">
                            <%
                                //如果是系统管理员，默认在下拉框中填入系统管理选项，并选中状态
                                if(isSystemAdmin){
                                    String option = "<option value='0'";
                                    if(cspSelected == 0){
                                        option += " selected ";
                                    }
                                    option +=">系统管理</option>";
                                    out.println(option);
                                }
                                //遍历当前用户绑定的csp，并生成相应的下拉选项
                                for(AdminCsp ac:bindCsp){
                                    if(ac.getCspId()==1){
                                        //continue;
                                    }
                                    String option = "<option value='"+ac.getCspId()+"'";
                                    if(cspSelected==ac.getCspId()){
                                        option += " selected ";
                                    }
                                    option +=">"+ac.getCsp().getName()+"</option>";
                                    out.println(option);
                                }
                            %>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <script type="text/javascript">
                            //控制菜单的展开及收合
                            function SwitchMenu(obj, sty)
                            {
                                if (document.getElementById)
                                {
                                    var el = document.getElementById(obj);
                                    var ml = document.getElementById(sty);
                                    var ar = document.getElementById("masterdiv").getElementsByTagName("ul");
                                    var mr = document.getElementById("masterdiv").getElementsByTagName("h1");
                                    if (el.style.display != "block")
                                    {
                                        for (var i = 0; i < ar.length; i++)
                                        {
                                            if (ar[i].className == "subMenu")
                                                ar[i].style.display = "none";
                                            if (mr[i].className == "menu1")
                                                mr[i].className = "menu";
                                        }
                                        el.style.display = "block";
                                        ml.className = "menu1";
                                    }
                                    else
                                    {
                                        el.style.display = "none";
                                        ml.className = "menu";
                                    }
                                }
                            }
                        </script>
                        <div id="masterdiv">
                            <%

                                //所有的菜单    {
                                //{
                                Admin opLogined =(Admin)session.getAttribute(Constants.SESSION_ADMIN);
                                if(opLogined==null){
                                    opLogined = new Admin();
                                    opLogined.setRealname("尚未登录");
                                }
                                //从配置文件中判断是否需要判断文件
                                boolean needCheckPermission = com.fortune.util.AppConfigurator.getInstance().getBoolConfig("menuNeedCheckPermission",true);
                                Integer isRoot = opLogined.getIsRoot();
                                if(isRoot!=null&&isRoot==1){
                                    // todo 让root也从权限中取值
                                    //needCheckPermission = false;
                                }
                                //从session 中获得当前的权限
                                Map<String, Permission> map = (Map<String, Permission>) session.getAttribute(Constants.SESSION_ADMIN_PERMISSION);
                                if(needCheckPermission&&map == null){
                                    System.err.println("用户权限为空！不能继续！");
                                    return;
                                }
                                //获得定义好的现实菜单
                                List<Menu> allMenus = (List<Menu>)request.getAttribute("menus");
                                if(allMenus!=null){
                                    //System.out.println("输出菜单数量："+allMenus.size());
                                    //遍历当前菜单，并检查是否要显示
                                    for(Menu menu:allMenus){
                                        out.println(getMenus(menu,needCheckPermission,map));
                                    }
                                }

                            %>
                        </div>


                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

</body>
</html>
<%!
    public String getMenus(Menu menu,boolean needCheckPermission,Map<String,Permission>map){
        StringBuffer result = new StringBuffer();
        //获得菜单的id
        String menuId = menu.getId().toString();
        //生成相应的 页面标签
        result.append("<h1 class=\"menu\" id=\"m").append(menuId).append(
                "\" onClick=\"SwitchMenu('sub").append(menuId).append("','m")
                .append(menu.getId()).append("')\">");
        result.append(menu.getName()).append( "</h1>\r\n");
        result.append("<ul class=\"subMenu\" id=\"sub").append(menu.getId()).append("\">\r\n");
        //定义一个是否显示主菜单的开关变量
        boolean outputOneSubMenu=false;
        //遍历当前主菜单的字菜单
        for(Menu subMenu:menu.getSubMenus()){
            boolean shouldPrintThisMenu = false;
            if(!needCheckPermission){
                shouldPrintThisMenu = true;
            }else{
                String permissionStr =subMenu.getPermissionStr();
                if(permissionStr==null||"".equals(permissionStr)){
                    shouldPrintThisMenu = true;
                }else{
                    String[] permissions = permissionStr.split(",");
                    for(String p:permissions){
                        if(map.containsKey(p)){
                            shouldPrintThisMenu = true;
                            break;
                        }
                    }
                }
            }
            if(shouldPrintThisMenu){
                result.append("<li class=\"menuOff\" id=s_td_")
                        .append(subMenu.getId()).append(" ><a href=\"")
                        .append(subMenu.getUrl()).append("\" target=\"main\">")
                        .append(subMenu.getName()).append("</a></li>\r\n");
                outputOneSubMenu=true;
            }
        }
        result.append("</ul>\r\n");
        if(outputOneSubMenu){
            return result.toString();
        }else{
            return "";
        }
    }
%>