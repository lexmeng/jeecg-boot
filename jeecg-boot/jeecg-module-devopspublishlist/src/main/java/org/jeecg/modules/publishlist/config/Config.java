package org.jeecg.modules.publishlist.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableAspectJAutoProxy
public class Config {
    public final static String ISSUE_PUBLISH_FILTER_STRING = "（不写发布声明）";

    public final static String ISSUE_EN_AND_CH_SEPARATOR_IN_KE = "||";

    public final static String ISSUE_EN_AND_CH_SEPARATOR_IN_KC = "||";

    public final static String ISSUE_TYPE_STORY = "story";//同jira系统保持一致


    public final static String ISSUE_TYPE_BUG = "bug";//同jira系统保持一致
    public final static String ISSUE_TYPE_BUG_CN = "缺陷";

    public final static String ITERATE_PLACEHOLDER_PREFIX = "$$Iterate";

    public final static String ITERATE_PLACEHOLDER_ISSUE = "$$Iterate(issue)";

    public final static String ITERATE_PLACEHOLDER_ISSUE_BUG = "$$Iterate(issue-bug)";

    public final static String ITERATE_PLACEHOLDER_ISSUE_STORY = "$$Iterate(issue-story)";

    public final static String ITERATE_PLACEHOLDER_DEPENDENT_COMPONENT = "$$Iterate(dependent-component)";

    public final static String ITERATE_PLACEHOLDER_PACKAGE_URL = "$$Iterate(package-url)";

    public final static String HISTORY_PLACEHOLDER_PREFIX = "$$History";

    public final static String HISTORY_PLACEHOLDER_DOCUMENT_VERSION = "$$History(document-version)";
    public final static String RELEASE_INFO_TYPE_RELEASE_NOTE = "ReleaseNote";
    public final static String RELEASE_INFO_TYPE_RELEASE_MAIL = "ReleaseMail";
    public final static String RELEASE_INFO_TYPE_HANDBOOK_PR_EN = "HandBookPREnContent";
    public final static String RELEASE_INFO_TYPE_HANDBOOK_PR_CH = "HandBookPRChContent";
    public final static String RELEASE_INFO_TYPE_PRODUCT_PACKAGE_PR = "ProductPackagePRContent";
    public final static String RELEASE_INFO_TYPE_COMPANY_WEBSITE = "CompanyWebsite";

    public static Map<String, List<String>> KE_PLACEHODLER_MAP= new HashMap<>();
    public static Map<String, List<String>> KE_ITERATE_PLACEHODLER_MAP = new HashMap<>();

    public static Map<String, List<String>> KE_HISTORY_PLACEHODLER_MAP = new HashMap<>();

    public static Map<String, List<String>> KC_PLACEHODLER_MAP= new HashMap<>();

    public static Map<String, List<String>> KC_ITERATE_PLACEHODLER_MAP = new HashMap<>();

    public static Map<String, List<String>> KC_HISTORY_PLACEHODLER_MAP = new HashMap<>();
    public static List<String> PRODUCT_LINE_NAME_LIST = new ArrayList<>();

    public final static String PRODUCT_LINE_NAME_KE = "Kyligence Enterprise";

    public final static String PRODUCT_LINE_NAME_KC = "Kyligence Cloud";


    @PostConstruct
    public void initPlaceholderMap(){
        PRODUCT_LINE_NAME_LIST.add(PRODUCT_LINE_NAME_KE);
        PRODUCT_LINE_NAME_LIST.add(PRODUCT_LINE_NAME_KC);

        List<String> placeholderList = new ArrayList<>();
        List<String> iteratePlacerholderList = new ArrayList<>();
        List<String> historyPlaceholderList = new ArrayList<>();


        placeholderList.add("${productLineName}");//from publishlist字段，例如：Kyligence Enterprise
        placeholderList.add("${productName}");
        placeholderList.add("${versionName}");//from publishlist字段，例如：4.6.7.0
        placeholderList.add("${versionType}");//from publishlist字段，例如：GA

        placeholderList.add("${jiraVersionName}");
        placeholderList.add("${scrumNum}");
        placeholderList.add("${scrumStage}");

        placeholderList.add("${pmId}");
        placeholderList.add("${pmName}");

        placeholderList.add("${documentVersion}");
        placeholderList.add("${commitId}");//
        placeholderList.add("${documentUrlId}");//

        placeholderList.add("${userManualChLink}");
        placeholderList.add("${userManualEnLink}");

        //placeholderList.add("${productBehaviorChangeUrl}");//

        iteratePlacerholderList.add(ITERATE_PLACEHOLDER_ISSUE);//循环占位符
        iteratePlacerholderList.add(ITERATE_PLACEHOLDER_ISSUE_STORY);//循环占位符
        iteratePlacerholderList.add(ITERATE_PLACEHOLDER_ISSUE_BUG);//循环占位符
        iteratePlacerholderList.add(ITERATE_PLACEHOLDER_DEPENDENT_COMPONENT);
        iteratePlacerholderList.add(ITERATE_PLACEHOLDER_PACKAGE_URL);

        historyPlaceholderList.add(HISTORY_PLACEHOLDER_DOCUMENT_VERSION);

        placeholderList.add("${issueNum}");//from issue字段，例如：AL-7095
        placeholderList.add("${issueName}");
        placeholderList.add("${issueChName}");//from releaseInfo字段
        placeholderList.add("${issueEnName}");//from releaseInfo字段
        placeholderList.add("${issueType}");
        placeholderList.add("${issueLink}");

        placeholderList.add("${componentCnName}");
        placeholderList.add("${componentEnName}");

        placeholderList.add("${storageType}");
        placeholderList.add("${packageUrl}");

        //简化一下，不再每个类型单独设置占位符检查了
        KE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_RELEASE_NOTE, placeholderList);
        KE_ITERATE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_RELEASE_NOTE, iteratePlacerholderList);
        KE_HISTORY_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_RELEASE_NOTE, historyPlaceholderList);

        KE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_RELEASE_MAIL, placeholderList);
        KE_ITERATE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_RELEASE_MAIL, iteratePlacerholderList);
        KE_HISTORY_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_RELEASE_MAIL, historyPlaceholderList);

        KE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_HANDBOOK_PR_EN, placeholderList);
        KE_ITERATE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_HANDBOOK_PR_EN, iteratePlacerholderList);
        KE_HISTORY_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_HANDBOOK_PR_EN, historyPlaceholderList);

        KE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_HANDBOOK_PR_CH, placeholderList);
        KE_ITERATE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_HANDBOOK_PR_CH, iteratePlacerholderList);
        KE_HISTORY_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_HANDBOOK_PR_CH, historyPlaceholderList);

        KE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_PRODUCT_PACKAGE_PR, placeholderList);
        KE_ITERATE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_PRODUCT_PACKAGE_PR, iteratePlacerholderList);
        KE_HISTORY_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_PRODUCT_PACKAGE_PR, historyPlaceholderList);

        KE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_COMPANY_WEBSITE, placeholderList);
        KE_ITERATE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_COMPANY_WEBSITE, iteratePlacerholderList);
        KE_HISTORY_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_COMPANY_WEBSITE, historyPlaceholderList);

        //简化一下，KE和KC以及别的产品线不再单独设置占位符检查了
        KC_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_RELEASE_NOTE, placeholderList);
        KC_ITERATE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_RELEASE_NOTE, iteratePlacerholderList);
        KC_HISTORY_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_RELEASE_NOTE, historyPlaceholderList);

        KC_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_RELEASE_MAIL, placeholderList);
        KC_ITERATE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_RELEASE_MAIL, iteratePlacerholderList);
        KC_HISTORY_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_RELEASE_MAIL, historyPlaceholderList);

        KC_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_HANDBOOK_PR_EN, placeholderList);
        KC_ITERATE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_HANDBOOK_PR_EN, iteratePlacerholderList);
        KC_HISTORY_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_HANDBOOK_PR_EN, historyPlaceholderList);

        KC_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_HANDBOOK_PR_CH, placeholderList);
        KC_ITERATE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_HANDBOOK_PR_CH, iteratePlacerholderList);
        KC_HISTORY_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_HANDBOOK_PR_CH, historyPlaceholderList);

        KC_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_PRODUCT_PACKAGE_PR, placeholderList);
        KC_ITERATE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_PRODUCT_PACKAGE_PR, iteratePlacerholderList);
        KC_HISTORY_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_PRODUCT_PACKAGE_PR, historyPlaceholderList);

        KC_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_COMPANY_WEBSITE, placeholderList);
        KC_ITERATE_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_COMPANY_WEBSITE, iteratePlacerholderList);
        KC_HISTORY_PLACEHODLER_MAP.put(RELEASE_INFO_TYPE_COMPANY_WEBSITE, historyPlaceholderList);


    }




}
