package com.justyna.englishsubtitled;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.justyna.englishsubtitled.games.ABCDActivity;
import com.justyna.englishsubtitled.games.WordActivity;

public class LessonsActivity extends AppCompatActivity {

    Button lesson1Btn;
    Button lesson2Btn;
    Button lesson3Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        lesson1Btn = findViewById(R.id.lesson1Btn);
        lesson2Btn = findViewById(R.id.lesson2Btn);
        lesson3Btn = findViewById(R.id.lesson3Btn);

        lesson1Btn.setOnClickListener(lesson1BtnOnClick);
        lesson2Btn.setOnClickListener(lesson2BtnOnClick);
        lesson3Btn.setOnClickListener(lesson3BtnOnClick);
    }


    private OnClickListener lesson1BtnOnClick = v -> {
        Intent intent = new Intent(LessonsActivity.this, ABCDActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(intent);
    };

    private OnClickListener lesson2BtnOnClick = v -> {
        Intent intent = new Intent(LessonsActivity.this, WordActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(intent);
    };

    private OnClickListener lesson3BtnOnClick = v -> {
        Toast.makeText(this.getApplicationContext(), "Not implemented yet", Toast.LENGTH_SHORT).show();
    };

}
