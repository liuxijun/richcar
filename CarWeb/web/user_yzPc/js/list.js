// ---       ����Ҷ���з�ҳ������Ȳ����Ĵ���
  function jumpTo(pageNum){
    var formData = FrmSearchResult;
    formData.page_no.value=pageNum;
    formData.submit();
    clearWindow();
  }
  function refresh(){
     jumpTo(1);
    clearWindow();
  }
  function gotoPage(){
  var formData = FrmSearchResult;
  formData.submit();
    clearWindow();
  }
  function returnBgColor(tdElement){
    tdElement.bgColor = "#E0E0E0";
    clearWindow();
  }
  function orderBy(NewOrderBy){
    var form = FrmSearchResult;
    form.page_no.value=1;
    if(form.order_by.value == NewOrderBy){
        form.order_by.value=NewOrderBy+" desc";
    }else{
        form.order_by.value=NewOrderBy;
    }
    form.submit();
    clearWindow();
  }
  function clearWindow(){
    document.body.innerHTML = "<br><br><p align='center'>���Ժ�.......</p>";
  }