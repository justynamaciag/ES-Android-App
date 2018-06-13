package com.justyna.englishsubtitled.games;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.justyna.englishsubtitled.R;

import java.util.HashMap;

public class ABCDActivity extends AppCompatActivity {

    HashMap<String, String> translations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abcd);

    }
}
