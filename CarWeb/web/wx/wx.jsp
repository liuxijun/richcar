<%@ page import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.util.AppConfigurator" %><%@ page
        import="java.security.NoSuchAlgorithmException" %><%@ page
        import="java.security.MessageDigest" %>
<%@ page import="java.io.Serializable" %>
<%@ page import="java.util.*" %>
<%@ page import="org.dom4j.Element" %>
<%@ page import="org.dom4j.Node" %>
<%@ page import="com.fortune.util.BeanUtils" %>
<%@ page import="com.fortune.util.StringUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 14-3-4
  Time: 下午3:44
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    //signature=fa4471079e68ea4d3f3e86d5832e599409ce4aad&timestamp=1393920937&nonce=1394140304&echostr=5987787073238961065
    String signature = request.getParameter("signature");
    String timestamp = request.getParameter("timestamp");
    String nonce = request.getParameter("nonce");
    String echostr = request.getParameter("echostr");
    String token = AppConfigurator.getInstance().getConfig("system.wx.token","fortune2009");
    String calculateString =  sort(token,timestamp,nonce);
    String calculated = encrypt(calculateString,"SHA-1");
    logger.debug("收到来自"+request.getRemoteAddr()+"的消息，参数："+signature+","+timestamp+","+nonce+","+echostr);
    boolean passed= calculated!=null&&calculated.equals(signature);
    logger.debug("校验结果是："+passed);
    if(passed){

    }else{
        logger.warn("校验失败，进行计算的string是：" +calculateString+
                "，计算出来的SHA-1是："+calculated+",传过来的是："+signature);
        // echostr = "SORRY,NOT PASSED";
    }
    String message1 = "<xml>\n" +
            " <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
            " <FromUserName><![CDATA[fromUser]]></FromUserName> \n" +
            " <CreateTime>1348831860</CreateTime>\n" +
            " <MsgType><![CDATA[text]]></MsgType>\n" +
            " <Content><![CDATA[this is a test]]></Content>\n" +
            " <MsgId>1234567890123456</MsgId>\n" +
            " </xml>\n";
    logger.debug(getMessage(message1));
%><%=echostr%><%!
    private Logger logger = Logger.getLogger("com.fortune.imin.jsp");
    public String bytes2Hex(byte[]bts) {
        String des="";
        String tmp;
        for (byte bt : bts) {
            tmp = (Integer.toHexString(bt & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
    public String encrypt(String strSrc,String encName) {
        //parameter strSrc is a string will be encrypted,
        //parameter encName is the algorithm name will be used.
        //encName dafault to "MD5"
        MessageDigest md;
        String strDes;

        byte[] bt=strSrc.getBytes();
        try {
            if (encName==null||encName.equals("")) {
                encName="MD5";
            }
            md= MessageDigest.getInstance(encName);
            md.update(bt);
            strDes=bytes2Hex(md.digest());  //to HexString
        }
        catch (NoSuchAlgorithmException e) {
            logger.error("Invalid algorithm.");
            return null;
        }
        return strDes;
    }
    public String sort(String s1,String s2,String s3){
        List<String> list = new ArrayList<String>();
        list.add(s1);list.add(s2);list.add(s3);
        Collections.sort(list,new SpellComparator<String>());
        String result = "";
        for(String s:list){
            result+=s;
        }
        return result;
    }
    class SpellComparator<E> implements Comparator<E> {
        public int compare(E o1, E o2) {
            try {
                // 取得比较对象的汉字编码，并将其转换成字符串
                String s1 = o1.toString();
                String s2 = o2.toString();
                // 运用String类的 compareTo（）方法对两对象进行比较
                return s1.compareTo(s2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
    public WeiXinMessage getMessage(String xml){

    }
    public WeiXinMessage getMessage(Element xmlElement){
        List<Node> children = xmlElement.elements();
        WeiXinMessage result = new WeiXinMessage();
        for(Node child:children){
            String name = child.getName();
            if(name!=null){
                String propertyName = name.substring(0,1).toLowerCase()+name.substring(1);
                String propertyValue = child.getText();
                Object value;
                if("createTime".equals(propertyName)){
                    value = new Date(StringUtils.string2long(propertyValue,0));
                }else if("location_X".equals(propertyName)||"location_Y".equals(propertyName)){
                    try {
                        value = Float.parseFloat(propertyValue);
                    } catch (NumberFormatException e) {
                        value = 0;
                    }
                }else{
                    value = propertyValue;
                }
                BeanUtils.setProperty(result,propertyName,value);
            }
        }
        return result;
    }
    class WeiXinBaseMessage implements Serializable{
        private String toUserName;
        private String fromUserName;
        private Date createTime;
        private String messageType;
        private String msgId;

        public String getToUserName() {
            return toUserName;
        }

        public void setToUserName(String toUserName) {
            this.toUserName = toUserName;
        }

        public String getFromUserName() {
            return fromUserName;
        }

        public void setFromUserName(String fromUserName) {
            this.fromUserName = fromUserName;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public String getMessageType() {
            return messageType;
        }

        public void setMessageType(String messageType) {
            this.messageType = messageType;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }
    }

    class WeiXinTextMessage extends WeiXinBaseMessage{
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    class WeiXinPicMessage extends WeiXinBaseMessage{
        private String picUrl;
        private String mediaId;

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }
    }
    class WeiXinVideoMessage extends WeiXinBaseMessage{
        private String videoUrl;
        private String thumbMediaId;
        private String mediaId;

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getThumbMediaId() {
            return thumbMediaId;
        }

        public void setThumbMediaId(String thumbMediaId) {
            this.thumbMediaId = thumbMediaId;
        }

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }
    }

    class WeiXinLocationMessage extends WeiXinBaseMessage{
        private Float location_X;
        private Float location_Y;
        private String label;
        private String scale;

        public Float getLocation_X() {
            return location_X;
        }

        public void setLocation_X(Float location_X) {
            this.location_X = location_X;
        }

        public Float getLocation_Y() {
            return location_Y;
        }

        public void setLocation_Y(Float location_Y) {
            this.location_Y = location_Y;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getScale() {
            return scale;
        }

        public void setScale(String scale) {
            this.scale = scale;
        }
    }
    class WeiXinLinkMessage extends WeiXinBaseMessage{
        private String title;
        private String description;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
%>