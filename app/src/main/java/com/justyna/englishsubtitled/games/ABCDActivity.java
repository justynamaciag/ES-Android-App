package com.justyna.englishsubtitled.games;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.justyna.englishsubtitled.Lesson;
import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.Translation;


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
    int positiveAnsw;

    List<Button> buttons;
    TextView wordTextView;
    Random rand;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abcd);

        rand = new Random();

//        Intent i = getIntent();
//        translations = (List<Translation>) i.getSerializableExtra("translation_list");

        translations = prepareTranslationList();

        buttons = new ArrayList<>();

        buttons.add((Button) findViewById(R.id.btnAns1));
        buttons.add((Button) findViewById(R.id.btnAns2));
        buttons.add((Button) findViewById(R.id.btnAns3));
        buttons.add((Button) findViewById(R.id.btnAns4));

        wordTextView = findViewById(R.id.wordTextView);
        positiveAnsw = 0;

        for (Button b:buttons) {
            b.setOnClickListener(btnAnsListener);
        }

        setButtons();

    }

    private View.OnClickListener btnAnsListener = v -> {
        Button pressedBtn = (Button) v;
        String word = wordTextView.getText().toString();
        for (Translation t:translations) {
            if(word.equals(t.getEngWord())) {
                if (pressedBtn.getText().equals(t.getPlWord())) {
                    positiveAnsw++;
                    Toast.makeText(this.getApplicationContext(), "Good answer", Toast.LENGTH_SHORT).show();
                }
                else{
                        Toast.makeText(this.getApplicationContext(), "Wrong answer", Toast.LENGTH_SHORT).show();
                    }

            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setButtons(){

        Set<String> answers = new HashSet<>();
        Translation word = getRandomTranslation();

        wordTextView.setText(word.getEngWord());
        answers.add(word.getPlWord());

        while(answers.size()<4){
            Translation randomTranslation = getRandomTranslation();
            if(!answers.contains(getRandomTranslation().getPlWord())){
                answers.add(randomTranslation.getPlWord());
            }
        }

        List<String> answerList = new ArrayList<>();
        answerList.addAll(answers);
        Collections.shuffle(answerList);


        for (Button b:buttons) {
            b.setText(answerList.remove(answerList.size()-1));
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
            String baseUrl = "http://10.0.2.2:8080"; // host machine from Android VM
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(baseUrl+"/lessons/2", Lesson.class);
        }
    }

}
