package org.jeecg.modules.publishlist.logic;

import org.apache.commons.collections4.map.LinkedMap;
import org.jeecg.modules.publishlist.config.Config;
import org.jeecg.modules.publishlist.entity.*;
import org.jeecg.modules.publishlist.exception.BussinessException;
import org.jeecg.modules.publishlist.vo.PublishlistQueryResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReleaseInfoLogic {

    /**
     * 是否需要生成issue info在文本中
     * 如果是kE产品线，不含分隔符也不需要生成
     * @param issueName
     * @param productLineName
     * @return
     */
    public static boolean isNeedToGenerateReleaseInfo(String issueName, String productLineName){
        if(issueName.contains(Config.ISSUE_PUBLISH_FILTER_STRING)){
            return false;
        }

        if(productLineName.toUpperCase().contains(Config.PRODUCT_LINE_NAME_KE)){
            if(!issueName.contains(Config.ISSUE_EN_AND_CH_SEPARATOR_IN_KE)){
                return false;
            }
        }

        return true;
    }

    public static Map<String, String> generatePublishlistPlaceholderMap(Publishlist publishlist){
        Map<String, String> placeholderContentMap = new HashMap<>();
        placeholderContentMap.put("${productLineName}", publishlist.getProductLineName());
        placeholderContentMap.put("${productName}", publishlist.getProductName());
        placeholderContentMap.put("${versionName}", publishlist.getVersionName());
        placeholderContentMap.put("${versionType}", publishlist.getVersionType());
        placeholderContentMap.put("${jiraVersionName}", publishlist.getJiraVersionName());
        placeholderContentMap.put("${scrumNum}", publishlist.getScrumNum());
        placeholderContentMap.put("${scrumStage}", publishlist.getScrumStage());

        placeholderContentMap.put("${pmId}", publishlist.getPmId());
        placeholderContentMap.put("${pmName}", publishlist.getPmName());
        placeholderContentMap.put("${documentVersion}", publishlist.getDocumentVersion());
        placeholderContentMap.put("${commitId}", publishlist.getCommitId());
        placeholderContentMap.put("${documentUrlId}", publishlist.getDocumentUrlId());
        placeholderContentMap.put("${userManualChLink}", publishlist.getUserManualChLink());
        placeholderContentMap.put("${userManualEnLink}", publishlist.getUserManualEnLink());

        return placeholderContentMap;
    }

    /**
     * 得到循环占位符后的大括弧内的内容，用栈来判断括弧匹配
     * @param content
     * @param index
     * @return
     */
    public static String getNextBracketAfterIteratePlaceholder(String content, Integer index){
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
/*

    public static String replacePlaceholderInIssueIterate(List<Issue> issueList, String issueContent) {
        String resultContent = "";
        List<String> strList = new ArrayList<>();

        for (Issue issue : issueList) {
            //ReleaseInfo releaseInfo = releaseInfoService.getById(issue.getId());

            Pattern issuePattern = Pattern.compile("\\$\\{.+?\\}");
            Matcher issueMatcher = issuePattern.matcher(issueContent);
            String tempIssueContent = issueContent;

            while (issueMatcher.find()) {
                //issueMatcher.replaceFirst();
                String issueGroup = issueMatcher.group(0);
                tempIssueContent = issueMatcher.replaceFirst(getIssuePlaceholderContent(issueGroup, issue));
                //tempIssueContent = issueMatcher.replaceFirst(getIssuePlaceholderContent(issueGroup, issue, releaseInfo));
                issueMatcher = issuePattern.matcher(tempIssueContent);
            }
            strList.add(tempIssueContent);

        }

        for (String str : strList) {
            resultContent = resultContent.concat(str.substring(1, str.length() - 1));//去掉字符串前后的大括弧
            resultContent = resultContent.concat("\n\t");
        }
        return resultContent;
    }
*/

/*

    public static String replacePlaceholderInDependentComponentIterate(List<DependentComponent> dependentComponentList, String dependentComponentContent) {
        String resultContent = "";
        List<String> strList = new ArrayList<>();

        for (DependentComponent dependentComponent : dependentComponentList) {

            Pattern pattern = Pattern.compile("\\$\\{.+?\\}");
            Matcher matcher = pattern.matcher(dependentComponentContent);
            String tempDependentComponentContent = dependentComponentContent;

            while (matcher.find()) {
                String group = matcher.group(0);
                tempDependentComponentContent = matcher.replaceFirst(getDependentComponentPlaceholderContent(group, dependentComponent));

                matcher = pattern.matcher(tempDependentComponentContent);
            }
            strList.add(tempDependentComponentContent);

        }

        for (String str : strList) {
            resultContent = resultContent.concat(str.substring(1, str.length() - 1));//去掉字符串前后的大括弧
            resultContent = resultContent.concat("\n\t");
        }
        return resultContent;
    }
*/

/*

    public static String replacePlaceholderInPackageUrlIterate(List<PackageUrl> packageUrlList, String packageUrlContent){
        String resultContent = "";
        List<String> strList = new ArrayList<>();

        for (PackageUrl packageUrl : packageUrlList) {

            Pattern pattern = Pattern.compile("\\$\\{.+?\\}");
            Matcher matcher = pattern.matcher(packageUrlContent);
            String tempPackageUrlContent = packageUrlContent;

            while (matcher.find()) {
                String group = matcher.group(0);
                tempPackageUrlContent = matcher.replaceFirst(getPackageUrlPlaceholderContent(group, packageUrl));

                matcher = pattern.matcher(tempPackageUrlContent);
            }
            strList.add(tempPackageUrlContent);

        }

        for (String str : strList) {
            resultContent = resultContent.concat(str.substring(1, str.length() - 1));//去掉字符串前后的大括弧
            resultContent = resultContent.concat("\n\t");
        }
        return resultContent;
    }
*/

    public static String replaceIteratePlaceholder(String content, List<Issue> issueList, List<DependentComponent> dependentComponentList, List<PackageUrl> packageUrlList){
        Pattern iteratePattern = Pattern.compile("\\$\\$Iterate\\(.+?\\)");
        Matcher matcher = iteratePattern.matcher(content);

        while(matcher.find()){
            String group = matcher.group(0);
            Integer index = matcher.end();
            if (group.equals(Config.ITERATE_PLACEHOLDER_ISSUE)){
                String issueContent = getNextBracketAfterIteratePlaceholder(content, index);

                IteratePlaceholderLogic<IssuePlaceholder, Issue> iteratePlaceholderLogic = new IteratePlaceholderLogic<>();
                String resultContent = iteratePlaceholderLogic.replacePlaceholderInIterate(new IssuePlaceholder(), issueList, issueContent);
                //String resultContent = replacePlaceholderInIssueIterate(issueList, issueContent)
                content = content.replace(Config.ITERATE_PLACEHOLDER_ISSUE+issueContent, resultContent);

            }else if(group.equals(Config.ITERATE_PLACEHOLDER_ISSUE_STORY)){
                List<Issue> storyIssueList = new ArrayList<>();
                for(Issue issue: issueList){
                    if(issue.getIssueType().equals(Config.ISSUE_TYPE_STORY)){
                        storyIssueList.add(issue);
                    }
                }
                String issueContent = getNextBracketAfterIteratePlaceholder(content, index);

                IteratePlaceholderLogic<IssuePlaceholder, Issue> iteratePlaceholderLogic = new IteratePlaceholderLogic<>();
                String resultContent = iteratePlaceholderLogic.replacePlaceholderInIterate(new IssuePlaceholder(), storyIssueList, issueContent);
                //String resultContent= replacePlaceholderInIssueIterate(storyIssueList, issueContent);
                content = content.replace(Config.ITERATE_PLACEHOLDER_ISSUE_STORY+issueContent, resultContent);
            }else if(group.equals(Config.ITERATE_PLACEHOLDER_ISSUE_BUG)){
                List<Issue> bugIssueList = new ArrayList<>();
                for(Issue issue: issueList){
                    if(Config.ISSUE_TYPE_BUG.equals(issue.getIssueType()) || Config.ISSUE_TYPE_BUG_CN.equals(issue.getIssueType()) ){
                        bugIssueList.add(issue);
                    }
                }

                String issueContent = getNextBracketAfterIteratePlaceholder(content, index);

                IteratePlaceholderLogic<IssuePlaceholder, Issue> iteratePlaceholderLogic = new IteratePlaceholderLogic<>();
                String resultContent = iteratePlaceholderLogic.replacePlaceholderInIterate(new IssuePlaceholder(), bugIssueList, issueContent);
                //String resultContent= replacePlaceholderInIssueIterate(bugIssueList, issueContent);
                content = content.replace(Config.ITERATE_PLACEHOLDER_ISSUE_BUG+issueContent, resultContent);
            }else if(group.equals(Config.ITERATE_PLACEHOLDER_DEPENDENT_COMPONENT)){
                String dependentComponentContent = getNextBracketAfterIteratePlaceholder(content, index);

                IteratePlaceholderLogic<DependentComponentPlaceholder, DependentComponent> iteratePlaceholderLogic = new IteratePlaceholderLogic<>();
                String resultContent = iteratePlaceholderLogic.replacePlaceholderInIterate(new DependentComponentPlaceholder(), dependentComponentList, dependentComponentContent);
                //String resultContent = replacePlaceholderInDependentComponentIterate(dependentComponentList, dependentComponentContent);
                content = content.replace(Config.ITERATE_PLACEHOLDER_DEPENDENT_COMPONENT+dependentComponentContent, resultContent);

            }else if(group.equals(Config.ITERATE_PLACEHOLDER_PACKAGE_URL)){
                String packageUrlContent = getNextBracketAfterIteratePlaceholder(content, index);

                IteratePlaceholderLogic<PackageUrlPlaceholder, PackageUrl> iteratePlaceholderLogic = new IteratePlaceholderLogic<>();
                String resultContent = iteratePlaceholderLogic.replacePlaceholderInIterate(new PackageUrlPlaceholder(), packageUrlList, packageUrlContent);
                //String resultContent = replacePlaceholderInPackageUrlIterate(packageUrlList, packageUrlContent);
                content = content.replace(Config.ITERATE_PLACEHOLDER_PACKAGE_URL+packageUrlContent, resultContent);

            }else{
                throw new BussinessException("循环占位符错误！");
            }
            matcher = iteratePattern.matcher(content);
        }
        return content;
    }

    public static String replacePlaceholder(String content, Map<String, String> placeholderMap){
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

    public static String[] splitNameInfoFromIssue(Issue issue, String seperatorString){
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

    public static void createTemplate(Template template){

        VarifyLogic.verifyTemplate(template);
    }

    public static String replaceHistoryIteratePlaceholder(String content, List<Issue> issueList, LinkedMap<String, PublishlistQueryResult> historyVersionPublishlistQueryResult, LinkedMap<String, List<Issue>> historyVersionIssueList){
        Pattern historyIteratePattern = Pattern.compile("\\$\\$History\\(.+?\\)");
        Matcher matcher = historyIteratePattern.matcher(content);

        while(matcher.find()){
            String group = matcher.group(0);
            Integer index = matcher.end();

            if(!group.equals(Config.HISTORY_PLACEHOLDER_DOCUMENT_VERSION)){
                throw new BussinessException("非法的占位符："+group);
            }

            String versionContent = ReleaseInfoLogic.getNextBracketAfterIteratePlaceholder(content, index);
            String resultContent= replacePlaceholderInHistory(historyVersionPublishlistQueryResult, historyVersionIssueList, versionContent);

            content = content.replace(Config.HISTORY_PLACEHOLDER_DOCUMENT_VERSION+versionContent, resultContent);

            matcher = historyIteratePattern.matcher(content);
        }
        return content;
    }

    /**
     * 替换发布单相关占位符
     * @param content
     * @param publishlist
     * @return
     */
    public static String replacePublishlistPlaceholder(String content,Publishlist publishlist){
        Map<String, String> placeholderContentMap = ReleaseInfoLogic.generatePublishlistPlaceholderMap(publishlist);
        content = ReleaseInfoLogic.replacePlaceholder(content, placeholderContentMap);
        return content;
    }


    private static String replacePlaceholderInHistory(LinkedMap<String, PublishlistQueryResult> historyVersionPublishlistQueryResult, LinkedMap<String, List<Issue>> historyVersionIssueList, String historyContent){
        String resultContent="";
        List<String> strList = new ArrayList<>();

        for(String version : historyVersionPublishlistQueryResult.keySet()){
            String tempContent;
            tempContent = replacePublishlistPlaceholder(historyContent, historyVersionPublishlistQueryResult.get(version).getPublishlist());
            tempContent = ReleaseInfoLogic.replaceIteratePlaceholder(tempContent, historyVersionIssueList.get(version), historyVersionPublishlistQueryResult.get(version).getDependentComponentList(), historyVersionPublishlistQueryResult.get(version).getPackageUrlList());
            strList.add(tempContent);
        }

        for(String str : strList){
            resultContent = resultContent.concat(str.substring(1, str.length()-1));//去掉字符串前后的大括弧
            resultContent = resultContent.concat("\n\t");
        }
        return resultContent;
    }

}
