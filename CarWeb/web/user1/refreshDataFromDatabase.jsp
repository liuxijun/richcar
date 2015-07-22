<%
    /**
     * Copyright ?2002 Shanghai Fudan Ghuanghua Co. Ltd.
     * All right reserved.
     * 文件名称: .jsp
     * 主要功能: 更新内存中的数据信息
     *
     * 输入参数：
     * 输出数据:
     *
     * 作    者:    xjliu
     * 日    期：
     *    开始时间： 2004-3-16 20:43:44
     *    结束时间：
     *
     *    修改人员：
     *    修改日期与原因：
     */
%><%@ page
contentType="text/html;charset=gb2312" %><%@page
import="java.util.*,
        cn.sh.guanghua.util.tools.PageHelper,
        cn.sh.guanghua.util.tools.ParamTools,
        cn.sh.guanghua.util.tools.StringTools,
        cn.sh.guanghua.mediastack.common.Constants,
        cn.sh.guanghua.cache.CacheManager"%><%
            PageHelper pageHelper = new PageHelper(request,session);
            StringBuffer sbOutMsg = new StringBuffer();
//获取必要的参数

//调用逻辑方法
            String result = null;
            long resultCode = 0;
            if(doInitDelay >0){
                result = "重新初始化已经受理，将在"+doInitDelay+"秒后开始初始化......";
                resultCode = 0;
            }else{

                        DoReInitData reinitData= new DoReInitData();
                        reinitData.start();
                        if (doInitDelay<=0){
                            result = "重新初始化已经受理，将在10秒后开始初始化......";
                        }else{
                            result = "重新初始化已经受理，将在"+doInitDelay+"秒后开始初始化......";
                        }
                        resultCode = 0;


            }
//添加数据到sbOutMsg中
            sbOutMsg.append(PageHelper.addElement("result",result));
            sbOutMsg.append(PageHelper.addElement("result-code",resultCode));
            sbOutMsg.append(PageHelper.addElement("delay-seconds",doInitDelay));
//输出必要的信息
            pageHelper.outPut(out,sbOutMsg.toString(),
                              "10-10-80-0065",
                              "no error","What is this file can do?",
                              "10-10-80-HELP_ID","xjliu");
%><%!
    static private long doInitDelay = 0;
    class DoReInitData extends Thread {
        public long getDelay(){
            return doInitDelay;
        }
        public DoReInitData(){
        }
        public void setDelay(long newDelay){
            synchronized(this){
               doInitDelay = newDelay;
            }
        }
        public void run(){
            if(doInitDelay <= 0){
                setDelay( 10 );
                try{
                    while(doInitDelay>=0){
                        Thread.sleep(1000);
                        doInitDelay --;
                    }
                }catch(Exception e){

                }
                CacheManager.getInstance().notifyServerUpdate("","");

                //Cach.getInstance().notifyServerUpdate(Constants.ALL_CACHE,"");
            }
        }
    }
%>