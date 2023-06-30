package org.jeecg.modules.devops.chatbot.adapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import lombok.SneakyThrows;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.jeecg.modules.devops.chatbot.Adapter;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class AzureAdapter implements Adapter {


    private interface API {
        @POST("/openai/deployments/{deployment}/chat/completions")
        Call<ChatCompletionResult> createChatCompletion(@Body ChatCompletionRequest request, @Path("deployment") String deployment, @Header("api-key") String token, @Query("api-version") String apiVersion);
    }

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(60);

    private static final ObjectMapper defaultObjectMapper = new ObjectMapper();
    private static final OkHttpClient defaultClient;

    static {
        defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        defaultObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        defaultObjectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        defaultClient = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
                .readTimeout(DEFAULT_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
                .build();
    }


    private final String deployment;
    private final String token;
    private final String apiVersion;

    private final API api;

    public AzureAdapter(String endpoint, String deployment, String token, String apiVersion) {
        this.deployment = deployment;
        this.token = token;
        this.apiVersion = apiVersion;

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpoint)
                .client(defaultClient)
                .addConverterFactory(JacksonConverterFactory.create(defaultObjectMapper))
                .build();

        this.api = retrofit.create(API.class);


    }

    @SneakyThrows
    @Override
    public ChatCompletionResult createChatCompletion(ChatCompletionRequest request) {
        return api.createChatCompletion(request, deployment, token, apiVersion).execute().body();
    }
}
