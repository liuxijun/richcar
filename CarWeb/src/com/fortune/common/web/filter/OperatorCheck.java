package com.fortune.common.web.filter;

import com.fortune.common.Constants;
import com.fortune.common.business.security.logic.logicImpl.MenuChecker;
import com.fortune.common.business.security.model.Menu;
import com.fortune.util.AppConfigurator;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by xjliu on 2014/11/1.
 * 检查操作员是否可以进行操作
 */
public class OperatorCheck    extends HttpServlet implements Filter {
    private Logger logger = Logger.getLogger(getClass());
    private MenuChecker menuChecker;
    private String notLoginUrl;
    private String noAuthAccessUrl;
    private boolean compactMode;
    public void init(FilterConfig filterConfig) {
        AppConfigurator config = AppConfigurator.getInstance();
        compactMode = config.getBoolConfig("system.compactMode",false);
        notLoginUrl = config.getConfig("system.operatorCheck.notLoginErrorUrl","/login.jsp");
        noAuthAccessUrl = config.getConfig("system.operatorCheck.noAuthAccessUrl","/man.jsp");
        if(compactMode){
            menuChecker = MenuChecker.getInstance();
        }else{
            menuChecker = null;
        }
    }
    @SuppressWarnings("unchecked")
    private int hasPrivilegesToVisit(String url,HttpSession session){
        if(menuChecker.needCheckUrl(url)){
            if(url.contains(notLoginUrl)){
                //登录连接，不禁止
                return 0;
            }
            Map<String,List<Menu>> adminCanVisitUrls = (Map<String,List<Menu>>) session.getAttribute(Constants.SESSION_ADMIN_CAN_VISIT_URLS);
            if(adminCanVisitUrls==null){
                logger.warn("没有session信息！用户可能没有登录！");
                return -1;
            }
            if(url.contains(noAuthAccessUrl)){
                //如果是登录后默认首页或者是越权访问提示页，也通过
                return 0;
            }
            if(adminCanVisitUrls.containsKey(url)){
                logger.debug("用户有权访问："+url);
                return 0;
            }else{
                logger.error("用户无权访问："+url);
                return 1;
            }
        }else{
            logger.debug("无需检查是否登录："+url);
        }
        return 0;
    }
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws java.io.IOException, javax.servlet.ServletException {
        if(compactMode){
            HttpServletRequest hsRequest = (HttpServletRequest) request;
            String url = hsRequest.getServletPath();
            //logger.debug("in request:" + url);
            HttpSession session = hsRequest.getSession();
            int checkResult = hasPrivilegesToVisit(url,session);
            if(checkResult!=0){
                HttpServletResponse hr = (HttpServletResponse)response;
                String redirectUrl= notLoginUrl;
                if(checkResult==1){
                    //已经登陆但是越权
                    redirectUrl= noAuthAccessUrl;
                }
                hr.sendRedirect(redirectUrl);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}