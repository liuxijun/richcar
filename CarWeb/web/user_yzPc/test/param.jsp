<%

    String BCG_CERT = "MIIDEjCCAfqgAwIBAgIRAPDOFyjy+R4i6Q23izCj30QwDQYJKoZIhvcNAQEFBQAwLzELMAkGA1UEBhMCQ04xDjAMBgNVBAoMBUNISU5BMRAwDgYDVQQDDAdDQ0lUIENBMB4XDTA3MDIyODE2MDAwMFoXDTEwMDIyODE2MDAwMFowODELMAkGA1UEBhMCQ04xEDAOBgNVBAoMB0NDSVQgQ0ExFzAVBgNVBAMMDkNDSVQgU1lTVEVNIENBMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtj3HoqwPBAO/U/ERJg8HruPO0LczJNmWxTks7cVuH+DjF1gQk5/N9znnqFY5JZAx8w7JIQS35KFaDrIEKTNTvr4jKxoD62LlenROPJfry8J4UJmtMC7UoZCQ5yBik9heCdOtQFXyXrKjgb6GI2h368doGSmaAmCvBRAC6rV28EtlXMiNzw9mj3/JcNc6tYpB2JQZYr/EVMXdH1V/mobI7rt9wHK4DP8HxiD991h3EM9AAE6xuW09zK+L3HFDOhuK5B/3rCcfX4W6xCsEjl+aiVkAbEufDQ99AKlb/Vci6QYk64bp2CEaX/6zmGyw+WIYnBtRMsCN5sp7krbCzVGgIQIDAQABoyAwHjALBgNVHQ8EBAMCAQYwDwYDVR0TBAgwBgEB/wIBAzANBgkqhkiG9w0BAQUFAAOCAQEArCx6yJf7/yJv3xqC64PsvZpVePSGuZCxE+FzFdsSnlHV7OFPLKK5ARPZwN1ZdglEfnyI8IUnQjYQ8Y096yhVek1f4OGQ+1CVde1yhWv/mJFE+CRG7HHQxiZaqZkgFWqTlGwf9Ko/+OKG4AGduwK5We8t1vsltUCF6fY0kKrql4TL2e3xH+SqIbky0mVEpvDQbMWmj9D62Wg4zHmi1LstXEr00v6forVt/9W1+Dnvs3l9Ly4C75NUJchvIhdeL/aDO7whEWafDPlJ7s1Cmjb5ysq+gSUCOt7QGTCcPi4h14tyV6mtRKw97ayfP3L2kBmWsXK53R5yBYRPtCUAPpwuVA==";

    String SP_PRIVATE_KEY = "MIICWwIBAAKBgQDW51I7oNw7I7cA1ui6aPGWlabIfPXsUThT/Wtuoq4Da5orZuje4fDZdpJt8pJY9bywWJ4w+P1NilI9mPP4RZlgK2QKWgE4/q/r9HQyIc7S9b3QDkuRWic1Tnr0bDnNprDc29l7HIGihrD+bJV5ICpSnyZ0SdZncNU5qmSTYl8+SwIDAQABAoGANdYpcaqwwVgHJnywPNLBgvnmmF9FtHf8Zn4cBdFXSqAQNUNIn1122uOrd0PHieW16W+p4ZbezigSxj+lDtNQwqMGAZzNf/W2a4knLofFtGejtIGE7QZSDw3rUu0b2AWgLtSPZX5gNDXQiDveJT/wkIKq7GuVdPwwoeIZet7+KgECQQD8aA+bp67Smrsgm6RRKUuebR8ETvr4+LLNxZoEczo646RPt1YJFQoOPQbcBp86f1t3BmQxftFvicLUfREAZrCBAkEA2faTEjCM5ckETTU1247KtiXfNJA9w0YQfV2dolRjj1mEMPmqXYZwZar6JZYpiwPeIH5hov5rGYAij79T2M1IywJAV0eMjiCPvVev+XiYe1AyS9gj75N4CXvGKI150qwdqJtrB/23jHBwB7hmlNLdYJcFg8T8BHl75nvWmS+eH2tjAQJAF7JD+QVIPC8nhZFUYqNajIT/iEJqRJGYd1i3K4/LJymPQpfSGsrj61m0SmPR7mgkPRogWU0ZrbMNjt05K2feEwJAf8d1IJk7H/qV6DAHgCWeufPRcYV6T/c2XmHVhpeMrdTofY12E+ylh0y+SwyXODf4ZnQ3fau/0I88d3Pno2+Kmw==";

    //String SP_PRIVATE_KEY = "MIICXAIBAAKBgQDSWd/5z4bvumK/C9DTXsgxYFBZwsvWB9bhjcxy601leh4zdCVMIuvMIaamAZl+r/NTcMVXp5YrohF1GvFhZ0mpRy2j8VT+mImObao2Z5fbBP34lCI7yYqAxHYoKqkPm4uDflDmkOgD2jcc29yWerx+hBkiJdPZtPHFgxpofjMTHQIDAQABAoGAQS6AbRSVp6uF9dVffGAegM5rl6T5LPV47KPX+rRXY16YBTG0bQdVMggrb72HJu624+BKBoNz0UmDR1p1+czb0fx+9v5PDMNIfmuCOszMUDtFde5Vg0VJaMgkH7Rz2CBdHrMp4bcjWU02kz8z76rKv8Z40J9awmmUagkhskbHNLECQQD5d2IxpijgN2umFjirV5bDmugrq0p0nfKilYWNGDUid3jv8XPwiuYhe7Q8nfyELJ37N28i1afux4I1Ewgg2bGvAkEA19w7bgWGRgufU1gCL1Qe2K++OGsFxLROlEblXnD6fT9qz50N3hGTgPcasgMql/zPUyxWYbIvGOzASP1maNO28wJADVCr8w51XBJZMpI5EBwqteIMxlfsL3FGxBrK+A70txQ9ZkEy+QHQ6rSZmCqukbd37zK0Qd25iusnLgIICwjTjQJACVxZj2nZ6suyOxED9z10oPzuCNBLTb5r8e4pIDFSC4Bmh15OLaUhemQ82h9dB35FF1xC2rW5aSFLLV8fw8f6lQJBAIs76/EbZ6E4mCSjkRY5X5kZqVjst6fWxxm1xYE0+cDJZCkpXNlILyGsEuvPEp1WvKlFcnCsaDt/xnB3vtJoAuo=";
    //String SP_PRIVATE_KEY="MIIEowIBAAKCAQEAtj3HoqwPBAO/U/ERJg8HruPO0LczJNmWxTks7cVuH+DjF1gQk5/N9znnqFY5JZAx8w7JIQS35KFaDrIEKTNTvr4jKxoD62LlenROPJfry8J4UJmtMC7UoZCQ5yBik9heCdOtQFXyXrKjgb6GI2h368doGSmaAmCvBRAC6rV28EtlXMiNzw9mj3/JcNc6tYpB2JQZYr/EVMXdH1V/mobI7rt9wHK4DP8HxiD991h3EM9AAE6xuW09zK+L3HFDOhuK5B/3rCcfX4W6xCsEjl+aiVkAbEufDQ99AKlb/Vci6QYk64bp2CEaX/6zmGyw+WIYnBtRMsCN5sp7krbCzVGgIQIDAQABAoIBAQCq+rWFcqY33wgvyjmLmohb1lHgUjFCvn6NFUKI5jWW5RhKe4454rGL5SDqN9Mv4eRrJREg0FtzvspOhUgQG57Tk3JNawAIZOXmCJ6qjOJmblMRU81AKn/GAWth3Wl/Sn2X/KhMdxaJMFLlVIpnSAlTBegty3E6D9nFByulZ5FqicwBMeWIkBCypyqLjkWM+2PiTybJ7Jf0wTGcnTKaXtL/U2g9L6KaS6KF5XZJZh0CHghm/IDHSBRHi5d9X8oFHhMO4Ml9ybel3XY8KpfSriPS7eDrtEuXhTxEZQuZFfkD2xADwco/LJzbd8jHNR0nrQO09/3W3jfQbiFIh/1GK+mtAoGBAOfsa2YBA7YjsoigUUqad3QeNjLxTkxKsJtW4SLG1AkSft3XHOnodWebf0248zlQMhiFwPmQ5RErEwX37evt6BKYOKAg/LeuLNAKEkfcNAQmUDrmxAgCfZbCFPgg4gOTyF6L9CCbzjtBcCaw+SDh5HkqXTMDs7vkV0Ef5lqzvI/DAoGBAMkpArNondjalZHoZ9s3bVBpoN+ff4I3t+S+FHbXmP8tizQv7oOpmOoqgbqS67pynXOqi+NvOFmIJvq9qqalS4WIS64qp7+/e115Wu6tbI51lGPwjRFizqui/CCe0aa0g5Fgi1ZsciF/VqV0yREYDVUGu+3AJr5WrXXAGHK7mFZLAoGAOnm1qoZe9TGS9jfVx0WjELf9WmLVJw898Cy7nxUaqR7stepi8+cUkwb11hbn3G+H8f7nZQVPfECsDzkv1+ioNugCfv1SOs3DpsCjU1MGGnW+jNtaWdqm57gPqXBBDD3aq1wGNaKTIQWizV6ZsdzCynoP/ajVTG6KQXVVk1D2HhkCgYAZo4l9lmrv9cQ0iZubHDBpnFztw6V70mA03mk7UHVmqMZiS22qCFa+GdbZEdrLs8oPsQANZ+KVvr3BHaV/gffjOCD1POiOjvhJTKtjWmVIrwHPzxwbyC0xQR/DhJqSFdXjFJwN0tqlHFbN1dm+vQMOAEK5pdEKV3/aTzaOgQyEiwKBgFS/zRwesgkZBxt6oei3wwd8oMKkbJrZojxP8kNcpZB2oDegyMpDtfM6e4HbfhpOyY6PQXHq4TF09IbLujWdDab+ODmVJPiRQwGLVEZXdBdGGImWamPQSNIcmT/TdSYE2E25MIZR2jvoSFeep91XLLwBBn4QzFBBUB+3NBi+mpM4";

    String spid = "05@000openhe";

    String ccitCheckLogin = "http://221.192.155.68/cnb/webadmin/spcharge/authorizeUser.jsp";
    String ccitCheckLoginBack = "http://61.55.144.213/user/test/result.jsp?flag=checklogin";

    String ccitLogin = "http://221.192.155.68/cnb/webadmin/spcharge/AuthorizeRequest0.jsp";
    //String ccitLoginBack = "http://61.55.144.213/user/test/result.jsp?flag=login";
    String ccitLoginBack = "http://61.55.144.213/user/test/ssoToken.jsp?return_url="+java.net.URLEncoder.encode(ccitCheckLoginBack);


    String ccitOnePay = "http://221.192.155.68/cnb/webadmin/spcharge/PayByItem.jsp";
    String ccitPeriodPay = "http://221.192.155.68/cnb/webadmin/spcharge/ServiceOrderByPeriodRequest.jsp";

    String bsspExtendServiceAddress = "http://221.192.155.68/bssp/services/BSSPExtendService";
    String bsspServiceAddress = "http://221.192.155.68/bssp/services/BSSPService";
%>

<%!
    public static String getParameter(HttpServletRequest request,String param,String defaultValue){
        String value = request.getParameter(param);
        if (value==null) return defaultValue;
        else return value;
    }

    public static long getLongParameter(HttpServletRequest request,String param,long defaultValue){
        String svalue = request.getParameter(param);
        if(svalue!=null){
            try {
                long value = Long.parseLong(svalue);
                return value;
            }
            catch (NumberFormatException ex) {
                return defaultValue;
            }
        } else return defaultValue;

    }
%>