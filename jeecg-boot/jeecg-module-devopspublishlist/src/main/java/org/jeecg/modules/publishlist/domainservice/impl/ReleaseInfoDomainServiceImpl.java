package org.jeecg.modules.publishlist.domainservice.impl;

import org.jeecg.modules.publishlist.config.Config;
import org.jeecg.modules.publishlist.domainservice.IReleaseInfoDomainService;
import org.jeecg.modules.publishlist.entity.Issue;
import org.jeecg.modules.publishlist.entity.ReleaseInfo;
import org.jeecg.modules.publishlist.entity.Template;
import org.jeecg.modules.publishlist.exception.BussinessException;
import org.jeecg.modules.publishlist.service.ITemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReleaseInfoDomainServiceImpl implements IReleaseInfoDomainService{

    @Autowired
    private ITemplateService templateService;

    /*
    @Override
    public ReleaseInfo convertReleaseInfoFromIssue(Issue issue, String seperatorString){

        String[] splitStringList = issue.getIssueName().split(seperatorString);
        if(!issue.getIssueName().contains(seperatorString)){
            throw new BussinessException("issue名称不包含分隔符："+seperatorString+"。请修改！issue名称："+issue.getIssueName());
        }
        if(splitStringList[0] == null || splitStringList[0].equals("")){
            throw new BussinessException("issue分隔错误："+issue.getId());
        }
        if(splitStringList[1] == null || splitStringList[1].equals("")){
            throw new BussinessException("issue分隔错误："+issue.getId());
        }

        ReleaseInfo releaseInfo = new ReleaseInfo();
        releaseInfo.setId(issue.getId());
        releaseInfo.setIssueNum(issue.getIssueNum());
        releaseInfo.setIssueName(issue.getIssueName());
        releaseInfo.setIssueEnName(splitStringList[1]);
        releaseInfo.setIssueChName(splitStringList[0]);
        releaseInfo.setPublishlistId(issue.getPublishlistId());

        return releaseInfo;

    }
    */


    public String[] splitNameInfoFromIssue(Issue issue, String seperatorString){
        String[] splitStringList = issue.getIssueName().split(seperatorString);
        if(!issue.getIssueName().contains(seperatorString)){
            throw new BussinessException("issue名称不包含分隔符："+seperatorString+"。请修改！issue名称："+issue.getIssueName());
        }
        if(splitStringList[0] == null || splitStringList[0].equals("")){
            throw new BussinessException("issue分隔错误："+issue.getId());
        }
        if(splitStringList[1] == null || splitStringList[1].equals("")){
            throw new BussinessException("issue分隔错误："+issue.getId());
        }

        return splitStringList;
    }

    public void createTemplate(Template template){

        verifyTemplate(template);
    }

    private void verifyTemplate(Template template){
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

    public Boolean verifyPlaceholder(Template template){
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

    private List<String> getAllPlaceholder(String content){
        List<String> placeholderList = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\$\\{.+?\\}");
        Matcher matcher = pattern.matcher(content);

        while(matcher.find()){
            String group = matcher.group(0);
            placeholderList.add(group);
        }

        return placeholderList;
    }

    private List<String> getAllIteratePlaceholder(String content){
        List<String> iteratePlaceholderList = new ArrayList<>();

        Pattern iteratePattern = Pattern.compile("\\$\\$Iterate\\(.+?\\)");
        Matcher matcher = iteratePattern.matcher(content);
        while(matcher.find()){
            String group = matcher.group(0);
            iteratePlaceholderList.add(group);
        }
        return iteratePlaceholderList;
    }

    private Boolean validatePlaceholder(String type, String productLineName, List<String> placeholderList){
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

    private Boolean validateProductLineName(String productLineName){
        if(productLineName == null || productLineName.equals("")){
            throw new BussinessException("产品线名称为空");
        }
        if(productLineName.toUpperCase().contains("KE")||productLineName.toUpperCase().contains("KC")){
            //pass
        }
        else{
            throw new BussinessException("产品线名称错误！");
        }

        return true;
    }

    public String replacePlaceholder(String content, Map<String, String> placeholderMap){
        Pattern pattern = Pattern.compile("\\$\\{.+?\\}");
        Matcher matcher = pattern.matcher(content);

        while(matcher.find()){
            String group = matcher.group(0);
            if(!placeholderMap.keySet().contains(group)){
                continue;
            }
            content = matcher.replaceFirst(placeholderMap.get(group));
            //content = content.replace(group, placeholderMap.get(group));
            matcher = pattern.matcher(content);
        }

        return content;
    }



    /**
     * 判断是否需要生成releaseInfo
     * 废弃掉，通用逻辑移到logic包中
     * @param content
     * @param productLineName
     * @return
     */
    /*
    public Boolean isNeedToGenerateReleaseInfo(String content, String productLineName){
        if(content.contains(Config.ISSUE_PUBLISH_FILTER_STRING)){
            return false;
        }

        if(productLineName.toUpperCase().contains("KE")){
            if(!content.contains(Config.ISSUE_EN_AND_CH_SEPARATOR_IN_KE)){
                return false;
            }
        }

        return true;
    }
    */



    public static void main(String[] args){
        String content = "fdsafdsafds${abc}afdsf${bcd}dsfs${abc}dds\n\tfdsafd${cdf}sfdsfdsfdsafddsafdfdsfds";
        Map<String, String> map = new HashMap<String, String>();
        map.put("${abc}","第一占位符");
        map.put("${bcd}","第二占位符");
        map.put("${cdf}","第三占位符");
        String content1 = new ReleaseInfoDomainServiceImpl().replacePlaceholder(content, map);
        System.out.println(content1);
    }



}
