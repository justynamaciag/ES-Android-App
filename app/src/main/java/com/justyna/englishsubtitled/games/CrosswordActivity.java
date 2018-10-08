package com.justyna.englishsubtitled.games;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.model.Translation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrosswordActivity extends AppCompatActivity {

    List<Translation> translations;
    Translation currentTranslation;
    List<String> gridViewLetters;
    Random rand;
    String[][] table;
    int i = 0;
    String prevClicked, actualClicked;
    TextView helperTV;
    int N = 10;
    int reverse;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossword);

        helperTV = findViewById(R.id.helperTv);

        Intent intent = getIntent();
        translations = (List<Translation>) intent.getSerializableExtra("translations");

        table = new String[N][N];
        rand = new Random();
        currentTranslation = getRandomTranslation();
        gridViewLetters = prepareTable(currentTranslation);

        helperTV.setText(currentTranslation.getPlWord());

        ArrayAdapter<String> crosswordAdapter = new ArrayAdapter<String>(this, R.layout.crossword, R.id.crossword, gridViewLetters);
        GridView crosswordGrid = findViewById(R.id.gridview);
        crosswordGrid.setNumColumns(N);
        crosswordGrid.setAdapter(crosswordAdapter);


        crosswordGrid.setOnTouchListener((v, event) -> {
            float X = event.getX();
            float Y = event.getY();

            int point = crosswordGrid.pointToPosition((int) X,(int) Y);

            CrosswordActivity.super.onTouchEvent(event);
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            if(action == MotionEvent.ACTION_MOVE){
                if(i==0)
                    prevClicked = "";
                actualClicked = gridViewLetters.get(point);
                if(!prevClicked.equals(actualClicked))
                    if(actualClicked.equals(String.valueOf(currentTranslation.getEngWord().charAt(i)))){
                        i++;
                        prevClicked = actualClicked;
                    }
                if(i==currentTranslation.getEngWord().length()) {
                    Toast.makeText(getApplicationContext(), "cool", Toast.LENGTH_SHORT).show();
                    i=0;
                    return false;
                }
                return true;
            }
            return true;

        });

    }

    private Translation getRandomTranslation() {
        System.out.println(translations);
        int random = rand.nextInt(translations.size());
        return translations.get(random);
    }

    private List<String> prepareTable(Translation translation){

        int row = rand.nextInt(N);
        int offset = rand.nextInt(N - translation.getEngWord().length());

        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                table[i][j] = Character.toString((char) (rand.nextInt('Z'-'A')+'A'));
            }
        }

        for(int i=offset; i<offset+translation.getEngWord().length(); i++) {
            table[row][i] = Character.toString(translation.getEngWord().charAt(i-offset)).toLowerCase();
        }

        reverse = rand.nextInt(2);
        ArrayList<String> tableList = new ArrayList<>();
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                if(reverse == 1)
                    tableList.add(table[j][i]);
                else
                    tableList.add(table[i][j]);
            }
        }
        return tableList;

    }
}
