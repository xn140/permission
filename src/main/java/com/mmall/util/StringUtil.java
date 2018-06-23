package com.mmall.util;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xuning on 2018/6/9.
 */
public class StringUtil {

    public static List<Integer> splitToListInt(String str){

        List<String> strList= Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);

        List<Integer> list=strList.stream().map(e -> Integer.parseInt(e)).collect(Collectors.toList());

        return list;
    }
}
