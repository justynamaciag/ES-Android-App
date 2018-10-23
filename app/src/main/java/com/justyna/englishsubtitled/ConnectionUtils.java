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
}
