package com.fortune.midware;

import com.fortune.util.cache.Cacheable;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-7-21
 * Time: 18:56:17
 * To change this template use File | Settings | File Templates.
 */
public class AuthCache   implements Cacheable {
    public int getSize(){
        return 1;
    }

    public long key;
    public String url;

}
