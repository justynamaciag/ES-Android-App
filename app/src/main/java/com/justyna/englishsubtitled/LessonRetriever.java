package com.justyna.englishsubtitled;

import android.os.AsyncTask;

import com.facebook.AccessToken;
import com.justyna.englishsubtitled.model.Lesson;
import com.justyna.englishsubtitled.model.LessonSummary;
import com.justyna.englishsubtitled.model.Translation;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.justyna.englishsubtitled.DisableSSLCertificateCheckUtil.disableChecks;

public class LessonRetriever {

    public static List<Translation> prepareTranslationList() {

        Lesson lesson;
        try {
            lesson = new RetrieveLesson().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e);
            lesson = null;
        }
        return lesson.getTranslations();

    }

    private static class RetrieveLesson extends AsyncTask<Void, Void, Lesson> {
        @Override
        protected Lesson doInBackground(Void... voids) {
            try {
                disableChecks();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String baseUrl = Configuration.getInstance().getBackendUrl();
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", accessToken.getToken());
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<List<LessonSummary>> lessonsListEntity =
                    restTemplate.exchange(baseUrl + "/lessons/",
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<LessonSummary>>() {
                            });

            int lessonId = lessonsListEntity.getBody().get(0).getLessonId();

            ResponseEntity<Lesson> responseEntity = restTemplate.exchange(baseUrl + "/lessons/" + lessonId,
                    HttpMethod.GET, entity, Lesson.class);
            return responseEntity.getBody();
        }
    }
}
