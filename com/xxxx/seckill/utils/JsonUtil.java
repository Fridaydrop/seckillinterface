package com.xxxx.seckill.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.json.JsonParseException;

import java.io.IOException;
import java.util.List;

/**
 * @author shkstart
 * @create 2022-09-24 10:06
 */
public class JsonUtil {
    private static ObjectMapper objectMapper=new ObjectMapper();


    public static String object2JsonStr(Object obj){
        try{
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T jsonStr2Object(String jsonStr,Class<T> clazz){

        try {
            return objectMapper.readValue(jsonStr.getBytes("UTF-8"),clazz);
        } catch (JsonParseException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> jsonToList(String jsonStr,Class<T> beanType){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class,beanType);
        try {
            List<T> list = objectMapper.readValue(jsonStr,javaType);
            return list;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
