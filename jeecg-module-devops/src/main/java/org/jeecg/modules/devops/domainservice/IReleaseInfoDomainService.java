package org.jeecg.modules.devops.domainservice;

import org.jeecg.modules.devops.entity.Issue;
import org.jeecg.modules.devops.entity.Template;

public interface IReleaseInfoDomainService {

    //public ReleaseInfo convertReleaseInfoFromIssue(Issue issue, String seperatorString);

    public String[] splitNameInfoFromIssue(Issue issue, String seperatorString);

    public Boolean verifyPlaceholder(Template template);

    //public String replacePlaceholder(String content, Map<String, String> placeholderMap);

    //public Boolean isNeedToGenerateReleaseInfo(String content, String productlineName);
}
