package com.justyna.englishsubtitled;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.justyna.englishsubtitled.games.fragments.ABCDFragment;
import com.justyna.englishsubtitled.games.fragments.CrosswordFragment;
import com.justyna.englishsubtitled.games.fragments.FinishLessonFragment;
import com.justyna.englishsubtitled.games.fragments.LoadingFragment;
import com.justyna.englishsubtitled.games.fragments.WordFragment;
import com.justyna.englishsubtitled.games.utilities.DictionarySender;
import com.justyna.englishsubtitled.games.utilities.Game;
import com.justyna.englishsubtitled.games.utilities.GameResult;
import com.justyna.englishsubtitled.model.Lesson;
import com.justyna.englishsubtitled.model.LessonResult;
import com.justyna.englishsubtitled.model.LessonSummary;
import com.justyna.englishsubtitled.model.Translation;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.justyna.englishsubtitled.DisableSSLCertificateCheckUtil.disableChecks;

public class LessonsActivity extends FragmentActivity implements CrosswordFragment.OnDataPass, WordFragment.OnDataPass, ABCDFragment.OnDataPass {

    Random rand = new Random();
    List<Translation> translations;
    Translation currentTranslation;
    boolean finishedLesson = true;
    int wordRepeats = 2, maxFails = 2, currentTranslationFailures = 0, correctAnswersInRow = 0;
    ImageButton dictionaryBtn;
    LessonResult lessonResult;


    @Override
    public void onDataPass(GameResult data) {
        if (data == GameResult.SUCCESS) {

            if (currentTranslationFailures == 0) {
                lessonResult.incrementCorrectAnswerAsFirst();
            }
            currentTranslation.setProgress(currentTranslation.getProgress() + 1);

            correctAnswersInRow++;
            if (correctAnswersInRow > lessonResult.getCorrectAnswersInRow())
                lessonResult.setCorrectAnswersInRow(correctAnswersInRow);

            finishedLesson = true;
            for (Translation t : translations) {
                if (t.getProgress() < wordRepeats || t.getFails() > maxFails)
                    finishedLesson = false;

            }
            if (!finishedLesson) {
                prepareGame();

            } else {
                dictionaryBtn.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putSerializable("lessonResult", lessonResult);
                Fragment finishLessonFragment = new FinishLessonFragment();
                finishLessonFragment.setArguments(bundle);
                callFragment(finishLessonFragment);
            }
        } else if (data == GameResult.FAIL) {
            currentTranslation.addFailAnswer();
            lessonResult.incrementMistakes();
            correctAnswersInRow = 0;
        } else if (data == GameResult.CANT_EXECUTE) {
            lessonResult.decrementCrosswords();
            prepareGame();
        }

    }

    private void callFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new LoadingFragment()).commit();

        dictionaryBtn = this.findViewById(R.id.dictionary_btn);
        dictionaryBtn.setOnClickListener(v -> sendToBackend(currentTranslation));
        dictionaryBtn.setVisibility(View.INVISIBLE);

        Bundle bundle = getIntent().getExtras();
        String lessonName = "";
        if (bundle != null)
            lessonName = (String) bundle.get("lessonName");

        lessonResult = new LessonResult();

        new LessonRetriever().execute(lessonName);
    }

    private void finishPreparations(Lesson lesson){
        translations = lesson.getTranslations();
        lessonResult.setLessonId(lesson.getLessonId());

        if (translations.size() == 0)
            finish();
        else {
            for (Translation translation : translations) {
                translation.setProgress(0);
                translation.setFails(0);
                translation.setDictionaryAdded(false);
            }
            prepareGame();
        }
    }

    private void sendToBackend(Translation translation) {
        if(!translation.getDictionaryAdded()){
            translation.setDictionaryAdded(true);
            lessonResult.incrementDictionaryAdditions();
            new DictionarySender().addToDict(translation, LessonsActivity.this);
        }
        else
            Toast.makeText(getApplicationContext(), "Słowo zostało już dodane do słownika", Toast.LENGTH_SHORT).show();

    }

    private Translation chooseNextTranslation() {

        Translation temp = translations.get(getRandomNumber(0, translations.size() - 1));

        if (temp.getProgress() < wordRepeats || temp.getFails() > maxFails)
            return temp;

        Collections.shuffle(translations);
        for (Translation t : translations) {
            if (t.getProgress() < wordRepeats || t.getFails() > maxFails)
                return t;

        }
        return null;
    }

    private void callGame(int gameNum, Bundle bundle) {

        currentTranslationFailures = 0;

        Fragment fragment = new Fragment();

        Game game = Game.values()[gameNum];
        switch (game) {
            case CROSSWORD:
                lessonResult.incrementCrosswordGames();
                fragment = new CrosswordFragment();
                break;
            case ABCD:
                lessonResult.incrementABCDGames();
                fragment = new ABCDFragment();
                bundle.putSerializable("translations", (Serializable) translations);
                break;
            case PUZZLE:
                lessonResult.incrementWordGames();
                fragment = new WordFragment();
                break;
        }
        fragment.setArguments(bundle);
        callFragment(fragment);
    }

    private void prepareGame() {
        if (translations.size() == 1)
            currentTranslation = translations.get(0);
        else
            currentTranslation = chooseNextTranslation();

        if (currentTranslation == null)
            callFragment(new FinishLessonFragment());

        currentTranslation.setFails(0);

        dictionaryBtn.setVisibility(View.VISIBLE);

        Bundle bundle = new Bundle();
        bundle.putSerializable("translation", currentTranslation);

        callGame(getRandomNumber(0, 3), bundle);
    }

    private int getRandomNumber(int a, int b) {
        return rand.nextInt(b) + a;
    }

    private class LessonRetriever extends AsyncTask<String, Void, Lesson> {
        @Override
        protected Lesson doInBackground(String... strings) {
            if (strings.length < 1) return null;
            String lessonName = strings[0];

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

            int lessonId = getLessonId(lessonName, lessonsListEntity.getBody());

            ResponseEntity<Lesson> responseEntity = restTemplate.exchange(baseUrl + "/lessons/" + lessonId,
                    HttpMethod.GET, entity, Lesson.class);
            return responseEntity.getBody();
        }

        @Override
        protected void onPostExecute(Lesson lesson) {
            super.onPostExecute(lesson);
            finishPreparations(lesson);
        }

        private int getLessonId(String lessonName, List<LessonSummary> lessonSummaries) {
            for (LessonSummary lesson : lessonSummaries)
                if (lesson.lessonTitle.equals(lessonName))
                    return lesson.getLessonId();

            return 0;
        }
    }

}