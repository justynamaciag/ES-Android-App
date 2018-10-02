package com.justyna.englishsubtitled.games;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.model.Lesson;
import com.justyna.englishsubtitled.model.Translation;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class CrosswordActivity extends AppCompatActivity {

    List<Translation> translations;
    Translation currentTranslation;
    List<String> trn;
    Random rand;
    String[][] table;
    int i;
    String prev, next;
    TextView helperTV;
    int N = 10;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        table = new String[N][N];
        rand = new Random();
        translations = prepareTranslationList();
        currentTranslation = getRandomTranslation();
        trn = prepareTable(currentTranslation);
        System.out.println(trn);
        i=0;

        ArrayAdapter<String> crosswordAdapter = new ArrayAdapter<String>(this, R.layout.crossword, R.id.crossword, trn);

        GridView crosswordGrid = new GridView(this);
        setContentView(crosswordGrid);
        crosswordGrid.setNumColumns(N);
        crosswordGrid.setColumnWidth(N);
        crosswordGrid.setAdapter(crosswordAdapter);


        crosswordGrid.setOnTouchListener((v, event) -> {
            float X = event.getX();
            float Y = event.getY();
            int row = (int) (Y/(N*10));
            int col = (int) (X/(N*10));

            CrosswordActivity.super.onTouchEvent(event);
//            int action = event.getAction() & MotionEvent.ACTION_MASK;
            if(event.getAction() == MotionEvent.ACTION_MOVE){
                System.out.println("Move");
                if(i==0)
                    prev = "blabla";
                next = table[row][col];
                System.out.println(next);
                System.out.println(currentTranslation.getEngWord().charAt(i));
                if(!prev.equals(next))
                    if(next.equals(String.valueOf(currentTranslation.getEngWord().charAt(i)))){
                        i++;
                        prev = next;

                    }
//                    else i=0;
                if(i==currentTranslation.getEngWord().length()) {
                    Toast.makeText(getApplicationContext(), "cool", Toast.LENGTH_SHORT).show();
                    i=0;
                }
                return true;
            }
//            vertical - not working
//            else if(action == MotionEvent.ACTION_DOWN){
//                if(i==0)
//                    prev = "blabla";
//                next = table[col][row];
//                System.out.println("down" + next + " " + i);
//                if(!prev.equals(next))
//                    if(next.equals(String.valueOf(currentTranslation.getEngWord().charAt(i)))){
//                        i++;
//                        prev = next;
//
//                    }
//                if(i==currentTranslation.getEngWord().length()) {
//                    Toast.makeText(getApplicationContext(), "cool", Toast.LENGTH_SHORT).show();
//                    i=0;
//                }
//                return true;
//            }

            return false;
        });

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
                table[i][j] = Character.toString((char) (rand.nextInt(90-65)+65));
            }
        }

        for(int i=offset; i<offset+translation.getEngWord().length(); i++) {
            table[row][i] = Character.toString(translation.getEngWord().charAt(i-offset)).toLowerCase();
        }

        int reverse = rand.nextInt(2);
        ArrayList<String> tableList = new ArrayList<>();
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
//                reverse == 1 - vertical
//                if(reverse == 1)
//                    tableList.add(table[j][i]);
//                else
                tableList.add(table[i][j]);
            }
        }
        return tableList;

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
