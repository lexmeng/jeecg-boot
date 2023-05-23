package org.jeecg.modules.devops.blueocean;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.InputStream;

@Slf4j
public class BlueOceanClientImpl {

    private static final Configuration JSONPATH_CONF = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider())
            .options(Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS).build();

    private final BlueOceanClient client;

    BlueOceanClientImpl(BlueOceanClient client) {
        this.client = client;
    }


    public JsonNode getFolders() {
        return execute(client.getFolders());
    }

    public JsonNode getJobs(String folder) {
        return execute(client.getJobs(folder));
    }

    public JsonNode getJobRuns(String folder, String jobFullName, int start, int limit) {
        return execute(client.getJobRuns(folder, jobFullName, start, limit));
    }

    public JsonNode getJobRun(String folder, String jobFullName, String runNumber) {
        return execute(client.getJobRun(folder, jobFullName, runNumber));
    }

    public JsonNode getJobRunNodes(String folder, String jobFullName, String runNumber) {
        return execute(client.getJobRunNodes(folder, jobFullName, runNumber, 1000));
    }

    public JsonNode getJobRunSteps(String folder, String jobFullName, String runNumber, String nodeNumber) {
        return execute(client.getJobRunSteps(folder, jobFullName, runNumber, nodeNumber));
    }

    public JsonNode getJobParameters(String folder, String jobFullName, String runNumber) {
        final JsonNode data = execute(client.getJobParameters(folder, jobFullName, runNumber));
        return JsonPath.using(JSONPATH_CONF).parse(data).read("$..parameters[*]");
    }

    public JsonNode getJobTestSummary(String folder, String jobFullName, String runNumber) {
        return execute(client.getJobTestSummary(folder, jobFullName, runNumber));
    }

    public JsonNode getJobRunTests(String folder, String jobFullName, String runNumber, int start, int limit) {
        return execute(client.getJobRunTests(folder, jobFullName, runNumber, start, limit));
    }

    public InputStream downloadJobRunLog(String folder, String jobFullName, String runNumber) {
        final ResponseBody content = execute(client.downloadJobRunLog(folder, jobFullName, runNumber));
        return content.byteStream();

    }

    public InputStream downloadJobStepLog(String folder, String jobFullName, String runNumber, String nodeNumber, String stepNumber) {
        final ResponseBody content = execute(client.downloadJobStepLog(folder, jobFullName, runNumber, nodeNumber, stepNumber));
        return content.byteStream();
    }


    @SneakyThrows
    private <T> T execute(Call<T> caller) {
        final Response<T> resp = caller.execute();
        if (!resp.isSuccessful()) {
            log.info("------ execute failed ------\n" +
                    "${caller.request().method()}: ${caller.request().url()}\n" +
                    "code: ${resp.code()}\n" +
                    "body: ${resp.errorBody().string()}\n" +
                    "----------------------------"
            );
            throw new RuntimeException(resp.message());
        }

        return resp.body();
    }
}
