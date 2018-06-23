package com.mmall.util;

import java.util.Date;
import java.util.Random;

/**
 * Created by xuning on 2018/5/16.
 * 密码生成工具类
 */
public class PasswordUtil {

    public final static String[] word = {
            "a", "b", "c", "d", "e", "f", "g",
            "h", "j", "k", "m", "n",
            "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G",
            "H", "J", "K", "M", "N",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };

    public final static String[] num = {
            "2", "3", "4", "5", "6", "7", "8", "9"
    };

    public static String randomPassword(){

        boolean flag=false;
        StringBuffer stringBuffer = new StringBuffer();
        //如果使用相同的random是有规律的，所以使用时间戳的这种随机random
        Random random = new Random(new Date().getTime());
        //定义密码的长度8-10之间
        int length=random.nextInt(3)+8;


        for(int i=0;i<length;i++){
            //如果flag为true,从数字数组中随机出一个数字
            if(flag){
                stringBuffer.append(num[random.nextInt(num.length)]);

            }else{
                stringBuffer.append(word[random.nextInt(word.length)]);
            }
            flag=!flag;

        }

        return stringBuffer.toString();
    }

    public static void  main(String args[]){
        System.out.println(randomPassword());
    }
}
