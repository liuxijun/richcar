package com.fortune.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-10-26
 * Time: 10:49:12
 * 文件过滤
 */
public class ExtFileNameFilter implements FileFilter {
    private List<Pattern> patterns;
    public static final String FILTER_DIR_ONLY="listDirOnly";
    private boolean dirOnly=false;
    private Logger logger = Logger.getLogger(getClass());
    public ExtFileNameFilter(String regexStr){
        patterns = new ArrayList<Pattern>();
        if(FILTER_DIR_ONLY.equals(regexStr)){
            dirOnly = true;
            regexStr = "*.*";
        }else{
            if(null == regexStr){
                regexStr="*";
            }
        }
        String[] regexes = regexStr.split(",");
        for(String regex:regexes){
            if(regex == null){
                regex = "*";
            }
            regex = regex.replace('.','#');
            regex = regex.replaceAll("#","\\\\.");
            regex = regex.replace('*','#');
            regex = regex.replaceAll("#",".*");
            regex = regex.replace('?','#');
            regex = regex.replaceAll("#",".?");
            regex = "^"+regex+"$";
            logger.debug("文件通配符："+regex);
            patterns.add(Pattern.compile(regex));
        }
    }
    public boolean accept(File file){
        boolean result = false;
        if(file.isDirectory())return true;
        if(dirOnly){
            return false;
        }
        String name = file.getName();
        if(patterns!=null){
            for(Pattern pattern:patterns){
                if(pattern.matcher(name).matches()){
                    result = true;
                    break;
                }
            }
        }
        logger.debug("输入检查文件名："+name+",结果："+result);
        return result;
    }
}
