package com.justyna.englishsubtitled;

import android.os.AsyncTask;

import com.justyna.englishsubtitled.model.Lesson;
import com.justyna.englishsubtitled.model.Translation;

import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class LessonRetriever {

    public static List<Translation> prepareTranslationList() {

        Lesson lesson;
        try {
            lesson = new RetrieveLesson().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            lesson = null;
        }
        return lesson.getTranslations();

    }

    private static class RetrieveLesson extends AsyncTask<Void, Void, Lesson> {
        @Override
        protected Lesson doInBackground(Void... voids) {
            String baseUrl = "http://10.0.2.2:8080";
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(baseUrl + "/lessons/2", Lesson.class);
        }
    }
}
