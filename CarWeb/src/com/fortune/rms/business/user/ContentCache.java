package com.fortune.rms.business.user;

import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.rms.business.product.model.ServiceProduct;
import com.fortune.rms.business.system.model.Device;
import com.fortune.util.cache.Cacheable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-7-8
 * Time: 19:42:46
 *
 */
public class ContentCache  implements Cacheable, Serializable {
    public int getSize(){
        return 1;
    }

    public Content content;
    public Device device;
    public HashMap<Long, List<ServiceProduct>> servicePropertyMap;
    public HashMap<Long, ContentProperty> contentPropertyMap;

}
