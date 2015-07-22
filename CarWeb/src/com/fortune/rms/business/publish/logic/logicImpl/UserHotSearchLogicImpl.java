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
        //��2���˹���Ԥ�Ϳͻ������ǰ5����ѡ��ǰ5���ȴ�
        hotSearchList=userHotSearchDaoInterface.getUserHotSearch();
        hotUpdateSearchList=userHotSearchDaoInterface.getUpdateHotSearch();
        int num=0;
        int isMax=0;
        //������������� ����ȥ���ظ��ģ�  �����Ⱥ�����˳��
         for(int i=0;i<hotSearchList.size();i++){
            for(int j=0;j<hotUpdateSearchList.size();j++){
                //��һ��ĺ͵�һ��ıȽ�
                if(j==0){
                j=j+num;
                }
                if(j>hotUpdateSearchList.size()){
                    break;
                }
                //�û������Ĵ�����ȼӵ�����
                if(hotSearchList.get(i).getSearchCount()-hotUpdateSearchList.get(j).getUpdateCount()>0){
                    searchArrayList.add(hotSearchList.get(i));
                    num=j;
                    isMax=1;
                    break;
                } else {
                   //���ǵ�ѭ�������һ��ֵ�Ѿ���ӵ����鲻��Ҫ�����
                    num=j;
                    isMax=0;
                    searchArrayList.add(hotUpdateSearchList.get(j));
                }
            }
            //���ѭ������ ��û���ľͰ������ں���
            if(isMax==0){
                searchArrayList.add(hotSearchList.get(i));
            }
        }
        for(int a=0;a<searchArrayList.size();a++){
            for(int j=a+1;j<searchArrayList.size();j++){
             if(searchArrayList.get(a).getContent().equalsIgnoreCase(searchArrayList.get(j).getContent())){
                 //��Ϊ�����Ѿ���������ظ���ɾ�������
                 searchArrayList.remove(j);
             }
            }
        }
        //�û��������ȥ�ظ�
//        for(int i=0;i<hotSearchList.size();i++){
//            //ȥ���ظ���ID(ȡֵ���)
//            int ifExit=0;
//            for(int j=0;j<hotUpdateSearchList.size();j++){
//                if(hotSearchList.get(i).getContent().equalsIgnoreCase(hotUpdateSearchList.get(j).getContent())){
//                    ifExit=1;
//                    //id��ͬ��
//                    if(hotSearchList.get(i).getSearchCount()-hotUpdateSearchList.get(j).getUpdateCount()>=0){
//                        searchArrayList.add(hotSearchList.get(i));
//                    }
//                }
//            }
//            if(ifExit==0){
//                //���û����ͬid����ӵ�����
//              searchArrayList.add(hotSearchList.get(i));
//            }
//        }
//        //�˹���Ԥȥ���ظ�
//        for(int i=0;i<hotUpdateSearchList.size();i++){
//            int ifExit=0;
//            //ȥ���ظ���ID(ȡֵ���)
//            for(int j=0;j<hotSearchList.size();j++){
//                if(hotSearchList.get(i).getContent().equalsIgnoreCase(hotUpdateSearchList.get(j).getContent())){
//                    //id��ͬ��
//                    ifExit=1;
//                    if(hotSearchList.get(i).getSearchCount()-hotUpdateSearchList.get(j).getUpdateCount()<0){
//                        searchArrayList.add(hotUpdateSearchList.get(i));
//                    }
//                }
//            }
//            if(ifExit==0){
//                //���û����ͬid����ӵ�����
//                searchArrayList.add(hotUpdateSearchList.get(i));
//            }
//        }
//        long temp;
//        UserHotSearch userHotSearch;
//        System.out.println(searchArrayList.size());
//        //����
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
