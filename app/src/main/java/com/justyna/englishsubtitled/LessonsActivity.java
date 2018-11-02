package com.justyna.englishsubtitled;

import android.os.Bundle;
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

public class LessonsActivity extends FragmentActivity implements CrosswordFragment.OnDataPass, WordFragment.OnDataPass, ABCDFragment.OnDataPass{

    List<Translation> translations;
    Translation currentTranslation;
    Random rand;
    int first = 1;
    boolean finished = true;


    @Override
    public void onDataPass(String data) {
        if(data.equals("1")){
            translations.remove(currentTranslation);
            currentTranslation.setProgress(1);
            translations.add(currentTranslation);
        }
        finished = true;
        for(Translation translation:translations)
            if(translation.getProgress() == 0)
                finished = false;
        if(!finished)
            callGame();
        else{
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.list_container, new FinishLessonFragment());
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        rand = new Random();
        translations = LessonRetriever.prepareTranslationList();
        for(Translation translation:translations){
            translation.setProgress(0);
        }
        callGame();
    }

    private void callGame() {

        ABCDFragment abcdFragment = new ABCDFragment();
        WordFragment wordFragment = new WordFragment();
        CrosswordFragment crosswordFragment = new CrosswordFragment();

        int game = getRandomNumber(0, 3);

        currentTranslation = translations.get(getRandomNumber(0, translations.size() - 1));
        Bundle bundle = new Bundle();
        bundle.putSerializable("translations", (Serializable) translations);
        bundle.putSerializable("translation", currentTranslation);

        crosswordFragment.setArguments(bundle);
        abcdFragment.setArguments(bundle);
        wordFragment.setArguments(bundle);

        switch (game) {

            case 0:
                if(first == 1){
                    first = 0;
                    getSupportFragmentManager().beginTransaction().add(R.id.list_container, crosswordFragment).commit();
                }
                else {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.list_container, crosswordFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                break;
            case 1:
                if(first == 1) {
                    first = 0;
                    getSupportFragmentManager().beginTransaction().add(R.id.list_container, abcdFragment).commit();
                }
                else{
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.list_container, abcdFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                break;
            case 2:
                if(first == 1) {
                    first = 0;
                    getSupportFragmentManager().beginTransaction().add(R.id.list_container, wordFragment).commit();
                }
                else{
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.list_container, wordFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                break;
        }
    }

    private int getRandomNumber(int a, int b) {
        return rand.nextInt(b) + a;
    }

}
