package com.justyna.englishsubtitled.games;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.model.Translation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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

        Intent intent = getIntent();
        translations = (List<Translation>) intent.getSerializableExtra("translations");

        rand = new Random();
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

}
