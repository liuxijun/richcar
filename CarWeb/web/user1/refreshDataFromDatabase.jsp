<%
    /**
     * Copyright ?2002 Shanghai Fudan Ghuanghua Co. Ltd.
     * All right reserved.
     * �ļ�����: .jsp
     * ��Ҫ����: �����ڴ��е�������Ϣ
     *
     * ���������
     * �������:
     *
     * ��    ��:    xjliu
     * ��    �ڣ�
     *    ��ʼʱ�䣺 2004-3-16 20:43:44
     *    ����ʱ�䣺
     *
     *    �޸���Ա��
     *    �޸�������ԭ��
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
//��ȡ��Ҫ�Ĳ���

//�����߼�����
            String result = null;
            long resultCode = 0;
            if(doInitDelay >0){
                result = "���³�ʼ���Ѿ���������"+doInitDelay+"���ʼ��ʼ��......";
                resultCode = 0;
            }else{

                        DoReInitData reinitData= new DoReInitData();
                        reinitData.start();
                        if (doInitDelay<=0){
                            result = "���³�ʼ���Ѿ���������10���ʼ��ʼ��......";
                        }else{
                            result = "���³�ʼ���Ѿ���������"+doInitDelay+"���ʼ��ʼ��......";
                        }
                        resultCode = 0;


            }
//������ݵ�sbOutMsg��
            sbOutMsg.append(PageHelper.addElement("result",result));
            sbOutMsg.append(PageHelper.addElement("result-code",resultCode));
            sbOutMsg.append(PageHelper.addElement("delay-seconds",doInitDelay));
//�����Ҫ����Ϣ
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