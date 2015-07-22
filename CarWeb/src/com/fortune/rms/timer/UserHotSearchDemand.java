package com.fortune.rms.timer;

import com.fortune.rms.business.publish.logic.logicInterface.UserHotSearchLogicInterface;
import com.fortune.rms.business.publish.model.UserHotSearch;
import com.fortune.rms.timer.base.TimerBase;
import com.fortune.util.SpringUtils;
import com.fortune.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xy
 * Date: 13-10-25
 * Time: ����12:07
 * To change this template use File | Settings | File Templates.
 */
public class UserHotSearchDemand extends TimerBase {
    public void run(){
        try {

            logger.debug("����������¼  UserHotSearchDemand run timer:"+ StringUtils.date2string(new Date()));
            UserHotSearchLogicInterface userHotSearchLogicInterface = (UserHotSearchLogicInterface) SpringUtils.getBean("userHotSearchLogicInterface");
            {
                Calendar cl = Calendar.getInstance();
                cl.setTime(new Date());
                cl.add(Calendar.DATE, -7);
                Date startTime = cl.getTime();
                //��������
                List<Object[]> searchCountList = hibernateUtils.findAll("select searchValue,count(searchValue) from SearchLog  group by searchValue");
                if(searchCountList.size()>0){
                     for(Object[] o : searchCountList){
                         String searchValue = o[0].toString();
                         long searchCount = Long.parseLong(o[1].toString());
                         if(searchValue.indexOf(String.valueOf("'"))==-1){
                             //������"'"�ַ������ݿ��в�ѯû���⡣
                         }else{
                            //�����������ַ��������ݿ��ѯ�ͻ������������������ź�Ӹ����š�
                             StringBuilder sb = new StringBuilder(searchValue);
                             sb.insert(searchValue.indexOf(String.valueOf("'"))+1,"'");
                             searchValue = sb.toString();
                         }
                         List<UserHotSearch> hotUserSearch = hibernateUtils.findAll("from UserHotSearch where content = '"+searchValue+"'");
                         if(hotUserSearch.size()>0){
                             UserHotSearch userHotSearch = hotUserSearch.get(0);
                             userHotSearch.setSearchCount(searchCount);
                             userHotSearch.setCreateTime(new Date());
                             userHotSearchLogicInterface.save(userHotSearch);
                         }else{
                             UserHotSearch userHotSearch = new UserHotSearch();
                             userHotSearch.setContent(searchValue);
                             userHotSearch.setSearchCount(searchCount);
                             userHotSearch.setCreateTime(new Date());
                             userHotSearchLogicInterface.save(userHotSearch);
                         }
                     }
                    logger.debug("�ܵ�������������ϡ�");
                }
                //һ�ܵ�������
                List<Object[]> searchWeekList = hibernateUtils.findAll("select searchValue,count(searchValue) from SearchLog  where searchTime>? group by searchValue",new Object[]{startTime});
                if(searchWeekList.size()>0){
                    for(Object[] o : searchWeekList){
                        String searchValue = o[0].toString();
                        long searchCount = Long.parseLong(o[1].toString());
                        if(searchValue.indexOf(String.valueOf("'"))==-1){
                            //������"'"�ַ������ݿ��в�ѯû���⡣
                        }else{
                            //�����������ַ��������ݿ��ѯ�ͻ������������������ź�Ӹ����š�
                            StringBuilder sb = new StringBuilder(searchValue);
                            sb.insert(searchValue.indexOf(String.valueOf("'"))+1,"'");
                            searchValue = sb.toString();
                        }
                        List<UserHotSearch> hotUserSearch = hibernateUtils.findAll("from UserHotSearch where content = '"+searchValue+"'");
                        if(hotUserSearch.size()>0){
                            UserHotSearch userHotSearch = hotUserSearch.get(0);
                            userHotSearch.setSearchWeekCount(searchCount);
                            userHotSearch.setCreateTime(new Date());
                            userHotSearchLogicInterface.save(userHotSearch);
                        }else{
                            UserHotSearch userHotSearch = new UserHotSearch();
                            userHotSearch.setContent(searchValue);
                            userHotSearch.setSearchWeekCount(searchCount);
                            userHotSearch.setCreateTime(new Date());
                            userHotSearchLogicInterface.save(userHotSearch);
                        }
                    }
                }
                logger.debug("�ܵ�������������ϡ�");
            }
            Calendar cl1 = Calendar.getInstance();
            cl1.setTime(new Date());
            cl1.add(Calendar.MONTH,-1);
            Date startTime = cl1.getTime();
            {
                List<Object[]> visitMonthLogs = hibernateUtils.findAll("select searchValue,count(searchValue) from SearchLog  where searchTime>? group by searchValue",new Object[]{startTime});
                if(visitMonthLogs.size()>0){
                    for(Object[] o : visitMonthLogs){
                        String searchValue = o[0].toString();
                        long searchCount = Long.parseLong(o[1].toString());
                        if(searchValue.indexOf(String.valueOf("'"))==-1){
                            //������"'"�ַ������ݿ��в�ѯû���⡣
                        }else{
                            //�����������ַ��������ݿ��ѯ�ͻ������������������ź�Ӹ����š�
                            StringBuilder sb = new StringBuilder(searchValue);
                            sb.insert(searchValue.indexOf(String.valueOf("'"))+1,"'");
                            searchValue = sb.toString();
                        }
                        List<UserHotSearch> hotUserSearch = hibernateUtils.findAll("from UserHotSearch where content = '"+searchValue+"'");
                        if(hotUserSearch.size()>0){
                            UserHotSearch userHotSearch = hotUserSearch.get(0);
                            userHotSearch.setSearchMonthCount(searchCount);
                            userHotSearch.setCreateTime(new Date());
                            userHotSearchLogicInterface.save(userHotSearch);
                        }else{
                            UserHotSearch userHotSearch = new UserHotSearch();
                            userHotSearch.setContent(searchValue);
                            userHotSearch.setSearchMonthCount(searchCount);
                            userHotSearch.setCreateTime(new Date());
                            userHotSearchLogicInterface.save(userHotSearch);
                        }
                    }
                }
                logger.debug("�µ�������������ϡ�");
            }

            logger.debug("���ݸ������");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
