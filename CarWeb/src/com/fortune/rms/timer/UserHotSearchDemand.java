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
 * Time: 下午12:07
 * To change this template use File | Settings | File Templates.
 */
public class UserHotSearchDemand extends TimerBase {
    public void run(){
        try {

            logger.debug("更新搜索记录  UserHotSearchDemand run timer:"+ StringUtils.date2string(new Date()));
            UserHotSearchLogicInterface userHotSearchLogicInterface = (UserHotSearchLogicInterface) SpringUtils.getBean("userHotSearchLogicInterface");
            {
                Calendar cl = Calendar.getInstance();
                cl.setTime(new Date());
                cl.add(Calendar.DATE, -7);
                Date startTime = cl.getTime();
                //总搜索量
                List<Object[]> searchCountList = hibernateUtils.findAll("select searchValue,count(searchValue) from SearchLog  group by searchValue");
                if(searchCountList.size()>0){
                     for(Object[] o : searchCountList){
                         String searchValue = o[0].toString();
                         long searchCount = Long.parseLong(o[1].toString());
                         if(searchValue.indexOf(String.valueOf("'"))==-1){
                             //不包含"'"字符在数据库中查询没问题。
                         }else{
                            //包含“’”字符串在数据库查询就会出错，解决方法，在引号后加个引号。
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
                    logger.debug("总的搜索量更新完毕。");
                }
                //一周的搜索量
                List<Object[]> searchWeekList = hibernateUtils.findAll("select searchValue,count(searchValue) from SearchLog  where searchTime>? group by searchValue",new Object[]{startTime});
                if(searchWeekList.size()>0){
                    for(Object[] o : searchWeekList){
                        String searchValue = o[0].toString();
                        long searchCount = Long.parseLong(o[1].toString());
                        if(searchValue.indexOf(String.valueOf("'"))==-1){
                            //不包含"'"字符在数据库中查询没问题。
                        }else{
                            //包含“’”字符串在数据库查询就会出错，解决方法，在引号后加个引号。
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
                logger.debug("周的搜索量更新完毕。");
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
                            //不包含"'"字符在数据库中查询没问题。
                        }else{
                            //包含“’”字符串在数据库查询就会出错，解决方法，在引号后加个引号。
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
                logger.debug("月的搜索量更新完毕。");
            }

            logger.debug("数据更新完毕");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
