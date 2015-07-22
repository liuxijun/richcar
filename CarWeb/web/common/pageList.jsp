<%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %>
<table width="100%">
    <tr>
        <td align="right">
            共${pageBean.rowCount}条，${pageBean.pageCount}页，当前${pageBean.pageNo}页。
            <a style="cursor:hand" onclick="prevPage()">上一页</a>，
            <a  style="cursor:hand" onclick="nextPage()">下一页</a>，跳转到<s:textfield name="pageBean.pageNo" id="pageNo" cssStyle="width:30px"/>页，
            <input type="submit" value="go"/>
        </td>
    </tr>
</table>
<s:hidden name="pageBean.orderBy" id="orderBy"/>
<s:hidden name="pageBean.orderDir" id="orderDir"/>
<script language="javascript">
var curForm = searchForm;
var currentPageNo = ${pageBean.pageNo};
if(curForm==null) curForm = document.forms[0];
    function prevPage(){
        if(currentPageNo>1){
            toPageQuery(currentPageNo-1);
        }else{

        }
    }
    function nextPage(){
        if(currentPageNo<${pageBean.pageCount}){
            toPageQuery(currentPageNo+1);
        }else{

        }
    }
    function toPageQuery(page){
		curForm.pageNo.value = page;
//		alert(curForm.page.value);
		curForm.method = "post";
		curForm.target = "_self";
		curForm.submit();
	} 
	function toSpecifiedPageQuery(){
		curForm.method = "post";
		curForm.target = "_self";
		curForm.submit();
	}
    function orderBy(colName){
 	  curForm.orderBy.value=colName;
	  if(curForm.orderDir.value==''||curForm.orderDir.value=='asc'){
		curForm.orderDir.value='desc';
	  }else{
		curForm.orderDir.value='asc';
	  }
      curForm.pageNo.value = 1;
	  curForm.submit();
}
</script>
