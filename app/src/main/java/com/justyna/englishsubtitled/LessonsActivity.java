package com.justyna.englishsubtitled;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;

import com.justyna.englishsubtitled.games.fragments.ABCDFragment;
import com.justyna.englishsubtitled.games.fragments.CrosswordFragment;
import com.justyna.englishsubtitled.games.fragments.FinishLessonFragment;
import com.justyna.englishsubtitled.games.fragments.WordFragment;
import com.justyna.englishsubtitled.games.utilities.DictionarySender;
import com.justyna.englishsubtitled.games.utilities.Game;
import com.justyna.englishsubtitled.games.utilities.GameResult;
import com.justyna.englishsubtitled.model.Lesson;
import com.justyna.englishsubtitled.model.LessonResult;
import com.justyna.englishsubtitled.model.Translation;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LessonsActivity extends FragmentActivity implements CrosswordFragment.OnDataPass, WordFragment.OnDataPass, ABCDFragment.OnDataPass {

    Random rand = new Random();
    List<Translation> translations;
    Translation currentTranslation;
    boolean first = true, finishedLesson = true;
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
                findViewById(R.id.dictionary_btn).setVisibility(View.GONE);
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

        dictionaryBtn = this.findViewById(R.id.dictionary_btn);
        dictionaryBtn.setImageResource(android.R.drawable.star_big_on);
        dictionaryBtn.setBackground(null);
        dictionaryBtn.setOnClickListener(v -> sendToBackend(currentTranslation));

        Bundle bundle = getIntent().getExtras();
        String lessonName = null;
        if (bundle != null)
            lessonName = (String) bundle.get("lessonName");

        lessonResult = new LessonResult();
        Lesson lesson = LessonRetriever.prepareTranslationList(lessonName);
        translations = lesson.getTranslations();
        lessonResult.setLessonId(lesson.getLessonId());

        if (translations.size() == 0)
            finish();
        else {
            for (Translation translation : translations) {
                translation.setProgress(0);
                translation.setFails(0);
            }
            prepareGame();
        }
    }

    private void sendToBackend(Translation translation) {
        lessonResult.incrementDictionaryAdditions();
        DictionarySender.addToDict(translation);
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
        if (first) {
            first = false;
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        } else
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


        Bundle bundle = new Bundle();
        bundle.putSerializable("translation", currentTranslation);

        callGame(getRandomNumber(0, 3), bundle);
    }

    private int getRandomNumber(int a, int b) {
        return rand.nextInt(b) + a;
    }

}
