package com.justyna.englishsubtitled.games;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.Translation;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class ABCDActivity extends AppCompatActivity {

    List<Translation> translations;
    Handler handler = new Handler();
    int positiveAnsw;

    List<Button> buttons;
    TextView wordTextView;
    Random rand;


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
                handler.postDelayed(new Runnable() {
                    public void run() {
                        setButtons();
                    }
                }, 1000);

            }
        }
    };


    private void setButtons(){

        Set<String> answers = new HashSet<>();
        Translation word = getRandomTranslation();

        wordTextView.setText(word.getEngWord().toUpperCase());
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

    private List<Translation> prepareTranslationList(){
        List<Translation> translations = new ArrayList();
        translations.add(new Translation("home", "dom"));
        translations.add(new Translation("bag", "torba"));
        translations.add(new Translation("computer", "komputer"));
        translations.add(new Translation("bike", "rower"));
        translations.add(new Translation("dog", "pies"));
        translations.add(new Translation("cat", "kot"));
        translations.add(new Translation("frog", "żaba"));
        translations.add(new Translation("bed", "łóżko"));
        translations.add(new Translation("leg", "noga"));
        translations.add(new Translation("sleep", "spać"));
        translations.add(new Translation("nose", "nos"));
        translations.add(new Translation("eat", "jeść"));
        translations.add(new Translation("talk", "mówić"));
        translations.add(new Translation("live", "żyć"));
        return translations;
    }

}
