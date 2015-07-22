package com.fortune.rms.business.publish.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.publish.dao.daoInterface.SearchDaoInterface;
import com.fortune.rms.business.publish.dao.daoInterface.UserHotSearchDaoInterface;
import com.fortune.rms.business.publish.logic.logicInterface.SearchLogicInterface;
import com.fortune.rms.business.publish.model.UserHotSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("SearchLogicInterface")
public class SearchLogicImpl extends BaseLogicImpl<UserHotSearch>
            implements
        SearchLogicInterface {
    private SearchDaoInterface searchDaoInterface;


    /**
     * @param searchDaoInterface the userHotSearchDaoInterface to set
     */
    @Autowired
    public void setUserHotSearchDaoInterface(
            SearchDaoInterface searchDaoInterface) {
        this.searchDaoInterface = searchDaoInterface;
        baseDaoInterface = (BaseDaoInterface)this.searchDaoInterface;
    }
    public boolean addSearchLog(String searchValue,long userIel){
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
        Date nowTime=now;
//        String nowTime = dateFormat.format(now);
        try {
            nowTime= dateFormat.parse(dateFormat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            searchValue = java.net.URLDecoder.decode(searchValue,"UTF-8");
            if(searchValue.length()>30){
                searchValue=searchValue.substring(0,29);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        boolean search;
        search=searchDaoInterface.addSearchValue(searchValue,userIel,nowTime);
        return search;
    }

}
