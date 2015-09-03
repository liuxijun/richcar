package com.fortune.common.business.base.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.dao.ConfigDaoInterface;
import com.fortune.common.business.base.dao.daoInterface.DictionaryDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.base.logic.ConfigLogicInterface;
import com.fortune.common.business.base.logic.logicInterface.DictionaryLogicInterface;
import com.fortune.common.business.base.model.Config;
import com.fortune.common.business.base.model.Dictionary;
import com.fortune.util.CacheUtils;
import com.fortune.util.DataInitWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xjliu on 2015/3/13.
 *
 */
@Service("dictionaryLogicInterface")
public class DictionaryLogicImpl extends BaseLogicImpl<Dictionary> implements DictionaryLogicInterface {
    private DictionaryDaoInterface dictionaryDaoInterface;

    @Autowired
    public void setDictionaryDaoInterface(DictionaryDaoInterface dictionaryDaoInterface) {
        this.dictionaryDaoInterface = dictionaryDaoInterface;
        this.baseDaoInterface = (BaseDaoInterface)dictionaryDaoInterface;
    }

    @Override
    public List<Dictionary> getItemsOfCode(String code,int repeatTimes) {
        if(repeatTimes>20){//防止死循环开关
            return null;
        }
        if(code!=null&&!code.trim().isEmpty()){
            Dictionary bean = new Dictionary();
            bean.setParentCode(code);
            List<Dictionary> items = search(bean);
            if(items!=null){
                for(Dictionary item:items){
                    item.setItems(getItemsOfCode(item.getCode(),repeatTimes+1));
                }
            }
            return items;
        }
        return null;
    }

    //parentCode对应的应该是car中的某一个field，所以判断一个field是否缓存，就这么办
    public boolean hasCode(final String code){
        String dictionaryKey = "code="+code+"_forCheckExists";
        return  CacheUtils.get(dictionaryKey,"directoryCacheCodeWithParent",new DataInitWorker(){
            public Object init(Object key,String cacheName){
                Dictionary item = new Dictionary();
//                item.setCode(code);
                item.setParentCode(code);
                List<Dictionary> result = search(item);
                if(result!=null&&result.size()>0){
                    return result.get(0).getName();
                }
                return null;
            }
        })==null;
    }
    public String getNameOfCode(final String code,final String parentCode){
        String dictionaryKey = "code="+code+"_of_parentCode="+parentCode;
        return (String) CacheUtils.get(dictionaryKey,"directoryCacheCodeWithParent",new DataInitWorker(){
           public Object init(Object key,String cacheName){
               Dictionary item = new Dictionary();
               item.setCode(code);
               item.setParentCode(parentCode);
               List<Dictionary> result = search(item);
               if(result!=null&&result.size()>0){
                   return result.get(0).getName();
               }
               return null;
           }
        });
    }
    public boolean codeExists(String code){//代码是否已经存在
        Dictionary item = get(code);
        return item != null;
    }
}