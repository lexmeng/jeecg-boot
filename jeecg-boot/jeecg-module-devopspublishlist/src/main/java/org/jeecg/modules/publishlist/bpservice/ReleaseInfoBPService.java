package org.jeecg.modules.publishlist.bpservice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections4.map.LinkedMap;
import org.jeecg.modules.publishlist.config.Config;
import org.jeecg.modules.publishlist.domainservice.IIssueDomainService;
import org.jeecg.modules.publishlist.domainservice.IPublishlistDomainService;
import org.jeecg.modules.publishlist.domainservice.IReleaseInfoDomainService;
import org.jeecg.modules.publishlist.entity.*;
import org.jeecg.modules.publishlist.exception.BussinessException;
import org.jeecg.modules.publishlist.service.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReleaseInfoBPService {

    @Autowired
    private IReleaseInfoDomainService releaseInfoDomainService;

    @Autowired
    private IPublishlistDomainService publishlistDomainService;

    @Autowired
    private IPublishlistService publishlistService;

    @Autowired
    private ITemplateService templateService;

    @Autowired
    private IIssueService issueService;

    @Autowired
    private IIssueDomainService issueDomainService;

    @Autowired
    private IPublishlistProjectService publishlistProjectService;

    @Autowired
    private IProjectService projectService;

    @Autowired
    private IssueBPService issueBPService;


    public String replaceHistoryIteratePlaceholder(String content, List<Issue> issueList, LinkedMap<String, Publishlist> historyVersionPublishlist, LinkedMap<String, List<Issue>> historyVersionIssueList){
        Pattern historyIteratePattern = Pattern.compile("\\$\\$History\\(.+?\\)");
        Matcher matcher = historyIteratePattern.matcher(content);

        while(matcher.find()){
            String group = matcher.group(0);
            Integer index = matcher.end();

            if(!group.equals(Config.HISTORY_PLACEHOLDER_DOCUMENT_VERSION)){
                throw new BussinessException("非法的占位符："+group);
            }

            String versionContent = getNextBracketAfterIteratePlaceholder(content, index);
            String resultContent= replacePlaceholderInHistory(historyVersionPublishlist, historyVersionIssueList, versionContent);

            content = content.replace(Config.HISTORY_PLACEHOLDER_DOCUMENT_VERSION+versionContent, resultContent);

            matcher = historyIteratePattern.matcher(content);
        }
        return content;
    }

    public String replacePublishlistPlaceholder(String content,Publishlist publishlist){
        Map<String, String> placeholderContentMap = new HashMap<>();
        placeholderContentMap.put("${pmName}", publishlist.getPmName());
        placeholderContentMap.put("${productName}", publishlist.getProductName());
        placeholderContentMap.put("${versionName}", publishlist.getVersionName());
        placeholderContentMap.put("${versionType}", publishlist.getVersionType());

        content = releaseInfoDomainService.replacePlaceholder(content, placeholderContentMap);
        return content;
    }

    public String replaceIteratePlaceholder(String content,List<Issue> issueList){
        Pattern iteratePattern = Pattern.compile("\\$\\$Iterate\\(.+?\\)");
        Matcher matcher = iteratePattern.matcher(content);

        while(matcher.find()){
            String group = matcher.group(0);
            Integer index = matcher.end();
            if (group.equals(Config.ITERATE_PLACEHOLDER_ISSUE)){
                String issueContent = getNextBracketAfterIteratePlaceholder(content, index);
                String resultContent= replacePlaceholderInIterate(issueList, issueContent);
                content = content.replace(Config.ITERATE_PLACEHOLDER_ISSUE+issueContent, resultContent);

            }else if(group.equals(Config.ITERATE_PLACEHOLDER_ISSUE_STORY)){
                List<Issue> storyIssueList = new ArrayList<>();
                for(Issue issue: issueList){
                    if(issue.getIssueType().equals(Config.ISSUE_TYPE_STORY)){
                        storyIssueList.add(issue);
                    }
                }
                String issueContent = getNextBracketAfterIteratePlaceholder(content, index);
                String resultContent= replacePlaceholderInIterate(storyIssueList, issueContent);
                content = content.replace(Config.ITERATE_PLACEHOLDER_ISSUE_STORY+issueContent, resultContent);
            }else if(group.equals(Config.ITERATE_PLACEHOLDER_ISSUE_BUG)){
                List<Issue> bugIssueList = new ArrayList<>();
                for(Issue issue: issueList){
                    if(Config.ISSUE_TYPE_BUG.equals(issue.getIssueType()) || Config.ISSUE_TYPE_BUG_CN.equals(issue.getIssueType()) ){
                        bugIssueList.add(issue);
                    }
                }

                String issueContent = getNextBracketAfterIteratePlaceholder(content, index);
                String resultContent= replacePlaceholderInIterate(bugIssueList, issueContent);
                content = content.replace(Config.ITERATE_PLACEHOLDER_ISSUE_BUG+issueContent, resultContent);
            }else{
                throw new BussinessException("循环占位符错误！");
            }
            matcher = iteratePattern.matcher(content);
        }
        return content;
    }

    private String replacePlaceholderInHistory(LinkedMap<String, Publishlist> historyVersionPublishlist, LinkedMap<String, List<Issue>> historyVersionIssueList, String historyContent){
        String resultContent="";
        List<String> strList = new ArrayList<>();

        for(String version : historyVersionPublishlist.keySet()){
            String tempContent;
            tempContent = replacePublishlistPlaceholder(historyContent, historyVersionPublishlist.get(version));
            tempContent = replaceIteratePlaceholder(tempContent, historyVersionIssueList.get(version));
            strList.add(tempContent);
        }

        for(String str : strList){
            resultContent = resultContent.concat(str.substring(1, str.length()-1));//去掉字符串前后的大括弧
            resultContent = resultContent.concat("\n\t");
        }
        return resultContent;
    }

    private String replacePlaceholderInIterate(List<Issue> issueList, String issueContent){
        String resultContent="";
        List<String> strList = new ArrayList<>();

        for(Issue issue: issueList){
            //ReleaseInfo releaseInfo = releaseInfoService.getById(issue.getId());

            Pattern issuePattern = Pattern.compile("\\$\\{.+?\\}");
            Matcher issueMatcher = issuePattern.matcher(issueContent);
            String tempIssueContent = issueContent;

            while(issueMatcher.find()){
                //issueMatcher.replaceFirst();
                String issueGroup = issueMatcher.group(0);
                tempIssueContent = issueMatcher.replaceFirst(getIssuePlaceholderContent(issueGroup, issue));
                //tempIssueContent = issueMatcher.replaceFirst(getIssuePlaceholderContent(issueGroup, issue, releaseInfo));
                issueMatcher = issuePattern.matcher(tempIssueContent);
            }
            strList.add(tempIssueContent);

        }

        for(String str : strList){
            resultContent = resultContent.concat(str.substring(1, str.length()-1));//去掉字符串前后的大括弧
            resultContent = resultContent.concat("\n\t");
        }
        return resultContent;
    }

    //得到循环占位符后的大括弧内的内容，用栈来判断括弧匹配
    private String getNextBracketAfterIteratePlaceholder(String content, Integer index){
        Integer oldIndex = index;
        List<String> stack = new ArrayList<>();

        while(index < content.length()){
            if(content.charAt(index) == '{'){
                //push to stack
                stack.add("{");
            }else if(content.charAt(index) == '}'){
                stack.remove(stack.size()-1);
                if(stack.isEmpty()){
                    String str = content.substring(oldIndex, index+1);
                    return str;
                }
            }else{

            }

            index++;
        }

        throw new BussinessException("循环占位符不匹配！");
    }

    private String getIssuePlaceholderContent(String placeholder, Issue issue){
        Object result;
        if(placeholder.equals("${issueId}")){
            result = issue.getId();
        }else if(placeholder.equals("${issueNum}")){
            result = issue.getIssueNum();
        }else if(placeholder.equals("${issueName}")){
            result = issue.getIssueName();
        }else if(placeholder.equals("${issueType}")){
            result = issue.getIssueType();
        }else if(placeholder.equals("${issueLink}")){
            result = issue.getIssueLink();
        }else if(placeholder.equals("${issueJiraVersionName}")){
            result = issue.getJiraVersionName();
        }else if(placeholder.equals("${issueEnName}")){
            result = issue.getIssueEnName();
        }else if(placeholder.equals("${issueChName}")){
            result = issue.getIssueChName();
        }else{
            throw new BussinessException("issue占位符错误！");
        }

        return result.toString();
    }

    private String generateTemplateContent(String publishlistId, String templateType, Map<String, String> placeholderContentMap) throws BussinessException{
        //一、判断状态是否是已发布
        if(!publishlistDomainService.isPublished(publishlistId)){
            throw new BussinessException("发布单状态还未发布！发布单id："+publishlistId);
        }

        //二、拿到Template
        Publishlist publishlist = publishlistService.getById(publishlistId);

        Map<String, Object> selectMap = new HashMap<>();
        selectMap.put("product_line_name",publishlist.getProductLineName());
        selectMap.put("product_name", publishlist.getProductName());
        selectMap.put("document_version", publishlist.getDocumentVersion());
        selectMap.put("type", templateType);
        List<Template> templateList = templateService.listByMap(selectMap);
        if(templateList.size() != 1){
            throw new BussinessException("模板数量错误！数量："+templateList.size());
        }
        Template template = templateList.get(0);

        //三、校验所有占位符
        if(!releaseInfoDomainService.verifyPlaceholder(template)){
            throw new BussinessException("有非法占位符！");
        }

        //四、读取模板内容
        String content = template.getContent();

        //五、先替换history占位符中的内容
        QueryWrapper<Publishlist> wrapper = new QueryWrapper<>();
        wrapper.eq("document_version", publishlist.getDocumentVersion()).orderByDesc("document_version");
        List<Publishlist> publishlistsList= publishlistService.list(wrapper);

        List<Issue> issueList = issueBPService.getIssueListForRelease(publishlistId);

        LinkedMap<String, Publishlist> historyVersionPublishlist = new LinkedMap<>();
        LinkedMap<String, List<Issue>> historyVersionIssueList = new LinkedMap<>();
        for(Publishlist historyPublishlist : publishlistsList){
            historyVersionPublishlist.put(historyPublishlist.getVersionName(), historyPublishlist);

            List<Issue> tempIssueList = issueBPService.getIssueListForRelease(historyPublishlist.getId());
            historyVersionIssueList.put(historyPublishlist.getVersionName(), tempIssueList);
        }

        content = replaceHistoryIteratePlaceholder(content, issueList, historyVersionPublishlist, historyVersionIssueList);

        //六、替代历史占位符和循环占位符，包括issue相关和releaseInfo相关
        content = replaceIteratePlaceholder(content,issueList);

        //七、根据前端内容和发布单字段值，替代相应占位符
        placeholderContentMap.put("${pmName}", publishlist.getPmName());
        placeholderContentMap.put("${productName}", publishlist.getProductName());
        placeholderContentMap.put("${versionName}", publishlist.getVersionName());
        placeholderContentMap.put("${versionType}", publishlist.getVersionType());

        content = releaseInfoDomainService.replacePlaceholder(content, placeholderContentMap);

        return content;
    }
    /**
     *
     * 生成ReleaseNote内容
     * @return
     */
    public String generateReleaseNoteContent(String publishlistId, Map<String, String> placeholderContentMap) throws BussinessException{
        return generateTemplateContent(publishlistId, Config.RELEASE_INFO_TYPE_RELEASE_NOTE, placeholderContentMap);
    }

    /**
     * 生成发布邮件的内容
     */
    public String generateReleaseMailContent(String publishlistId, Map<String, String> placeholderContentMap) throws BussinessException{
        return generateTemplateContent(publishlistId, Config.RELEASE_INFO_TYPE_RELEASE_MAIL, placeholderContentMap);
    }

    /**
     * 生成产品手册PR内容
     * @return
     */
    public String generateProductHandbookPRContent(String publishlistId, Map<String, String> placeholderContentMap) throws BussinessException{
        return generateTemplateContent(publishlistId, Config.RELEASE_INFO_TYPE_HANDBOOK_PR, placeholderContentMap);

    }

    /**
     * 生成产品包PR内容
     * @return
     */
    public String generateProductPackagePRContent(String publishlistId, Map<String, String> placeholderContentMap) throws BussinessException{
        return generateTemplateContent(publishlistId, Config.RELEASE_INFO_TYPE_PRODUCT_PACKAGE_PR, placeholderContentMap);
    }

    /**
     * 生成官网修改内容
     * @return
     */
    public String generateCompanyWebsiteContent(String publishlistId, Map<String, String> placeholderContentMap) throws BussinessException{
        return generateTemplateContent(publishlistId, Config.RELEASE_INFO_TYPE_COMPANY_WEBSITE, placeholderContentMap);
    }

    /**
     * 更新releaseInfo信息，调用时机：首次拉取issue后，更新issue后，发布单发布前更新issue后
     */
    /*
    @Transactional
    public void updateReleaseInfo(String publishlistId){
        Publishlist publishlist = publishlistService.getById(publishlistId);

        List<ReleaseInfo> releaseInfoList = new ArrayList<>();

        //1、拉取issue列表
        List<PublishlistProject> projectList = publishlistProjectService.selectByMainId(publishlist.getId());
        List<Issue> totalIssueList = new ArrayList<>();
        for(PublishlistProject publishlistProject : projectList){
            Project project = projectService.getById(publishlistProject.getProjectId());
            List<Issue> issueList = issueDomainService.getIssueListByProject(publishlistId, project.getName(), publishlist.getJiraVersionName());
            totalIssueList.addAll(issueList);
        }


        //2、删除之前生成的release info信息
        Map<String,Object> map = new HashMap<>();
        map.put("publishlist_id",publishlistId);
        releaseInfoService.removeByMap(map);


        //3、生成release info信息
        for(Issue issue: totalIssueList){
            //如果不需要生成发布信息，则跳过
            if(!releaseInfoDomainService.isNeedToGenerateReleaseInfo(issue.getIssueName(), publishlist.getProductLineName())){
                continue;
            }

            ReleaseInfo releaseInfo;
            if(publishlist.getProductLineName().toUpperCase().contains("KE")){
                releaseInfo = releaseInfoDomainService.convertReleaseInfoFromIssue(issue, Config.ISSUE_EN_AND_CH_SEPARATOR_IN_KE);
            }else if(publishlist.getProductLineName().toUpperCase().contains("KC")){
                releaseInfo = releaseInfoDomainService.convertReleaseInfoFromIssue(issue, Config.ISSUE_EN_AND_CH_SEPARATOR_IN_KC);
            }else{
                throw new BussinessException("产品线名称错误！");
            }

            releaseInfoList.add(releaseInfo);
        }
        releaseInfoService.saveBatch(releaseInfoList);
    }
    */



    /*
    @Transactional
    public void updateReleaseInfo(String publishlistId, List<Issue> totalIssueList){
        Publishlist publishlist = publishlistService.getById(publishlistId);

        List<ReleaseInfo> releaseInfoList = new ArrayList<>();

        //1、删除之前生成的release info信息
        Map<String,Object> map = new HashMap<>();
        map.put("publishlist_id",publishlistId);
        releaseInfoService.removeByMap(map);


        //2、生成release info信息
        for(Issue issue: totalIssueList){
            //如果不需要生成发布信息，则跳过
            if(!releaseInfoDomainService.isNeedToGenerateReleaseInfo(issue.getIssueName(), publishlist.getProductLineName())){
                continue;
            }

            ReleaseInfo releaseInfo;
            if(publishlist.getProductLineName().toUpperCase().contains("KE")){
                releaseInfo = releaseInfoDomainService.convertReleaseInfoFromIssue(issue, Config.ISSUE_EN_AND_CH_SEPARATOR_IN_KE);
            }else if(publishlist.getProductLineName().toUpperCase().contains("KC")){
                releaseInfo = releaseInfoDomainService.convertReleaseInfoFromIssue(issue, Config.ISSUE_EN_AND_CH_SEPARATOR_IN_KC);
            }else{
                throw new BussinessException("产品线名称错误！");
            }

            releaseInfoList.add(releaseInfo);
        }
        releaseInfoService.saveBatch(releaseInfoList);
    }
    */

}
