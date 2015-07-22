package com.fortune.common;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-4-1
 * Time: 15:23:06
 * 乱码解决方法
 */
public class SetCharacterEncodingFilter implements Filter {
    protected FilterConfig filterConfig;
    protected String encodingName;
    protected boolean enable;

    public SetCharacterEncodingFilter() {
      this.encodingName = "UTF-8";
      this.enable = false;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
      this.filterConfig = filterConfig;
      loadConfigParams();
    }

    private void loadConfigParams() {
      //encoding
      this.encodingName = this.filterConfig.getInitParameter("encoding");
      //filter enable flag...
      String strIgnoreFlag = this.filterConfig.getInitParameter("enable");
      if (strIgnoreFlag.equalsIgnoreCase("true")) {
        this.enable = true;
      } else {
        this.enable = false;
      }
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        System.out.println("requestUrl:"+((HttpServletRequest)request).getServletPath());
         if(this.enable) {
           request.setCharacterEncoding(this.encodingName);
         }
         chain.doFilter(request, response);
       }

    public void destroy() {
    }

}
