/***************************************\\ ������ library.js //***********************/
//��������Ƿ���Ч
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
 

//ȥ���ַ�����ͷ�Ŀո�
function trim(str) {
    return str.replace(/(^\s*)|(\s*$)/g, "");
}

//Ӣ����ĸ���
function isletter(c) {
    if (((c>='a') && (c<='z')) || ((c>='A') && (c<='Z')))
        return true;
    else
    {
        alert("����Ӣ����ĸ!");
        return false;
    }    
}

//���ּ��
function isnumber(c) {
    if ((c>='0') && (c<='9'))
        return true;
    else
    {
        alert("��������!");
        return false;
    }
}

//�ַ����
function iscode(c) {
    if (((c>='a') && (c<='z')) || ((c>='A') && (c<='Z')) || ((c>='0') && (c<='9')) || (c=='_'))
        return true;
    else
    {
        alert("�����ַ�!");
        return false;
    }
}

//*****************�����ǻ�������***********************//

//�õ��ַ������Գ���(���ְ������ַ���)
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

//�����ַ��ж�
function CheckCNCode(str) {
  str=trim(str);
  var length=str.length;
    for (i=0; i < length; i++)
    {
        if (str.charCodeAt(i) > 255 || str.charCodeAt(i) < 0)
        {
          alert("����\n\n����ʹ������!");
          return false;
        }
    }
}

//������Ч�Լ��
function CheckNumber(value) {
var theValue;
theValue = value;
    if (theValue=="")
    {
        alert("����!\n\n��������ȷ����!");
        return false;
    }        
    else if (theValue.indexOf(" ")>=0)
    {
        alert("����!\n\n��������ȷ����!\��������ո�!");
        return false;
    }
    else if (isNaN(theValue)) 
    {    
        alert("����!\n\n��������ȷ����!");
        return false;
    }
    else if (theValue.indexOf('.')>1)
    {
        alert("����!\n\nֻ�ܰ���һ��С����!")
        return false;
    }
    return true;
}


//��������ֵ�ж�
function isIntNumber(value) {
var theValue;
theValue = value;
    if (CheckNumber(theValue))
    { 
        if (parseInt(theValue)<0)
        {
        alert("����!\n\n��������ȷ����!");
        return false;
        }
        else if (parseInt(theValue)>32768)
        {
        alert("����!\n\n��ֵ̫����!");
        return false;
        }
    }
    else
    { 
        alert("����!\n\n��������ȷ����!");
        return false;
    }

}


//�绰������
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
        alert("��Ч�绰����!");
        return false;
    }    
}

//����������
function isZipCode(zip) {
    if (zip!=null)
    {
        zip=trim(zip);
        if(zip.length ==0 || zip.length ==5 || zip.length ==6)
        {
            if(!isnumber(zip))
            {
                alert("�����������!");
                return false;
            }
        }
        else
        {
            alert("�����������!");
            return false;
        }
    }
    else    
    return true;
}

//Email��ַ��֤
function CheckEmail(email) {
    var i,schar,batbegin,bdotbegin;
    if (email == "")
    {
        alert("E-mail����Ϊ��!");
        return false;
    }
    schar=email.substr(i, 1);
    if ((schar == "@") || (schar == "."))
    {
        alert("E-mail����!");
        return false;
    }
    for (i = 1;  i < email.length;  i++)
    {
        schar=email.substr(i, 1);
        if (email.substr(i, 1) == "@")
        {
            if (bdotbegin || batbegin)
            {
                alert("E-mail����!");
                return false;
            }
            batbegin=true;
        }
        if (email.substr(i, 1) == ".")
        {
            if (!batbegin)
            {
                alert("E-mail����!");
                return false;
            }
            bdotbegin=true;
        }
    }
    
    if ((!batbegin) || (!bdotbegin))
    {
        alert("E-mail����!");
        return false;
    }
    return true;
}    

