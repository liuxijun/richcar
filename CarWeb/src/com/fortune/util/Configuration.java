package com.fortune.util;


import java.io.*;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-3-1
 * Time: 21:21:30
 * read configuraetor
 */
public class Configuration {
    private Properties properties;
    private long initTime = 0;
    private String configFileName;

    /** */
    /**
     * ��ʼ��Configuration��
     */
    public Configuration() {
        properties = new Properties();
        initConfigFile();
    }
    public void initConfigFile(){
        properties = new Properties();
        if(configFileName == null || "".equals(configFileName)){
            configFileName = "/fortune_application.properties";
        }
        try {
            InputStream is;
            is = getClass().getResourceAsStream(configFileName);
            
            if(is!=null){
                properties.load(is);
                is.close();
            }else{
                System.out.println("��ȡ�����ļ�--->����- ԭ��getResourceAsStream��ʼ��ʧ��"+configFileName);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("��ȡ�����ļ�--->ʧ�ܣ�- ԭ���ļ�·����������ļ�������"+configFileName);
            //ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("װ���ļ�--->ʧ��:"+ex.getMessage());
            //ex.printStackTrace();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        initTime = System.currentTimeMillis();
    }
    /** */
    /**
     * ��ʼ��Configuration��
     *
     * @param configFileName Ҫ��ȡ�������ļ���·��+����
     */
    public Configuration(String configFileName) {
        this.configFileName = configFileName;
        initConfigFile();
    }//end ReadConfigInfo()

    /** */

    /**
     * ���غ������õ�key��ֵ
     *
     * @param key ȡ����ֵ�ļ�
     * @param defaultValue default
     * @return key��ֵ
     */

    public String getValue(String key,String defaultValue) {
        return getValue(key,defaultValue,true);
    }
    public String getValue(String key,String defaultValue,boolean encodeToGBK) {
        //System.out.println("Trying to get "+key);
        long nowTime = System.currentTimeMillis();
        if((nowTime - initTime)>1000*60*10L){
            initConfigFile();
        }
        if (properties.containsKey(key)) {
           // System.out.println("Got a value:"+properties.getProperty(key));
            String result = properties.getProperty(key);//�õ�ĳһ���Ե�ֵ
            if(result !=null){
                if(encodeToGBK){
                    try {
                        return new String(result.getBytes("ISO8859-1"), "GBK");
                    } catch (UnsupportedEncodingException e) {
                        return result;
                    }
                }else{
                    return result;
                }
            }else{
                return null;
            }
        } else{
           // System.out.println("Don't have this key value:"+key);
            return defaultValue;
        }
    }//end getValue()

    /** */
    /**
     * ���غ������õ�key��ֵ
     *
     * @param fileName properties�ļ���·��+�ļ���
     * @param defaultValue defaultValue
     * @param key      ȡ����ֵ�ļ�
     * @return key��ֵ
     */
    public String getValue(String fileName, String key, String defaultValue) {
        FileInputStream inputFile;
        try {
            String value = defaultValue;
            inputFile = new FileInputStream(fileName);
            properties.load(inputFile);
            inputFile.close();
            if (properties.containsKey(key)) {
                value = properties.getProperty(key);
                return value;
            } else
                return value;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return defaultValue;
        } catch (IOException e) {
            e.printStackTrace();
            return defaultValue;
        } catch (Exception ex) {
            ex.printStackTrace();
            return defaultValue;
        }
    }//end getValue()

    /** */
    /**
     * ���properties�ļ������е�key����ֵ
     */
    public void clear() {
        properties.clear();
    }//end clear();
    /** */
    /**
     * �ı�����һ��key��ֵ����key������properties�ļ���ʱ��key��ֵ��value�����棬
     * ��key������ʱ����key��ֵ��value
     *
     * @param key   Ҫ����ļ�
     * @param value Ҫ�����ֵ
     */
    public void setValue(String key, String value) {
        properties.setProperty(key, value);
    }//end setValue()

    /** */
    /**
     * �����ĺ���ļ����ݴ���ָ�����ļ��У����ļ��������Ȳ����ڡ�
     *
     * @param fileName    �ļ�·��+�ļ�����
     * @param description �Ը��ļ�������
     */
    public void saveFile(String fileName, String description) {
        FileOutputStream outputFile;
        try {
            outputFile = new FileOutputStream(fileName);
            properties.store(outputFile, description);
            outputFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }//end saveFile()

    public Properties getProperties() {
        return properties;
    }
}
