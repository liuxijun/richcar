<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2009-4-17
  Time: 7:54:06
  普通树结构展示
--%><%@ taglib prefix="s" uri="/struts-tags" %><%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<base target="_self">
<head>
	<title>树</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="树,科目树,频道树">
	<meta http-equiv="description" content="树的结构展示，通用的">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/inc/style.css">
	<link rel="StyleSheet" href="<%=request.getContextPath()%>/js/dtree.css" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/dtree.js"></script>
	<SCRIPT language=javascript src="<%=request.getContextPath()%>/inc/fade.js"></SCRIPT>
	<SCRIPT language=javascript src="<%=request.getContextPath()%>/inc/pz_chromeless.js" type=text/javascript></SCRIPT>

</head>

<script language="javascript" type="text/javascript">
    var aTree = new dTree("aTree");
    <s:if test="multiSelect==true">
      aTree.config.useCheckbox= true;
    </s:if>
<s:iterator id="treeNode" value="objs">
    aTree.add("${id}","${parentId}","${name}","javascript:onSelect('${name}','${id}')");
</s:iterator>
		/*function onSelect(value) {
    		returnValue = value;
    		close();
		}*/

		function onSelect(name,id) {
        	window.location.replace(actionUrl+"-" + id + "!update.action");
		}
    function missionDone(){
        var aForm = document.forms[0];
        returnValue = "";
        if(aForm){
            var i;
            for(i=0;i<aForm.elements.length;i++){
                var ele = aForm.elements[i];
                if(ele.type=="checkbox" && ele.checked){
                    if(returnValue != ""){
                        returnValue += "|";
                    }
                    returnValue += ele.name+","+ele.value;
                }
            }

        }
        //alert(returnValue);
        close();
    }
</script>

<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<div align="center">


           <table width="100%" height="68" border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                <td ><br><script>
                    document.write(aTree)
                </script></td>
              </tr>
            </table>
      </div>
</body>
</html>
