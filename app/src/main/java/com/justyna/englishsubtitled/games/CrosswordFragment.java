package com.justyna.englishsubtitled.games;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.model.Translation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrosswordFragment extends Fragment {

    //za długie słówko - obsluga

    Translation currentTranslation;
    List<String> gridViewLetters;
    Random rand;
    String[][] table;
    String clicked;
    TextView helperTV;
    int i = 0, N = 10, reverse, row, offset;
    View view;
    int prev = 0;


    OnDataPass dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_crossword, container, false);
        helperTV = view.findViewById(R.id.helperTv);

        Bundle bundle = getArguments();
        if (bundle != null) {
            currentTranslation = (Translation) bundle.getSerializable("translation");
        }
        String currentTranslationPL = currentTranslation.getPlWord();

        if(currentTranslationPL.length()>N) {
            System.out.println("c");
        }
        table = new String[N][N];
        rand = new Random();
        gridViewLetters = prepareTable(currentTranslation);

        helperTV.setText(currentTranslationPL);

        ArrayAdapter<String> crosswordAdapter = new ArrayAdapter<String>(getContext(), R.layout.crossword, R.id.crossword, gridViewLetters);
        GridView crosswordGrid = view.findViewById(R.id.gridview);
        crosswordGrid.setNumColumns(N);
        crosswordGrid.setAdapter(crosswordAdapter);

        crosswordGrid.setOnTouchListener((v, event) -> {
            float x = event.getX();
            float y = event.getY();
            try {
                int point = crosswordGrid.pointToPosition((int) x, (int) y);
                int action = event.getAction() & MotionEvent.ACTION_MASK;
                if (action == MotionEvent.ACTION_MOVE) {
                    clicked = gridViewLetters.get(point);
                    if(reverse==1) {
                        System.out.println(clicked + " " + table[row][offset+i] + " " + i);
                        if (clicked.toLowerCase().equals(table[row][offset+i].toLowerCase())) {
                            System.out.println("jetsem w euals");
                            i++;
                        }
                    }
                    else if(reverse==0) {
                        System.out.println(clicked + " " + table[row][offset+i] + " " + i);
                        if (clicked.toLowerCase().equals(table[row][offset+i].toLowerCase())) {
                            i++;
                        }
                    }
                    if (i == currentTranslation.getEngWord().length()) {
                        Toast.makeText(getContext(), "cool", Toast.LENGTH_SHORT).show();
                        passData("1");

                    }
                }

                if (i == currentTranslation.getEngWord().length()) {
                    Toast.makeText(getContext(), "cool", Toast.LENGTH_SHORT).show();
                    passData("1");

                }
                return true;
            }
            catch (IndexOutOfBoundsException e){
                return false;
            }

        });

        return view;
    }


    private List<String> prepareTable(Translation translation){

        row = rand.nextInt(N);
        offset = rand.nextInt(N - translation.getEngWord().length());

        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                table[i][j] = Character.toString((char) (rand.nextInt('Z'-'A')+'A'));
            }
        }

        for(int i=offset; i<offset+translation.getEngWord().length(); i++) {
            table[row][i] = Character.toString(translation.getEngWord().charAt(i-offset));
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

    public void passData(String data) {
        dataPasser.onDataPass(data);
    }

    public interface OnDataPass {
        void onDataPass(String data);
    }
}
