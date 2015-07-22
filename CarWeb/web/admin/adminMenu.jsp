<%@ page
        contentType="text/html;charset=UTF-8" %><%@ page
        import="com.fortune.common.business.security.model.Menu,
                java.util.List,
                java.util.ArrayList" %><%
    boolean compactModeForMenu = AppConfigurator.getInstance().getBoolConfig("system.compactMode",false);
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
            Menu showMenu = new Menu(menuId++, "广告管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "素材管理", "../ad/adList.jsp", -1, "adManage"));
            showMenu.addSubMenu(new Menu(menuId++, "投放管理", "../ad/adRangeList.jsp", -1, "adPlayManage"));
            showMenu.addSubMenu(new Menu(menuId++, "频次统计", "../ad/adLogState.jsp", -1, "adLogManage"));
            menus.add(showMenu);
        }


        {
            Menu showMenu = new Menu(menuId++, "高铁列车管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "线路管理", "../train/trainLineTree.jsp", -1, "trainLineManage"));
            showMenu.addSubMenu(new Menu(menuId++, "车次管理", "../train/trainList.jsp", -1, "trainManage"));
            showMenu.addSubMenu(new Menu(menuId++, "广告机管理", "../train/showMachineList.jsp", -1, "trainAdDeviceManage"));
            showMenu.addSubMenu(new Menu(menuId++, "列车日志导入", "../train/logImport.jsp", -1, "adLogManage"));
            menus.add(showMenu);
        }

        {
            Menu showMenu = new Menu(menuId++, "组织管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "CSP管理", "../csp/cspList.jsp", -1, "cspManage"));
            if(compactModeForMenu){
/*
                Menu showMenu = new Menu(menuId++, "组织管理", "", -1, "");
                showMenu.addSubMenu(new Menu(menuId++, "CSP管理", "../csp/cspList.jsp", -1, "cspManage"));
                showMenu.addSubMenu(new Menu(menuId++, "设备管理", "../system/deviceList.jsp", -1, "deviceManage"));
                menus.add(showMenu);
*/
            }else{
                showMenu.addSubMenu(new Menu(menuId++, "设备管理", "../system/deviceList.jsp", -1, "deviceManage"));
            }
            menus.add(showMenu);
        }

        {
            Menu showMenu = new Menu(menuId++, "平台管理员管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "平台管理员管理", "../security/adminList.jsp", -1, "adminManage"));
            showMenu.addSubMenu(new Menu(menuId++, "角色管理", "../security/roleList.jsp", -1, "roleManage"));
            showMenu.addSubMenu(new Menu(menuId++, "权限管理", "../security/permissionList.jsp", -1, "permissionManage"));

            menus.add(showMenu);
        }

        {
            Menu showMenu = new Menu(menuId++, "CSP管理员管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "CSP管理员管理", "../security/cspAdminList.jsp", -1, "adminManage222"));
            menus.add(showMenu);
        }

        {
            Menu showMenu = new Menu(menuId++, "浏览资源", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "批量导出", "../content/exportSD.jsp", -1, "contentExportManage"));
            showMenu.addSubMenu(new Menu(menuId++, "浏览资源", "../content/systemContentList.jsp", -1, "systemContentManage"));
            showMenu.addSubMenu(new Menu(menuId++, "浏览删除资源", "../content/systemDeleted.jsp", -1, "systemContentManage"));
            menus.add(showMenu);
        }

        {
            Menu showMenu = new Menu(menuId++, "资源审核", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "SP审核上线", "../content/systemSpContentAuditOnlineList.jsp", -1, "systemContentSpAudit"));
            showMenu.addSubMenu(new Menu(menuId++, "SP审核下线", "../content/systemSpContentAuditOfflineList.jsp", -1, "systemContentSpAuditOffline"));
            showMenu.addSubMenu(new Menu(menuId++, "CP审核上线", "../content/systemCpContentAuditOnlineList.jsp", -1, "systemContentCPAudit"));
            showMenu.addSubMenu(new Menu(menuId++, "CP审核下线", "../content/systemCpContentAuditOfflineList.jsp", -1, "systemContentCpAuditOffline"));

            menus.add(showMenu);
        }

        {
            Menu showMenu = new Menu(menuId++, "日志统计分析", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "并发情况统计", "../log/systemBingFa.jsp", -1, "systemBingFa"));
            showMenu.addSubMenu(new Menu(menuId++, "区域点播量统计", "../log/systemAreaLogList.jsp", -1, "areaLogList"));
            showMenu.addSubMenu(new Menu(menuId++, "SP点播量统计", "../log/systemSpLogList.jsp", -1, "spLogList"));
            //（/**/注释页面于6月18日注释掉。暂时不用这些功能
            /*showMenu.addSubMenu(new Menu(menuId++, "CP点播量统计", "../log/systemCpLogList.jsp", -1, "cspLogList"));*/
            showMenu.addSubMenu(new Menu(menuId++, "频道点播量统计", "../log/systemChannelLogList.jsp", -1, "channelLogList"));
            showMenu.addSubMenu(new Menu(menuId++, "频道资源点击排行", "../log/systemChannelContentLogList.jsp", -1, "channelLogList"));
            showMenu.addSubMenu(new Menu(menuId++, "媒体点播量统计", "../log/systemContentLogList.jsp", -1, "contentLogList"));
            showMenu.addSubMenu(new Menu(menuId++, "媒体点播时长统计", "../log/systemContentLogByLengthList.jsp", -1, "contentLogByLengthList"));
            showMenu.addSubMenu(new Menu(menuId++, "媒体点播流量统计", "../log/systemContentLogByNetFlowList.jsp", -1, "contentLogByNetFlowList"));
            showMenu.addSubMenu(new Menu(menuId++, "媒体直播量统计", "../log/systemContentLiveLogList.jsp", -1, "contentLogList"));
            showMenu.addSubMenu(new Menu(menuId++, "媒体直播趋势统计", "../su/systemLiveTrendStaticsLogList.jsp", -1, "visitLogList"));
            showMenu.addSubMenu(new Menu(menuId++, "有播放媒体统计", "../su/systemPlayedLogList.jsp", -1, "visitLogList"));
///*
            if(!compactModeForMenu)
            {
                showMenu.addSubMenu(new Menu(menuId++, "拉动点击排行", "../log/systemContentLadongLogList.jsp", -1, "contentLogList"));
                showMenu.addSubMenu(new Menu(menuId++, "电影点播排行", "../log/systemFilmLogList.jsp", -1, "contentLogList"));
                showMenu.addSubMenu(new Menu(menuId++, "电视剧点播排行", "../log/systemTvLogList.jsp", -1, "contentLogList"));
                showMenu.addSubMenu(new Menu(menuId++, "流量使用情况统计","../log/systemNetFlowLogList.jsp",-1,"contentLogList"));
            }
            //*/
            showMenu.addSubMenu(new Menu(menuId++, "专题点播量统计", "../log/systemContentZtLogList.jsp", -1, "contentZtLogList"));
            showMenu.addSubMenu(new Menu(menuId++, "用户登录情况统计","../log/userLoginLogList.jsp",-1,"userLoginLogList"));
            showMenu.addSubMenu(new Menu(menuId++, "客户端情况统计","../log/clientLogList.jsp",-1,"clientLogList"));
            //showMenu.addSubMenu(new Menu(menuId++, "频道点播人次统计", "../log/channelUserStat.jsp", -1, "logStat"));
            //showMenu.addSubMenu(new Menu(menuId++, "资源点播人次统计", "../log/contentUserStat.jsp", -1, "logStat"));
            /*showMenu.addSubMenu(new Menu(menuId++, "点播日志查询", "../log/systemVisitLogList.jsp", -1, "visitLogList"));*/
            //showMenu.addSubMenu(new Menu(menuId++, "频道浏览量统计", "../log/channelWebStat.jsp", -1, "logStat"));
            //showMenu.addSubMenu(new Menu(menuId++, "资源浏览量统计", "../log/contentWebStat.jsp", -1, "logStat"));
            showMenu.addSubMenu(new Menu(menuId++, "登陆日志查询","../log/userLoginList.jsp",-1,"visitLogGetUserLoginCount"));
            showMenu.addSubMenu(new Menu(menuId++, "点播日志查询", "../log/userDemandList.jsp", -1, "visitLogGetUserDemandCount"));
            showMenu.addSubMenu(new Menu(menuId++, "统计数据导出", "../log/systemAllDateList.jsp", -1, "visitLogGetUserDemandCount"));

            /*
            showMenu.addSubMenu(new Menu(menuId++, "在线用户分析", "../log/onLineUserAnalysisList.jsp", -1, "visitLogGetOnLineUserAnalysisCount"));
            showMenu.addSubMenu(new Menu(menuId++, "区域贡献统计", "../log/areaContributionList.jsp", -1, "visitLogGetAreaContributionCount"));
            showMenu.addSubMenu(new Menu(menuId++, "组织贡献统计", "../log/organizationContributionList.jsp", -1, "logGetChannelList"));
            showMenu.addSubMenu(new Menu(menuId++, "资源贡献统计", "../log/resourceContributionList.jsp", -1, "visitLogGetResourceContributionCount"));
            showMenu.addSubMenu(new Menu(menuId++, "用户活动分析", "../log/activityUserAnalysisList.jsp", -1, "visitLogGetActivityUserAnalysisCount"));
            showMenu.addSubMenu(new Menu(menuId++, "频道点播统计", "../log/channelDemandList.jsp", -1, "visitLogGetChannelDemandCount"));
            showMenu.addSubMenu(new Menu(menuId++, "资源点播人次统计", "../log/resourceOnDemandList.jsp", -1, "visitLogGetResourceOnDemandCount"));
            showMenu.addSubMenu(new Menu(menuId++, "频道点播人次统计", "../log/channelOnDemandList.jsp", -1, "visitLogGetChannelOnDemandCount"));
            */

            showMenu.addSubMenu(new Menu(menuId++, "下载日志统计", "../log/downloadLogList.jsp", -1, "visitLogGetUserDemandCount"));
            showMenu.addSubMenu(new Menu(menuId++, "登录号码统计", "../log/phoneList.jsp", -1, "visitLogGetUserDemandCount"));
            showMenu.addSubMenu(new Menu(menuId++, "用户购买统计", "../log/userBuyStat.jsp", -1, "visitLogGetUserDemandCount"));
            menus.add(showMenu);
        }

        {
            Menu showMenu = new Menu(menuId++, "销售业绩统计", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "SP销售统计", "../salescount/spSalesList.jsp", -1, "spSalesList"));
            showMenu.addSubMenu(new Menu(menuId++, "产品销售统计", "../salescount/productSalesList.jsp", -1, "userBuySearchProductSalesCount"));
            showMenu.addSubMenu(new Menu(menuId++, "资源销售统计", "../salescount/contentSalesList.jsp", -1, "userBuySearchContentSalesCount"));

            menus.add(showMenu);
        }

        {
            Menu showMenu = new Menu(menuId++, "资源模版管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "资源模版管理", "../V5/module/moduleList.jsp", -1, "moduleManage"));

            menus.add(showMenu);
        }


        {
            Menu showMenu = new Menu(menuId++, "产品管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "产品管理", "../product/productList.jsp", -1, "productManage"));
            showMenu.addSubMenu(new Menu(menuId++, "服务产品管理", "../product/systemServiceProductList.jsp", -1, "systemServiceProductManage"));
            showMenu.addSubMenu(new Menu(menuId++, "平台产品设置", "../product/serviceProductList.jsp", -1, "systemServiceProductManage"));
            //showMenu.addSubMenu(new Menu(menuId++, "跨SP产品赠送", "../product/systemServiceProductGiftList.jsp", -1, "systemServiceProductGiftList"));

            menus.add(showMenu);
        }


        {
            Menu showMenu = new Menu(menuId++, "推荐管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "推荐管理", "../publish/systemRecommendList.jsp", -1, "recommendManage"));
            showMenu.addSubMenu(new Menu(menuId++, "栏目推荐管理", "../publish/channelRecommendList.jsp",-1,"channelRecommendList"));
            showMenu.addSubMenu(new Menu(menuId++, "搜索热词管理", "../publish/userHotSearchList.jsp", -1, "userHotSearchManage"));
            showMenu.addSubMenu(new Menu(menuId++, "走马灯内容管理", "../content/contentNoticeManagement.jsp", -1, "contentNoticeManage"));
//            showMenu.addSubMenu(new Menu(menuId++,"点播量评分干预","../publish/contentInfoInterference.jsp",-1,"contentInfoInterference"));
            menus.add(showMenu);
        }
        {
            Menu showMenu = new Menu(menuId++, "广告管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "广告管理", "../ad/adList.jsp", -1, "adManagement"));
            showMenu.addSubMenu(new Menu(menuId++, "广告统计", "../ad/adLogList.jsp", -1, "adManagement"));
            menus.add(showMenu);
        }
        {
            Menu showMenu = new Menu(menuId++, "系统管理", "", -1, "");
            if(compactModeForMenu){
                showMenu.addSubMenu(new Menu(menuId++, "设备管理", "../system/deviceList.jsp", -1, "deviceManage"));
            }
            showMenu.addSubMenu(new Menu(menuId++, "区域管理", "../system/areaList.jsp", -1, "areaManage"));
//            showMenu.addSubMenu(new Menu(menuId++, "IP列表管理", "../system/ipRangeList.jsp", -1, "systemManagement"));
            showMenu.addSubMenu(new Menu(menuId++, "系统操作日志查询", "../system/systemLogList.jsp", -1, "systemLogManage"));
            showMenu.addSubMenu(new Menu(menuId++, "用户管理", "../user/userList.jsp", -1, "usrUserManage"));
            showMenu.addSubMenu(new Menu(menuId++, "机顶盒管理", "../user/stbList.jsp", -1, "stbManage"));
            menus.add(showMenu);
        }


        {
            Menu showMenu = new Menu(menuId++, "资源管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "课件添加", "../content/contentAdd.jsp", -1, "contentCourseManage"));
            showMenu.addSubMenu(new Menu(menuId++, "课件浏览", "../content/contentList.jsp", -1, "contentCourseManage"));
            showMenu.addSubMenu(new Menu(menuId++, "直播管理", "../V5/media/liveList.jsp", -1, "compactLiveManage"));
            showMenu.addSubMenu(new Menu(menuId++, "点播管理", "../V5/media/mediaList.jsp", -1, "compactMediaManage"));
            showMenu.addSubMenu(new Menu(menuId++, "资源添加", "../content/cpContentSelect.jsp", -1, "contentManage"));
            //showMenu.addSubMenu(new Menu(menuId++, "资源添加", "../content/cpContentDefaultAdd.jsp", -1, "contentManage"));
            if(!compactModeForMenu){
                showMenu.addSubMenu(new Menu(menuId++, "录入资源名称", "../content/inputContentName.jsp", -1, "contentManage"));
                showMenu.addSubMenu(new Menu(menuId++, "上传专题页面", "../content/cpRecommendUpload.jsp", -1, "contentManage"));
                showMenu.addSubMenu(new Menu(menuId++, "审核情况查询", "../content/cpAuditResult.jsp", -1, "CpAuditResultManage"));
            }
            showMenu.addSubMenu(new Menu(menuId++, "浏览", "../content/cpContentList.jsp", -1, "cpContentManage"));
            showMenu.addSubMenu(new Menu(menuId++, "回收站", "../content/cpRecycleBin.jsp", -1, "CpRecycleBinManage"));
            showMenu.addSubMenu(new Menu(menuId++, "版权管理", "../content/cpDigitalRightManagement.jsp", -1, "cpDigitalRightManage"));
            showMenu.addSubMenu(new Menu(menuId++, "资源批量导入", "../content/cpContentXmlAdd.jsp", -1, "contentManage"));

            //showMenu.addSubMenu(new Menu(menuId++, "资源批量导入", "../content/cpContentXmlAdd.jsp", -1, "cpContentManagement"));
            //showMenu.addSubMenu(new Menu(menuId++, "资源批量导出", "../content/cpManyOut.jsp", -1, "cpContentManagement"));
            menus.add(showMenu);
        }

        {
            Menu showMenu = new Menu(menuId++, "资源发布", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "新资源列表", "../content/spNewContentList.jsp", -1, "spNewContentManage"));
            showMenu.addSubMenu(new Menu(menuId++, "待审资源列表", "../content/spAuditResult.jsp?ca_type=4&ca_result=0", -1, "spAuditResultContentManage"));
            showMenu.addSubMenu(new Menu(menuId++, "已审核资源列表", "../content/spAuditedContentList.jsp", -1, "SpAuditedOnlineContentManage"));
            showMenu.addSubMenu(new Menu(menuId++, "线上资源列表", "../content/spOnlineContentList.jsp", -1, "SpOnlineContentManage"));
            showMenu.addSubMenu(new Menu(menuId++, "批量海报处理", "../content/spOnlineContentPosterCapture.jsp", -1, "SpOnlineContentManage"));
            showMenu.addSubMenu(new Menu(menuId++, "刷新页面缓存", "../content/refreshAllCache.jsp", -1, "SpOnlineContentManage"));
            showMenu.addSubMenu(new Menu(menuId++, "线下资源列表", "../content/spOfflineContentList.jsp", -1, "SpOfflineContentManage"));
            showMenu.addSubMenu(new Menu(menuId++, "审核情况查询", "../content/spAuditResult.jsp", -1, "SpAuditResultManage"));
            showMenu.addSubMenu(new Menu(menuId++, "推荐设置", "../content/spRecommendSet.jsp", -1, "SpRecommendContentManage"));
            showMenu.addSubMenu(new Menu(menuId++, "回收站", "../content/spRecycleBin.jsp", -1, "SpRecycleBinManage"));
            menus.add(showMenu);
        }


        {
            Menu showMenu = new Menu(menuId++, "资源审核", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "SP审核上线", "../content/spContentAuditOnlineList.jsp", -1, "contentSpAuditOnline"));
            showMenu.addSubMenu(new Menu(menuId++, "SP审核下线", "../content/spContentAuditOfflineList.jsp", -1, "contentSpAuditOffline"));
            showMenu.addSubMenu(new Menu(menuId++, "CP审核上线", "../content/cpContentAuditOnlineList.jsp", -1, "contentCpAuditOnline"));
            showMenu.addSubMenu(new Menu(menuId++, "CP审核下线", "../content/cpContentAuditOfflineList.jsp", -1, "contentCpAuditOffline"));

            menus.add(showMenu);
        }

        {
            Menu showMenu = new Menu(menuId++, "频道管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "频道管理", "../publish/channelList.jsp?type=1", -1, "channelManage"));
            showMenu.addSubMenu(new Menu(menuId++, "栏目管理", "../publish/channelList.jsp?type=2", -1, "channelColumn"));
            showMenu.addSubMenu(new Menu(menuId++, "标签管理", "../publish/channelList.jsp?type=3", -1, "channelTag"));

            menus.add(showMenu);
        }
        {
            Menu showMenu = new Menu(menuId++, "资源相关管理", "", -1, "");

            showMenu.addSubMenu(new Menu(menuId++, "资源相关管理", "../publish/relatedList.jsp", -1, "spRelatedManage"));

            menus.add(showMenu);
        }
        {
            Menu showMenu = new Menu(menuId++, "推荐管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "推荐管理", "../publish/spRecommendList.jsp", -1, "spRecommendManage"));
            showMenu.addSubMenu(new Menu(menuId++, "栏目推荐管理", "../publish/channelRecommendList.jsp",-1,"spChannelRecommendList"));
            menus.add(showMenu);
        }

        {
            Menu showMenu = new Menu(menuId++, "页面模版管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "页面模版管理", "../template/templateList.jsp", -1, "templateManage"));
            showMenu.addSubMenu(new Menu(menuId++, "频道页面模版管理", "../template/channelTemplateList.jsp?type=1", -1, "channelTemplateManage"));
            showMenu.addSubMenu(new Menu(menuId++, "CSP模版管理", "../template/cspTemplateView.jsp", -1, "cspTemplateManage"));
            showMenu.addSubMenu(new Menu(menuId++, "同步控制", "../syn/synTaskList.jsp", -1, "synTaskManage"));
            showMenu.addSubMenu(new Menu(menuId++, "模板和资源上传", "../syn/uploadSynTask.jsp", -1, "synFileManage"));
            showMenu.addSubMenu(new Menu(menuId++, "文件删除", "../syn/delSynTask.jsp", -1, "synFileDel"));
            menus.add(showMenu);
        }

        {
            Menu showMenu = new Menu(menuId++, "服务产品管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "服务产品管理", "../product/serviceProductList.jsp", -1, "spServiceProductManage"));
            menus.add(showMenu);
        }

        {
            Menu showMenu = new Menu(menuId++, "直播管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "直播管理", "../publish/recommendList.jsp", -1, "liveManagement"));
            menus.add(showMenu);
        }
        if(!compactModeForMenu){
            Menu showMenu = new Menu(menuId++, "运营管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "影评管理", "../portal/userReviewList.jsp", -1, "userReviewManage"));
            showMenu.addSubMenu(new Menu(menuId++, "影评关键词管理", "../portal/userReviewKeywordList.jsp", -1, "userReviewKeywordManage"));
            //  showMenu.addSubMenu(new Menu(menuId++, "影评报告管理", "../portal/userReviewReportList.jsp", -1, "userVisitManagement"));
            showMenu.addSubMenu(new Menu(menuId++, "评分管理", "../portal/userScoringCountList.jsp", -1, "userScoringManage"));
            //   showMenu.addSubMenu(new Menu(menuId++, "推荐管理", "../portal/userRecommandCountList.jsp", -1, "userVisitManagement"));
            //   showMenu.addSubMenu(new Menu(menuId++, "收藏管理", "../portal/userFavoritesCountList.jsp", -1, "userVisitManagement"));

            menus.add(showMenu);
        }
        {
            Menu showMenu = new Menu(menuId++, "文件管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "文件同步复制", "../file/fileList.jsp?fileType=1", -1, "fileManage"));
            showMenu.addSubMenu(new Menu(menuId++, "本地文件删除", "../file/fileList.jsp?fileType=2", -1, "fileManage"));
            showMenu.addSubMenu(new Menu(menuId++, "存储文件删除", "../file/fileList.jsp?fileType=3", -1, "fileManage"));
            menus.add(showMenu);
        }

        {
            Menu showMenu = new Menu(menuId++, "日志统计分析", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "并发情况统计", "../log/spSyncStat.jsp", -1, "cspLogStat"));
            showMenu.addSubMenu(new Menu(menuId++, "区域点播量统计", "../log/spAreaStat.jsp", -1, "cspLogStat"));
            showMenu.addSubMenu(new Menu(menuId++, "CP点播量统计", "../log/spCpStat.jsp", -1, "cspLogStat"));
            showMenu.addSubMenu(new Menu(menuId++, "频道点播量统计", "../log/spChannelStat.jsp", -1, "cspLogStat"));
            showMenu.addSubMenu(new Menu(menuId++, "资源点播量统计", "../log/spContentStat.jsp", -1, "cspLogStat"));
            showMenu.addSubMenu(new Menu(menuId++, "频道点播人次统计", "../log/spChannelUserStat.jsp", -1, "cspLogStat"));
            showMenu.addSubMenu(new Menu(menuId++, "资源点播人次统计", "../log/spContentUserStat.jsp", -1, "cspLogStat"));
            showMenu.addSubMenu(new Menu(menuId++, "点播日志查询", "../log/spLogList.jsp", -1, "cspLogStat"));
            showMenu.addSubMenu(new Menu(menuId++, "频道浏览量统计", "../log/spChannelWebStat.jsp", -1, "cspLogStat"));
            showMenu.addSubMenu(new Menu(menuId++, "资源浏览量统计", "../log/spContentWebStat.jsp", -1, "cspLogStat"));
            menus.add(showMenu);
        }
        {
            Menu showMenu = new Menu(menuId++, "编码管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "配置管理", "../encoder/encoderTemplate.jsp", -1, "encoderTemplateManage"));
            showMenu.addSubMenu(new Menu(menuId++, "设备管理", "../system/deviceList.jsp?obj.type=4", -1, "deviceManage"));
            showMenu.addSubMenu(new Menu(menuId++, "队列管理", "../encoder/encoderTaskListCanOrder.jsp", -1, "encoderTaskManage"));
            menus.add(showMenu);
        }

        {
            Menu showMenu = new Menu(menuId++, "管理员管理", "", -1, "");
            showMenu.addSubMenu(new Menu(menuId++, "管理员管理", "../security/cspCspAdminList.jsp", -1, "cspCspAdminList"));
            menus.add(showMenu);
        }
        {
            Menu menu = new Menu(menuId++, "退出", "../security/logout.action", -1, "");
            menu.addSubMenu(new Menu(menuId++, "退出登录", "../security/logout.action", -1, ""));
            menus.add(menu);
        }
        request.setAttribute("menuCounts", menuId);
        request.setAttribute("menus", menus);
    }

%>