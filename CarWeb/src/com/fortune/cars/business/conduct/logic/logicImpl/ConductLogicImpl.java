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
import com.fortune.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    public void setItemValues(List<ConductItem> items,List<ConductValue> values){
        if(items==null||items.size()==0){
            return;
        }
        for(ConductItem item:items){
            List<ConductItem> subItems = item.getItems();
            if(subItems==null||subItems.size()<=0){
                for(int i=0,l=values.size();i<l;i++){
                    ConductValue value = values.get(i);
                    if(item.getId().equals(value.getItemId())){
                        item.setCurrentValue(value.getCurrentValue());
                        item.setUnit(value.getUnit());
                        item.setCreateTime(value.getCreateTime());
                        item.setExtraObj(value.getId());
                        item.setErrorRange(value.getErrorRange());
                        item.setStandValue(value.getCorrectValue());
                        item.setStatus(value.getStatus());
                        logger.debug("发现匹配数据："+item.getName()+",value="+item.getCurrentValue());
                        values.remove(i);
                        break;
                    }
                }
            }else{
                setItemValues(subItems,values);
            }
        }
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
                    for(int i=allItems.size()-1;i>=0;i--){
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
                items = SortUtils.sortArray(items,"id","asc");
                return items;
            }
            public Object init(Object key,String cacheName){
                List<ConductItem> allItems = conductItemLogicInterface.getAll();
                logger.debug("累计有"+allItems.size()+"个项目需要填写处理");
                List<ConductItem> roots = new ArrayList<ConductItem>();
                for(ConductItem item:allItems){
                    initItems(item,allItems);
                    if(item.getParentId()==-1){
                        logger.debug("发现数据："+item.getName()+",子节点个数："+item.getItems().size());
                        roots.add(item);
                    }
                }
                return roots;
            }
        });
        if(items!=null){
            if(id>0){
                ConductValue bean = new ConductValue();
                bean.setItemId(-1);
                bean.setConductId(id);
                List<ConductValue> values = conductValueLogicInterface.search(bean);
                if(values!=null&&values.size()>0){
                    for(ConductItem item:items){
                        setItemValues(item.getItems(),values);
                    }
                    if(values.size()>0){
                        logger.debug("还有为匹配的检测值，剩余数量"+values.size()+"个");
                    }
                }else{
                    logger.warn("未发现任何的数据集合：conductId="+id);
                }
            }
        }else{
            logger.debug("没有找到任何的测试项目！一定是出了问题！");
        }
        return items;
    }

    @Override
    public List<ConductItem> saveItems(Conduct obj) {
        if(obj==null){
            return null;
        }
        List<ConductItem> items = obj.getItems();
        ConductValue bean = new ConductValue();
        bean.setConductId(obj.getId());
        List<ConductValue> values = conductValueLogicInterface.search(bean);
        for(ConductItem item:items){
            ConductValue value = new ConductValue();
            value.setConductId(obj.getId());
            value.setItemId(item.getId());
            value.setCurrentValue(item.getCurrentValue());
            value.setUnit(item.getUnit());
            value.setCreateTime(item.getCreateTime());
            if(value.getCreateTime()==null){
                value.setCreateTime(new Date());
            }
            try {
                value.setId(StringUtils.string2int(item.getExtraObj().toString(),-1));
            } catch (Exception e) {
                logger.error("没有发现id，可能是新建....");
            }
            for(ConductValue v0:values){
                if(v0.getId().equals(value.getId())){
                    values.remove(v0);
                    break;
                }
            }
            value.setErrorRange(item.getErrorRange());
            value.setCorrectValue(item.getStandValue());
            value.setStatus(item.getStatus());
            value = conductValueLogicInterface.save(value);
            item.setExtraObj(value.getId());
        }
        for(ConductValue v0:values){
            conductValueLogicInterface.remove(v0);
        }
        return items;
    }
}
