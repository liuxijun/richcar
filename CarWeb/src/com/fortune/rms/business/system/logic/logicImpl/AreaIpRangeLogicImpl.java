package com.fortune.rms.business.system.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.system.dao.daoInterface.AreaDaoInterface;
import com.fortune.rms.business.system.dao.daoInterface.AreaIpRangeDaoInterface;
import com.fortune.rms.business.system.dao.daoInterface.IpRangeDaoInterface;
import com.fortune.rms.business.system.logic.logicInterface.AreaIpRangeLogicInterface;
import com.fortune.rms.business.system.model.Area;
import com.fortune.rms.business.system.model.AreaIpRange;
import com.fortune.rms.business.system.model.IpRange;
import com.fortune.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service("areaIpRangeLogicInterface")
public class AreaIpRangeLogicImpl extends BaseLogicImpl<AreaIpRange>
        implements
        AreaIpRangeLogicInterface {
    private AreaIpRangeDaoInterface areaIpRangeDaoInterface;
    private AreaDaoInterface areaDaoInterface;
    private IpRangeDaoInterface ipRangeDaoInterface;


    /**
     * @param areaIpRangeDaoInterface the areaIpRangeDaoInterface to set
     */
    @Autowired
    public void setAreaIpRangeDaoInterface(
            AreaIpRangeDaoInterface areaIpRangeDaoInterface) {
        this.areaIpRangeDaoInterface = areaIpRangeDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.areaIpRangeDaoInterface;
    }
    @Autowired
    public void setAreaDaoInterface(AreaDaoInterface areaDaoInterface) {
        this.areaDaoInterface = areaDaoInterface;
    }
    @Autowired
    public void setIpRangeDaoInterface(IpRangeDaoInterface ipRangeDaoInterface) {
        this.ipRangeDaoInterface = ipRangeDaoInterface;
    }

    public List<AreaIpRange> getAreaIpRange(AreaIpRange areaIpRange, PageBean pageBean) {
        List<AreaIpRange> result;
        try {
            result = areaIpRangeDaoInterface.getObjects(areaIpRange, pageBean);
            List<AreaIpRange> objs = result;
            if (objs != null) {
                for (AreaIpRange air : objs) {
                    String areaName = "";
                    String ipRangeName = "";
                    if (air.getAreaId() != null) {
                        Long areaId = air.getAreaId();
                        Area area = areaDaoInterface.get(areaId);
                        if (area != null) {
                            areaName = area.getName();
                        }
                    }
                    if (air.getIpRangeId() != null) {
                        Long ipRangeId = air.getIpRangeId();
                        IpRange ipRange = ipRangeDaoInterface.get(ipRangeId);
                        if (ipRange != null) {
                            ipRangeName = ipRange.getName();
                        }
                    }
                      air.setAreaName(areaName);
                     air.setIpRangeName(ipRangeName);
                }
            }
        } catch (Exception e) {
            result = new ArrayList<AreaIpRange>();
        }
        return result;
    }
}
