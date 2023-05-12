package org.jeecg.modules.publishlist.logic;

import org.jeecg.modules.publishlist.config.Config;
import org.jeecg.modules.publishlist.entity.Template;
import org.jeecg.modules.publishlist.exception.BussinessException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VarifyLogic {
    public static void verifyTemplate(Template template){
        String type = template.getType();
        if(type==null || type.equals("")){
            throw new BussinessException("模板类型为空！id："+template.getId());
        }

        if(type.equals(Config.RELEASE_INFO_TYPE_RELEASE_NOTE) || type.equals(Config.RELEASE_INFO_TYPE_RELEASE_MAIL)
                || type.equals(Config.RELEASE_INFO_TYPE_HANDBOOK_PR) || type.equals(Config.RELEASE_INFO_TYPE_PRODUCT_PACKAGE_PR)
                || type.equals(Config.RELEASE_INFO_TYPE_COMPANY_WEBSITE)){
            //pass
        }else{
            throw new BussinessException("模板类型错误！id："+template.getId());
        }

    }

    private static List<String> getAllPlaceholder(String content){
        List<String> placeholderList = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\$\\{.+?\\}");
        Matcher matcher = pattern.matcher(content);

        while(matcher.find()){
            String group = matcher.group(0);
            placeholderList.add(group);
        }

        return placeholderList;
    }

    private static List<String> getAllIteratePlaceholder(String content){
        List<String> iteratePlaceholderList = new ArrayList<>();

        Pattern iteratePattern = Pattern.compile("\\$\\$Iterate\\(.+?\\)");
        Matcher matcher = iteratePattern.matcher(content);
        while(matcher.find()){
            String group = matcher.group(0);
            iteratePlaceholderList.add(group);
        }
        return iteratePlaceholderList;
    }

    public static Boolean verifyPlaceholder(Template template){
        String productLineName = template.getProductLineName();
        List<String> placeholderStrList;

        if(productLineName.isEmpty()){
            throw new BussinessException("产品线为空");
        }
        if(productLineName.toUpperCase().contains(Config.PRODUCT_LINE_NAME_KE)){
            placeholderStrList = Config.KE_PLACEHODLER_MAP.get(template.getType());
        }else if(productLineName.toUpperCase().contains(Config.PRODUCT_LINE_NAME_KC)){
            placeholderStrList = Config.KC_PLACEHODLER_MAP.get(template.getType());
        }else{
            throw new BussinessException("模板产品线名错误");
        }

        //抓取所有placeholder
        List<String> placeholderList = getAllPlaceholder(template.getContent());
        List<String> iteratePlaceholderList = getAllIteratePlaceholder(template.getContent());

        //判断是否在可选的placeholder中
        if(!validatePlaceholder(template.getType(), productLineName, placeholderList)){
            return false;
        }
        if(!validatePlaceholder(template.getType(), productLineName, iteratePlaceholderList)){
            return false;
        }

        return true;

    }

    private static Boolean validatePlaceholder(String type, String productLineName, List<String> placeholderList){
        validateProductLineName(productLineName);

        if(productLineName.isEmpty()){
            throw new BussinessException("产品线为空");
        }
        List<String> placeholderAllList;
        if (productLineName.toUpperCase().contains(Config.PRODUCT_LINE_NAME_KE)){
            placeholderAllList = Config.KE_PLACEHODLER_MAP.get(type);
        }else if(productLineName.toUpperCase().contains(Config.PRODUCT_LINE_NAME_KC)){
            placeholderAllList = Config.KC_PLACEHODLER_MAP.get(type);
        }else{
            throw new BussinessException("产品线名称错误！");
        }

        if(placeholderAllList.containsAll(placeholderList)){
            return true;
        }else{
            return false;
        }

    }

    private static Boolean validateProductLineName(String productLineName){
        if(productLineName == null || productLineName.equals("")){
            throw new BussinessException("产品线名称为空");
        }
        if(productLineName.toUpperCase().contains(Config.PRODUCT_LINE_NAME_KE)||productLineName.toUpperCase().contains(Config.PRODUCT_LINE_NAME_KC)){
            //pass
        }
        else{
            throw new BussinessException("产品线名称错误！");
        }

        return true;
    }
}
