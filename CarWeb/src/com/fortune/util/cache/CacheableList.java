package com.fortune.util.cache;

/**
 * <p>Title: mediastack3.0</p>
 * <p>Description: 流媒体管理系统3.0</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: 上海复旦光华</p>
 * @author 周黎
 * @version 3.0
 */

import java.util.List;

public class CacheableList  implements Cacheable {

    /**
     * Wrapped long object.
     */
    private List list;

    /**
     * Creates a new CacheableLong.
     *
     * @param string the Long object to wrap.
     */
    public CacheableList(List list) {
        this.list = list;
    }

    /**
     * Returns the long [] wrapped by the CacheableLongArray object.
     *
     * @return the long [] object.
     */
    public List getList() {
        return list;
    }

    //FROM THE CACHEABLE INTERFACE//

    public int getSize() {
        return list.size();
    }
}