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
     * 初始化Configuration类
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
                System.out.println("读取属性文件--->错误！- 原因：getResourceAsStream初始化失败"+configFileName);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("读取属性文件--->失败！- 原因：文件路径错误或者文件不存在"+configFileName);
            //ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("装载文件--->失败:"+ex.getMessage());
            //ex.printStackTrace();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        initTime = System.currentTimeMillis();
    }
    /** */
    /**
     * 初始化Configuration类
     *
     * @param configFileName 要读取的配置文件的路径+名称
     */
    public Configuration(String configFileName) {
        this.configFileName = configFileName;
        initConfigFile();
    }//end ReadConfigInfo()

    /** */

    /**
     * 重载函数，得到key的值
     *
     * @param key 取得其值的键
     * @param defaultValue default
     * @return key的值
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
            String result = properties.getProperty(key);//得到某一属性的值
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
     * 重载函数，得到key的值
     *
     * @param fileName properties文件的路径+文件名
     * @param defaultValue defaultValue
     * @param key      取得其值的键
     * @return key的值
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
     * 清除properties文件中所有的key和其值
     */
    public void clear() {
        properties.clear();
    }//end clear();
    /** */
    /**
     * 改变或添加一个key的值，当key存在于properties文件中时该key的值被value所代替，
     * 当key不存在时，该key的值是value
     *
     * @param key   要存入的键
     * @param value 要存入的值
     */
    public void setValue(String key, String value) {
        properties.setProperty(key, value);
    }//end setValue()

    /** */
    /**
     * 将更改后的文件数据存入指定的文件中，该文件可以事先不存在。
     *
     * @param fileName    文件路径+文件名称
     * @param description 对该文件的描述
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
