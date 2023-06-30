package org.jeecg.modules.devops.logic;

import org.jeecg.modules.devops.config.Config;
import org.jeecg.modules.devops.entity.Template;
import org.jeecg.modules.devops.exception.BussinessException;

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
                || type.equals(Config.RELEASE_INFO_TYPE_HANDBOOK_PR_EN) || type.equals(Config.RELEASE_INFO_TYPE_HANDBOOK_PR_CH) || type.equals(Config.RELEASE_INFO_TYPE_PRODUCT_PACKAGE_PR)
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

    private static List<String> getAllHistoryPlaceholder(String content){
        List<String> historyPlaceholderList = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\$\\$History\\(.+?\\)");
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()){
            String group = matcher.group(0);
            historyPlaceholderList.add(group);
        }
        return historyPlaceholderList;
    }

    public static Boolean verifyPlaceholder(Template template){
        String productLineName = template.getProductLineName();
        List<String> placeholderStrList;

        if(productLineName.isEmpty()){
            throw new BussinessException("产品线为空");
        }
        /*
        if(productLineName.contains(Config.PRODUCT_LINE_NAME_KE)){
            placeholderStrList = Config.KE_PLACEHODLER_MAP.get(template.getType());
        }else if(productLineName.contains(Config.PRODUCT_LINE_NAME_KC)){
            placeholderStrList = Config.KC_PLACEHODLER_MAP.get(template.getType());
        }else{
            throw new BussinessException("模板产品线名错误");
        }
        */
        //抓取所有placeholder
        List<String> placeholderList = getAllPlaceholder(template.getContent());
        List<String> iteratePlaceholderList = getAllIteratePlaceholder(template.getContent());
        List<String> historyPlaceholderList = getAllHistoryPlaceholder(template.getContent());

        //判断是否在可选的placeholder中
        if(!validatePlaceholder(template.getType(), productLineName, placeholderList)){
            return false;
        }
        if(!validateIteratePlaceholder(template.getType(), productLineName, iteratePlaceholderList)){
            return false;
        }
        if(!validateHistoryPlaceholder(template.getType(), productLineName, historyPlaceholderList)){
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
        if (productLineName.contains(Config.PRODUCT_LINE_NAME_KE)){
            placeholderAllList = Config.KE_PLACEHODLER_MAP.get(type);
        }else if(productLineName.contains(Config.PRODUCT_LINE_NAME_KC)){
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

    private static Boolean validateIteratePlaceholder(String type, String productLineName, List<String> placeholderList){
        validateProductLineName(productLineName);

        if(productLineName.isEmpty()){
            throw new BussinessException("产品线为空");
        }

        List<String> placeholderAllList;
        if (productLineName.contains(Config.PRODUCT_LINE_NAME_KE)){
            placeholderAllList = Config.KE_ITERATE_PLACEHODLER_MAP.get(type);
        }else if(productLineName.contains(Config.PRODUCT_LINE_NAME_KC)){
            placeholderAllList = Config.KC_ITERATE_PLACEHODLER_MAP.get(type);
        }else{
            throw new BussinessException("产品线名称错误！");
        }


        for(String placeholder : placeholderList){
            if(placeholder.startsWith(Config.ITERATE_PLACEHOLDER_ISSUE_PREFIX)){
                //先简单判断是否含有2个-，未来再判断项目和类型的枚举值
                String[] itemArray = placeholder.split("-");
                if(itemArray.length < 3){
                    return false;
                }
            }else{
                if(!placeholderAllList.contains(placeholder)){
                    return false;
                }
            }
        }
        return true;
    }

    private static Boolean validateHistoryPlaceholder(String type, String productLineName, List<String> placeholderList){
        validateProductLineName(productLineName);

        if(productLineName.isEmpty()){
            throw new BussinessException("产品线为空");
        }

        List<String> placeholderAllList;
        if (productLineName.contains(Config.PRODUCT_LINE_NAME_KE)){
            placeholderAllList = Config.KE_HISTORY_PLACEHODLER_MAP.get(type);
        }else if(productLineName.contains(Config.PRODUCT_LINE_NAME_KC)){
            placeholderAllList = Config.KC_HISTORY_PLACEHODLER_MAP.get(type);
        }else{
            throw new BussinessException("产品线名称错误！");
        }

        if(placeholderAllList.containsAll(placeholderList)){
            return true;
        }else{
            return false;
        }
    }

    private static List<String> getAllNormalPlaceholder(String type, String productLineName){
        validateProductLineName(productLineName);

        if(productLineName.isEmpty()){
            throw new BussinessException("产品线为空");
        }
        List<String> placeholderAllList;
        if (productLineName.contains(Config.PRODUCT_LINE_NAME_KE)){
            placeholderAllList = Config.KE_PLACEHODLER_MAP.get(type);
        }else if(productLineName.contains(Config.PRODUCT_LINE_NAME_KC)){
            placeholderAllList = Config.KC_PLACEHODLER_MAP.get(type);
        }else{
            throw new BussinessException("产品线名称错误！");
        }
        return placeholderAllList;
    }

    private static List<String> getAllIteratePlaceholder(String type, String productLineName){
        validateProductLineName(productLineName);

        if(productLineName.isEmpty()){
            throw new BussinessException("产品线为空");
        }

        List<String> placeholderAllList;
        if (productLineName.contains(Config.PRODUCT_LINE_NAME_KE)){
            placeholderAllList = Config.KE_ITERATE_PLACEHODLER_MAP.get(type);
        }else if(productLineName.contains(Config.PRODUCT_LINE_NAME_KC)){
            placeholderAllList = Config.KC_ITERATE_PLACEHODLER_MAP.get(type);
        }else{
            throw new BussinessException("产品线名称错误！");
        }

        return placeholderAllList;
    }

    private static List<String> getAllHistoryPlaceholder(String type, String productLineName){
        validateProductLineName(productLineName);

        if(productLineName.isEmpty()){
            throw new BussinessException("产品线为空");
        }

        List<String> placeholderAllList;
        if (productLineName.contains(Config.PRODUCT_LINE_NAME_KE)){
            placeholderAllList = Config.KE_HISTORY_PLACEHODLER_MAP.get(type);
        }else if(productLineName.contains(Config.PRODUCT_LINE_NAME_KC)){
            placeholderAllList = Config.KC_HISTORY_PLACEHODLER_MAP.get(type);
        }else{
            throw new BussinessException("产品线名称错误！");
        }

        return placeholderAllList;
    }


    private static Boolean validateProductLineName(String productLineName){
        if(productLineName == null || productLineName.equals("")){
            throw new BussinessException("产品线名称为空");
        }
        if(productLineName.contains(Config.PRODUCT_LINE_NAME_KE)||productLineName.contains(Config.PRODUCT_LINE_NAME_KC)){
            //pass
        }
        else{
            throw new BussinessException("产品线名称错误！");
        }

        return true;
    }

    public static String findInvalidatePlaceholder(Template template){
        String productLineName = template.getProductLineName();
        List<String> placeholderStrList;

        if(productLineName.isEmpty()){
            throw new BussinessException("产品线为空");
        }
        /*
        if(productLineName.contains(Config.PRODUCT_LINE_NAME_KE)){
            placeholderStrList = Config.KE_PLACEHODLER_MAP.get(template.getType());
        }else if(productLineName.contains(Config.PRODUCT_LINE_NAME_KC)){
            placeholderStrList = Config.KC_PLACEHODLER_MAP.get(template.getType());
        }else{
            throw new BussinessException("模板产品线名错误");
        }
        */
        //抓取所有placeholder
        List<String> placeholderList = getAllPlaceholder(template.getContent());
        List<String> iteratePlaceholderList = getAllIteratePlaceholder(template.getContent());
        List<String> historyPlaceholderList = getAllHistoryPlaceholder(template.getContent());

        List<String> allNormalPlaceholderList = getAllNormalPlaceholder(template.getType(), template.getProductLineName());
        List<String> allIteratePlaceholderList = getAllIteratePlaceholder(template.getType(), template.getProductLineName());
        List<String> allHistoryPlaceholderList = getAllHistoryPlaceholder(template.getType(), template.getProductLineName());

        List<String> invalidateNormalPlaceholderList = notInList(placeholderList,allNormalPlaceholderList);
        List<String> invalidateIteratePlaceholderList = notInList(iteratePlaceholderList,allIteratePlaceholderList);
        List<String> invalidateHistoryPlaceholderList = notInList(historyPlaceholderList,allHistoryPlaceholderList);

        String result = listToString(invalidateNormalPlaceholderList)+listToString(invalidateIteratePlaceholderList)+listToString(invalidateHistoryPlaceholderList);
        return result.substring(0, result.length() - 1);
    }

    private static List<String> notInList(List<String> placeholderList, List<String> allPlaceholderList){
        List<String> resultList = new ArrayList<>();
        for(String str : placeholderList){
            if(!allPlaceholderList.contains(str)){
                resultList.add(str);
            }
        }

        return resultList;
    }

    private static String listToString(List<String> list){
        if(list == null || list.size()==0) return "";

        StringBuilder stringBuilder = new StringBuilder();
        for(String str : list){
            stringBuilder.append(str);
            stringBuilder.append(",");
        }
        String str = stringBuilder.toString();
        return str;
    }
}
