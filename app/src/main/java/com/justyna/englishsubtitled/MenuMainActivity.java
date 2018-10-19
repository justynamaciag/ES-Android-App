package com.justyna.englishsubtitled;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.justyna.englishsubtitled.menu.MenuFindLessonActivity;
import com.justyna.englishsubtitled.model.LessonSummary;
import com.justyna.englishsubtitled.model.Progress;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.justyna.englishsubtitled.DisableSSLCertificateCheckUtil.disableChecks;

public class MenuMainActivity extends AppCompatActivity {

    Button startNewLesson;
    Button dictionary;
    Button achievements;
    Button finishedLessons;
    Button logout;

    Button recentLesson1;
    Button recentLesson2;
    Button recentLesson3;

    List<Button> recentLessonButtons = new ArrayList<>(3);

    ImageButton removeRecent1;
    ImageButton removeRecent2;
    ImageButton removeRecent3;

    List<ImageButton> removeRecentButtons = new ArrayList<>(3);

    List<LessonSummary> recentLessons = Collections.emptyList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        startNewLesson = findViewById(R.id.start_new_lesson);
        dictionary = findViewById(R.id.dictionary);
        achievements = findViewById(R.id.achievements);
        finishedLessons = findViewById(R.id.finished_lessons);
        logout = findViewById(R.id.logout);

        recentLesson1 = findViewById(R.id.recent_lesson_1);
        recentLesson2 = findViewById(R.id.recent_lesson_2);
        recentLesson3 = findViewById(R.id.recent_lesson_3);

        removeRecent1 = findViewById(R.id.remove_recent_lesson_1);
        removeRecent2 = findViewById(R.id.remove_recent_lesson_2);
        removeRecent3 = findViewById(R.id.remove_recent_lesson_3);

        startNewLesson.setOnClickListener(startNewLessonBtnOnClick);
        dictionary.setOnClickListener(dictionaryBtnOnClick);
        achievements.setOnClickListener(achievementsBtnOnClick);
        finishedLessons.setOnClickListener(finishedLessonsBtnOnClick);
        logout.setOnClickListener(logoutBtnOnClick);

        recentLesson1.setOnClickListener(recentLesson1BtnOnClick);
        recentLesson2.setOnClickListener(recentLesson2BtnOnClick);
        recentLesson3.setOnClickListener(recentLesson3BtnOnClick);

        recentLessonButtons.add(recentLesson1);
        recentLessonButtons.add(recentLesson2);
        recentLessonButtons.add(recentLesson3);

        removeRecent1.setOnClickListener(removeRecent1BtnOnClick);
        removeRecent2.setOnClickListener(removeRecent2BtnOnClick);
        removeRecent3.setOnClickListener(removeRecent3BtnOnClick);

        removeRecentButtons.add(removeRecent1);
        removeRecentButtons.add(removeRecent2);
        removeRecentButtons.add(removeRecent3);

        refreshRecentLessons();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        refreshRecentLessons();
    }

    private void refreshRecentLessons() {
        try {
            recentLessons = new RecentLessonsRetriever().execute().get();

            for (int i = 0; i < 3; i++) {
                if (recentLessons.size() < i + 1) break;
                recentLessonButtons.get(i).setText(recentLessons.get(i).lessonTitle);
                recentLessonButtons.get(i).setVisibility(View.VISIBLE);
                removeRecentButtons.get(i).setVisibility(View.VISIBLE);
            }

            for (int i = recentLessons.size(); i < 3; i++) {
                recentLessonButtons.get(i).setVisibility(View.INVISIBLE);
                removeRecentButtons.get(i).setVisibility(View.INVISIBLE);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error while downloading recent lessons list:");
            e.printStackTrace();
        }
    }


    private OnClickListener startNewLessonBtnOnClick = v -> {
        Intent intent = new Intent(MenuMainActivity.this, MenuFindLessonActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    };

    private OnClickListener dictionaryBtnOnClick = v -> {
//        Intent intent = new Intent(MenuMainActivity.this, WordActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
//        intent.putExtra("translations", (Serializable) translations);
//        startActivity(intent);
    };

    private OnClickListener achievementsBtnOnClick = v -> {
//        Intent intent = new Intent(MenuMainActivity.this, CrosswordActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
//        intent.putExtra("translations", (Serializable) translations);
//        startActivity(intent);
    };

    private OnClickListener finishedLessonsBtnOnClick = v -> {
//        Intent intent = new Intent(MenuMainActivity.this, CrosswordActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
//        intent.putExtra("translations", (Serializable) translations);
//        startActivity(intent);
    };

    private OnClickListener logoutBtnOnClick = v -> {
        Intent intent = new Intent(MenuMainActivity.this, FacebookActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        LoginManager.getInstance().logOut();
        startActivity(intent);
    };

    private OnClickListener recentLesson1BtnOnClick = v -> {
//        Intent intent = new Intent(MenuMainActivity.this, LessonsActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    };

    private OnClickListener recentLesson2BtnOnClick = v -> {
//        Intent intent = new Intent(MenuMainActivity.this, LessonsActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    };

    private OnClickListener recentLesson3BtnOnClick = v -> {
//        Intent intent = new Intent(MenuMainActivity.this, LessonsActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    };

    private OnClickListener removeRecent1BtnOnClick = v -> {
        if (recentLessons.size() >= 1) {
            int lessonId = recentLessons.get(0).getLessonId();
            removeRecentLesson(lessonId);
        }
    };

    private OnClickListener removeRecent2BtnOnClick = v -> {
        if (recentLessons.size() >= 2) {
            int lessonId = recentLessons.get(1).getLessonId();
            removeRecentLesson(lessonId);
        }
    };

    private OnClickListener removeRecent3BtnOnClick = v -> {
        if (recentLessons.size() >= 3) {
            int lessonId = recentLessons.get(2).getLessonId();
            removeRecentLesson(lessonId);
        }
    };

    private void removeRecentLesson(int lessonId) {
        boolean deleted = false;
        try {
            deleted = new RecentLessonsRemover().execute(lessonId).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (deleted) {
            refreshRecentLessons();
        }
    }

    private static class RecentLessonsRetriever extends AsyncTask<Void, Void, List<LessonSummary>> {
        @Override
        protected List<LessonSummary> doInBackground(Void... voids) {
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

            ResponseEntity<Progress> progress =
                    restTemplate.exchange(baseUrl + "/progress/",
                            HttpMethod.GET, entity, new ParameterizedTypeReference<Progress>() {
                            });

            return progress.getBody().getRented();
        }
    }

    private static class RecentLessonsRemover extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... integers) {
            if (integers.length < 1) return false;
            int lessonId = integers[0];
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

            ResponseEntity<String> result =
                    restTemplate.exchange(baseUrl + "/progress/" + lessonId,
                            HttpMethod.DELETE, entity, String.class);

            if (result.getStatusCode() != HttpStatus.OK) {
                System.out.println("Back-end responded with: " + result.getBody());
                return false;
            }
            return true;
        }
    }
}
