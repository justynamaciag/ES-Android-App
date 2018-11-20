package com.justyna.englishsubtitled.games.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.games.utilities.CrosswordAdapter;
import com.justyna.englishsubtitled.games.utilities.GameResult;
import com.justyna.englishsubtitled.model.Translation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrosswordFragment extends Fragment implements CrosswordAdapter.customTVListener {

    Translation currentTranslation;
    Random rand = new Random();
    String[][] table;
    String clicked;
    TextView polishTranslationDisplay;
    int i = 0, N = 10, row, offset;
    View view;
    boolean transpose, firstCellCorrect = false;

    OnDataPass dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_crossword, container, false);
        polishTranslationDisplay = view.findViewById(R.id.polishTranslation);

        Bundle bundle = getArguments();
        if (bundle != null) {
            currentTranslation = (Translation) bundle.getSerializable("translation");
        }

        if (currentTranslation.getEngWord().length() >= N) {
            passData(GameResult.CANT_EXECUTE);
            return view;
        }

        rand = new Random();
        List<String> gridViewLetters = prepareGameTable(currentTranslation);
        GridView crosswordGrid = prepareCrosswordGrid(gridViewLetters);
        callGame(crosswordGrid, gridViewLetters);

        return view;
    }


    private GridView prepareCrosswordGrid(List<String> gridViewLetters) {

        List<TextView> crosswordCells = new ArrayList<>();
        for (String letter : gridViewLetters) {
            TextView tv = new TextView(getContext());
            tv.setText(letter);
            crosswordCells.add(tv);
        }

        GridView crosswordGrid = view.findViewById(R.id.gridview_crossword);
        crosswordGrid.setNumColumns(N);
        CrosswordAdapter adapter = new CrosswordAdapter(getContext(), crosswordCells);
        adapter.setCustomTVListner(CrosswordFragment.this);
        crosswordGrid.setAdapter(adapter);

        return crosswordGrid;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void callGame(GridView crosswordGrid, List<String> gridViewLetters) {

        polishTranslationDisplay.setText(currentTranslation.getPlWord());

        crosswordGrid.setOnTouchListener((v, event) -> {
            float x = event.getX();
            float y = event.getY();
            try {

                int point = crosswordGrid.pointToPosition((int) x, (int) y);
                int action = event.getAction() & MotionEvent.ACTION_MASK;

                clicked = gridViewLetters.get(point);

                if (action == MotionEvent.ACTION_MOVE)
                    checkIfUserClicksCorrect();

//                ACTION_DOWN - called when first touched cell, touching next cells - ACTION_MOVE
                if (action == MotionEvent.ACTION_DOWN)
                    firstCellCorrect = checkIfFirstCellCorrect();
                return true;

            } catch (IndexOutOfBoundsException e) {
                return false;
            }

        });
    }

    //  check if first touched cell is correct,
//  drawing entire row/column containing correct translation wont work, only specific letters
    private boolean checkIfFirstCellCorrect() {
        if(clicked.equalsIgnoreCase((table[row][offset]))){
            return true;
        }
        else {
            passData(GameResult.FAIL);
            return false;
        }
    }

    private void checkIfUserClicksCorrect() {
//        correct
        if (clicked.equalsIgnoreCase(table[row][offset + i]) && firstCellCorrect)
            i++;
//      incorrect - (-1) added due to fact that listener is called multiple times in drawing at one cell
        else if (!clicked.equalsIgnoreCase(table[row][offset + i - 1])) {
            i = 0;
        }
//        if entire word was marked
        if (i == currentTranslation.getEngWord().length()) {
            Toast.makeText(view.getContext(), "Perfect!", Toast.LENGTH_SHORT).show();
            passData(GameResult.SUCCESS);
        }
    }

    private List<String> prepareGameTable(Translation translation) {

        table = new String[N][N];
        row = rand.nextInt(N);
        offset = rand.nextInt(N - translation.getEngWord().length());

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                table[i][j] = Character.toString((char) (rand.nextInt('Z' - 'A') + 'A'));
            }
        }

//        offset - where correct translation starts
        for (int i = offset; i < offset + translation.getEngWord().length(); i++) {
            table[row][i] = Character.toString(translation.getEngWord().charAt(i - offset)).toUpperCase();
        }


        transpose = rand.nextBoolean();
        ArrayList<String> tableList = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (transpose)
                    tableList.add(table[j][i]);
                else
                    tableList.add(table[i][j]);
            }
        }
        return tableList;

    }

    public void passData(GameResult data) {
        dataPasser.onDataPass(data);
    }

    @Override
    public boolean onTVClickListner(int position, TextView tv, MotionEvent event) {
        return true;
    }

    public interface OnDataPass {
        void onDataPass(GameResult data);
    }
}
