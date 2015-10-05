package com.fortune.cars.business.repair.logic.logicImpl;

import com.fortune.cars.business.repair.dao.daoInterface.RepairDaoInterface;
import com.fortune.cars.business.repair.logic.logicInterface.PartsLogicInterface;
import com.fortune.cars.business.repair.logic.logicInterface.RepairLogicInterface;
import com.fortune.cars.business.repair.model.Parts;
import com.fortune.cars.business.repair.model.Repair;
import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.util.BeanUtils;
import com.fortune.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by xjliu on 2015/8/9.
 *
 */
@Service("repairLogicInterface")
public class RepairLogicImpl extends BaseLogicImpl<Repair> implements RepairLogicInterface {
    private RepairDaoInterface repairDaoInterface;
    private PartsLogicInterface partsLogicInterface;
    @Autowired
    public void setRepairDaoInterface(RepairDaoInterface repairDaoInterface) {
        this.repairDaoInterface = repairDaoInterface;
        this.baseDaoInterface = (BaseDaoInterface) repairDaoInterface;
    }

    @Autowired
    public void setPartsLogicInterface(PartsLogicInterface partsLogicInterface) {
        this.partsLogicInterface = partsLogicInterface;
    }

    public Repair get(Serializable id){
        Repair repair = super.get(id);
        if(repair!=null){
            Date now =  new Date();
            BeanUtils.setDefaultValue(repair,"createTime",now);
            repair.setModifyTime(new Date());
            BeanUtils.setDefaultValue(repair, "inTime", now);
            BeanUtils.setDefaultValue(repair, "fileId", StringUtils.date2string(now, "yyyyMMddHHmmss"));
            Parts searchBean = new Parts();
            searchBean.setRepairId(repair.getId());
            repair.setParts(partsLogicInterface.search(searchBean));
        }
        return  repair;
    }

    public Repair save(Repair repair){
        BeanUtils.setDefaultValue(repair,"createTime",new Date());
        repair.setModifyTime(new Date());
        BeanUtils.setDefaultValue(repair, "inTime", new Date());
        List<Parts> parts = repair.getParts();
        repair = super.save(repair);
        if(repair!=null&&parts!=null){
            Parts searchBean = new Parts();
            searchBean.setRepairId(repair.getId());
            List<Parts> oldParts = partsLogicInterface.search(searchBean);
            for(int l=parts.size()-1;l>=0;l--){
                Parts p1 = parts.get(l);
                for(int i=oldParts.size()-1;i>=0;i--){
                    Parts p0 = oldParts.get(i);
                    if(p0.getId().equals(p1.getId())){
                        oldParts.remove(i);
                        break;
                    }
                }
            }
            for(int i=oldParts.size()-1;i>=0;i--){
                Parts p0 = oldParts.get(i);
                partsLogicInterface.remove(p0);
            }
            oldParts.clear();
            for(Parts p:parts){
                p.setRepairId(repair.getId());
                p = partsLogicInterface.save(p);
                oldParts.add(p);
            }
            repair.setParts(oldParts);
        }
        return  repair;
    }

    public void remove(Repair repair){
        Parts searchBean = new Parts();
        searchBean.setRepairId(repair.getId());
        List<Parts> oldParts = partsLogicInterface.search(searchBean);
        for(int l=oldParts.size()-1;l>=0;l--){
            Parts p1 = oldParts.get(l);
            partsLogicInterface.remove(p1);
        }
        super.remove(repair);
    }
}
