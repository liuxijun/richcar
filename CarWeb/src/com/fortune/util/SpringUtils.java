package com.fortune.util;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.struts2.ServletActionContext;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.servlet.ServletContext;

import com.fortune.common.Constants;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-4-13
 * Time: 10:19:33
 */
public class SpringUtils {
    private static Logger log = Logger.getLogger("com.fortune.util.SpringUtils");

    public SpringUtils() {

    }

    public static Object getBean(String beanName, ServletContext context) {
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        return ctx.getBean(beanName);
    }

    public static Object getBean(String beanName) throws Exception {
        ServletContext context;
        try {
            context = ServletActionContext.getServletContext();
        } catch (Exception e) {
            context = null;
        }

        if(context!=null){
            return getBean(beanName, ServletActionContext.getServletContext());
        }else{
            return getBeanForApp(beanName);
        }
    }

    public static Object getBeanForApp(String beanName) {
        if (Constants.SPRING_APPLICATION_CONTEXT == null) {
            log.info("启动，开始初始化，App形式实例化Bean:"+beanName);
            try {
                Constants.SPRING_APPLICATION_CONTEXT = new ClassPathXmlApplicationContext(
                        new String[]{"spring-config.xml"});
                //log.info("SPRING_APPLICATION_CONTEXT:" + Constants.SPRING_APPLICATION_CONTEXT);
            } catch (Exception e) {
                log.warn("初始化SPRING_APPLICATION_CONTEXT失败", e);
            }
        }
        if (Constants.SPRING_APPLICATION_CONTEXT != null) {
            return Constants.SPRING_APPLICATION_CONTEXT.getBean(beanName);
        }
        return null;
    }
}
