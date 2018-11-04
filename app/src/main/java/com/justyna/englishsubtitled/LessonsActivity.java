package com.justyna.englishsubtitled;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.justyna.englishsubtitled.games.ABCDFragment;
import com.justyna.englishsubtitled.games.CrosswordFragment;
import com.justyna.englishsubtitled.games.WordFragment;
import com.justyna.englishsubtitled.model.Translation;
import com.justyna.englishsubtitled.utilities.FinishLessonFragment;
import com.justyna.englishsubtitled.utilities.Game;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class LessonsActivity extends FragmentActivity implements CrosswordFragment.OnDataPass, WordFragment.OnDataPass, ABCDFragment.OnDataPass {

    Random rand = new Random();
    List<Translation> translations;
    Translation currentTranslation;
    boolean first = true, finishedLesson = true, finishLessonSuccess = true;
    int wordRepeats = 2;

    @Override
    public void onDataPass(boolean data) {
        if (data == finishLessonSuccess)
            currentTranslation.setProgress(currentTranslation.getProgress() + 1);

        finishedLesson = true;
        for (Translation t : translations) {
            if (t.getProgress() < wordRepeats) {
                finishedLesson = false;
            }
        }
        if (!finishedLesson)
            prepareGame();
        else
            callFragment(new FinishLessonFragment());

    }

    private void callFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.list_container, fragment);
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

        translations = LessonRetriever.prepareTranslationList();
        for (Translation translation : translations) {
            translation.setProgress(0);
        }
        prepareGame();
    }

    private Translation chooseNextTranslation() {
        Translation tempTranslation = translations.get(getRandomNumber(0, translations.size() - 1));
        while ((tempTranslation.getProgress() >= wordRepeats))
            tempTranslation = translations.get(getRandomNumber(0, translations.size() - 1));

        return tempTranslation;
    }

    private void callGame(int gameNum, Bundle bundle) {

        Fragment fragment = new Fragment();

        Game game = Game.values()[gameNum];
        switch (game) {
            case CROSSWORD:
                fragment = new CrosswordFragment();
                break;
            case ABCD:
                fragment = new ABCDFragment();
                bundle.putSerializable("translations", (Serializable) translations);
                break;
            case PUZZLE:
                fragment = new WordFragment();
                break;
        }
        fragment.setArguments(bundle);
        if (first) {
            first = false;
            getSupportFragmentManager().beginTransaction().add(R.id.list_container, fragment).commit();
        } else
            callFragment(fragment);

    }


    private void prepareGame() {
        currentTranslation = chooseNextTranslation();
        Bundle bundle = new Bundle();
        bundle.putSerializable("translation", currentTranslation);

        callGame(getRandomNumber(0, 3), bundle);
    }

    private int getRandomNumber(int a, int b) {
        return rand.nextInt(b) + a;
    }

}
