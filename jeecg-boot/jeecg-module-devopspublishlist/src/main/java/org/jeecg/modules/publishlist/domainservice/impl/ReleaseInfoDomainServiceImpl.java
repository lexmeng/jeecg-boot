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
public class ReleaseInfoDomainServiceImpl{

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
        //String content1 = new ReleaseInfoDomainServiceImpl().replacePlaceholder(content, map);
        //System.out.println(content1);
    }



}
