package com.fortune.rms.business.system.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.system.dao.daoInterface.PhoneRangeDaoInterface;
import com.fortune.rms.business.system.model.Area;
import com.fortune.rms.business.system.model.PhoneRange;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PhoneRangeDaoAccess extends BaseDaoAccess<PhoneRange, Long>
        implements
        PhoneRangeDaoInterface {

    public PhoneRangeDaoAccess() {
        super(PhoneRange.class);
    }

    @SuppressWarnings("unchecked")
    public Area getAreaOfPhone(Long phone) {
        List<Area> areas = this.getHibernateTemplate().find("from Area a where a.id in (select pr.areaId from PhoneRange pr where pr.phoneFrom<=" +
                phone+" and pr.phoneTo>="+phone+")");
        if(areas!=null&&areas.size()>0){
            return areas.get(0);
        }
        return null;
    }
    public PhoneRange getPhoneRangeOfPhone(Long phone) {
        List<PhoneRange> results = this.getHibernateTemplate().find("from PhoneRange pr where pr.phoneFrom<=" +
                phone+" and pr.phoneTo>="+phone);
        if(results!=null&&results.size()>0){
            return results.get(0);
        }
        return null;
    }
}
