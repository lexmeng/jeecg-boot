package org.jeecg.modules.publishlist.tools;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHCommitStatus;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestCommitDetail;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class GithubTools {

    @Value("${github.url}")
    private String GITHUB_BASE_URL;

    @Value("${github.account}")
    private String GITHUB_ACCOUNT;

    @Value("${github.token}")
    private String GITHUB_TOKEN;


    private volatile GitHub gitHub;

    @SneakyThrows
    public GHRepository getRepository(String name) {
        return connect().getRepository(name);
    }

    @SneakyThrows
    public GitHub connect() {
        if (gitHub == null) {
            synchronized (this) {
                if (gitHub == null) {
                    gitHub = GitHub.connect(GITHUB_ACCOUNT, GITHUB_TOKEN);
                }
            }
        }

        return gitHub;
    }

    @SneakyThrows
    public GHPullRequest pullRequest(GHRepository repository, int id) {
        return repository.getPullRequest(id);
    }

    @SneakyThrows
    public List<GHCommitStatus> status(GHPullRequest pullRequest) {
        final GHPullRequestCommitDetail lastCommit = pullRequest.listCommits().toList().get(pullRequest.getCommits() - 1);
        final Map<String, GHCommitStatus> status = new HashMap<>();
        pullRequest.getRepository().getCommit(lastCommit.getSha()).listStatuses().forEach((s) -> {
            status.putIfAbsent(s.getContext(), s);
        });
        return Lists.newArrayList(status.values());
    }

}
