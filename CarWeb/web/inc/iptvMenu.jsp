<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %>
<%@ include file="webroot.jsp"%>
<SCRIPT language=JavaScript type=text/JavaScript>
<!--
function MM_jumpMenu(targ,selObj,restore){ //v3.0
  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+document.form1.texts.value+"'");
  if (restore) selObj.selectedIndex=0;
}
//-->
</SCRIPT>
<SCRIPT language=JavaScript src="<%=webroot%>/js/jquery.js"></SCRIPT>
<script type="text/javascript">
	$(function(){
		$("#nav").find("li").each(function(i){
			if($(this).find("a").size()<=0){
				$(this).hide();
			}
		});
}); 
</script>
<TABLE height="30" cellSpacing="0" cellPadding="0" width="1000" align="center" 
	background="<%=webroot%>/images/jinfen_02.jpg" border="0">
  <TBODY>
    <tr><td>
    <table width=94% align=center><tr>
	<td>
<div id="nav">
	<ul>
	<li class="menu2" onMouseOver="this.className='menu1'" onMouseOut="this.className='menu2'">管理员管理	
	<div class="list">
<fts:permission target="operatorActionAdd"><a href="/security/operatorAction!add.action">添加管理员</a><br/></fts:permission>
<fts:permission target="operatorActionList"><a href="/security/operatorAction!list.action">管理员查询</a><br/></fts:permission>
	</div>
	</li>
<li class="menu2" onMouseOver="this.className='menu1'" onMouseOut="this.className='menu2'">天气预报管理
	<div class="list">
<fts:permission target="weatherActionAdd"><a href="/media/weatherAction!view.action">输入天气信息</a><br/></fts:permission>
<fts:permission target="weatherActionList"><a href="/media/weatherAction!list.action">查询天气信息</a><br/></fts:permission>
	</div>
	</li>
	<li class="menu2" onMouseOver="this.className='menu1'" onMouseOut="this.className='menu2'">单位管理
	<div class="list">
<fts:permission target="orgActionAdd"><a href="/organization/orgAction!add.action">添加单位</a><br/></fts:permission>
<fts:permission target="orgActionList"><a href="/organization/orgAction!list.action">单位管理</a><br/></fts:permission>
	</div>
	</li>
	<li class="menu2" onMouseOver="this.className='menu1'" onMouseOut="this.className='menu2'">媒体文件管理	
	<div class="list">
<fts:permission target="mediaActionList"><a href="/media/mediaAction!list.action">媒体资源管理</a><br/></fts:permission>
<fts:permission target="nodeActionAdd"><a href="/media/nodeAction!add.action">添加频道</a><br/></fts:permission>
<fts:permission target="nodeActionList"><a href="/media/nodeAction!list.action">频道管理</a><br/></fts:permission>
<fts:permission target="subjectActionAdd"><a href="/media/subjectAction!add.action">添加科目</a><br/></fts:permission>
<fts:permission target="subjectActionList"><a href="/media/subjectAction!list.action">科目管理</a><br/></fts:permission>
	</div>
	</li>
	<li class="menu2" onMouseOver="this.className='menu1'" onMouseOut="this.className='menu2'">产品管理	
	<div class="list">
<fts:permission target="serviceActionAdd"><a href="/product/serviceAction!add.action">产品定义</a><br/></fts:permission>
<fts:permission target="serviceActionList"><a href="/product/serviceAction!list.action">产品管理</a><br/></fts:permission>
	</div>
	</li>
	<li class="menu2" onMouseOver="this.className='menu1'" onMouseOut="this.className='menu2'">服务器管理	
	<div class="list">
        <fts:permission target="serverActionAdd"><a href="/device/serverAction!add.action">新增服务器</a><br/></fts:permission>
        <fts:permission target="serverActionList"><a href="/device/serverAction!list.action">服务器列表</a><br/></fts:permission>
        <fts:permission target="cdnAction"><a href="/device/cdnServerAction!list.action">调度管理</a><br/></fts:permission>
        <fts:permission target="cdnServerInfo"><a href="/device/cdnMonitorAction!list.action">调度监控</a><br/></fts:permission>
	</div>
	</li>
	<li class="menu2" onMouseOver="this.className='menu1'" onMouseOut="this.className='menu2'">系统管理	
	<div class="list">
<fts:permission target="permissionActionAdd"><a href="/security/permissionAction!add.action">新建权限</a><br/></fts:permission>
<fts:permission target="permissionActionList"><a href="/security/permissionAction!list.action">权限管理</a><br/></fts:permission>
<fts:permission target="roleActionAdd"><a href="/security/roleAction!add.action">新建角色</a><br/></fts:permission>
<fts:permission target="roleActionList"><a href="/security/roleAction!list.action">角色管理</a><br/></fts:permission>
<%--
<fts:permission target="systemLogActionList"><a href="/organization/systemLogAction!list.action">日志管理</a><br/></fts:permission>
--%>
	</div>
	</li>
	</ul>
</div>
	</td>
	<td width=75 class="menu2"  align=center><a href="/security/logout.action" >退出系统</a></td>
    </tr></table>
    </td></tr>
    </TBODY>
    </TABLE>

<!-- 
//menu2.addItem("  绑定频道","???");
//menu2.addItem("  绑定服务器","???");
//menu5.addItem("  价格策略", "???");
//menu5.addItem("  产品的上线", "???");
//menu4.addItem("  上线管理", "???");
//menu6.addItem("  绑定SP", "???");

//menu6.addItem("  监控", "???");
 -->