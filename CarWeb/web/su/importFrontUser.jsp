<%@ taglib prefix="s" uri="/struts-tags" %><%@ page import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.rms.business.frontuser.logic.logicInterface.OrganizationLogicInterface" %><%@ page
        import="com.fortune.util.SpringUtils" %><%@ page
        import="com.fortune.rms.business.frontuser.model.Organization" %><%@ page
        import="java.util.List" %><%@ page import="java.util.Map" %><%@ page
        import="java.util.HashMap" %><%@ page
        import="com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface" %><%@ page
        import="com.fortune.rms.business.publish.model.Channel" %><%@ page
        import="java.util.ArrayList" %><%@ page
        import="com.fortune.rms.business.frontuser.model.FrontUser" %><%@ page
        import="com.fortune.util.StringUtils" %><%--  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2014/11/22
  Time: 11:24
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String allChannels="";
//    File f = new File("f:/users.txt");
    String users = request.getParameter("users");
    String command = request.getParameter("command");
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.importFrontUserr.jsp");
    ChannelLogicInterface channelLogicInterface = (ChannelLogicInterface) SpringUtils.getBean("channelLogicInterface",session.getServletContext());
    List<Channel> channels = channelLogicInterface.getAll();
    List<Channel> leafChannels = new ArrayList<Channel>();
    for(Channel c:channels){
        if(channelLogicInterface.isLeafChannel(c.getId())){
           leafChannels.add(c);
        }
    }
    OrganizationLogicInterface organizationLogicInterface = (OrganizationLogicInterface) SpringUtils.getBean("organizationLogicInterface",session.getServletContext());
    //先把原有的组织读出来放到缓存里
    Map<String,Organization> parentOrg = new HashMap<String,Organization>();
    List<Organization> organizations = organizationLogicInterface.getAll();
    Long maxOrgId = 1000L;
    for(Organization o:organizations){
        if(maxOrgId<o.getId()){
            maxOrgId = o.getId();
        }
        parentOrg.put(o.getName(),o);
/*
        if(o.getParentId()==null||o.getParentId()==-1){

        }
*/
    }
    String userSql = "/* delete from front_user where user_id like 'wp%';*/\r\n";
    String errorMsg = "";
    long userType=2;
    List<Organization> willAddOrgs = new ArrayList<Organization>();
    List<FrontUser> willAddUsers = new ArrayList<FrontUser>();
    if("check".equals(command)){
        if (users!=null){
            Map<String,String> frontUser=new HashMap<String,String>();
            try {
                String[] lines = users.split("\n") ;
                long i=1;
                String parentOrgName = null;
                maxOrgId++;
                long orgId=maxOrgId+1;
                for(String s:lines){
                    String[] data = s.split("\t");
                    if(data.length>=6){
                        if(data[0]!=null&&!"".equals(data[0])){
                            parentOrgName = data[0];
                        }
                        String orgName = data[1];
                        String sn = data[2];
                        String userName = data[3];
                        Long gender = data[4]==null||"女".equals(data[4])?0L:1L;
                        String userId = data[5];

                        String pwd="12345";
                        if(data.length>=7){
                            pwd = data[6];
                        }
                        if(userId==null||"".equals(userId.trim())||"wp".equals(userId)){
                            errorMsg+="--用户数据异常：userId="+userId+","+s+"\r\n";
                            continue;
                        }
                        if(frontUser.containsKey(userId)){
                            errorMsg+="--用户重复：userId="+userId+","+s+"\r\n";
                            continue;
                        }
                        frontUser.put(userId,s);
                        Organization o=parentOrg.get(orgName);
                        if(o==null){
                            Organization parent = parentOrg.get(parentOrgName);
                            if(parent==null){
                                parent = new Organization();
                                parent.setName(parentOrgName);
                                parent.setParentId(-1L);
                                parent.setChannels(allChannels);
                                parent.setSequence(i++);
                                //parent = organizationLogicInterface.save(parent);
                                parent.setId(orgId++);
                                parentOrg.put(parentOrgName,parent);
                            }
                            if(!orgName.equals(parentOrgName)){
                                o = new Organization();
                                o.setName(orgName);
                                o.setParentId(parent.getId());
                                o.setChannels(allChannels);
                                o.setSequence(i++);
                                o.setId(orgId++);
                                //o = organizationLogicInterface.save(o);
                                parentOrg.put(orgName,o);
                            }else{
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
                        userSql+="\r\ninsert into front_user(user_id,name,organization_id," +
                                "gender,password,type_id,create_time,logon_times,status)" +
                                "values('" +userId+"','"+userName+"',"+o.getId()+","+gender+",md5('"+pwd+"'),"
                                +userType+",now(),0,1);";
                    }else{
                        errorMsg +="-- 这条数据有问题："+s+"\r\n";
                    }
                }
                //logger.debug(sb);
                String orgSql = "delete from organization where id>=" +maxOrgId+";\r\n" +
                        "delete from organization_channel where organization_id>="+maxOrgId+";\r\n";
                //int ocId=1000;
                for(String key:parentOrg.keySet()){
                    Organization o = parentOrg.get(key);
                    if(o.getId()<=maxOrgId){
                        continue;
                    }
                    if(o.getSequence()==null){
                        o.setSequence(o.getId()-999);
                    }
                    orgSql +="insert into organization(id,name,sequence,parent_id) values(" +
                            o.getId()+",'"+o.getName()+"',"+o.getSequence()+","+o.getParentId()+
                            ");\r\n";
                    willAddOrgs.add(o);
                    for(Channel c:leafChannels){
                        orgSql+="insert into organization_channel(organization_id,channel_id) values(" +
                                o.getId()+","+c.getId()+");\r\n";
                        //ocId++;
                    }
                }
                userSql= errorMsg+"\r\n"+orgSql+userSql;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logger.error("导入数据为空，不能继续");
        }
    }else if("doImport".equals(command)){
        String[] orgIds = request.getParameterValues("orgs");
        String orgSql;
        if(orgIds!=null&&orgIds.length>0){
            Long sequence;
            for(String orgId:orgIds){
                Long id = StringUtils.string2long(orgId,-1L);
                if(id>0){
                    sequence = id;
                    String orgName = request.getParameter("orgName_"+id);
                    if(orgName!=null){
                        long parentId = StringUtils.string2long(request.getParameter("orgParentId_"+id),-1);
                        orgSql ="insert into organization(id,name,sequence,parent_id) values(" +
                                id+",'"+orgName+"',"+sequence+","+parentId+")";
                        errorMsg+=executeInsert(orgSql,"组织："+orgId+"",orgName)+"\r\n";
                        for(Channel c:leafChannels){
                            errorMsg+=executeInsert("insert into organization_channel(organization_id,channel_id) values(" +
                                    id+","+c.getId()+")","绑定组织"+orgName,"到频道"+c.getName())+"\r\n";
                            //ocId++;
                        }
                    }
                }
            }
        }
        String[] userIds = request.getParameterValues("userIds");
        if(userIds!=null&&userIds.length>0){
            for(String userId:userIds){
                String userName = request.getParameter("userName_" + userId);
                if(userName!=null){
                    long orgId = StringUtils.string2long(request.getParameter("userOrgId_"+userId),-1);
                    long gender = StringUtils.string2int(request.getParameter("userGender_"+userId),-1);
                    String pwd = request.getParameter("userPwd_"+userId);
                    if(pwd==null){
                        pwd = "12345";
                    }
                    orgSql ="insert into front_user(user_id,name,organization_id," +
                    "gender,password,type_id,create_time,logon_times,status)" +
                            "values('" +userId.trim()+"','"+userName.trim()+"',"+orgId+","+gender+",md5('"+pwd.trim()+"'),"
                            +userType+",now(),0,1);";
                    errorMsg+=executeInsert(orgSql,userId,userName)+"\r\n";
                }
            }
        }
    }
    if(users==null){
        users = "";
    }

%>
<html>
<head>
    <title>导入前台用户</title>
    <script type="text/javascript">
        function doCheck(){
            doCommand('check');
        }
        function doImport(){
            doCommand('doImport');
        }
        function doCommand(command){
            var importForm = document.forms[0];
            importForm.command.value = command;
            importForm.submit();
        }
    </script>
</head>
<body>
<pre><%=errorMsg%></pre>
<form id="importForm" name="importForm" action="importFrontUser.jsp" method="post">
    <table>
        <%
            if("check".equals(command)){
%>
        <tr>
            <td>请确认</td>
            <td>以下数据将会添加：</td>
        </tr>
        <tr>
            <td colspan="2">增加组织：<br>
                <%
                    for(int i=0,l=willAddOrgs.size();i<l;i++){
                        Organization o =willAddOrgs.get(i);
                        %>
                <input type="checkbox" checked  name="orgs" value="<%=o.getId()%>"><%=o.getName()%><br/>
                    <input type="hidden" value="<%=o.getName()%>" name="orgName_<%=o.getId()%>"/>
                    <input type="hidden" value="<%=o.getParentId()%>" name="orgParentId_<%=o.getId()%>"/>
                <%
                    }
                %>
            </td>
        </tr>
        <tr>
            <td colspan="2">增加员工：<br/>
                <%
                    for(int i=0,l=willAddUsers.size();i<l;i++){
                        FrontUser u =willAddUsers.get(i);
                %>
                <input type="checkbox" checked name="userIds" value="<%=u.getUserId()%>"><%=u.getName()%>,<%=u.getUserId()%><br/>
                    <input type="hidden" value="<%=u.getName()%>" name="userName_<%=u.getUserId()%>"/>
                    <input type="hidden" value="<%=u.getPassword()%>" name="userPwd_<%=u.getUserId()%>"/>
                <input type="hidden" value="<%=u.getGender()%>" name="userGender_<%=u.getUserId()%>"/>
                    <input type="hidden" value="<%=u.getOrganizationId()%>" name="userOrgId_<%=u.getUserId()%>"/>
                        <%
                    }
                %>
            </td>
        </tr>
        <tr>
            <td colspan="2"><input type="button" value="确认，添加这些数据" onclick="doImport()"></td>
        </tr>
        <%
            }
        %>
        <tr>
            <td>数据格式：</td>
            <td>每一行的数据格式为：公司 部门 工号 姓名 性别 登陆账号 口令，中间以tab符间隔(\t)，至少7列！例如：<br/>潍柴动力股份有限公司	上海运营中心	ws000	张三	男	ws000	12345
            </td>
        </tr>
        <tr>
            <td>用户数据：</td>
            <td><textarea id="users" name="users" rows="20" cols="80"><%=users%></textarea></td>
        </tr>
        <tr>
            <td colspan="2"><input type="button" onclick="doCheck()" value="检查数据"></td>
        </tr>
    </table>
    <input name="command" type="hidden" value="">

</form>
</body>
</html><%@include file="../admin/sqlBase.jsp"%><%!
    public String executeInsert(String sql,String userId,String userName){
        logger.debug("准备执行："+sql);
        List<Object> execResult = executeSql(sql,null,null, null,null);
        if(execResult==null||execResult.size()<=0){
            return "无法添加:"+userName+","+userId;
        }else{
            if(execResult.get(0).equals("0")){
                String result = "无法添加:"+userName+","+userId;
                if(execResult.size()>2){
                    result +=","+ execResult.get(2);
                }
                return result;
            }
        }
        return "添加成功:"+userName+","+userId;
    }
%>