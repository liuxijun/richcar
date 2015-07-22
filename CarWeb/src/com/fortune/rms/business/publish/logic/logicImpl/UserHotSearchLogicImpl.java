package com.fortune.rms.business.publish.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.publish.dao.daoInterface.UserHotSearchDaoInterface;
import com.fortune.rms.business.publish.logic.logicInterface.UserHotSearchLogicInterface;
import com.fortune.rms.business.publish.model.UserHotSearch;
import edu.emory.mathcs.backport.java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userHotSearchLogicInterface")
public class UserHotSearchLogicImpl extends BaseLogicImpl<UserHotSearch>
            implements
        UserHotSearchLogicInterface{
    private UserHotSearchDaoInterface userHotSearchDaoInterface;


    /**
     * @param userHotSearchDaoInterface the userHotSearchDaoInterface to set
     */
    @Autowired
    public void setUserHotSearchDaoInterface(
            UserHotSearchDaoInterface userHotSearchDaoInterface) {
        this.userHotSearchDaoInterface = userHotSearchDaoInterface;
        baseDaoInterface = (BaseDaoInterface)this.userHotSearchDaoInterface;
    }
    public List<UserHotSearch> getUserHotSearch(){
       List<UserHotSearch> hotSearchList=new ArrayList<UserHotSearch>();
       List<UserHotSearch> hotUpdateSearchList=new ArrayList<UserHotSearch>();
      List<UserHotSearch> searchArrayList=new ArrayList<UserHotSearch>();
        //在2个人工干预和客户点击的前5名中选出前5个热词
        hotSearchList=userHotSearchDaoInterface.getUserHotSearch();
        hotUpdateSearchList=userHotSearchDaoInterface.getUpdateHotSearch();
        int num=0;
        int isMax=0;
        //对数组进行排序 （后去掉重复的）  利用先后的添加顺序
         for(int i=0;i<hotSearchList.size();i++){
            for(int j=0;j<hotUpdateSearchList.size();j++){
                //第一大的和第一大的比较
                if(j==0){
                j=j+num;
                }
                if(j>hotUpdateSearchList.size()){
                    break;
                }
                //用户搜索的词语多先加到数组
                if(hotSearchList.get(i).getSearchCount()-hotUpdateSearchList.get(j).getUpdateCount()>0){
                    searchArrayList.add(hotSearchList.get(i));
                    num=j;
                    isMax=1;
                    break;
                } else {
                   //这是当循环到最后一个值已经添加到数组不需要再添加
                    num=j;
                    isMax=0;
                    searchArrayList.add(hotUpdateSearchList.get(j));
                }
            }
            //如果循环玩了 还没最大的就把他放在后面
            if(isMax==0){
                searchArrayList.add(hotSearchList.get(i));
            }
        }
        for(int a=0;a<searchArrayList.size();a++){
            for(int j=a+1;j<searchArrayList.size();j++){
             if(searchArrayList.get(a).getContent().equalsIgnoreCase(searchArrayList.get(j).getContent())){
                 //因为数组已经排序好了重复就删除下面的
                 searchArrayList.remove(j);
             }
            }
        }
        //用户点击排序去重复
//        for(int i=0;i<hotSearchList.size();i++){
//            //去掉重复的ID(取值大的)
//            int ifExit=0;
//            for(int j=0;j<hotUpdateSearchList.size();j++){
//                if(hotSearchList.get(i).getContent().equalsIgnoreCase(hotUpdateSearchList.get(j).getContent())){
//                    ifExit=1;
//                    //id相同的
//                    if(hotSearchList.get(i).getSearchCount()-hotUpdateSearchList.get(j).getUpdateCount()>=0){
//                        searchArrayList.add(hotSearchList.get(i));
//                    }
//                }
//            }
//            if(ifExit==0){
//                //如果没有相同id就添加到数组
//              searchArrayList.add(hotSearchList.get(i));
//            }
//        }
//        //人工干预去掉重复
//        for(int i=0;i<hotUpdateSearchList.size();i++){
//            int ifExit=0;
//            //去掉重复的ID(取值大的)
//            for(int j=0;j<hotSearchList.size();j++){
//                if(hotSearchList.get(i).getContent().equalsIgnoreCase(hotUpdateSearchList.get(j).getContent())){
//                    //id相同的
//                    ifExit=1;
//                    if(hotSearchList.get(i).getSearchCount()-hotUpdateSearchList.get(j).getUpdateCount()<0){
//                        searchArrayList.add(hotUpdateSearchList.get(i));
//                    }
//                }
//            }
//            if(ifExit==0){
//                //如果没有相同id就添加到数组
//                searchArrayList.add(hotUpdateSearchList.get(i));
//            }
//        }
//        long temp;
//        UserHotSearch userHotSearch;
//        System.out.println(searchArrayList.size());
//        //排序
//        for(int a=0;a<searchArrayList.size();a++){
//            long searchCount=searchArrayList.get(a).getSearchCount();
//            long updateCount=searchArrayList.get(a).getUpdateCount();
//            for(int j=searchArrayList.size()-1;j>=a;j--){
//                if(searchCount-searchArrayList.get(j).getSearchCount()>0){
//                    userHotSearch=searchArrayList.get(a);
//                }
//            }
//
//        }
        return searchArrayList;
    }
}
