package com.justyna.englishsubtitled;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.justyna.englishsubtitled.model.LessonSummary;
import com.justyna.englishsubtitled.model.Progress;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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

        try {
            List<LessonSummary> recentLessons = new RecentLessonsRetriever().execute().get();

            for (int i = 0; i < 3; i++) {
                if (recentLessons.size() < i + 1) break;
                recentLessonButtons.get(i).setText(recentLessons.get(i).lessonTitle);
                recentLessonButtons.get(i).setVisibility(View.VISIBLE);
            }
        }catch(InterruptedException | ExecutionException e){
            System.out.println("Error while downloading recent lessons list:");
            e.printStackTrace();
        }
    }


    private OnClickListener startNewLessonBtnOnClick = v -> {
        Intent intent = new Intent(MenuMainActivity.this, LessonsActivity.class);
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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
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

    private class RecentLessonsRetriever extends AsyncTask<Void, Void, List<LessonSummary>> {
        @Override
        protected List<LessonSummary> doInBackground(Void... voids) {
            try {
                disableChecks();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String baseUrl = "https://ec2-34-215-76-93.us-west-2.compute.amazonaws.com"; //"http://10.0.2.2:8080";
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", accessToken.getToken());
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Progress> progress =
                    restTemplate.exchange(baseUrl + "/progress/",
                            HttpMethod.GET, entity, new ParameterizedTypeReference<Progress>() {});

            return progress.getBody().getRented();
        }
    }
}
