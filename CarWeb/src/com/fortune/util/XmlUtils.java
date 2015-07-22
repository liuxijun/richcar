package com.fortune.util;

import org.apache.log4j.Logger;
import org.dom4j.Node;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.Date;
import java.lang.reflect.Field;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-5-23 ,2013-3-1
 * 2013-3-1 add method to read xml file
 * Time: 20:41:46
 */
@SuppressWarnings("unused")
public class XmlUtils {
    public static Logger logger = Logger.getLogger("com.fortune.utils.XmlUtils");
    public static String guessFileEncoding(String fileName){
        try {
            FileInputStream is =(new FileInputStream(new File(fileName)));
            String fileEncoding = null;
            byte[] fileHeader = new byte[3];
            try {
                int i=is.read(fileHeader);
                if(i==3){
                    long value = ((long)(0x00FF&fileHeader[0]))<<16+((long)(0x00ff&fileHeader[1]))<<8+0x00ff&fileHeader[2];
                    if(value==0xEFBBBF){
                        fileEncoding = "UTF-8";
                    }else if(fileHeader[0]==-17&&fileHeader[1]==-69&&fileHeader[2]==-65){
                        fileEncoding = "UTF-8";
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("发生读取异常：fileName="+fileName+",e="+e.getMessage());
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("关闭异常：fileName="+fileName+",e="+e.getMessage());
            }
            if(fileEncoding==null){
                String fileContent = FileUtils.readFileInfo(fileName);
                int p = fileContent.indexOf("encoding");
                if(p>0){
                    p += 8;
                    int i=0;
                    int l=fileContent.length();
                    boolean beginFound = true;
                    while(fileContent.charAt(p+i)!='"'){
                        i++;
                        if(i>100||(p+i)>=l){
                            beginFound = false;
                            break;
                        }
                    }
                    p+=i;
                    if(beginFound){
                        fileEncoding = "";
                        i=1;
                        while(fileContent.charAt(p+i)!='"'){
                            fileEncoding +=fileContent.charAt(p+i);
                            i++;
                            if(i>100||(p+i)>=l){
                                break;
                            }
                        }
                    }
                }
            }
            return fileEncoding;
        } catch (FileNotFoundException e) {
            logger.error("文件没有找到："+fileName+",e="+e.getMessage());
        }
        return null;
    }
    public static Element getRootFromXmlFile(String fileName){
        return getRootFromXmlFile(fileName,guessFileEncoding(fileName));
    }

    public static Element getRootFromXmlFile(String fileName,String encoding){
        File xmlFile = new File(fileName);
        if(xmlFile.exists()){
            InputStreamReader is;
            if(encoding==null||"".equals(encoding.trim())){
                encoding = guessFileEncoding(fileName);
            }
            String readResult = FileUtils.readFileInfo(xmlFile.getAbsolutePath(),encoding);
            while(!readResult.startsWith("<")){
                readResult = readResult.substring(1);
            }
            return getRootFromXmlStr(readResult);
        }else{
            logger.error("无法读取文件："+xmlFile.getAbsolutePath());
        }
        return null;
    }
    public static org.dom4j.Element getRootFromXmlStr(String xmlStr){
        if (xmlStr != null) {
            SAXReader reader = new SAXReader();
            try {
                Document doc = reader.read(new StringReader(xmlStr));
                return doc.getRootElement();
            } catch (DocumentException e) {
                logger.error("xml format error :" + e.getMessage()+"" +
                        "\ncontent is "+
                        ":"+xmlStr);
                //e.printStackTrace();
            }
        }
        return null;
    }
    public static String getValue(org.dom4j.Node ele, String childName, String defaultValue) {
        if (ele == null || childName == null || "".equals(childName)) {
            return null;
        }
        org.dom4j.Node childNode = ele.selectSingleNode(childName);
        String result = defaultValue;
        if (childNode != null) {
            result = childNode.getText();
        }
        return result;
    }

    public static long getLongValue(org.dom4j.Node ele, String childName, long defaultValue) {
        try {
            return Long.parseLong(getValue(ele, childName, "" + defaultValue));
        } catch (NumberFormatException e) {
            return defaultValue;
        }

    }

    public static int getIntValue(org.dom4j.Node ele, String childName, int defaultValue) {
        return (int) getLongValue(ele, childName, defaultValue);
    }

    public static String addElement(String name, Object value) {
        if (value instanceof Date) {
            return addElement(name, StringUtils.date2string((Date) value));
        }
        if (value == null) {
            return addElement(name, "");
        }
        return addElement(name, value.toString());
    }

    public static String addElement(String name, String value) {
        name = StringUtils.escapeXMLTags(name);
        return "\t<" + name + ">" + StringUtils.escapeXMLTags(value) + "</" + name + ">\r\n";
    }

    public static Object toObject(Class objClass, org.dom4j.Node ele) {
        if (ele == null) {
            return null;
        }
        try {
            Object result = objClass.newInstance();
            if (result != null) {
                for (Field field : objClass.getFields()) {
                    String name = field.getName();

                    Node fieldNode = ele.selectSingleNode(name);
                    if (fieldNode == null) {
                        fieldNode = ele.selectSingleNode(classNameToXmlTag(name));
                    }
                    if (fieldNode != null) {
                        String value = fieldNode.getText();
                        BeanUtils.setProperty(result, name, value, field.getType());
                    } else {
                        //return null;
                    }
                }
            }
            return result;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String classNameToXmlTag(String className) {
        if (className == null) {
            return "null-name";
        }
        boolean firstTime = false;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < className.length(); i++) {
            char ch = className.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                ch = (char) (ch + 'a' - 'A');
                if (firstTime) {
                    firstTime = false;
                    result.append("-");
                }
            } else if (ch == '_') {
                ch = '-';
            } else {
                firstTime = true;
            }
            result.append(ch);
        }
        return result.toString();
    }
}
