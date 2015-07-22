package com.fortune.common.web.interceptor;

import java.util.Iterator;
import java.util.Map;

import com.fortune.common.business.security.model.Admin;
import com.fortune.common.business.security.model.Permission;
import com.fortune.util.AppConfigurator;
import org.apache.log4j.Logger;

import com.fortune.common.Constants;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ValidationPermissionInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 7641775175862663858L;
	private static final Logger log = Logger
			.getLogger(ValidationPermissionInterceptor.class);

	@SuppressWarnings( { "unchecked", "unchecked" })
	public String intercept(ActionInvocation arg0) throws Exception {

		log.debug("in interceptor");
		Map<String, Object> session = arg0.getInvocationContext().getSession();

		Admin admin = (Admin) session.get(Constants.SESSION_ADMIN);

		if (admin == null){
            if(AppConfigurator.getInstance().getBoolConfig("needCheckLogin",false)){
                return Action.LOGIN;
            }

        }
//        return arg0.invoke();

///*
        if(AppConfigurator.getInstance().getBoolConfig("needCheckPermission",false)){
            Map<String, Permission> myop = (Map<String, Permission>) session
                    .get(Constants.SESSION_ADMIN_PERMISSION);
            String context = arg0.getProxy().getAction().getClass().getName();
            String method = arg0.getProxy().getMethod();
            if("".equals(context)){

            }
            log.debug("visit class = " + context + "|" + method);
            // 存在的情况下进行后续操作。
            if (method!=null&&"com.fortune.common.web.config.Dictionary".equals(context)){
                //访问树，直接放行
                return arg0.invoke();
            }
            Iterator it = myop.entrySet().iterator();
            log.debug(myop.size());
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Permission value = (Permission)entry.getValue();
                if (value != null) {
                    //log.debug("value.getClassname()="+value.getClassname());
                    //log.debug("value.getMethodName()="+value.getMethodName());
                    if (value.getClassname().equals(context)
                            && value.getMethodName().indexOf(method) >= 0)
                        return arg0.invoke();
                }
            }
            String errorMsg = "没有操作权限";
            if(admin !=null){
                errorMsg = admin.getLogin()+":"+ admin.getRealname()+errorMsg;
            }
            log.error(errorMsg+""+ context + "|" + method);
            return Constants.ACTION_NO_PERMISSION;
        }else{
            return arg0.invoke();
        }
//*/
	}

}
