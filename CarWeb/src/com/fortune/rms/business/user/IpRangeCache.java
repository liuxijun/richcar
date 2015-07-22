package com.fortune.rms.business.user;

import com.fortune.rms.business.system.model.IpRange;
import com.fortune.util.cache.Cacheable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-7-11
 * Time: 16:56:19
 * To change this template use File | Settings | File Templates.
 */
public class IpRangeCache  implements Cacheable, Serializable {
    public int getSize(){
        return 1;
    }
    public List ipRanges;
}
