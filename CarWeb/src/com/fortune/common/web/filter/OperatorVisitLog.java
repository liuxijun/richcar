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
 * ��¼����Ա������־
 */
public class OperatorVisitLog extends HttpServlet implements Filter {
    private Logger logger = Logger.getLogger(getClass());
    public void init(FilterConfig filterConfig) {
    }
    @SuppressWarnings("unchecked")
    private boolean saveVisitLog(String url,HttpSession session){
        logger.debug("in request:" + url);
        //���session��visitLogMapΪ�գ��Ͳ���¼������Ϊ����record.jsp�������͹رռ�¼���ܡ�
        Map<String,String> visitLog = (Map<String,String>)session.getAttribute("visitLogMap");
        if(visitLog!=null){
            if(url.contains("/common/record.jsp")){
               logger.debug("�ڷ��ʼ�¼���ߣ�����¼�ôη�����Ϣ��"+url);
            }else{
                visitLog.put(url,url);
            }
        }else{
            logger.debug("��¼��Ϊ��û��������"+url);
        }
        return true;
    }
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws java.io.IOException, javax.servlet.ServletException {
        HttpServletRequest hsRequest = (HttpServletRequest) request;
        saveVisitLog(hsRequest.getServletPath(),hsRequest.getSession());
        filterChain.doFilter(request, response);
    }
}
