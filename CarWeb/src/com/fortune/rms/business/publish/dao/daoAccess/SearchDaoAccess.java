package com.fortune.rms.business.publish.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.publish.dao.daoInterface.SearchDaoInterface;
import com.fortune.rms.business.publish.model.SearchLog;
import com.fortune.rms.business.publish.model.UserHotSearch;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.List;

@Repository
public class SearchDaoAccess
        extends
        BaseDaoAccess<UserHotSearch,Long>
        implements
        SearchDaoInterface {

    public SearchDaoAccess() {
        super(UserHotSearch.class);
    }
     //Ìí¼ÓËÑË÷µÄ´ÊÓï
    public boolean addSearchValue(String searchValue,long userIel,java.util.Date nowTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowdate = sdf.format(nowTime);
        Session session = getSession();
        try {
            String sql="insert into search_Log(id,search_value,search_time,user_tel) values(FORTUNE_GLOBAL_SEQ.nextval,'"+searchValue+"',to_date('"+nowdate+"','yy-mm-dd hh24:mi:ss'),"+userIel+")";
            Query query = session.createSQLQuery(sql);
            query.executeUpdate();
            return true;
        } catch (HibernateException e) {
            e.printStackTrace();
            return  false;
        } finally {
            session.close();
        }

    }

}
