package com.fortune.rms.business.user.logic.logicInterface;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-5-9
 * Time: 18:58:42
 * To change this template use File | Settings | File Templates.
 */
public interface SsoTokenLogicInterface {
    public String userBindSso(String userId, String url, String key);
    public String userLogicSso(String userId, String url, String key);
}
