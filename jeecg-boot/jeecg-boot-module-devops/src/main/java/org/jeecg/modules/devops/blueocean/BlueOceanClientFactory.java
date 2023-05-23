package org.jeecg.modules.devops.blueocean;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BlueOceanClientFactory {
    private final static String SET_COOKIE_URL = "/login";
    private final static String LOGIN_URL = "/j_spring_security_check";

    private BlueOceanClientFactory() {
    }

    static class CookieJar implements okhttp3.CookieJar {

        private final Map<String, Cookie> cookies = new HashMap<>();

        @Override
        public void saveFromResponse(@NotNull HttpUrl url, List<Cookie> cookies) {
            cookies.forEach(it -> {
                this.cookies.put(it.name(), it);
            });
        }

        @NotNull
        @Override
        public List<Cookie> loadForRequest(@NotNull HttpUrl url) {
            return new ArrayList<Cookie>(cookies.values());
        }
    }


    public static BlueOceanClientImpl create(BlueOceanConfig config) throws IOException {
        final CookieJar cookieAuthenticator = new CookieJar();
        final OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .cookieJar(cookieAuthenticator)
                .build();

        final Request setCookieReq = new Request.Builder()
                .url(Paths.get(config.baseUrl, SET_COOKIE_URL).toString())
                .build();
        final Response setCookie = client.newCall(setCookieReq)
                .execute();

        if (!setCookie.isSuccessful()) {
            log.info("set cookie failed[${setCookie.code()}]: ${setCookie.body().string()}");
            throw new RuntimeException();
        }

        final FormBody loginForm = new FormBody.Builder()
                .add("j_username", config.account)
                .add("j_password", config.password)
                .build();

        final Response loginResponse = client.newCall(new Request.Builder()
                        .url(Paths.get(config.baseUrl, LOGIN_URL).toString())
                        .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36")
                        .post(loginForm)
                        .build())
                .execute();

        if (!loginResponse.isSuccessful()) {
            log.info("login failed[${loginResponse.code()}]: ${loginResponse.body().string()}");
            throw new RuntimeException();
        }

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.baseUrl)
                .callFactory((Call.Factory) client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        return new BlueOceanClientImpl(retrofit.create(BlueOceanClient.class));
    }

}
