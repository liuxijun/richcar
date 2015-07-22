package com.fortune.rms.business.system.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.system.dao.daoInterface.PhoneRangeDaoInterface;
import com.fortune.rms.business.system.logic.logicInterface.PhoneRangeLogicInterface;
import com.fortune.rms.business.system.model.Area;
import com.fortune.rms.business.system.model.PhoneRange;
import com.fortune.util.CacheUtils;
import com.fortune.util.DataInitWorker;
import com.fortune.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("phoneRangeLogicInterface")
public class PhoneRangeLogicImpl extends BaseLogicImpl<PhoneRange>
        implements
        PhoneRangeLogicInterface {
    private PhoneRangeDaoInterface phoneRangeDaoInterface;
    /**
     * @param phoneRangeDaoInterface the ipRangeDaoInterface to set
     */
    @Autowired
    public void setPhoneRangeDaoInterface(PhoneRangeDaoInterface phoneRangeDaoInterface) {
        this.phoneRangeDaoInterface = phoneRangeDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.phoneRangeDaoInterface;
    }

    public Area getAreaOfPhone(Long phone){
        if(phone==null)return null;
        return phoneRangeDaoInterface.getAreaOfPhone(phone);
    }

    /**
     * 折半查找法找到一个号码对应的区域
     * @param phone 手机号码
     * @return 对应的数据PhoneRange
     */
    @SuppressWarnings("unchecked")
    public PhoneRange getPhoneRangeOfPhone(Long phone){
        if(phone==null)return null;
        List<PhoneRange> ranges = (List<PhoneRange>) CacheUtils.get("phoneRangeCache","orderByFromAsc",new DataInitWorker(){
            public Object init(Object key,String cacheName){
                PhoneRange bean = new PhoneRange();
                return search(bean, new PageBean(0, Integer.MAX_VALUE, "o1.phoneFrom", "asc"));
            }
        });
        if (phone <= 0) {
            logger.error("无法计算客户手机号码到长整数，客户Phone：" + phone);
            return null;
        }
        int max = ranges.size() - 1;
        int min = 0;
        int i = max / 2;
        int j;
        if(max<0){
            return null;
        }
        while (true) {
            PhoneRange range = ranges.get(i);
            if (phone >= range.getPhoneFrom() && phone <= range.getPhoneTo()) {
                return range ;
            }

            if (max - min == 1 && phone > ranges.get(min).getPhoneTo() && phone < ranges.get(max).getPhoneFrom()) {
                break;
            }

            j = i;
            if (phone < range.getPhoneFrom()) {
                max = i;
                i = min + (max - min) / 2;
                if (i == j) {
                    i--;
                }
            } else if (phone > range.getPhoneTo()) {
                min = i;
                i = min + (max - min) / 2;
                if (i == j) {
                    i++;
                }
            }

            if (i < 0 || i > max) {
                break;
            }
        }

        return null;
    }

    public Long getAreaIdOfPhone(Long phone) {
        PhoneRange result = getPhoneRangeOfPhone(phone);
        if(result==null)return -1L;
        return result.getAreaId();
    }

}