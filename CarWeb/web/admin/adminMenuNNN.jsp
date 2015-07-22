<%@ page
        contentType="text/html;charset=UTF-8" %><%@ page
        import="com.fortune.common.business.security.model.Menu" %><%@ page
        import="java.util.List" %><%@ page
        import="java.util.ArrayList" %><%
    {
        List<Menu> menus = new ArrayList<Menu>();
        int menuId = 1;
        /*{
        Menu operatorMenu = new Menu(menuId++,"管理员管理",null,0l,"");
        operatorMenu.addMenu(new Menu(menuId++,"添加管理员","../security/operatorAction!add.action",-1L,"operatorActionAdd"));
        operatorMenu.addMenu(new Menu(menuId++,"管理员列表","../security/operatorAction!list.action",-1L,"operatorActionList"));
        userMenu.addSubMenu(new Menu(menuId++,"管理员列表","../security/operatorAction!list.action",-1L,"operatorActionList"));
        menus.add(operatorMenu);
        }*/
        {
            Menu showMenu = new Menu(menuId++, "推广计划管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "广告管理", "../show/showList.jsp", -1, "sgmGroupsManage"));
            showMenu.addSubMenu(new Menu(menuId++, "默认推广计划", "../show/groupsList.jsp?obj.groupType=1", -1, "sgmGroupsManage"));
            showMenu.addSubMenu(new Menu(menuId++, "强制推广计划", "../show/groupsList.jsp?obj.groupType=2", -1, "sgmGroupsManage"));
            showMenu.addSubMenu(new Menu(menuId++, "MAC广告管理", "../show/showList.jsp", -1, "macGroupsManage"));
            showMenu.addSubMenu(new Menu(menuId++, "MAC推广计划", "../show/groupsList.jsp?obj.groupType=3", -1, "macGroupsManage"));
            showMenu.addSubMenu(new Menu(menuId++, "MAC广告查看", "../show/showList.jsp", -1, "rfsGroupsManage"));
            showMenu.addSubMenu(new Menu(menuId++, "推广计划审核", "../show/groupsList.jsp?obj.groupType=3", -1, "rfsGroupsManage"));
            showMenu.addSubMenu(new Menu(menuId++, "LSS字幕管理", "../show/textList.jsp", -1, "lssTextManage"));
/*
            showMenu.addSubMenu(new Menu(menuId++, "投放统计", "../show/showStat.jsp", -1, "showActionList"));
*/
            menus.add(showMenu);
        }
        {
            Menu missionMenu = new Menu(menuId++, "公告管理", "", -1, "");
            missionMenu.addSubMenu(new Menu(menuId++, "新建公告", "../system/noticeView.jsp", -1, "noticeActionView"));
            missionMenu.addSubMenu(new Menu(menuId++, "公告列表", "../system/noticeList.jsp", -1, "noticeActionList"));
/*
            missionMenu.addSubMenu(new Menu(menuId++, "推送统计", "../resource/pushMissionStat.jsp", -1, "pushMissionActionStat"));
*/
            menus.add(missionMenu);
        }
        {
            Menu missionMenu = new Menu(menuId++, "资源管理", "", -1, "");
            missionMenu.addSubMenu(new Menu(menuId++, "资源审核", "../resource/resourceList.jsp?action=audit", -1, "resourceActionAudit"));
            missionMenu.addSubMenu(new Menu(menuId++, "资源列表", "../resource/resourceList.jsp", -1, "resourceActionList"));
            missionMenu.addSubMenu(new Menu(menuId++, "资源推送", "../resource/pushMissionWizard.jsp", -1, "pushMissionAction"));
/*
            missionMenu.addSubMenu(new Menu(menuId++, "推送统计", "../resource/pushMissionStat.jsp", -1, "pushMissionActionStat"));
*/
            menus.add(missionMenu);
        }
        {
            Menu missionMenu = new Menu(menuId++, "推广统计", "", -1, "");
            missionMenu.addSubMenu(new Menu(menuId++, "新建任务", "../resource/pushMissionWizard.jsp?action=add", -1, "pushMissionActionView"));
            missionMenu.addSubMenu(new Menu(menuId++, "任务列表", "../resource/pushMissionList.jsp", -1, "pushMissionActionList"));
            missionMenu.addSubMenu(new Menu(menuId++, "推送统计", "../resource/pushMissionStat.jsp", -1, "pushMissionActionStat"));
            menus.add(missionMenu);
        }
        {
        Menu menu = new Menu(menuId++,"日志统计","",-1,"");
        menu.addSubMenu(new Menu(menuId++,"播出统计","../show/showLogList.jsp",-1,"showLogActionList"));
        menu.addSubMenu(new Menu(menuId++,"操作日志","../show/logList.jsp",-1,"logActionList"));
        menus.add(menu);
        }
        {
            Menu menu = new Menu(menuId++, "设备管理", "", -1, "");
            menu.addSubMenu(new Menu(menuId++, "媒体终端添加", "../system/deviceView.jsp", -1, "deviceActionAdd"));
            menu.addSubMenu(new Menu(menuId++, "媒体终端列表", "../system/deviceList.jsp", -1, "deviceActionList"));
/*
            menu.addSubMenu(new Menu(menuId++, "服务器添加", "../system/serverView.jsp", -1, "serverActionAdd"));
            menu.addSubMenu(new Menu(menuId++, "服务器列表", "../system/serverList.jsp", -1, "serverActionList"));
*/
            menu.addSubMenu(new Menu(menuId++, "区域维护", "../system/areaList.jsp", -1, "areaActionList"));
            //menu.addSubMenu(new Menu(menuId++,"设备监控","../system/deviceMonitorList.jsp",-1,"deviceActionMonitor"));
            menus.add(menu);
        }
        {
            Menu operatorMenu = new Menu(menuId++, "管理员管理", null, 0, "");
            operatorMenu.addSubMenu(new Menu(menuId++, "添加管理员", "../security/adminView.jsp", -1, "operatorActionAdd"));
            operatorMenu.addSubMenu(new Menu(menuId++, "管理员列表", "../security/adminList.jsp", -1, "operatorActionList"));
            operatorMenu.addSubMenu(new Menu(menuId++, "角色管理", "../security/roleList.jsp", -1, "permissionActionList"));
            operatorMenu.addSubMenu(new Menu(menuId++, "权限管理", "../security/permissionList.jsp", -1, "permissionActionList"));
            menus.add(operatorMenu);
        }/*{
        Menu userMenu = new Menu(menuId++,"用户管理","",-1,"");
        userMenu.addSubMenu(new Menu(menuId++,"添加用户","../user/userView.jsp",-1,"userActionAdd"));
        userMenu.addSubMenu(new Menu(menuId++,"用户审批","../user/userList.jsp?obj.status=3",-1,"userActionAudit"));
        userMenu.addSubMenu(new Menu(menuId++,"查询用户","../user/userList.jsp",-1,"userActionList"));
        userMenu.addSubMenu(new Menu(menuId++,"单位维护","../user/organList.jsp",-1,"organActionList"));
        menus.add(userMenu);
        }{
        Menu menu = new Menu(menuId++,"存储管理","",-1,"");
        menu.addSubMenu(new Menu(menuId++,"SP管理","../files/spAction!list.action",-1,"spActionList"));
        menu.addSubMenu(new Menu(menuId++,"MP管理","../files/mpAction!list.action",-1,"mpActionList"));
        menus.add(menu);
        }*/
        /*{
        Menu menu = new Menu(menuId++,"日志统计管理","",-1,"");
        menu.addSubMenu(new Menu(menuId++,"点播并发统计","../chart/mediaLogAction!vodLogConcurrent.action",-1,"vodLogConcurrent"));
        menu.addSubMenu(new Menu(menuId++,"点播统计","../chart/mediaLogAction!vodLogCount.action",-1,"vodLogCount"));
        menu.addSubMenu(new Menu(menuId++,"媒体点播统计","../chart/mediaLogAction!mediaLog.action",-1,"mediaLog"));
        menus.add(menu);
        } */
        {
            Menu menu = new Menu(menuId++, "退出", "../security/logout.action", -1, "");
            menu.addSubMenu(new Menu(menuId++, "退出登录", "../security/logout.action", -1, ""));
            menus.add(menu);
        }
        request.setAttribute("menuCounts", menuId);
        request.setAttribute("menus", menus);
    }

%>