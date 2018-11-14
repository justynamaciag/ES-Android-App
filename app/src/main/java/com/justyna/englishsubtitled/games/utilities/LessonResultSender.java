package com.justyna.englishsubtitled.games.utilities;

import android.os.AsyncTask;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.justyna.englishsubtitled.Configuration;
import com.justyna.englishsubtitled.model.LessonResult;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

import static com.justyna.englishsubtitled.DisableSSLCertificateCheckUtil.disableChecks;

public class LessonResultSender {

    public static Boolean sendStatistics(LessonResult lessonResult){

        Boolean result;
        try {
            result = new SendResult().execute(lessonResult).get();
        }catch (InterruptedException | ExecutionException e) {
            System.out.println(e);
            result = false;
        }
        return result;
    }

    private static class SendResult extends AsyncTask<LessonResult, Void, Boolean>{

        @Override
        protected Boolean doInBackground(LessonResult... lessonResults) {
            if(lessonResults.length < 1) return false;
            LessonResult lessonResult = lessonResults[0];
            try {
                disableChecks();
            } catch (Exception e) {
                e.printStackTrace();
            }

            String baseUrl = Configuration.getInstance().getBackendUrl();
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", accessToken.getToken());
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");

            Gson gson = new Gson();
            String json = gson.toJson(lessonResult);

            HttpEntity<String> entity = new HttpEntity<>(json, headers);


            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

            ResponseEntity<String> result =
                    restTemplate.exchange(baseUrl + "/progress/" + lessonResult.getLessonId(), HttpMethod.PUT, entity, String.class);

            if (result.getStatusCode() != HttpStatus.OK) {
                System.out.println("Back-end responded with: " + result.getBody());
                return true;
            }
            return false;
        }
    }

}
