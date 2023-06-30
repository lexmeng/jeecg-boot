package org.jeecg.modules.devops.tools;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.devops.exception.BussinessException;

import java.util.List;

@Slf4j
public class ParamUtils {
    public static void checkNotNull(Object param){
        if(param == null){
            throw new BussinessException("有参数为空！");
        }
    }


    public static void checkInEnum(String param, List<String> paramEnumList){
        if(param == null || param.isEmpty()){
            throw new BussinessException("参数"+param+"为空！");
        }

        if(!paramEnumList.contains(param)){
            throw new BussinessException("参数"+param+"不在可选值中！");
        }
    }

    public static String checkDefaultValue(String param, String defaultValue){
        if(param == null || param.isEmpty()){
            return defaultValue;
        }else{
            return param;
        }
    }

    public static void main(String[] args) {
        String defaultValue = "aa";
        String param = "bb";

        param = checkDefaultValue(param,defaultValue);
        System.out.println(param);
    }
}
