package com.fortune.common.web.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by xjliu on 2014/11/1.
 * 记录操作员访问日志
 */
public class OperatorVisitLog extends HttpServlet implements Filter {
    private Logger logger = Logger.getLogger(getClass());
    public void init(FilterConfig filterConfig) {
    }
    @SuppressWarnings("unchecked")
    private boolean saveVisitLog(String url,HttpSession session){
        logger.debug("in request:" + url);
        //如果session中visitLogMap为空，就不记录。这是为了在record.jsp中启动和关闭记录功能。
        Map<String,String> visitLog = (Map<String,String>)session.getAttribute("visitLogMap");
        if(visitLog!=null){
            if(url.contains("/common/record.jsp")){
               logger.debug("在访问记录工具，不记录该次访问信息："+url);
            }else{
                visitLog.put(url,url);
            }
        }else{
            logger.debug("记录行为还没有启动："+url);
        }
        return true;
    }
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws java.io.IOException, javax.servlet.ServletException {
        HttpServletRequest hsRequest = (HttpServletRequest) request;
        saveVisitLog(hsRequest.getServletPath(),hsRequest.getSession());
        filterChain.doFilter(request, response);
    }
}
