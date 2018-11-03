package com.justyna.englishsubtitled;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.justyna.englishsubtitled.games.ABCDFragment;
import com.justyna.englishsubtitled.games.CrosswordFragment;
import com.justyna.englishsubtitled.games.WordFragment;
import com.justyna.englishsubtitled.model.Translation;
import com.justyna.englishsubtitled.utils.FinishLessonFragment;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class LessonsActivity extends FragmentActivity implements CrosswordFragment.OnDataPass, WordFragment.OnDataPass, ABCDFragment.OnDataPass {

    List<Translation> translations;
    Translation currentTranslation;
    Random rand = new Random();
    boolean first = true, finished = true;
    int minRepeats = 2;

    @Override
    public void onDataPass(String data) {
        if (data.equals("1")) {
            translations.remove(currentTranslation);
            currentTranslation.setProgress(currentTranslation.getProgress() + 1);
            translations.add(currentTranslation);
        }
        finished = true;
        for (Translation t : translations) {
            if (t.getProgress() < minRepeats) {
                finished = false;
            }
        }
        if (!finished)
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

    private Translation getRandomTranslation() {
        Translation tempTranslation = translations.get(getRandomNumber(0, translations.size() - 1));
        while ((tempTranslation.getProgress() >= minRepeats))
            tempTranslation = translations.get(getRandomNumber(0, translations.size() - 1));

        return tempTranslation;
    }

    private void callGame(int game, Bundle bundle) {

        Fragment fragment = new Fragment();
        switch (game) {
            case 0:
                fragment = new CrosswordFragment();
                break;
            case 1:
                fragment = new ABCDFragment();
                bundle.putSerializable("translations", (Serializable) translations);
                break;
            case 2:
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
        int game = getRandomNumber(0, 3);
        currentTranslation = getRandomTranslation();
        Bundle bundle = new Bundle();
        bundle.putSerializable("translation", currentTranslation);

        callGame(game, bundle);
    }

    private int getRandomNumber(int a, int b) {
        return rand.nextInt(b) + a;
    }

}
