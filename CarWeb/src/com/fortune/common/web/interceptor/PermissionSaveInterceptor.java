package com.fortune.common.web.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2012-1-12
 * Time: 14:18:55
 * 自动过滤一个权限要操作的所有功能的action和方法，形成一个权限，或者一个角色
 */
public class PermissionSaveInterceptor extends AbstractInterceptor {
    private Logger logger = Logger
            .getLogger(this.getClass());

    public void init() {

    }


    public void destroy() {

    }


    @SuppressWarnings("unchecked")
    public String intercept(ActionInvocation arg0) throws Exception {
        Map<String, Object> session = arg0.getInvocationContext().getSession();
        String context = arg0.getProxy().getAction().getClass().getName();
        String method = arg0.getProxy().getMethod();
        Map<String, Long> accessList = (Map<String, Long>) session.get("SessionAccessList");
        if (accessList == null) {
            accessList = new HashMap<String, Long>();
        }
        String key = context + ":" + method;
        Long count = accessList.get(key);
        if (count == null) {
            count = 1L;
        } else {
            count++;
        }
        logger.debug("key=" + key + ",count=" + count);
        accessList.put(key, count);
        session.put("SessionAccessList",accessList);
        return arg0.invoke();
    }
}
