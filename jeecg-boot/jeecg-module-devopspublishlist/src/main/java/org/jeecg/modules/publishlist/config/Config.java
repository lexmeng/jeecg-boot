package org.jeecg.modules.publishlist.config;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Config {
    public final static String ISSUE_PUBLISH_FILTER_STRING = "（不写发布声明）";

    public final static String ISSUE_EN_AND_CH_SEPARATOR_IN_KE = "/";

    public final static String ISSUE_EN_AND_CH_SEPARATOR_IN_KC = "||";

    public final static String ISSUE_TYPE_STORY = "story";//同jira系统保持一致


    public final static String ISSUE_TYPE_BUG = "bug";//同jira系统保持一致
    public final static String ISSUE_TYPE_BUG_CN = "缺陷";

    public final static String ITERATE_PLACEHOLDER_PREFIX = "$$Iterate";

    public final static String ITERATE_PLACEHOLDER_ISSUE = "$$Iterate(issue)";

    public final static String ITERATE_PLACEHOLDER_ISSUE_BUG = "$$Iterate(issue-bug)";

    public final static String ITERATE_PLACEHOLDER_ISSUE_STORY = "$$Iterate(issue-story)";

    public final static String HISTORY_PLACEHOLDER_PREFIX = "$$History";

    public final static String HISTORY_PLACEHOLDER_DOCUMENT_VERSION = "$$History(document-version)";
    public final static String RELEASE_INFO_TYPE_RELEASE_NOTE = "ReleaseNote";
    public final static String RELEASE_INFO_TYPE_RELEASE_MAIL = "ReleaseMail";
    public final static String RELEASE_INFO_TYPE_HANDBOOK_PR = "HandBookPRContent";
    public final static String RELEASE_INFO_TYPE_PRODUCT_PACKAGE_PR = "ProductPackagePRContent";
    public final static String RELEASE_INFO_TYPE_COMPANY_WEBSITE = "CompanyWebsite";

    public static Map<String, List<String>> KE_PLACEHODLER_MAP= new HashMap<>();
    public static Map<String, List<String>> KE_ITERATE_PLACEHODLER_MAP = new HashMap<>();
    @PostConstruct
    public void initPlaceholderMap(){
        List<String> releaseNotePlaceholderList = new ArrayList<>();
        List<String> releaseNoteIteratePlacerholderList = new ArrayList<>();

        releaseNotePlaceholderList.add("${pmName}");//from publishlist字段
        releaseNotePlaceholderList.add("${commitId}");//from 前端输入
        releaseNotePlaceholderList.add("${productName}");//from publishlist字段，例如：Kyligence Enterprise
        releaseNotePlaceholderList.add("${versionName}");//from publishlist字段，例如：4.6.7.0
        releaseNotePlaceholderList.add("${versionType}");//from publishlist字段，例如：GA

        releaseNotePlaceholderList.add("${issueNum}");//from issue字段，例如：AL-7095
        releaseNotePlaceholderList.add("${issueEnName}");//from releaseInfo字段
        releaseNotePlaceholderList.add("${issueChName}");//from releaseInfo字段

        releaseNotePlaceholderList.add("${productBehaviorChangeUrl}");//from 前端输入

        releaseNotePlaceholderList.add(ITERATE_PLACEHOLDER_ISSUE);//循环占位符
        releaseNotePlaceholderList.add(ITERATE_PLACEHOLDER_ISSUE_STORY);//循环占位符
        releaseNotePlaceholderList.add(ITERATE_PLACEHOLDER_ISSUE_BUG);//循环占位符

        releaseNoteIteratePlacerholderList.add("${issueName}");
        releaseNoteIteratePlacerholderList.add("${issueChName}");
        releaseNoteIteratePlacerholderList.add("${issueEnName}");

        KE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_RELEASE_NOTE, releaseNotePlaceholderList);
        KE_ITERATE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_RELEASE_NOTE, releaseNoteIteratePlacerholderList);

        List<String> releaseMailPlaceholderList = new ArrayList<>();


    }

    public static Map<String, List<String>> KC_PLACEHODLER_MAP= new HashMap<>();


}
