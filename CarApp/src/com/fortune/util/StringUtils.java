package com.fortune.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-4-1
 * Time: 14:14:49
 * 字符串处理的一些工具
 */
public class StringUtils {
    private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();
    private static final char[] AMP_ENCODE = "&amp;".toCharArray();
    private static final char[] LT_ENCODE = "&lt;".toCharArray();
    private static final char[] GT_ENCODE = "&gt;".toCharArray();
    private static final char[] APOS_ENCODE = "&apos;".toCharArray();

    public static String getTimeStr(Long time) {
        if (time==null) return "00:00";
        int timeValue = time.intValue();
        String result = "";

        if (timeValue % 1000 > 0) {
           // result = "." + timeValue % 1000;
        }
        timeValue = timeValue / 1000;
        result = (timeValue % 60) + result;
        if((timeValue % 60)<10){
            result = "0"+result;
        }
        timeValue = timeValue / 60;
        if(timeValue<=0){
           result = "00:"+result;
        }else{
            while (timeValue > 0) {
                if (result.equals("")) {

                } else {
                    result = ":" + result;
                }
                result = timeValue % 60 + result;
                if((timeValue % 60)<10){
                    result = "0"+result;
                }
                timeValue = timeValue / 60;
            }
        }
        return result;
    }
    
    public static String replace(String line, String oldString, String newString) {
        if (line == null) {
            return null;
        }
        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0) {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = line.indexOf(oldString, i)) > 0) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }

    public static String escapeXMLTags(String in) {
        if (in == null) {
            return "";
        }
        char ch;
        int i = 0;
        int last = 0;
        char[] input = in.toCharArray();
        int len = input.length;
        StringBuffer out = new StringBuffer((int) (len * 1.3));
        for (; i < len; i++) {
            ch = input[i];
            if (ch > '>') {
                //continue;
            } else if (ch == '<') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(LT_ENCODE);
            } else if (ch == '>') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(GT_ENCODE);
            } else if (ch == '&') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(AMP_ENCODE);
            } else if (ch == '\"') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(QUOTE_ENCODE);
            } else if (ch == '\'') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(APOS_ENCODE);
            } else if (ch == '\u0008') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(" ");
            }
        }
        if (last == 0) {
            return in;
        }
        if (i > last) {
            out.append(input, last, i - last);
        }
        return out.toString();
    }

    /**
     * 编码转换：gb2312 -> iso-8859-1
     *
     * @param sSource source
     * @return result
     */
    public static String gb2iso(String sSource) {
        try {
            return new String(sSource.getBytes("gb2312"), "iso-8859-1");
        } catch (Exception e) {
            //System.out.println("Charset gb2iso Err:" + e.getMessage());
            return sSource;
        }
    }


    /**
     * 编码转换：iso-8859-1 -> gb2312
     *
     * @param sSource 输入的源
     * @return result
     */
    public static String iso2gb(String sSource) {
        try {
            return new String(sSource.getBytes("iso-8859-1"), "gb2312");
        } catch (Exception e) {
            //System.out.println("Charset iso2gb Err:" + e.getMessage());
            return sSource;
        }
    }

    /**
     * 编码转换：byte -> gb2312
     *
     * @param sSource 输入的源
     * @return result
     */
    public static String byte2gb(byte[] sSource) {
        try {
            return new String(sSource, "gb2312");
        } catch (Exception e) {
            //System.out.println("Charset byte2gb Err:" + e.getMessage());
            return "";
        }
    }


    static public String native2Unicode(String s) {
        char c;
        int j = 0;
        if (s == null || s.length() == 0) {
            return null;
        }
        byte[] buffer = new byte[s.length()];
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= 0x100) {
                c = s.charAt(i);
                byte[] buf = ("" + c).getBytes();
                buffer[j++] = buf[0];
                buffer[j++] = buf[1];
            } else {
                buffer[j++] = (byte) s.charAt(i);
            }
        }
        return new String(buffer, 0, j);
    }


    /**
     * 从全路径字符串中获取文件名
     *
     * @param sPath filename  with path
     * @return only filename
     */
    public static String getFileName(String sPath) {
        try {
            return sPath.substring(sPath.lastIndexOf('\\') + 1, sPath.length());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 从全路径字符串中获取无后缀名的文件名
     *
     * @param sPath file name with path and extname
     * @return result
     */
    public static String getFileNameNoExt(String sPath) {
        try {
            int iBegin, iEnd;
            iBegin = sPath.lastIndexOf('\\');
            iEnd = sPath.lastIndexOf('.');
            return sPath.substring(iBegin + 1, iEnd);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 检查无文件名的路径名，如果末尾没有\则添加
     *
     * @param sDir a
     * @return a
     */
    public static String checkDir(String sDir) {
        if (!sDir.substring(sDir.length() - 1, sDir.length()).equals("\\")) {
            sDir = sDir + "\\";
        }
        return sDir;
    }

    /**
     * 将字符串（yyyy-MM-dd格式）转化为Date类型
     *
     * @param sDate date
     * @return result
     */
    public static Date string2date(String sDate) {
        return string2date(sDate, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * boolean与String互相转换
     *
     * @param data data
     * @return result
     */
    public static boolean string2bool(String data) {
        return data != null && ("true".equals(data.toLowerCase()) || "1".equals(data.toLowerCase()));
    }

    public static String bool2string(boolean data) {
        if (data) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * int 与 String互相转换
     *
     * @param value      value
     * @param defaultVal default
     * @return result
     */
    public static int string2int(String value, int defaultVal) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    /**
     * 日期 与 long互相转换
     *
     * @param value      source
     * @param defaultVal defualt
     * @return result
     */
    public static long date2long(Date value, long defaultVal) {
        try {
            return value.getTime();
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static long string2long(String value, long defaultVal) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static Date string2date(String sDate, String sFormat) {
        SimpleDateFormat sf = new SimpleDateFormat(sFormat);
        try {
            return sf.parse(sDate);
        } catch (Exception ex) {
            return new Date();
        }
    }

    /**
     * 将一个IP地址字符串转换成一个long类型
     *
     * @param ipAddress source
     * @return result
     */
    public static long string2IpLong(String ipAddress) {
        StringTokenizer st = new StringTokenizer(ipAddress.trim(), ".");
        long result = 0;
        while (st.hasMoreTokens()) {
            try {
                result = result * 256;
                result = result + Integer.parseInt(st.nextToken().trim());
            } catch (Exception e) {
                //如果真的有变态数据进来的话，还是要考虑一下是否需要产生异常

            }
        }
        return result;
    }

    /**
     * 将一个long型IP转换为一个字符串
     *
     * @param ip source
     * @return result
     */
    public static String longIp2String(long ip) {
        String Result = "";
        //支持IPv6的哦
        for (int i = 0; i < 6; i++) {
            if (!"".equals(Result)) {
                Result = "." + Result;
            }
            Result = (ip & 0xFF) + Result;
            ip = ip >> 8;
            if (ip == 0) {
                break;
            }
        }
        return Result;
    }

    /**
     * 将Date（）转化为String类型
     *
     * @param date date
     * @return result
     */
    public static String date2string(Date date) {
        return date2string(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String date2string(long date) {
        Date theTime = new Date(date);
        return date2string(theTime);
    }

    public static String date2string(Date date, String format) {
        if (date == null) {
            date = new Date();
        }
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }

    /**
     * 判断是否为闰年,并对异常的日做处理
     *
     * @param syear  year
     * @param smonth month
     * @param sday   day
     * @return day
     */
    public static String LeapYear(String syear, String smonth, String sday) {
        long year = Long.parseLong(syear);
        long month = Long.parseLong(smonth);
        long day = Long.parseLong(sday);

        if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
            if (day == 31) day = 30;
        } else if (month == 2) {
            if ((day == 31) || (day == 30) || (day == 29)) {
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    day = 29;
                } else {
                    day = 28;
                }
            }
        }
        if (day < 10) {
            return "0" + String.valueOf(day);
        } else {
            return String.valueOf(day);
        }
    }

    /**
     * 判断是否为闰年,并对异常的日做处理
     *
     * @param year  year
     * @param month month
     * @param day   day
     * @return result
     */
    public static long LeapYear(long year, long month, long day) {
        if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
            if (day == 31) day = 30;
        } else if (month == 2) {
            if ((day == 31) || (day == 30) || (day == 29)) {
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    day = 29;
                } else {
                    day = 28;
                }
            }
        }
        return day;
    }

    /**
     * 解析规则的字符串 如将#123#23456#222333# 分解成list[0]=123,list[1]=23456,list[3]=222333
     *
     * @param fullstr    source
     * @param indexofstr dot
     * @return result
     */
    public static List getList(String fullstr, String indexofstr) {
        List<String> list = new ArrayList<String>();
        if (fullstr == null) {
            return list;
        }
        if (indexofstr == null) {
            list.add(fullstr);
            return list;
        }
        fullstr = fullstr.trim();
        /*
indexofstr = indexofstr.trim();
        int length = fullstr.length();
        int j = 1;
*/
        int indexLen = indexofstr.length();
        String tempstr = fullstr;
        String tempstr2;
        int i = tempstr.indexOf(indexofstr);
        while (i >= 0) {
            tempstr2 = tempstr.substring(0, i).trim();
            if (tempstr2 != null && tempstr2.length() > 0) {
                list.add(tempstr2);
            }
            if (i + indexLen < tempstr.length()) {
                tempstr = tempstr.substring(i + indexLen);
            } else {
                break;
            }
            i = tempstr.indexOf(indexofstr);
        }
        if (tempstr.length() >= 1) {
            list.add(tempstr);
        }
        /*
        if (i != -1) {
            for (; i < length - indexLen;) {
                tempstr = fullstr.substring(i + indexLen, length).trim();
                j = tempstr.indexOf(indexofstr);
                tempstr2 = tempstr.substring(0, j);
                list.add(tempstr2.trim());
                i = i + j + indexLen;
            }
        } */
        return list;
    }

    /**
     * 将指定的list转化为String[]
     *
     * @param list source
     * @return result
     */
    public static String[] list2StringArray(List list) {
        if (list == null) {
            return null;
        }
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i).toString();
        }
        return result;
    }

    /**
     * 获得媒体播放完整的路径
     *
     * @param serverPath server path rstp://serverIP:port///
     * @param mediaPath  media path   ///dir1/dir2/movie.wmv
     * @return result of rtsp://serverIp:port/dir1/dir2/movie.wmv
     */
    public static String composeFullUrl(String serverPath, String mediaPath) {

        if (serverPath == null || mediaPath == null)
            return "faint";
        serverPath = serverPath.replace('\\', '/');

        mediaPath = mediaPath.replace('\\', '/');

        //路径合成的健壮性
        if (!serverPath.substring(serverPath.length() - 1, serverPath.length()).equals("/")) {
            serverPath = serverPath + "/";
        }
        if (mediaPath.substring(0, 1).equals("/")) {
            mediaPath = mediaPath.substring(1, mediaPath.length());
        }
        return serverPath + mediaPath;
    }

    /**
     * 修正URL，将其中的错误，例如多个斜杠，“///”，“..”等进行修正，获得正确的url
     *
     * @param sURL like rtsp://serverIp/dir1//dir2///dir3/movie.wmv
     * @return result rtsp://serverIp/dir1/dir2/dir3/movie.wmv
     */
    public static String checkURL(String sURL) {
        StringBuffer ResultBuffer = new StringBuffer();
        int i = sURL.length();
        int TokenCount = 0, DotCount = 0, lastTokenPos = -1, prevDirPos = -1;
        //循环，将字符串进行检验
        for (int j = 0; j < i; j++) {
            char ch = sURL.charAt(j);
            switch (ch) {
                case '?':
                    //ResultBuffer.append(ch);
                    ResultBuffer.append(sURL.substring(j));
                    j = i;
                    break;
                case '.':
                    if (0 == DotCount) {
                        ResultBuffer.append(ch);
                    } else if (1 == DotCount) { // get a ".."

                    }
                    DotCount++;
                    TokenCount = 0;
                    break;
                case '/':
                    //如果不是开头的://的话
                    if (j > 5) {
                        if (2 == DotCount) {
                            int l = ResultBuffer.length();
                            // ..前面必须是/才有效
                            if (lastTokenPos + 1 == l) {
                                if (l >= 2 && lastTokenPos > 0) {
                                    ResultBuffer.delete(prevDirPos - 1, l);
                                }
                            }
                        }
                        if (0 == TokenCount) {
                            ResultBuffer.append(ch);
                            prevDirPos = lastTokenPos;
                            lastTokenPos = ResultBuffer.length();
                        }
                        TokenCount++;
                    } else {
                        ResultBuffer.append(ch);

                    }
                    DotCount = 0;
                    break;
                case '\\':
                    DotCount = 0;
                    TokenCount = 0;
                    break;
                case '\n':
                    DotCount = 0;
                    TokenCount = 0;
                    break;
                case '\r':
                    DotCount = 0;
                    TokenCount = 0;
                    break;
                case '\t':
                    DotCount = 0;
                    TokenCount = 0;
                    break;
                default:
                    DotCount = 0;
                    TokenCount = 0;
                    ResultBuffer.append(ch);
                    break;
            }
        }
        return ResultBuffer.toString();

    }

    /**
     * 去掉URL中的://.../前面的字符。原因是MMS9会自以为是的将协议进行转换，原来的判断规则会出现错误。
     *
     * @param sURL like rtsp://serverIp:port/dir1/dir2/dir3/movie.wmv
     * @return result of /dir1/dir2/dir3/movie.wmv
     */
    public static String getClearURL(String sURL) {
        //找到"://"，把它前面的部分去掉
        int iPos = sURL.indexOf("://");
        int iLen = sURL.length();
        if (iPos >= 0) {//发现
            if (iPos + 4 < iLen) {  //是否超出范围？
                sURL = sURL.substring(iPos + 4);  //跳过://，直接复制后面的
                iPos = sURL.indexOf("/");       //找到/
                if (iPos >= 0) {                    //OK
                    sURL = sURL.substring(iPos);
                }
            }
            return sURL;
        } else {
            //没有找到"://",返回原来的内容
            return sURL;
        }
    }

    public static int getIntParameter(String Url, String parameterName, int defaultValue) {
        return (int) getLongParameter(Url,parameterName,defaultValue);
    }

    public static long getLongParameter(String Url, String parameterName, long defaultValue) {
        String strValue = getParameter(Url, parameterName);
        if (strValue == null || "".equals(strValue.trim())) {
            return defaultValue;
        }
        try {
            return Long.parseLong(strValue.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public static String getStringParameter(String Url, String parameterName, String defaultValue) {
        String strValue = getParameter(Url, parameterName);
        if (strValue == null || "".equals(strValue.trim())) {
            return defaultValue;
        }
        return strValue;
    }

    /**
     * 从制定的URL中获取queryString
     *
     * @param URL           用户请求的URL
     * @param ParameterName 想获取的参数名
     * @return sResult       想获取的参数值
     */
    public static String getParameter(String URL, String ParameterName) {
        int ParameterIndex, URLLength;
        if(URL==null)
                return null;
        //获取URL的长度
        URLLength = URL.length();
        StringBuffer Result;
        //以后面的第一个为参数内容。这样的作用时，一般参数在后面，速度会块一点点，另外，key肯定在最后面，
        //即使与其他应用产生的参数冲突，我们的数据依然可以正确获取
        ParameterIndex = URL.lastIndexOf(ParameterName + "=");
        //如果找不到参数名，则放弃
        if (ParameterIndex < 0) {
            return null;
        }
        //初始化结果的StringBuffer。这样做的目的是，速度会快一点
        Result = new StringBuffer();
        char ch;
        //循环，把参数结果加到返回值里面。循环起始值要加一的，注意。
        for (int i = ParameterIndex + ParameterName.length() + 1; i < URLLength; i++) {
            //获取ch，参数值的一部分
            ch = URL.charAt(i);
            //如果是&，则认为已经结束
            if (ch == '&'||ch=='?') {
                break;
            }
            //将这个char放到结果的尾部
            Result.append(ch);
        }
        //返回结果
        return Result.toString();
    }

    public static List<String> encodeTestor(String sourceStr){
        String[] encodings=  new String[]{"gbk","gb2312","utf-8","iso-8859-1",null};
        return encodeTestor(sourceStr,encodings);
    }

    public static List<String> encodeTestor(String sourceStr,String[] encodings){
        List<String> result = new ArrayList<String>();
        for(String srcEncoding:encodings){
            byte[] sourceBytes = null;
            if(srcEncoding==null){
                sourceBytes=sourceStr.getBytes();
            }else{
                try {
                    sourceBytes=sourceStr.getBytes(srcEncoding);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if(sourceBytes!=null){
                for(String dstEncoding:encodings){
                    String dstStr = null;
                    if(dstEncoding!=null){
                        try {
                            dstStr = new String(sourceBytes,dstEncoding);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }else{
                        dstStr = new String(sourceBytes);
                    }
                    result.add("("+srcEncoding+")->("+dstEncoding+")="+dstStr);
                }
            }
        }
        return result;
    }
	public static String formatUnits(long baseNumber,long[] unitDivisors,String[] unitLabels,boolean singleFractional) {
		int i;
        long unitDivisor;
        long unit;
        String unitLabel;

		if (baseNumber == 0) {
			return "0 " + unitLabels[unitLabels.length - 1];
		}

		if (singleFractional) {
			unit = baseNumber;
			unitLabel = unitLabels.length >= unitDivisors.length ? unitLabels[unitDivisors.length - 1] : "";
			for (i = 0; i < unitDivisors.length; i++) {
				if (baseNumber >= unitDivisors[i]) {
					unit = Math.round(baseNumber * 100 / unitDivisors[i])/100;
					unitLabel = unitLabels.length >= i ? " " + unitLabels[i] : "";
					break;
				}
			}

			return unit + unitLabel;
		} else {
			StringBuffer formattedStrings = new StringBuffer();
			long remainder = baseNumber;
			for (i = 0; i < unitDivisors.length; i++) {
				unitDivisor = unitDivisors[i];
				unitLabel = unitLabels.length > i ? " " + unitLabels[i] : "";
				unit = remainder / unitDivisor;
				if (i < unitDivisors.length -1) {
					unit = Math.round(unit);
				} else {
					unit = Math.round(unit * 100)/100;
				}
				if (unit > 0) {
					remainder = remainder % unitDivisor;
					formattedStrings.append(unit).append(unitLabel);
				}
			}
			return formattedStrings.toString();
		}
	}

    public static String[] getStrings(String s,String split) {
        int point = 0;

        List result = new ArrayList();
        while (s.indexOf(split,point)!=-1){
            result.add(s.substring(point,s.indexOf(split,point)));
            point = s.indexOf(split,point)+split.length();
        }

        result.add(s.substring(point,s.length()));

        String[] ss = new String[result.size()];
        for(int i=0;i<result.size();i++){
            ss[i] = result.get(i).toString();
        }

        return ss;
    }

    public static String formatBPS(long baseNumber) {
        long[] bpsUnits = new long[]{1073741824, 1048576, 1024, 1};
        String[] bpsUnitLabels = new String[]{"Gbps", "Mbps", "Kbps", "bps"};
        return formatUnits(baseNumber, bpsUnits, bpsUnitLabels, true);

    }
    public static String formatTime(long baseNumber) {
        long[] timeUnits = new long[]{365*24*3600L,86400, 3600, 60, 1};
        String[] timeUnitLabels = new String[]{"年","天", "小时", "分", "秒"};
        return formatUnits(baseNumber, timeUnits, timeUnitLabels, false);

    }
    public static String formatBytes(long baseNumber) {
        long[] sizeUnits = new long[]{1073741824, 1048576, 1024, 1};
        String[] sizeUnitLabels = new String[]{"GB", "MB", "KB", "bytes"};
        return formatUnits(baseNumber, sizeUnits, sizeUnitLabels, true);

    }
}
