package com.fortune.cars.business.conduct.logic.logicImpl;

import com.fortune.cars.business.conduct.dao.daoInterface.ConductDaoInterface;
import com.fortune.cars.business.conduct.logic.logicInterface.ConductItemLogicInterface;
import com.fortune.cars.business.conduct.logic.logicInterface.ConductLogicInterface;
import com.fortune.cars.business.conduct.logic.logicInterface.ConductValueLogicInterface;
import com.fortune.cars.business.conduct.model.Conduct;
import com.fortune.cars.business.conduct.model.ConductItem;
import com.fortune.cars.business.conduct.model.ConductValue;
import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.util.CacheUtils;
import com.fortune.util.DataInitWorker;
import com.fortune.util.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xjliu on 2015/8/9.
 *
 */
@Service("conductLogicInterface")
public class ConductLogicImpl extends BaseLogicImpl<Conduct> implements ConductLogicInterface  {
    private ConductDaoInterface conductDaoInterface;
    private ConductItemLogicInterface conductItemLogicInterface;
    private ConductValueLogicInterface conductValueLogicInterface;
    @Autowired
    public void setConductDaoInterface(ConductDaoInterface conductDaoInterface) {
        this.conductDaoInterface = conductDaoInterface;
        this.baseDaoInterface = (BaseDaoInterface) conductDaoInterface;
    }

    @Autowired
    public void setConductItemLogicInterface(ConductItemLogicInterface conductItemLogicInterface) {
        this.conductItemLogicInterface = conductItemLogicInterface;
    }

    @Autowired
    public void setConductValueLogicInterface(ConductValueLogicInterface conductValueLogicInterface) {
        this.conductValueLogicInterface = conductValueLogicInterface;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ConductItem> getItems(Integer id, Integer carId) {
        List<ConductItem> items =(List<ConductItem>) CacheUtils.get("conductItems","allSystemCache",new DataInitWorker(){
            public List<ConductItem> initItems(ConductItem item,List<ConductItem> allItems){
                List<ConductItem> items = null;
                if(item!=null){
                    items = item.getItems();
                    if(items == null){
                        items = new ArrayList<ConductItem>();
                        item.setItems(items);
                    }
                    Integer itemId = item.getId();
                    for(int i=allItems.size()-1;i>=0;i++){
                        ConductItem child = allItems.get(i);
                        if(child!=null){
                            Integer parentId = child.getParentId();
                            if(parentId!=null&&parentId.equals(itemId)){
                                items.add(child);
//                                allItems.remove(i);
                            }
                        }
                    }
                }
                return items;
            }
            public Object init(Object key,String cacheName){
                List<ConductItem> allItems = conductItemLogicInterface.getAll();
                List<ConductItem> roots = new ArrayList<ConductItem>();
                for(ConductItem item:allItems){
                    initItems(item,allItems);
                    if(item.getParentId()==-1){
                        roots.add(item);
                    }
                }
                return roots;
            }
        });
        if(items!=null){
            if(id>0){
                ConductValue bean = new ConductValue();
                bean.setConductId(id);
                List<ConductValue> values = conductValueLogicInterface.search(bean);
                for(ConductItem item:items){
                    if(item.isLeaf()){
                        for(int i=values.size()-1;i>=0;i--){
                            ConductValue value = values.get(i);
                            if(value.getItemId()==item.getId()){
                                item.setCurrentValue(value.getCurrentValue());
                                item.setUnit(value.getUnit());
                                item.setCreateTime(value.getCreateTime());
                                item.setCreateTime(value.getCreateTime());
                                item.setExtraObj(value.getId());
                                item.setErrorRange(value.getErrorRange());
                                item.setStandValue(value.getCorrectValue());
                                item.setStatus(value.getStatus());
                                values.remove(i);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return items;
    }
}
