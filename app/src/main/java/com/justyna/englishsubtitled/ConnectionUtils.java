package com.justyna.englishsubtitled;

import com.facebook.AccessToken;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class ConnectionUtils {
    private static final ConnectionUtils ourInstance = new ConnectionUtils();

    public static ConnectionUtils getInstance() {
        return ourInstance;
    }

    private ConnectionUtils() {
    }

    public HttpEntity<String> prepareHttpEntity() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken.getToken());
        return new HttpEntity<>(headers);
    }

    public HttpEntity<String> prepareHttpEntity(String json) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken.getToken());
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        return new HttpEntity<>(json, headers);
    }
}
