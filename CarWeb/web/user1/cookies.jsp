<%@page contentType="text/html; charset=GBK" %><%!

	/*
	 * 本文件集中了读、写、删除COOKIE的方法
	 */
	 
	// 定义ICP的域名.jspdemo.com.cn
	String hostName = ".kdsj2.sx.cn";
	
	/**
	 * <p>Desc: 读取的COOKIE的方法  </p>
	 * 
	 * @param request HttpServletRequest的请求句本.
	 * @param cookieName COOKIE对象的名称.
	 * 
	 * @return 返回对应于COOKIE对象的名称的内容.
	 */	
	String getCookieValue(HttpServletRequest request, String cookieName)
	{
        Cookie cookie,aryCookie[];
        aryCookie = request.getCookies();
        if ( aryCookie == null ) {
            return null;
        }

        for(int i = 0; i < aryCookie.length; i++){
            cookie = aryCookie[i];

            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            } 
        }
        
        return null;
	}
	   
	
	/**
	 * <p>Desc: 删除的COOKIE的方法  </p>
	 * 
	 * @param request HttpServletRequest的请求句本.
	 * @param response HttpServletResponse的请求句本.
	 * @param cookieName COOKIE对象的名称.
	 * 
	 * @return 返回对应于COOKIE对象的名称的内容..
	 */	
	void removeCookie(HttpServletRequest request, 
	                  HttpServletResponse response, 
	                  String cookieName) 
    {
		Cookie cookie, aryCookie[];
		aryCookie = request.getCookies();
		int i;
	
		for (i = 0; i < aryCookie.length; i++) {
			cookie = aryCookie[i];
	
			if (cookie.getName().equals(cookieName)) {
				cookie.setValue(null);
				response.addCookie(cookie);
	        } else {
				Cookie cook = new Cookie(cookieName, null);
				cook.setVersion(0);
				response.addCookie(cook);
			}
	    }	
	}
	
	/**
	 * <p>Desc: 写的COOKIE的方法  </p>
	 * 
	 * @param request HttpServletRequest的请求句本.
	 * @param response HttpServletResponse的请求句本.
	 * @param cookieName COOKIE对象的名称.
	 * @param cookieValue 对应于COOKIE对象的名称的内容.
	 * 
	 */	
	void addCookie(HttpServletResponse response,
                   String cookieName, 
                   String cookieValue)
    {
        Cookie cookie;
        cookie = new Cookie(cookieName, cookieValue);
        cookie.setDomain(hostName);
        cookie.setPath("/");
        cookie.setVersion(0);
        response.addCookie(cookie);
    }
	
	/**
	 * <p>Desc: 写的COOKIE的方法  </p>
	 * 
	 * @param request HttpServletRequest的请求句本.
	 * @param response HttpServletResponse的请求句本.
	 * @param cookieName COOKIE对象的名称.
	 * @param cookieValue 对应于COOKIE对象的名称的内容.
	 * 
	 */	
	void addCookie(HttpServletRequest request,
                   HttpServletResponse response,
                   String cookieName, 
                   String cookieValue)
    {
        Cookie cookie;
        cookie = new Cookie(cookieName, cookieValue);
        //cookie.setDomain(hostName);
        //cookie.setPath("/");
        //cookie.setVersion(0);
        response.addCookie(cookie);
    }
%>    