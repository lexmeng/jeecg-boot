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
    public static String IssuePublishFilterString = "（不写发布声明）";

    public static String IssueEnAndChSeparatorInKE = "/";

    public static String IssueEnAndChSeparatorInKC = "||";

    public static String IssueTypeStory = "story";//同jira系统保持一致

    public static String IssueTypeBug = "bug";//同jira系统保持一致

    public static String ReleaseInfoTypeReleaseNote = "ReleaseNote";
    public static String ReleaseInfoTypeReleaseMail = "ReleaseMail";
    public static String ReleaseInfoTypeHandBookPR = "HandBookPR";
    public static String ReleaseInfoTypeProductPackagePR = "ProductPackagePR";
    public static String ReleaseInfoTypeCompanyWebsite = "CompanyWebsite";

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

        releaseNotePlaceholderList.add("Iterate(issue)");//循环占位符
        releaseNotePlaceholderList.add("Iterate(issue-story)");//循环占位符
        releaseNotePlaceholderList.add("Iterate(issue-bug)");//循环占位符

        releaseNoteIteratePlacerholderList.add("${issueName}");
        releaseNoteIteratePlacerholderList.add("${issueChName}");
        releaseNoteIteratePlacerholderList.add("${issueEnName}");

        KE_PLACEHODLER_MAP.put(ReleaseInfoTypeReleaseNote, releaseNotePlaceholderList);
        KE_ITERATE_PLACEHODLER_MAP.put(ReleaseInfoTypeReleaseNote, releaseNoteIteratePlacerholderList);

        List<String> releaseMailPlaceholderList = new ArrayList<>();


    }

    public static Map<String, List<String>> KC_PLACEHODLER_MAP= new HashMap<>();


}
