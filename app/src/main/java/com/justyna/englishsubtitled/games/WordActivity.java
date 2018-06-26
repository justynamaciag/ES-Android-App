package com.justyna.englishsubtitled.games;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.justyna.englishsubtitled.stuctures.Lesson;
import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.stuctures.Translation;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class WordActivity extends AppCompatActivity {

    List<Translation> translations;
    Translation currentTranslation;
    List<Button> buttons;
    List<TextView> letters;
    Random rand;
    int checkedIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        rand = new Random();
        translations = prepareTranslationList();
        buttons = new ArrayList<>();
        letters = new ArrayList<>();
        checkedIndex = 0;
        currentTranslation = getRandomTranslation();
        setButtons(currentTranslation);

    }

    private View.OnClickListener btnListener = v -> {

        Button pressedBtn = (Button) v;
        char pressed = pressedBtn.getText().charAt(0);
        char actual = currentTranslation.getEngWord().charAt(checkedIndex);

        if(pressed == actual) {
            letters.get(checkedIndex).setText(pressedBtn.getText().toString().toUpperCase());
            checkedIndex++;

            if (checkedIndex == currentTranslation.getEngWord().length()) {

                Toast.makeText(this.getApplicationContext(), "Great!", Toast.LENGTH_SHORT).show();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkedIndex = 0;
                        for(int i=0; i<currentTranslation.getEngWord().length(); i++){

                            ViewGroup layout = (ViewGroup) buttons.get(i).getParent();
                            ViewGroup layoutLetters = (ViewGroup) letters.get(i).getParent();

                            if(null!=layout) {
                                layout.removeView(buttons.get(i));
                            }

                            if(null!=layoutLetters) {
                                layoutLetters.removeView(letters.get(i));
                            }
                        }
                        currentTranslation = getRandomTranslation();
                        setButtons(currentTranslation);
                    }
                }, 2000);

            }
        }

    };


    public void setButtons(Translation translation){

        buttons = new ArrayList<>();
        letters = new ArrayList<>();

        LinearLayout buttonsLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        LinearLayout wordLayout = (LinearLayout) findViewById(R.id.wordLayout);


        for (int i=0; i<translation.getEngWord().length(); i++) {

            Button letterBtn = new Button(this);
            letterBtn.setText(Character.toString(translation.getEngWord().charAt(i)));
            letterBtn.setOnClickListener(btnListener);
            buttons.add(letterBtn);

        }

        Collections.shuffle(buttons);

        for (Button b :buttons) {
            b.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            b.setWidth(20);
            buttonsLayout.addView(b);

            TextView letterTV = new TextView(this);
            letters.add(letterTV);
            letterTV.setTextSize(20);
            letterTV.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            wordLayout.addView(letterTV);
        }

    }


    private Translation getRandomTranslation(){

        System.out.println(translations);
        int random = rand.nextInt(translations.size());
        return translations.get(random);
    }


    private List<Translation> prepareTranslationList() {
        Lesson lesson;
        try {
            lesson = new RetrieveLesson().execute().get();
        } catch(InterruptedException | ExecutionException e){
            lesson = null;
            this.finish();
        }
        return lesson.getTranslations();
    }

    private class RetrieveLesson extends AsyncTask<Void, Void, Lesson> {
        @Override
        protected Lesson doInBackground(Void... voids) {
            String baseUrl = "http://192.168.100.5:8080"; // host machine from Android VM
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(baseUrl+"/lessons/2", Lesson.class);
        }
    }
}
