package org.jeecg.modules.publishlist.bpservice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections4.map.LinkedMap;
import org.jeecg.modules.publishlist.config.Config;
import org.jeecg.modules.publishlist.domainservice.IIssueDomainService;
import org.jeecg.modules.publishlist.domainservice.IPublishlistDomainService;
import org.jeecg.modules.publishlist.domainservice.IReleaseInfoDomainService;
import org.jeecg.modules.publishlist.entity.*;
import org.jeecg.modules.publishlist.exception.BussinessException;
import org.jeecg.modules.publishlist.logic.ReleaseInfoLogic;
import org.jeecg.modules.publishlist.logic.VarifyLogic;
import org.jeecg.modules.publishlist.service.*;
import org.jeecg.modules.publishlist.vo.PublishlistQueryResult;
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


    private String generateTemplateContent(String publishlistId, String templateType, Map<String, String> placeholderContentMap) throws BussinessException{
        //一、判断状态是否是已发布
        /*
        if(!publishlistDomainService.isPublished(publishlistId)){
            throw new BussinessException("发布单状态还未发布！发布单id："+publishlistId);
        }
        */

        //二、拿到Template
        PublishlistQueryResult publishlistQueryResult = publishlistService.queryByMainId(publishlistId);
        Publishlist publishlist = publishlistQueryResult.getPublishlist();

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
        if(!VarifyLogic.verifyPlaceholder(template)){
            throw new BussinessException("有非法占位符！");
        }

        //四、读取模板内容
        String content = template.getContent();

        //五、先替换history占位符中的内容
        QueryWrapper<Publishlist> wrapper = new QueryWrapper<>();
        wrapper.eq("document_version", publishlist.getDocumentVersion()).orderByDesc("document_version");
        List<PublishlistQueryResult> publishlistsQueryResultList= publishlistService.ListByMainWrapper(wrapper);

        List<Issue> issueList = issueBPService.getIssueListForRelease(publishlistId);

        LinkedMap<String, PublishlistQueryResult> historyVersionPublishlistQueryResult = new LinkedMap<>();
        LinkedMap<String, List<Issue>> historyVersionIssueList = new LinkedMap<>();
        for(PublishlistQueryResult historyPublishlistQueryResult : publishlistsQueryResultList){
            Publishlist historyPublishlist = historyPublishlistQueryResult.getPublishlist();
            historyVersionPublishlistQueryResult.put(historyPublishlist.getVersionName(), historyPublishlistQueryResult);

            List<Issue> tempIssueList = issueBPService.getIssueListForRelease(historyPublishlist.getId());
            historyVersionIssueList.put(historyPublishlist.getVersionName(), tempIssueList);

        }

        content = ReleaseInfoLogic.replaceHistoryIteratePlaceholder(content, issueList, historyVersionPublishlistQueryResult, historyVersionIssueList);

        //六、替代历史占位符和循环占位符，包括issue相关和releaseInfo相关
        content = ReleaseInfoLogic.replaceIteratePlaceholder(content,issueList, publishlistQueryResult.getDependentComponentList(), publishlistQueryResult.getPackageUrlList());

        //七、根据前端内容和发布单字段值，替代相应占位符
        /*
        placeholderContentMap.put("${pmName}", publishlist.getPmName());
        placeholderContentMap.put("${productName}", publishlist.getProductName());
        placeholderContentMap.put("${versionName}", publishlist.getVersionName());
        placeholderContentMap.put("${versionType}", publishlist.getVersionType());

        content = ReleaseInfoLogic.replacePlaceholder(content, placeholderContentMap);
        */
        content = ReleaseInfoLogic.replacePublishlistPlaceholder(content, publishlist);

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
     * 生成产品手册PR EN内容
     * @return
     */
    public String generateProductHandbookPREnContent(String publishlistId, Map<String, String> placeholderContentMap) throws BussinessException{
        return generateTemplateContent(publishlistId, Config.RELEASE_INFO_TYPE_HANDBOOK_PR_EN, placeholderContentMap);

    }

    /**
     * 生成产品手册PR CH内容
     * @return
     */
    public String generateProductHandbookPRChContent(String publishlistId, Map<String, String> placeholderContentMap) throws BussinessException{
        return generateTemplateContent(publishlistId, Config.RELEASE_INFO_TYPE_HANDBOOK_PR_CH, placeholderContentMap);

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
