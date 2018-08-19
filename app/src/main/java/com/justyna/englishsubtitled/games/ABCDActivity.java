package com.justyna.englishsubtitled.games;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.justyna.englishsubtitled.model.Lesson;
import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.model.Translation;


import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;


public class ABCDActivity extends AppCompatActivity {

    List<Translation> translations;
    Handler handler = new Handler();
    List<Button> buttons;
    TextView wordTextView;
    Random rand;
    Translation word;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abcd);

        rand = new Random();
        translations = prepareTranslationList();

        buttons = new ArrayList<>();
        buttons.add((Button) findViewById(R.id.btnAns1));
        buttons.add((Button) findViewById(R.id.btnAns2));
        buttons.add((Button) findViewById(R.id.btnAns3));
        buttons.add((Button) findViewById(R.id.btnAns4));

        wordTextView = findViewById(R.id.wordTextView);

        for (Button b : buttons) {
            b.setOnClickListener(btnAnsListener);
        }

        setButtons();

    }

    private View.OnClickListener btnAnsListener = v -> {

        Button pressedBtn = (Button) v;
        Toast answerToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        for (Translation t : translations) {
            if (word.getEngWord().equals(t.getEngWord())) {
                if (pressedBtn.getText().equals(t.getPlWord())) {
                    answerToast.setText("Good answer");
                    answerToast.show();
                } else {
                    answerToast.setText("Wrong answer");
                    answerToast.show();
                }
                handler.postDelayed(new Runnable() {
                    public void run() {
                        setButtons();
                    }
                }, 1000);

            }
        }
    };


    private void setButtons() {

        Set<String> answers = new HashSet<>();
        word = getRandomTranslation();

        wordTextView.setText(word.getEngWord());
        answers.add(word.getPlWord());

        while (answers.size() < 4) {
            Translation randomTranslation = getRandomTranslation();
            if (!answers.contains(getRandomTranslation().getPlWord())) {
                answers.add(randomTranslation.getPlWord());
            }
        }

        List<String> answerList = new ArrayList<>();
        answerList.addAll(answers);
        Collections.shuffle(answerList);


        for (Button b : buttons) {
            b.setText(answerList.remove(answerList.size() - 1));
        }

    }

    private Translation getRandomTranslation() {

        int random = rand.nextInt(translations.size());
        return translations.get(random);

    }

    private List<Translation> prepareTranslationList() {

        Lesson lesson;
        try {
            lesson = new RetrieveLesson().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            lesson = null;
            this.finish();
        }
        return lesson.getTranslations();

    }

    private class RetrieveLesson extends AsyncTask<Void, Void, Lesson> {

        @Override
        protected Lesson doInBackground(Void... voids) {
            String baseUrl = "http://10.0.2.2:8080";
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(baseUrl + "/lessons/2", Lesson.class);
        }
    }

}
