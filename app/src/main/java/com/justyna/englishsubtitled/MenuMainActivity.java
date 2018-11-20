package com.justyna.englishsubtitled;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.justyna.englishsubtitled.menu.MenuAchievementsActivity;
import com.justyna.englishsubtitled.menu.MenuDictionaryActivity;
import com.justyna.englishsubtitled.menu.MenuFindLessonActivity;
import com.justyna.englishsubtitled.menu.MenuFinishedLessonsActivity;
import com.justyna.englishsubtitled.model.LessonSummary;
import com.justyna.englishsubtitled.model.Progress;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    private final List<LessonSummary> recentLessonsCollection = new ArrayList<>(3);
    private final List<LessonSummary> finishedLessonsCollection = new LinkedList<>();

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
        recentLesson1.getBackground().setColorFilter(getResources().getColor(R.color.colorBrown), PorterDuff.Mode.MULTIPLY);

        recentLesson2 = findViewById(R.id.recent_lesson_2);
        recentLesson2.getBackground().setColorFilter(getResources().getColor(R.color.colorBrown), PorterDuff.Mode.MULTIPLY);

        recentLesson3 = findViewById(R.id.recent_lesson_3);
        recentLesson3.getBackground().setColorFilter(getResources().getColor(R.color.colorBrown), PorterDuff.Mode.MULTIPLY);

        removeRecent1 = findViewById(R.id.remove_recent_lesson_1);
        removeRecent2 = findViewById(R.id.remove_recent_lesson_2);
        removeRecent3 = findViewById(R.id.remove_recent_lesson_3);

        startNewLesson.setOnClickListener(startNewLessonBtnOnClick);
        dictionary.setOnClickListener(dictionaryBtnOnClick);
        achievements.setOnClickListener(achievementsBtnOnClick);
        finishedLessons.setOnClickListener(finishedLessonsBtnOnClick);
        logout.setOnClickListener(logoutBtnOnClick);

        recentLesson1.setOnClickListener(recentLessonBtnOnClick);
        recentLesson2.setOnClickListener(recentLessonBtnOnClick);
        recentLesson3.setOnClickListener(recentLessonBtnOnClick);

        recentLessonButtons.add(recentLesson1);
        recentLessonButtons.add(recentLesson2);
        recentLessonButtons.add(recentLesson3);

        removeRecent1.setOnClickListener(removeRecent1BtnOnClick);
        removeRecent2.setOnClickListener(removeRecent2BtnOnClick);
        removeRecent3.setOnClickListener(removeRecent3BtnOnClick);

        removeRecentButtons.add(removeRecent1);
        removeRecentButtons.add(removeRecent2);
        removeRecentButtons.add(removeRecent3);

        new ProgressRetriever().execute();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        new ProgressRetriever().execute();
    }

    private OnClickListener startNewLessonBtnOnClick = v -> {
        Intent intent = new Intent(MenuMainActivity.this, MenuFindLessonActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    };

    private OnClickListener dictionaryBtnOnClick = v -> {
        Intent intent = new Intent(MenuMainActivity.this, MenuDictionaryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    };

    private OnClickListener achievementsBtnOnClick = v -> {
        Intent intent = new Intent(MenuMainActivity.this, MenuAchievementsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    };

    private OnClickListener finishedLessonsBtnOnClick = v -> {
        Intent intent = new Intent(MenuMainActivity.this, MenuFinishedLessonsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        synchronized (finishedLessonsCollection) {
            intent.putExtra("finishedLessons", (Serializable) finishedLessonsCollection);
        }
        startActivity(intent);
    };

    private OnClickListener logoutBtnOnClick = v -> {
        Intent intent = new Intent(MenuMainActivity.this, FacebookActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        LoginManager.getInstance().logOut();
        startActivity(intent);
    };

    private OnClickListener recentLessonBtnOnClick = v -> {

        Intent intent = new Intent(this, LessonsActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("lessonName", ((Button) v).getText().toString());
        intent.putExtras(bundle);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    };

    private OnClickListener removeRecent1BtnOnClick = v -> {
        removeRecentLesson(1);
    };

    private OnClickListener removeRecent2BtnOnClick = v -> {
        removeRecentLesson(2);
    };

    private OnClickListener removeRecent3BtnOnClick = v -> {
        removeRecentLesson(3);
    };

    private void removeRecentLesson(int buttonOrder) {
        boolean removing = false;
        int lessonId = -1; // Value just for compiler
        synchronized (recentLessonsCollection) {
            if (recentLessonsCollection.size() >= buttonOrder) {
                lessonId = recentLessonsCollection.get(buttonOrder - 1).getLessonId();
                removing = true;
            }
        }
        if (removing) {
            new RecentLessonsRemover().execute(lessonId);
        }
    }

    private class ProgressRetriever extends AsyncTask<Void, Void, Progress> {
        @Override
        protected Progress doInBackground(Void... voids) {
            try {
                disableChecks();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String baseUrl = Configuration.getInstance().getBackendUrl();
            HttpEntity<String> entity = ConnectionUtils.getInstance().prepareHttpEntity();

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Progress> progress;

            try {
                progress = restTemplate.exchange(baseUrl + "/progress/",
                        HttpMethod.GET, entity, new ParameterizedTypeReference<Progress>() {
                        });
            } catch (Exception e) {
                this.cancel(true);
                return null;
            }

            return progress.getBody();
        }

        @Override
        protected void onPostExecute(Progress progress) {
            super.onPostExecute(progress);
            synchronized (finishedLessonsCollection) {
                finishedLessonsCollection.clear();
                finishedLessonsCollection.addAll(progress.getFinished());
            }
            synchronized (recentLessonsCollection) {
                recentLessonsCollection.clear();
                recentLessonsCollection.addAll(progress.getRented());
                for (int i = 0; i < 3; i++) {
                    if (recentLessonsCollection.size() < i + 1) break;
                    recentLessonButtons.get(i).setText(recentLessonsCollection.get(i).lessonTitle);
                    recentLessonButtons.get(i).setVisibility(View.VISIBLE);
                    removeRecentButtons.get(i).setVisibility(View.VISIBLE);
                }

                for (int i = recentLessonsCollection.size(); i < 3; i++) {
                    recentLessonButtons.get(i).setVisibility(View.INVISIBLE);
                    removeRecentButtons.get(i).setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(MenuMainActivity.this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
    }

    private class RecentLessonsRemover extends AsyncTask<Integer, Void, Boolean> {
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
            HttpEntity<String> entity = ConnectionUtils.getInstance().prepareHttpEntity();

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

        @Override
        protected void onPostExecute(Boolean deleted) {
            super.onPostExecute(deleted);
            if (deleted) {
                new ProgressRetriever().execute();
            }
        }
    }
}
