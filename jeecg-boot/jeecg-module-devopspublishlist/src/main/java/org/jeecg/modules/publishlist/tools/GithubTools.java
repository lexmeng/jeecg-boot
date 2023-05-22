package org.jeecg.modules.publishlist.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class GithubTools {

    @Value("${github.url}")
    private String GITHUB_BASE_URL;

    @Value("${github.account}")
    private String GITHUB_ACCOUNT;

    @Value("${github.token}")
    private String GITHUB_TOKEN;

    private String repo = "";

    private void getPRAction(){
        String url = GITHUB_BASE_URL + "/repos/" + GITHUB_ACCOUNT + "/" + repo + "/pulls";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token "+GITHUB_TOKEN);
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        headers.setAccept(mediaTypes);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<PullRequest>> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<PullRequest>>() {
        });
        List<PullRequest> prList = response.getBody();
        for(PullRequest pr : prList){
            System.out.println(pr.getTitle());
        }
    }

    private static class PullRequest{
        private String title;
        public String getTitle(){
            return title;
        }
        public void setTitle(String title){
            this.title = title;
        }
    }
}
