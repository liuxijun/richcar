/***************************************\\ 库内容 library.js //***********************/
//检查日期是否有效
function isDate(strDate,strMsg){
//    strDate = objFormField.value;
    if(strDate.length>0){
            var dateregex=/^[ ]*[0]?(\d{4})\-(\d{2})\-(\d{2}) (\d{2}):(\d{2}):(\d{2})[ ]*$/;
             var match=strDate.match(dateregex);
             if (match){
                      //alert(match[1]+'-'+match[2]+'-'+match[3]+' '+match[4]+':'+match[5]+':'+match[6]);
                       var tmpdate=new Date(match[1],parseInt(match[2],10)-1,
                                parseInt(match[3])
                                //);/*
                                ,
                                parseInt(match[4]),
                                parseInt(match[5]),
                                parseInt(match[6])
                                );
                                //  */
                  if (tmpdate.getDate()==parseInt(match[3],10)
                   && tmpdate.getFullYear()==parseInt(match[1],10)
                   && (tmpdate.getMonth()+1)== parseInt(match[2],10)
                   && (tmpdate.getHours()   == parseInt(match[4],10))
                   && (tmpdate.getMinutes() == parseInt(match[5],10))
                   && (tmpdate.getSeconds() == parseInt(match[6],10))
                   ){
                    return true;
                    }
             }
                    //alert("- "+ strMsg +" is Required.\nExample: 01/01/2003\n");
         return false;
    }
    else{
          return true;
    }
}

function checkDate(inString)
{
var tempDate;
///*
 var tempDate;
 try{
//  alert(inString);
  var formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  alert("checking string:"+inString);
  var a = new java.util.Date(inString);
  var y=a.getFullYear();
  var m=a.getMonth()+1;
  var d=a.getDate();
  var h=a.getHours();
  var mi=a.getMinutes();
  var se=a.getSeconds();

  var myday=y + "-" + m + "-" + d+" "+h+":"+mi+":"+se;
  var b= formatter.parse(myday);
  alert(a + "\n" + b);
//  alert(b.getTime()==a.getTime());
  if (a.getTime() != b.getTime()){
   return false;
  }
  return true;

 }catch(e){
  return false;
 }
// */
}
 

//去除字符串两头的空格
function trim(str) {
    return str.replace(/(^\s*)|(\s*$)/g, "");
}

//英文字母检查
function isletter(c) {
    if (((c>='a') && (c<='z')) || ((c>='A') && (c<='Z')))
        return true;
    else
    {
        alert("不是英文字母!");
        return false;
    }    
}

//数字检查
function isnumber(c) {
    if ((c>='0') && (c<='9'))
        return true;
    else
    {
        alert("不是数字!");
        return false;
    }
}

//字符检查
function iscode(c) {
    if (((c>='a') && (c<='z')) || ((c>='A') && (c<='Z')) || ((c>='0') && (c<='9')) || (c=='_'))
        return true;
    else
    {
        alert("不是字符!");
        return false;
    }
}

//*****************以上是基本函数***********************//

//得到字符串绝对长度(汉字按两个字符计)
function getStrLen(str) {
    var i, counter, length;
    str=trim(str);
    length = str.length;
    counter = 0;
    for (i = 0; i < length; i ++)
    {
        if (str.charCodeAt(i) > 255 || str.charCodeAt(i) < 0)
        {
            counter +=2;
        }
        else 
        {
            counter +=1;
        }
    }
    return counter;
}

//中文字符判断
function CheckCNCode(str) {
  str=trim(str);
  var length=str.length;
    for (i=0; i < length; i++)
    {
        if (str.charCodeAt(i) > 255 || str.charCodeAt(i) < 0)
        {
          alert("错误\n\n不能使用中文!");
          return false;
        }
    }
}

//数字有效性检查
function CheckNumber(value) {
var theValue;
theValue = value;
    if (theValue=="")
    {
        alert("错误!\n\n请输入正确数字!");
        return false;
    }        
    else if (theValue.indexOf(" ")>=0)
    {
        alert("错误!\n\n请输入正确数字!\请勿包含空格!");
        return false;
    }
    else if (isNaN(theValue)) 
    {    
        alert("错误!\n\n请输入正确数字!");
        return false;
    }
    else if (theValue.indexOf('.')>1)
    {
        alert("错误!\n\n只能包含一个小数点!")
        return false;
    }
    return true;
}


//正整型数值判断
function isIntNumber(value) {
var theValue;
theValue = value;
    if (CheckNumber(theValue))
    { 
        if (parseInt(theValue)<0)
        {
        alert("错误!\n\n请输入正确数字!");
        return false;
        }
        else if (parseInt(theValue)>32768)
        {
        alert("错误!\n\n数值太大了!");
        return false;
        }
    }
    else
    { 
        alert("错误!\n\n请输入正确数字!");
        return false;
    }

}


//电话号码检查
function isTel(str) {
    var c,i, checkresult,length;
    str=trim(str);
    length = str.length;
    checkresult = true;
    for (i = 0; i < length; i++)
    {
        c=str.substr(i,1);    
        if (((c>='0') && (c<='9')) || (c=='-') || (c=='(') || (c==')') || (c=='*') || (c=='_'))
        { checkresult=true; }
        else
        {
            checkresult=false;
            break;    
        }
    }
    if (!checkresult)
    {
        alert("无效电话号码!");
        return false;
    }    
}

//邮政编码检查
function isZipCode(zip) {
    if (zip!=null)
    {
        zip=trim(zip);
        if(zip.length ==0 || zip.length ==5 || zip.length ==6)
        {
            if(!isnumber(zip))
            {
                alert("邮政编码错误!");
                return false;
            }
        }
        else
        {
            alert("邮政编码错误!");
            return false;
        }
    }
    else    
    return true;
}

//Email地址验证
function CheckEmail(email) {
    var i,schar,batbegin,bdotbegin;
    if (email == "")
    {
        alert("E-mail不可为空!");
        return false;
    }
    schar=email.substr(i, 1);
    if ((schar == "@") || (schar == "."))
    {
        alert("E-mail错误!");
        return false;
    }
    for (i = 1;  i < email.length;  i++)
    {
        schar=email.substr(i, 1);
        if (email.substr(i, 1) == "@")
        {
            if (bdotbegin || batbegin)
            {
                alert("E-mail错误!");
                return false;
            }
            batbegin=true;
        }
        if (email.substr(i, 1) == ".")
        {
            if (!batbegin)
            {
                alert("E-mail错误!");
                return false;
            }
            bdotbegin=true;
        }
    }
    
    if ((!batbegin) || (!bdotbegin))
    {
        alert("E-mail错误!");
        return false;
    }
    return true;
}    

