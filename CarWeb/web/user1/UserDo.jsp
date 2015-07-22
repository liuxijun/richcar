<%
/**
 * Copyright ?2002 Shanghai Fudan Ghuanghua Co. Ltd.
 * All right reserved.
 *
 * @�ļ����ƣ�UserDo.jsp
 * @��Ҫ���ܣ�
 * ���user�������ɾ���ĺ���֤������
 * @���������
 * 1. user_id���������Ϣ
 * 2. command(add��delete,modify,audit)
 * @���������
 * �����ɹ����
 * @��    �ߣ�zbxue
 * @��    �ڣ�
 *    @��ʼʱ�䣺 2004-08-31
 *    @����ʱ�䣺
 *
 *    @�޸���Ա��
 *    @�޸�������ԭ��
 *
 */
%><%@page
contentType="text/html;charset=gb2312" %><%@page
import="java.util.*,
        cn.sh.guanghua.mediastack.common.ConfigManager,
        cn.sh.guanghua.mediastack.user.UserLogic,
        cn.sh.guanghua.cache.CacheManager" %><%@page
import="cn.sh.guanghua.mediastack.dataunit.*" %><%@page
import="cn.sh.guanghua.util.tools.ParamTools" %><%@page
import="cn.sh.guanghua.util.tools.MD5" %><%@page
import="cn.sh.guanghua.mediastack.common.Constants"%><%@page
import="cn.sh.guanghua.mediastack.common.Db"%><%@page
import="cn.sh.guanghua.mediastack.common.Logger"%><%@page
import="cn.sh.guanghua.util.tools.*" %><%
    PageHelper pageHelper = new PageHelper(request,session);
    StringBuffer sbData = new StringBuffer("");
    String msg = "";
    String returnUrl = "";
    String sUserId = ParamTools.getParameter(request,"user_id","");
    try{
        String sCommand = ParamTools.getParameter(request,"command","invalid");
        sbData.append(PageHelper.addElement("command",sCommand));
        MD5 myMd5=new MD5();

        if ("add".equals(sCommand) || "modify".equals(sCommand)){
            //String sUserId = ParamTools.getParameter(request,"user_id","");
            String userName = ParamTools.getParameter(request,"user_name","");
            String userPas = ParamTools.getParameter(request,"user_pas","");
            String userSex = ParamTools.getParameter(request,"user_sex","");
            long userCity = ParamTools.getLongParameter(request,"user_city",0);
            long userConntype = ParamTools.getLongParameter(request,"user_conntype",0);
            String userQuestion = ParamTools.getParameter(request,"user_question","");
            String userAnswer = ParamTools.getParameter(request,"user_answer","");
            String userEmail = ParamTools.getParameter(request,"user_email","");
            String userBirthday = ParamTools.getParameter(request,"user_birthday","");
            long userPapertype = ParamTools.getLongParameter(request,"user_papertype",0);
            String userPapercode = ParamTools.getParameter(request,"user_papercode","");
            long userEducation = ParamTools.getLongParameter(request,"user_education",0);
            long userIncome = ParamTools.getLongParameter(request,"user_income",0);
            long userOccupation = ParamTools.getLongParameter(request,"user_occupation",0);
            long userMarry = ParamTools.getLongParameter(request,"user_marry",0);
            String userTel = ParamTools.getParameter(request,"user_tel","");
            String userPostcode = ParamTools.getParameter(request,"user_postcode","");
            String userAddress = ParamTools.getParameter(request,"user_address","");
            User user = new User();
            if ("modify".equals(sCommand)){
                user =  (User)CacheManager.getInstance().getFromDB("User",sUserId);
                if (user == null){
                    msg = "�޸��û�ʧ��";
                    throw new Exception();
                }
            }
            user.setUserId(sUserId);
            user.setUserName(userName);
            user.setUserPassword(myMd5.getMD5ofStr(userPas));
            user.setUserSex(userSex);
            user.setUserCity(userCity);
            user.setUserConnectType(userConntype);
            user.setUserQuestion(userQuestion);
            user.setUserAnswer(userAnswer);
            user.setUserEmail(userEmail);
            user.setUserBirthday(userBirthday);
            user.setUserCertType(userPapertype);
            user.setUserCertCode(userPapercode);
            user.setUserEducation(userEducation);
            user.setUserIncome(userIncome);
            user.setUserWork(userOccupation);
            user.setUserMarried(userMarry);
            user.setUserTel(userTel);
            user.setUserPostcode(userPostcode);
            user.setUserAddress(userAddress);
            if ("add".equals(sCommand)){
                User user1 =  (User)CacheManager.getInstance().getFromDB("User",sUserId);
                if (user1 == null){
                    user.setUserAudited(ConfigManager.getConfig().node("user-default-status").getLong("value",0));
                    if (!UserLogic.createUser(user)){
                        msg = "ע���û�ʧ��";
                        returnUrl = "javascript:history.back();";
                        throw new Exception();
                    }
                }else{
                    msg = "���û��Ѵ���";
                    returnUrl = "javascript:history.back();";
                    throw new Exception();
                }
                msg= sUserId + "����ϲ���Ѿ�ע��ɹ��ˣ������û���½����½ϵͳ��";
                //returnUrl ="AccountList.jsp";
            }
            if ("modify".equals(sCommand)){
                if (!UserLogic.modifyUser(user)){
                    msg = "�޸��û�ʧ��";
                    throw new Exception();
                }
                msg= "�޸��û��ɹ�";

                CacheManager.getInstance().notifyServerUpdate("User",sUserId);
            }

        }

        if ("chgpwd".equals(sCommand)){
            //String sUserId = ParamTools.getParameter(request,"user_id","-1");
            User user =  (User)CacheManager.getInstance().getFromDB("User",sUserId);
            String oldPas =  ParamTools.getParameter(request,"user_oldpassword","");
            String pas =  ParamTools.getParameter(request,"user_password","");
            if (myMd5.getMD5ofStr(oldPas).equals(user.getUserPassword())){
                if (!UserLogic.changePwd(sUserId,myMd5.getMD5ofStr(pas))){
                    msg = "�޸�����ʧ��";
                    throw new Exception();
                }
            }else{
                msg = "�������";
                throw new Exception();
            }
            msg = "�޸�����ɹ�";

            CacheManager.getInstance().notifyServerUpdate("User",sUserId);
        }



        //sbData.append(PageHelper.addElement("flag","success"));
    }catch(Throwable t){
        //t.printStackTrace();
        //sbData.append(PageHelper.addElement("flag","fail"));

    }


    msg = java.net.URLEncoder.encode(msg);
    returnUrl =  java.net.URLEncoder.encode(returnUrl);
    response.sendRedirect("info.jsp?msg="+msg+"&return_url="+returnUrl);
    return;

    //pageHelper.outPut(out,sbData.toString(),"","","","","zbxue");

%>