package org.jeecg.modules.publishlist.bpservice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.publishlist.config.Config;
import org.jeecg.modules.publishlist.domainservice.IIssueDomainService;
import org.jeecg.modules.publishlist.domainservice.IPublishlistDomainService;
import org.jeecg.modules.publishlist.domainservice.IReleaseInfoDomainService;
import org.jeecg.modules.publishlist.entity.Issue;
import org.jeecg.modules.publishlist.entity.Publishlist;
import org.jeecg.modules.publishlist.entity.ReleaseInfo;
import org.jeecg.modules.publishlist.entity.Template;
import org.jeecg.modules.publishlist.service.IIssueService;
import org.jeecg.modules.publishlist.service.IPublishlistService;
import org.jeecg.modules.publishlist.service.IReleaseInfoService;
import org.jeecg.modules.publishlist.service.ITemplateService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReleaseInfoBPService {

    @Autowired
    private IReleaseInfoDomainService releaseInfoDomainService;

    @Autowired
    private IReleaseInfoService releaseInfoService;

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


    public String replaceIteratePlaceholder(String content,List<Issue> issueList){
        Pattern iteratePattern = Pattern.compile("Iterate\\(.+?\\)");
        Matcher matcher = iteratePattern.matcher(content);

        while(matcher.find()){
            String group = matcher.group(0);
            Integer index = matcher.end();
            if (group.equals("Iterate(issue)")){
                String issueContent = getNextBracketAfterIteratePlaceholder(content, index);
                String resultContent= replacePlaceholderInIterate(issueList, issueContent);
                content = content.replace("Iterate(issue)"+issueContent, resultContent);

            }else if(group.equals("Iterate(issue-story)")){
                List<Issue> storyIssueList = new ArrayList<>();
                for(Issue issue: issueList){
                    if(issue.getIssueType().equals(Config.IssueTypeStory)){
                        storyIssueList.add(issue);
                    }
                }
                String issueContent = getNextBracketAfterIteratePlaceholder(content, index);
                String resultContent= replacePlaceholderInIterate(storyIssueList, issueContent);
                content = content.replace("Iterate(issue-story)"+issueContent, resultContent);
            }else if(group.equals("Iterate(issue-bug)")){
                List<Issue> bugIssueList = new ArrayList<>();
                for(Issue issue: issueList){
                    if(issue.getIssueType().equals(Config.IssueTypeBug)){
                        bugIssueList.add(issue);
                    }
                }

                String issueContent = getNextBracketAfterIteratePlaceholder(content, index);
                String resultContent= replacePlaceholderInIterate(bugIssueList, issueContent);
                content = content.replace("Iterate(issue-bug)"+issueContent, resultContent);
            }else{
                throw new RuntimeException("循环占位符错误！");
            }
            matcher = iteratePattern.matcher(content);
        }
        return content;
    }

    private String replacePlaceholderInIterate(List<Issue> issueList, String issueContent){
        String resultContent="";
        List<String> strList = new ArrayList<>();

        for(Issue issue: issueList){
            ReleaseInfo releaseInfo = releaseInfoService.getById(issue.getId());

            Pattern issuePattern = Pattern.compile("\\$\\{.+?\\}");
            Matcher issueMatcher = issuePattern.matcher(issueContent);
            String tempIssueContent = issueContent;

            while(issueMatcher.find()){
                //issueMatcher.replaceFirst();
                String issueGroup = issueMatcher.group(0);
                tempIssueContent = issueMatcher.replaceFirst(getIssuePlaceholderContent(issueGroup, issue, releaseInfo));
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

        throw new RuntimeException("循环占位符不匹配！");
    }

    private String getIssuePlaceholderContent(String placeholder, Issue issue, ReleaseInfo releaseInfo){
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
            result = releaseInfo.getIssueEnName();
        }else if(placeholder.equals("${issueChName}")){
            result = releaseInfo.getIssueChName();
        }else{
            throw new RuntimeException("issue占位符错误！");
        }

        return result.toString();
    }

    private String generateTemplateContent(String publishlistId, String templateType, Map<String, String> placeholderContentMap) throws RuntimeException{
        //一、判断状态是否是已发布
        if(!publishlistDomainService.isPublished(publishlistId)){
            throw new RuntimeException("发布单状态还未发布！发布单id："+publishlistId);
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
            throw new RuntimeException("模板数量错误！数量："+templateList.size());
        }
        Template template = templateList.get(0);

        //三、校验所有占位符
        if(!releaseInfoDomainService.verifyPlaceholder(template)){
            throw new RuntimeException("有非法占位符！");
        }

        //四、读取模板内容
        String content = template.getContent();

        //五、根据前端内容和发布单字段值，替代相应占位符
        placeholderContentMap.put("${pmName}", publishlist.getPmName());
        placeholderContentMap.put("${productName}", publishlist.getProductName());
        placeholderContentMap.put("${versionName}", publishlist.getVersionName());
        placeholderContentMap.put("${versionType}", publishlist.getVersionType());

        content = releaseInfoDomainService.replacePlaceholder(content, placeholderContentMap);

        //六、替代循环占位符，包括issue相关和releaseInfo相关
        List<Issue> issueList = issueDomainService.getIssueListForRelease(publishlistId);
        content = replaceIteratePlaceholder(content,issueList);

        return content;
    }
    /**
     *
     * 生成ReleaseNote内容
     * @return
     */
    public String generateReleaseNoteContent(String publishlistId, Map<String, String> placeholderContentMap) throws RuntimeException{
        return generateTemplateContent(publishlistId, Config.ReleaseInfoTypeReleaseNote, placeholderContentMap);
    }

    /**
     * 生成发布邮件的内容
     */
    public String generateReleaseMailContent(String publishlistId, Map<String, String> placeholderContentMap) throws RuntimeException{
        return generateTemplateContent(publishlistId, Config.ReleaseInfoTypeReleaseMail, placeholderContentMap);
    }

    /**
     * 生成产品手册PR内容
     * @return
     */
    public String generateProductHandbookPRContent(String publishlistId, Map<String, String> placeholderContentMap) throws RuntimeException{
        return generateTemplateContent(publishlistId, Config.ReleaseInfoTypeHandBookPR, placeholderContentMap);

    }

    /**
     * 生成产品包PR内容
     * @return
     */
    public String generateProductPackagePRContent(String publishlistId, Map<String, String> placeholderContentMap) throws RuntimeException{
        return generateTemplateContent(publishlistId, Config.ReleaseInfoTypeProductPackagePR, placeholderContentMap);
    }

    /**
     * 生成官网修改内容
     * @return
     */
    public String generateCompanyWebsiteContent(String publishlistId, Map<String, String> placeholderContentMap) throws RuntimeException{
        return generateTemplateContent(publishlistId, Config.ReleaseInfoTypeCompanyWebsite, placeholderContentMap);
    }
}
