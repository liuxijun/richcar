<%@ taglib prefix="s" uri="/struts-tags" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.rms.business.frontuser.logic.logicInterface.OrganizationLogicInterface" %><%@ page
        import="com.fortune.util.SpringUtils" %><%@ page
        import="com.fortune.rms.business.frontuser.model.Organization" %><%@ page
        import="java.util.List" %><%@ page import="java.util.Map" %><%@ page
        import="java.util.HashMap" %><%@ page
        import="com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface" %><%@ page
        import="com.fortune.rms.business.publish.model.Channel" %><%@ page
        import="java.util.ArrayList" %><%@ page
        import="com.fortune.rms.business.frontuser.model.FrontUser" %><%@ page
        import="com.fortune.util.StringUtils" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2014/11/22
  Time: 11:24
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String allChannels = "";
//    File f = new File("f:/users.txt");
    String users = request.getParameter("users");
    String command = request.getParameter("command");
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.importFrontUserr.jsp");
    ChannelLogicInterface channelLogicInterface = (ChannelLogicInterface) SpringUtils.getBean("channelLogicInterface", session.getServletContext());
    List<Channel> channels = channelLogicInterface.getAll();
    List<Channel> leafChannels = new ArrayList<Channel>();
    for (Channel c : channels) {
        if (channelLogicInterface.isLeafChannel(c.getId())) {
            leafChannels.add(c);
        }
    }
    OrganizationLogicInterface organizationLogicInterface = (OrganizationLogicInterface) SpringUtils.getBean("organizationLogicInterface", session.getServletContext());
    //先把原有的组织读出来放到缓存里
    Map<String, List<Organization>> parentOrg = new HashMap<String, List<Organization>>();
    List<Organization> organizations = organizationLogicInterface.getAll();
    Long maxOrgId = 1000L;
    for (Organization o : organizations) {
        if (maxOrgId < o.getId()) {
            maxOrgId = o.getId();
        }
        List<Organization> orgs = parentOrg.get(o.getName());
        if (orgs == null) {
            orgs = new ArrayList<Organization>();
        } else {
            logger.debug("真的有重复的：" + o.getName() + ",parentId=" + o.getParentId());
        }
        orgs.add(o);
        parentOrg.put(o.getName(), orgs);
/*
        if(o.getParentId()==null||o.getParentId()==-1){

        }
*/
    }
    String userSql = "/* delete from front_user where user_id like 'wp%';*/\r\n";
    String errorMsg = "";
    long userType = 2;
    List<Organization> willAddOrgs = new ArrayList<Organization>();
    List<FrontUser> willAddUsers = new ArrayList<FrontUser>();
    if ("check".equals(command)) {
        if (users != null) {
            Map<String, String> frontUser = new HashMap<String, String>();
            try {
                String[] lines = users.split("\n");
                long i = 1;
                String parentOrgName = null;
                maxOrgId++;
                long orgId = maxOrgId + 1;
                for (String s : lines) {
                    String[] data = s.split("\t");
                    if (data.length >= 6) {
                        if (data[0] != null && !"".equals(data[0])) {
                            parentOrgName = data[0];
                        }
                        String orgName = data[1];
                        String sn = data[2];
                        String userName = data[3];
                        Long gender = data[4] == null || "女".equals(data[4]) ? 0L : 1L;
                        String userId = data[5];

                        String pwd = "12345";
                        if (data.length >= 7) {
                            pwd = data[6];
                        }
                        if (userId == null || "".equals(userId.trim()) || "wp".equals(userId)) {
                            errorMsg += "--用户数据异常：userId=" + userId + "," + s + "\r\n";
                            continue;
                        }
                        if (frontUser.containsKey(userId)) {
                            errorMsg += "--用户重复：userId=" + userId + "," + s + "\r\n";
                            continue;
                        }
                        frontUser.put(userId, s);
                        Organization o = getOrganizationFromMaps(parentOrg, orgName, parentOrgName);
                        if (o == null) {
                            Organization parent = getOrganizationFromMaps(parentOrg, parentOrgName, null);
                            if (parent == null) {
                                parent = new Organization();
                                parent.setName(parentOrgName);
                                parent.setParentId(-1L);
                                parent.setChannels(allChannels);
                                parent.setSequence(i++);
                                //parent = organizationLogicInterface.save(parent);
                                parent.setId(orgId++);
                                List<Organization> orgs = parentOrg.get(parentOrgName);
                                if(orgs==null){
                                    orgs =  new ArrayList<Organization>();
                                }
                                orgs.add(parent);
                                parentOrg.put(parentOrgName, orgs);
                            }
                            if (!orgName.equals(parentOrgName)) {
                                o = new Organization();
                                o.setName(orgName);
                                o.setParentId(parent.getId());
                                o.setChannels(allChannels);
                                o.setSequence(i++);
                                o.setId(orgId++);
                                //o = organizationLogicInterface.save(o);
                                List<Organization> orgs = parentOrg.get(orgName);
                                if(orgs==null){
                                    orgs =  new ArrayList<Organization>();
                                }
                                orgs.add(o);
                                parentOrg.put(orgName, orgs);
                            } else {
                                o = parent;
                            }
                        }
                        FrontUser user = new FrontUser();
                        user.setUserId(userId);
                        user.setPassword(pwd);
                        user.setGender(gender);
                        user.setName(userName);
                        user.setOrganizationId(o.getId());
                        user.setTypeId(userType);
                        willAddUsers.add(user);
                        userSql += "\r\ninsert into front_user(user_id,name,organization_id," +
                                "gender,password,type_id,create_time,logon_times,status)" +
                                "values('" + userId + "','" + userName + "'," + o.getId() + "," + gender + ",md5('" + pwd + "'),"
                                + userType + ",now(),0,1);";
                    } else {
                        errorMsg += "-- 这条数据有问题：" + s + "\r\n";
                    }
                }
                //logger.debug(sb);
                String orgSql = "delete from organization where id>=" + maxOrgId + ";\r\n" +
                        "delete from organization_channel where organization_id>=" + maxOrgId + ";\r\n";
                //int ocId=1000;
                for (String key : parentOrg.keySet()) {
                    List<Organization> orgs = parentOrg.get(key);
                    for (Organization o : orgs) {
                        if (o.getId() <= maxOrgId) {
                            continue;
                        }
                        if (o.getSequence() == null) {
                            o.setSequence(o.getId() - 999);
                        }
                        orgSql += "insert into organization(id,name,sequence,parent_id) values(" +
                                o.getId() + ",'" + o.getName() + "'," + o.getSequence() + "," + o.getParentId() +
                                ");\r\n";
                        willAddOrgs.add(o);
                        for (Channel c : leafChannels) {
                            orgSql += "insert into organization_channel(organization_id,channel_id) values(" +
                                    o.getId() + "," + c.getId() + ");\r\n";
                            //ocId++;
                        }
                    }
                }
                userSql = errorMsg + "\r\n" + orgSql + userSql;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            errorMsg = "导入数据为空，不能继续";
            logger.error(errorMsg);
        }
    } else if ("doImport".equals(command)) {
        String[] orgIds = request.getParameterValues("orgs");
        String orgSql;
        if (orgIds != null && orgIds.length > 0) {
            Long sequence;
            for (String orgId : orgIds) {
                Long id = StringUtils.string2long(orgId, -1L);
                if (id > 0) {
                    sequence = id;
                    String orgName = request.getParameter("orgName_" + id);
                    if (orgName != null) {
                        long parentId = StringUtils.string2long(request.getParameter("orgParentId_" + id), -1);
                        orgSql = "insert into organization(id,name,sequence,parent_id) values(" +
                                id + ",'" + orgName + "'," + sequence + "," + parentId + ")";
                        errorMsg += executeInsert(orgSql, "ID=" + orgId + "", "添加组织：" + orgName) + "\r\n";
                        errorMsg += executeInsert("delete from  organization_channel where organization_id=" + id, "所有栏目",
                                "删除绑定关系：" + orgName) + "\r\n";
                        for (Channel c : leafChannels) {
                            errorMsg += executeInsert("insert into organization_channel(organization_id,channel_id) values(" +
                                    id + "," + c.getId() + ")", "到栏目" + c.getName(), "绑定组织" + orgName) + "\r\n";
                            //ocId++;
                        }
                    }
                }
            }
        } else {
            errorMsg += "没有组织需要导入！\r\n";
        }
        String[] userIds = request.getParameterValues("userIds");
        if (userIds != null && userIds.length > 0) {
            for (String userId : userIds) {
                String userName = request.getParameter("userName_" + userId);
                if (userName != null) {
                    long orgId = StringUtils.string2long(request.getParameter("userOrgId_" + userId), -1);
                    long gender = StringUtils.string2int(request.getParameter("userGender_" + userId), -1);
                    String pwd = request.getParameter("userPwd_" + userId);
                    if (pwd == null) {
                        pwd = "12345";
                    }
                    orgSql = "insert into front_user(user_id,name,organization_id," +
                            "gender,password,type_id,create_time,logon_times,status)" +
                            "values('" + userId.trim() + "','" + userName.trim() + "'," + orgId + "," + gender + ",md5('" + pwd.trim() + "'),"
                            + userType + ",now(),0,1);";
                    errorMsg += executeInsert(orgSql, userId, "添加用户：" + userName) + "\r\n";
                }
            }
        } else {
            errorMsg += "没有用户需要导入！\r\n";
        }
    } else if (command == null) {
        errorMsg += "请在按照格式输入文本内容后点击检查数据按钮！\r\n";
    } else {
        errorMsg += "还不支持的命令操作:" + command +
                "\r\n";
    }
    if (users == null) {
        users = "";
    }

%><!DOCTYPE html>
<html lang="zh_CN">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>管理首页</title>
    <meta name="description" content="overview &amp; stats"/>
    <%@include file="../inc/displayCssJsLib.jsp" %>
    <style type="text/css">
        .hover {
            cursor: pointer;
            background: white;
            color: #000000;
        }

        .asc {
            background: url('/images/sort_asc.png') no-repeat 50px;

        }

        .desc {
            background: url('/images/sort_desc.png') no-repeat 50px;
        }
    </style>
</head>
<body class="no-skin">
<!-- #section:basics/navbar.layout -->
<%@include file="../inc/displayHeader.jsp" %>
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    <%@include file="/inc/displayMenu.jsp" %>
    <div class="main-content">
        <!-- #section:basics/content.breadcrumbs -->
        <div class="breadcrumbs" id="breadcrumbs">
            <script type="text/javascript">
                try {
                    ace.settings.check('breadcrumbs', 'fixed')
                } catch (e) {
                }
            </script>

            <ul class="breadcrumb">
                <li>
                    当前位置:
                    <a href="../man.jsp"> 网站首页</a>
                </li>
                <li class="active">${folderName}</li>
                <li class="active">${functionName}</li>
            </ul>
            <!-- /.breadcrumb -->


            <!-- /section:basics/content.searchbox -->
        </div>

        <!-- /section:basics/content.breadcrumbs -->
        <div class="page-header">
            <h1>
                <i class="ace-icon fa fa-user"></i>${functionName}
            </h1>
        </div>
        <!-- /.page-header -->
        <div class="page-content">

            <div class="page-content-area">


                <div class="row page-content-main">

                    <form class="form-horizontal" id="importForm" name="importForm" action="importFrontUsers.jsp"
                          method="post">
                        <div class="col-xs-12 no-padding movie-info">
                            <div class="tabbable">
                                <table class="table table-striped table-bordered table-hover table-30">
                                    <thead>
                                    <tr>
                                        <th colspan="2">批量用户导入功能</th>
                                    </tr>
                                    </thead>
                                    <tbody id="bodyContain">
                                    <%
                                        if (errorMsg != null && !"".equals(errorMsg.trim())) {
                                    %>
                                    <tr>
                                        <td colspan="2">
                                            <pre><%=errorMsg%></pre>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    %>
                                    <%
                                        if ("check".equals(command)) {
                                    %>
                                    <tr>
                                        <td colspan="2">请确认以下数据将会添加：</td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">增加组织：<br>
                                            <%
                                                for (int i = 0, l = willAddOrgs.size(); i < l; i++) {
                                                    Organization o = willAddOrgs.get(i);
                                            %>
                                            <input onclick="confirmUnSelectOrg('<%=o.getName()%>',this.checked,this.id,this)"
                                                   id="org_<%=o.getId()%>" type="checkbox" checked name="orgs"
                                                   value="<%=o.getId()%>"><%=o.getName()%><br/>
                                            <input type="hidden" value="<%=o.getName()%>"
                                                   name="orgName_<%=o.getId()%>"/>
                                            <input type="hidden" value="<%=o.getParentId()%>"
                                                   name="orgParentId_<%=o.getId()%>"/>
                                            <%
                                                }
                                            %>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">增加员工：<br/>
                                            <%
                                                for (int i = 0, l = willAddUsers.size(); i < l; i++) {
                                                    FrontUser u = willAddUsers.get(i);
                                            %>
                                            <input type="checkbox" checked name="userIds"
                                                   value="<%=u.getUserId()%>"><%=u.getName()%>，<%=u.getUserId()%>
                                            ，<%=u.getGender() == 1 ? "男" : "女"%><br/>
                                            <input type="hidden" value="<%=u.getName()%>"
                                                   name="userName_<%=u.getUserId()%>"/>
                                            <input type="hidden" value="<%=u.getPassword()%>"
                                                   name="userPwd_<%=u.getUserId()%>"/>
                                            <input type="hidden" value="<%=u.getGender()%>"
                                                   name="userGender_<%=u.getUserId()%>"/>
                                            <input type="hidden" value="<%=u.getOrganizationId()%>"
                                                   name="userOrgId_<%=u.getUserId()%>"/>
                                            <%
                                                }
                                            %>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2"><span class="btn btn-green btn-big" onclick="doImport()">确认，添加这些数据</span>
                                            &nbsp;&nbsp;<span class="btn btn-gray btn-big"
                                                              onclick="window.history.go(-1)">返回</span>
                                        </td>
                                    </tr>
                                    <%
                                    } else {
                                    %>
                                    <tr>
                                        <td>数据格式：</td>
                                        <td>每一行的数据格式为：公司 部门 工号 姓名 性别 登录账号 口令，中间以tab符间隔(\t)，至少6列！例如：<br/>
                                            潍柴动力股份有限公司 上海运营中心 ws000 张三 男 ws000 12345<br/>
                                            提示：可直接从Excel中选择复制，然后黏贴到下面的输入框中！例如：<br/>
                                            <img src="../images/batchImportUsers.png"><br/>
                                            切记：公司部门名字必须与<a target="_blank" href="../sys/organization.jsp">组织管理</a>中完全一致，否则就会重复添加组织机构！每行至少6列！口令列可以不给，就采用默认12345作为口令<br/>
                                            <a href="example.xls">下载示例Excel文件</a>，<span style="color:red">切切记：每次不要超过1000行！</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>用户数据：</td>
                                        <td><textarea style="height:100px;" class="col-sm-12" id="users" name="users"
                                                      rows="10" cols="80"><%=users%></textarea></td>
                                    </tr>
                                    <tr>
                                        <td colspan="2"><span class="btn btn-green btn-big"
                                                              onclick="doCheck()">检查数据</span></td>
                                    </tr>
                                    <%
                                        }
                                    %>

                                    </tbody>
                                </table>

                            </div>
                            <%--
                                          <div class="space-6"></div>
                                          <div class="row">
                                            <div class="col-md-2"></div>
                                            <div class="col-md-6 col-md-offset-4">
                                              <ul  id="page-nav" class="pagination pull-right">
                                              </ul></div>
                                          </div>

                            --%>

                        </div>

                        <!-- /.row -->
                        <input name="command" id="command" type="hidden" value="">
                    </form>
                </div>
                <!-- /.page-content-area -->
            </div>
            <!-- /.page-content -->
        </div>
        <!-- /.main-content -->


        <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
            <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
        </a>
    </div>
    <!-- /.main-container -->
    <%@include file="../inc/displayFooter.jsp" %>
    <!-- Modal -->
    <!-- inline scripts related to this page -->
    <script type="text/javascript">
        var page_index = 1;
        var page_size = 10;
        jQuery(function ($) {
            $('.scrollable').each(function () {
                var $this = $(this);
                $(this).ace_scroll({
                    size: $this.data('size') || 100
                    //styleClass: 'scroll-left scroll-margin scroll-thin scroll-dark scroll-light no-track scroll-visible'
                });
            });
            $('[data-rel=tooltip]').tooltip();
            $('[data-rel=popover]').popover({html: true});
            $('textarea[class*=autosize]').autosize({append: "\n"});
            $(document).ajaxStart(function () {
                $("#loading_container").show();
            });

            $(document).ajaxStop(function () {
                setTimeout(function () {
                    $("#loading_container").hide();
                }, 200);
            });
            $('textarea.limited').inputlimiter({
                remText: '%n character%s remaining...',
                limitText: 'max allowed : %n.'
            });

            $("#open-movie-title-boxs").click(function () {
                var $a = $(".movie-title-boxs")
                $this = $(this);
                if ($a.is(':visible')) {
                    $a.slideUp(300, function () {
                        $this.html("查看更多文件");
                    });
                } else {
                    $a.slideDown(300, function () {
                        $this.html("收起");
                    });


                }
            });

            $('#id-input-file-1,#id-input-file-2').ace_file_input({
                no_file: '未选择文件 ...',
                btn_choose: '选择',
                btn_change: '选择',
                droppable: false,
                onchange: null,
                thumbnail: false //| true | large
                //whitelist:'gif|png|jpg|jpeg'
                //blacklist:'exe|php'
                //onchange:''
                //
            });


            var DataSourceTree = function (options) {
                this._data = options.data;
                this._delay = options.delay;
            };

            DataSourceTree.prototype.data = function (options, callback) {
                var self = this;
                var $data = null;

                if (!("name" in options) && !("type" in options)) {
                    $data = this._data;//the root tree
                    callback({data: $data});
                    return;
                }
                else if ("type" in options && options.type == "folder") {
                    if ("additionalParameters" in options && "children" in options.additionalParameters)
                        $data = options.additionalParameters.children;
                    else $data = {};//no data
                }

                if ($data != null)//this setTimeout is only for mimicking some random delay
                    setTimeout(function () {
                        callback({data: $data});
                    }, parseInt(Math.random() * 500) + 200);

                //we have used static data here
                //but you can retrieve your data dynamically from a server using ajax call
                //checkout examples/treeview.html and examples/treeview.js for more info
            };

            $(".btn-dropdown").click(function () {
                $("#tree1").slideDown();
            });
            $("#tree1").mouseleave(function () {
                $(this).slideUp();

            });
            //Android's default browser somehow is confused when tapping on label which will lead to dragging the task
            //so disable dragging when clicking on label
            var agent = navigator.userAgent.toLowerCase();
            if ("ontouchstart" in document && /applewebkit/.test(agent) && /android/.test(agent))
                $('#tasks').on('touchstart', function (e) {
                    var li = $(e.target).closest('#tasks li');
                    if (li.length == 0)return;
                    var label = li.find('label.inline').get(0);
                    if (label == e.target || $.contains(label, e.target)) e.stopImmediatePropagation();
                });
        });
        function doCheck() {
            doCommand('check');
        }
        function doImport() {
            doCommand('doImport');
        }
        function doCommand(command) {
            var importForm = $("#importForm");
            $("#command").val(command);
            if ("check" == command) {
                var users = $("#users").val();
                if (users == null || users == '') {
                    alert("没有输入任何数据！请仔细检查！");
                    return;
                }
            } else if ("doImport" == command) {

            }
            importForm.submit();
        }
        function confirmUnSelectOrg(orgName, checked, id, obj) {
            if (!checked) {
                if (!confirm("您确认不增加组织“" + orgName +
                        "”吗？\r\n如果不添加这个组织，这个组织名下的用户可能会无法使用本平台的资源！\r\n死活都不添加“" + orgName +
                        "”选择“确定”，\r\n如果保持添加，选择“取消”")) {
                    try {
                        obj.checked = true;
                    } catch (e) {
                        $("#" + id).checked = true;
                    }
                }
            }
        }
    </script>

</body>
</html>
<%@include file="../admin/sqlBase.jsp" %>
<%!
    public String executeInsert(String sql, String userId, String userName) {
        logger.debug("准备执行：" + sql);
        List<Object> execResult = executeSql(sql, null, null, null, null);
        if (execResult == null || execResult.size() <= 0) {
            return "无法添加:" + userName + "," + userId;
        } else {
            if (execResult.get(0).equals("0")) {
                String result = "<font color='red'>无法执行:" + userName + "," + userId;
                if (execResult.size() > 2) {
                    String msg = execResult.get(2).toString();
                    if (msg.contains("Duplicate entry")) {
                        result += ",用户重复";
                    } else {
                        result += "," + execResult.get(2);
                    }
                }
                result += "</font>";
                return result;
            }
        }
        return "执行成功，" + userName + "," + userId;
    }

    public Organization getOrganizationFromMaps(Map<String, List<Organization>> parentOrg, String orgName, String parentOrgName) {
        List<Organization> orgs = parentOrg.get(orgName);
        if (orgs != null) {
            Organization p = null;
            if (parentOrgName != null) {
                p = getOrganizationFromMaps(parentOrg, parentOrgName, null);
            }
            if (orgs.size() == 1) {
                Organization o = orgs.get(0);
                if (p != null) {
                    if (p.getId().equals(o.getParentId())) {
                        return o;
                    } else {
                        return null;
                    }
                } else {//如果父节点是null，就返回当前节点
                    if (parentOrgName == null) {
                        return o;
                    } else {
                        return null;
                    }
                }
            } else if (orgs.size() > 1) {
                if (p != null) {
                    for (Organization o1 : orgs) {
                        if (o1.getParentId().equals(p.getId())) {
                            return o1;
                        }
                    }
                } else {
                    //没有父节点，就认为这是一个没有添加的组织，直接返回空
                    if(parentOrgName==null){
                    //如果这是找父节点，那么就返回第一个。如果不是，就返回空
                        return orgs.get(0);
                    }else{
                        return null;
                    }
                }
            }
        }
        return null;
    }
%>