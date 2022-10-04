package com.xxxx.seckill.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shkstart
 * @create 2022-09-11 19:40
 */
public class ValidatorUtil {
    private static final Pattern moblie_pattern = Pattern.compile("[1]([3-9])[0-9]{9}$");
    public static boolean isMobile(String mobile){
        if(StringUtils.isEmpty(mobile)){
            return false;
        }
        Matcher matcher = moblie_pattern.matcher(mobile);
        return matcher.matches();
    }


}
