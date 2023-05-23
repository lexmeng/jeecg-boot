package org.jeecg.modules.devops.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.jeecg.modules.devops.blueocean.BlueOceanClientFactory;
import org.jeecg.modules.devops.blueocean.BlueOceanClientImpl;
import org.jeecg.modules.devops.blueocean.BlueOceanConfig;
import org.jeecg.modules.devops.blueocean.vo.BlueOceanFolder;
import org.jeecg.modules.devops.blueocean.vo.BlueOceanJob;
import org.jeecg.modules.devops.blueocean.vo.BlueOceanRun;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlueOceanService implements ApplicationListener<ContextRefreshedEvent> {

    private BlueOceanClientImpl client;

    @Autowired
    private BlueOceanConfig blueOceanConfig;

    @Autowired
    private ObjectMapper objectMapper;


    public List<BlueOceanFolder> getFolders() {
        return toObject(client.getFolders(), new TypeReference<List<BlueOceanFolder>>() {
        });
    }

    public List<BlueOceanJob> getJobs(String folder) {
        return toObject(client.getJobs(folder), new TypeReference<List<BlueOceanJob>>() {
        });
    }

    public List<BlueOceanRun> getRuns(String folder, String job) {
        return toObject(client.getJobRuns(folder, job, 0, 3), new TypeReference<List<BlueOceanRun>>() {
        });
    }

    public String downloadJobRunLog(String folder, String job, String number) {
        return String.valueOf(client.downloadJobRunLog(folder, job, number));
    }

    private <T> T toObject(JsonNode jsonNode, Class<T> clazz) {
        return objectMapper.convertValue(jsonNode, clazz);
    }

    private <T> T toObject(JsonNode jsonNode, TypeReference<T> typeReference) {
        return objectMapper.convertValue(jsonNode, typeReference);
    }


    @Override
    @SneakyThrows
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        if (client != null) {
            return;
        }

        client = BlueOceanClientFactory.create(blueOceanConfig);
    }
}
