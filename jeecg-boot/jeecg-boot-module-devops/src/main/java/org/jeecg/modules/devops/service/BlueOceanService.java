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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlueOceanService {

    private volatile BlueOceanClientImpl client;


    //@Autowired
    private BlueOceanConfig blueOceanConfig = new BlueOceanConfig();

    @Autowired
    private ObjectMapper objectMapper;


    public List<BlueOceanFolder> getFolders() {
        return toObject(getClient().getFolders(), new TypeReference<List<BlueOceanFolder>>() {
        });
    }

    public List<BlueOceanJob> getJobs(String folder) {
        return toObject(getClient().getJobs(folder), new TypeReference<List<BlueOceanJob>>() {
        });
    }

    public List<BlueOceanRun> getRuns(String folder, String job) {
        return toObject(getClient().getJobRuns(folder, job, 0, 3), new TypeReference<List<BlueOceanRun>>() {
        });
    }

    public String downloadJobRunLog(String folder, String job, String number) {
        return String.valueOf(getClient().downloadJobRunLog(folder, job, number));
    }

    private <T> T toObject(JsonNode jsonNode, Class<T> clazz) {
        return objectMapper.convertValue(jsonNode, clazz);
    }

    private <T> T toObject(JsonNode jsonNode, TypeReference<T> typeReference) {
        return objectMapper.convertValue(jsonNode, typeReference);
    }

    @SneakyThrows
    private BlueOceanClientImpl getClient() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    client = BlueOceanClientFactory.create(blueOceanConfig);
                }
            }
        }

        return client;
    }
}
