<%@ page import="com.fortune.rms.business.log.logic.logicInterface.ClientLogLogicInterface" %>
<%@ page import="com.fortune.util.SpringUtils" %>
<%@ page import="com.fortune.rms.business.log.model.ClientLog" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fortune.rms.business.system.model.PhoneRange" %>
<%@ page import="com.fortune.rms.business.system.logic.logicInterface.PhoneRangeLogicInterface" %>
<%@ page import="com.fortune.util.StringUtils" %>
<%@ page import="com.fortune.util.PageBean" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.fortune.rms.business.user.logic.logicInterface.UserLoginLogicInterface" %>
<%@ page import="com.fortune.rms.business.user.model.UserLogin" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 14-6-4
  Time: 上午10:26
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    PhoneRangeLogicInterface phoneRangeLogicInterface = (PhoneRangeLogicInterface) SpringUtils.getBean("phoneRangeLogicInterface",session.getServletContext());
    PhoneRange bean =new PhoneRange();
    List<PhoneRange> allRange = phoneRangeLogicInterface.search(bean,new PageBean(0,Integer.MAX_VALUE,"o1.phoneFrom","asc"));
    int i=0;
    String command = request.getParameter("command");
    if("repairClientLog".equals(command)){
        ClientLogLogicInterface clientLogLogicInterface = (ClientLogLogicInterface) SpringUtils.getBean("clientLogLogicInterface",session.getServletContext());
        List<ClientLog> all = clientLogLogicInterface.getAll();
        int allCount = all.size();
        logger.debug("要扫描："+all.size()+"条记录！");
        for(ClientLog log:all){
            String phone = log.getPhoneCode();
            i++;
            if(i%50==0){
                logger.debug("扫描了"+i+"/" +allCount+
                        "(" +i*100/allCount+
                        "%)条记录");
            }
            if(phone!=null&&phone.length()==11){
                Long oldAreaId = log.getAreaId();
                Long areaId = getAreaId(phone,allRange);
                if(!areaId.equals(oldAreaId)){
                    logger.debug("尝试修正号码"+phone+"的归属地："+oldAreaId+"到"+areaId);
                    log.setAreaId(areaId);
                    clientLogLogicInterface.save(log);
                }
            }
        }
    }else{
        UserLoginLogicInterface userLoginLogicInterface = (UserLoginLogicInterface) SpringUtils.getBean("userLoginLogicInterface",session.getServletContext());
        List<UserLogin> all = userLoginLogicInterface.getAll();
        int allCount = all.size();
        logger.debug("要扫描："+all.size()+"条记录！");
        for(UserLogin log:all){
            String phone = log.getTel();
            i++;
            if(i%50==0){
                logger.debug("扫描了"+i+"/" +allCount+
                        "(" +i*100/allCount+
                        "%)条记录");
            }
            if(phone!=null&&phone.length()==11){
                Long oldAreaId = log.getAreaId();
                Long areaId = getAreaId(phone,allRange);
                if(!areaId.equals(oldAreaId)){
                    logger.debug("尝试修正号码"+phone+"的归属地："+oldAreaId+"到"+areaId);
                    log.setAreaId(areaId);
                    userLoginLogicInterface.save(log);
                }
            }
        }
    }

%><%!
    private Logger logger = Logger.getLogger("com.fortune.rms.jsp.repairAreaId.jsp");
    public Long getAreaIdOfPhone(String phone,List<PhoneRange> allPhoneRange){
        long phoneNum = StringUtils.string2long(phone,-1);
        if(phoneNum<0){
            return -1L;
        }
        int i=0,l=allPhoneRange.size();
        for(;i<l;i++){
            PhoneRange phoneRange = allPhoneRange.get(i);
            if(phoneRange!=null){
                Long phoneFrom = phoneRange.getPhoneFrom();
                Long phoneTo = phoneRange.getPhoneTo();
                if(phoneFrom!=null&&phoneTo!=null){
                    if(phoneFrom<=phoneNum&&phoneTo>=phoneNum){
                        Long result = phoneRange.getAreaId();
                        if(result!=null){
                            return result;
                        }
                        return -1L;
                    }
                }
            }
        }
        return -1L;
    }

        /**
         * 折半查找
         * @param phoneStr 电话号码
         * @param ranges    号码池
         * @return
         */
    public Long getAreaId(String phoneStr,List<PhoneRange> ranges){
        Long phone = StringUtils.string2long(phoneStr,-1);
        if (phone <= 0) {
            logger.error("无法计算客户手机号码到长整数，客户Phone：" + phoneStr);
            return -1L;
        }
        int max = ranges.size() - 1;
        int min = 0;
        int i = max / 2;
        int j;
        while (true) {
            PhoneRange range = ranges.get(i);
            if (phone >= range.getPhoneFrom() && phone <= range.getPhoneTo()) {
                return range.getAreaId();
            }

            if (max - min == 1 && phone > ranges.get(min).getPhoneTo() && phone < ranges.get(max).getPhoneFrom()) {
                break;
            }

            j = i;
            if (phone < range.getPhoneFrom()) {
                max = i;
                i = min + (max - min) / 2;
                if (i == j) {
                    i--;
                }
            } else if (phone > range.getPhoneTo()) {
                min = i;
                i = min + (max - min) / 2;
                if (i == j) {
                    i++;
                }
            }

            if (i < 0 || i > max) {
                break;
            }
        }

        return -1L;
    }
%>
<html>
<head>
    <title>修复区域ID的错误</title>
</head>
<body>

</body>
</html>
