<%@ page
        contentType="text/html;charset=UTF-8" %><%@ page
        import="com.fortune.common.business.security.model.Menu" %><%@ page
        import="java.util.List" %><%@ page
        import="java.util.ArrayList" %><%@ page
        import="com.fortune.common.business.security.logic.logicInterface.MenuLogicInterface" %><%@ page
        import="com.fortune.util.SpringUtils" %>
<%@ page import="com.fortune.common.business.security.model.Role" %><%
    int menuId = 1;
    int id=menuId;
    List<Menu> allMenu = new ArrayList<Menu>();
    Menu menu = new Menu(id++,"发布视频","#");
    {
        menu.addSubMenu(new Menu(id++,"发布视频","/pub/pubFile.jsp",-1,"/pub/pubFile.jsp;do.upload;",menuId));
        menu.addSubMenu(new Menu(id++,"批量发布","/pub/batchPub.jsp",-1,"/pub/batchPub.jsp;",menuId));
        menu.addSubMenu(new Menu(id++,"内容一览","/pub/pubList.jsp",-1,"/pub/pubList.jsp;",menuId));
    }
    allMenu.add(menu);
    menu = new Menu(id++,"视频管理","#");
    {
        menuId = menu.getId();
        menu.addSubMenu(new Menu(id++,"视频审核","/man/audit.jsp",-1,"/man/audit.jsp",menuId));
        menu.addSubMenu(new Menu(id++,"视频管理","/man/contentMan.jsp",-1,"/man/contentMan.jsp",menuId));
        menu.addSubMenu(new Menu(id++,"栏目管理","/man/channel.jsp",-1,"/man/channel.jsp",menuId));
        menu.addSubMenu(new Menu(id++,"推荐管理","/man/recommend.jsp",-1,"/man/recommend.jsp",menuId));
        menu.addSubMenu(new Menu(id++,"访问统计","#",-1,"",menuId));
    }
    allMenu.add(menu);
    menu = new Menu(id++,"系统管理","#");
    {
        menuId = menu.getId();
        menu.addSubMenu(new Menu(id++,"系统一览","",-1,"",menuId));
        menu.addSubMenu(new Menu(id++,"转码任务一览","",-1,"",menuId));
        menu.addSubMenu(new Menu(id++,"前台用户管理","/sys/user.jsp",-1,"",menuId));
        menu.addSubMenu(new Menu(id++,"组织管理","",-1,"/sys/organization.jsp",menuId));
        menu.addSubMenu(new Menu(id++,"用户类型管理","/sys/userType.jsp",-1,"",menuId));
        menu.addSubMenu(new Menu(id++,"管理员管理","/sys/admin.jsp",-1,"",menuId));
        menu.addSubMenu(new Menu(id++,"编码配置","#",-1,"",menuId));
        menu.addSubMenu(new Menu(id++,"系统个性化","#",-1,"",menuId));
    }
    allMenu.add(menu);
    menu = new Menu(id++,"超户专属","#");
    {
        menuId = menu.getId();
        menu.addSubMenu(new Menu(id++,"安全日志","",-1,"",menuId));
        menu.addSubMenu(new Menu(id++,"角色管理","",-1,"",menuId));
        menu.addSubMenu(new Menu(id++,"权限管理","",-1,"",menuId));
        menu.addSubMenu(new Menu(id++,"模版管理","",-1,"",menuId));
    }
    allMenu.add(menu);
    MenuLogicInterface menuLogicInterface = (MenuLogicInterface) SpringUtils.getBean("menuLogicInterface",session.getServletContext());
    for(Menu m:allMenu){
        Menu savedM = menuLogicInterface.save(m);
        out.println(savedM);
        for(Menu sm :m.getSubMenus()){
            sm.setParentId(savedM.getId());
            sm=menuLogicInterface.save(sm);
            out.println(sm);
        }
    }
    Role role = new Role(1,"超户专属","只有超级管理员可以使用的角色");

%>