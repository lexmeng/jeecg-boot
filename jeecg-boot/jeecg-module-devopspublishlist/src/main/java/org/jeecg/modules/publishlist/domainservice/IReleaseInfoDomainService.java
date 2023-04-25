package org.jeecg.modules.publishlist.domainservice;

import org.jeecg.modules.publishlist.entity.Issue;
import org.jeecg.modules.publishlist.entity.ReleaseInfo;
import org.jeecg.modules.publishlist.entity.Template;

import java.util.Map;

public interface IReleaseInfoDomainService {

    public ReleaseInfo convertReleaseInfoFromIssue(Issue issue, String seperatorString);

    public Boolean verifyPlaceholder(Template template);

    public String replacePlaceholder(String content, Map<String, String> placeholderMap);
}
