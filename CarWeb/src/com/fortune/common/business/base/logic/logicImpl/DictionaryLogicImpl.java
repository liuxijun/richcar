package com.fortune.common.business.base.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.dao.ConfigDaoInterface;
import com.fortune.common.business.base.dao.daoInterface.DictionaryDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.base.logic.ConfigLogicInterface;
import com.fortune.common.business.base.logic.logicInterface.DictionaryLogicInterface;
import com.fortune.common.business.base.model.Config;
import com.fortune.common.business.base.model.Dictionary;
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

    public boolean codeExists(String code){//代码是否已经存在
        Dictionary item = get(code);
        return item != null;
    }
}