<%@page contentType="text/html; charset=GBK" %><%!

	/*
	 * ���ļ������˶���д��ɾ��COOKIE�ķ���
	 */
	 
	// ����ICP������.jspdemo.com.cn
	String hostName = ".kdsj2.sx.cn";
	
	/**
	 * <p>Desc: ��ȡ��COOKIE�ķ���  </p>
	 * 
	 * @param request HttpServletRequest������䱾.
	 * @param cookieName COOKIE���������.
	 * 
	 * @return ���ض�Ӧ��COOKIE��������Ƶ�����.
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
	 * <p>Desc: ɾ����COOKIE�ķ���  </p>
	 * 
	 * @param request HttpServletRequest������䱾.
	 * @param response HttpServletResponse������䱾.
	 * @param cookieName COOKIE���������.
	 * 
	 * @return ���ض�Ӧ��COOKIE��������Ƶ�����..
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
	 * <p>Desc: д��COOKIE�ķ���  </p>
	 * 
	 * @param request HttpServletRequest������䱾.
	 * @param response HttpServletResponse������䱾.
	 * @param cookieName COOKIE���������.
	 * @param cookieValue ��Ӧ��COOKIE��������Ƶ�����.
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
	 * <p>Desc: д��COOKIE�ķ���  </p>
	 * 
	 * @param request HttpServletRequest������䱾.
	 * @param response HttpServletResponse������䱾.
	 * @param cookieName COOKIE���������.
	 * @param cookieValue ��Ӧ��COOKIE��������Ƶ�����.
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