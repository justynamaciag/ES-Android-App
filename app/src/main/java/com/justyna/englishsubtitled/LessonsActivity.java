package com.justyna.englishsubtitled;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.justyna.englishsubtitled.games.ABCDActivity;

import java.util.ArrayList;
import java.util.List;

public class LessonsActivity extends AppCompatActivity {

    Button lesson1Btn;
    Button lesson2Btn;
    Button lesson3Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        prepareTranslationList();

        lesson1Btn = findViewById(R.id.lesson1Btn);
        lesson2Btn = findViewById(R.id.lesson2Btn);
        lesson3Btn = findViewById(R.id.lesson3Btn);

        lesson1Btn.setOnClickListener(lesson1BtnOnClick);
        lesson1Btn.setOnClickListener(lesson2BtnOnClick);
        lesson1Btn.setOnClickListener(lesson3BtnOnClick);
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

    private OnClickListener lesson1BtnOnClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LessonsActivity.this, ABCDActivity.class);
            LessonsActivity.this.startActivity(intent);
        }

    };

    private OnClickListener lesson2BtnOnClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LessonsActivity.this, ABCDActivity.class);
            LessonsActivity.this.startActivity(intent);
        }

    };

    private OnClickListener lesson3BtnOnClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LessonsActivity.this, ABCDActivity.class);
            LessonsActivity.this.startActivity(intent);
        }

    };
}
