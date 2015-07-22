<%@ page import="cn.sh.guanghua.util.tools.ParamTools,
                 cn.sh.guanghua.mediastack.common.Constants,
                 cn.sh.guanghua.cache.CacheManager,
                 cn.sh.guanghua.mediastack.user.ChannelManager"%>
finished!

<%
String tableName = ParamTools.getParameter(request,"table_name","");
String pid = ParamTools.getParameter(request,"pid","");

UpdateData ud = new UpdateData(tableName,pid);
ud.start();

%>
<%!
    class UpdateData extends Thread {
        private String tableName;
        private String pid;
        public UpdateData(String tableName,String pid){
            this.tableName = tableName;
            this.pid = pid;
        }
        public void run(){
            CacheManager.getInstance().clearCache(tableName,pid);
            ChannelManager.getInstance().refreshChannel();
        }
    }
%>
