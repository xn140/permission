package com.mmall.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by xuning on 2018/5/7.
 */
public class LevelUtil {

    public final static String SEPARATOR = ".";

    public final static String ROOT = "0";

    // 0
    // 0.1
    // 0.1.2
    // 0.1.3
    // 0.4
//level的结构是，当前部门的父id拼上当前部门父部门的level
    public static  String calculateLevel(String parentLevel,Integer parentId){

        if(StringUtils.isBlank(parentLevel)){

            return ROOT;
        }else{
            return  StringUtils.join(parentLevel,SEPARATOR,parentId);
        }

    }
}
