package com.justyna.englishsubtitled.games.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
    Random rand;
    String[][] table;
    String clicked;
    TextView polishTranslationDisplay;
    int i = 0, row, offset;
    View view;
    boolean transpose, firstCellCorrect = false;
    List<TextView> crosswordCells;
    List<String> gridViewLetters;
    boolean isFirstCellCorrect;
//    N set to 10 - max word length in crossword
    int N = 10;


    OnDataPass dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_crossword, container, false);
        polishTranslationDisplay = view.findViewById(R.id.polishTranslation);

        Bundle bundle = getArguments();
        if (bundle != null) {
            currentTranslation = (Translation) bundle.getSerializable("translation");
        }

        if (currentTranslation.getEngWord().length() >= N || currentTranslation.getProgress() == 0) {
            passData(GameResult.CANT_EXECUTE);
            return view;
        }

        setBoardSize();

        rand = new Random();
        gridViewLetters = prepareGameTable(currentTranslation);
        GridView crosswordGrid = prepareCrosswordGrid(gridViewLetters);
        callGame(crosswordGrid, gridViewLetters);

        return view;
    }

    private void setBoardSize(){
        if (currentTranslation.getEngWord().length() <= 4)
            N = 5;
        else if (currentTranslation.getEngWord().length() > 4 && currentTranslation.getEngWord().length() <= 7)
            N = 8;
        else
            N =10;
    }


    private GridView prepareCrosswordGrid(List<String> gridViewLetters) {

        crosswordCells = new ArrayList<>();
        for (String letter : gridViewLetters) {
            TextView tv = new TextView(getContext());
            tv.setText(letter);
            crosswordCells.add(tv);
        }

        GridView crosswordGrid = view.findViewById(R.id.gridviewCrossword);
        crosswordGrid.setNumColumns(N);
        CrosswordAdapter adapter = new CrosswordAdapter(getContext(), crosswordCells);
        adapter.setCustomTVListner(CrosswordFragment.this);
        crosswordGrid.setAdapter(adapter);

        return crosswordGrid;
    }

//    remove green and red colors from board
    private void clearColorsOnBoard(GridView crosswordGrid){
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            for (int i = 0; i < crosswordGrid.getChildCount(); i++) {
                TextView t = (TextView) crosswordGrid.getChildAt(i);
                t.getBackground().clearColorFilter();
            }
        }, 400);
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

                if (action == MotionEvent.ACTION_MOVE) {
                    checkIfUserClicksCorrect(crosswordGrid, point);
                }

                if (action == MotionEvent.ACTION_UP)
                    clearColorsOnBoard(crosswordGrid);

//                ACTION_DOWN - called when first touched cell, touching next cells - ACTION_MOVE
                if (action == MotionEvent.ACTION_DOWN) {
                    firstCellCorrect = checkIfFirstCellCorrect();
                }

                return true;

            } catch (IndexOutOfBoundsException e) {
                clearColorsOnBoard(crosswordGrid);
                return false;
            }

        });
    }

//      check if first touched cell is correct,
//      drawing entire row/column containing correct translation wont work, only specific letters
    private boolean checkIfFirstCellCorrect() {
        if (clicked.equalsIgnoreCase((table[row][offset]))) {
            isFirstCellCorrect = true;
        } else {
            isFirstCellCorrect = false;
            passData(GameResult.FAIL);
        }
        return isFirstCellCorrect;
    }

    private void checkIfUserClicksCorrect(GridView crosswordGrid, int point) {
        TextView t = (TextView) crosswordGrid.getChildAt(point);
        int correctCell, nextPoint;

        if (! transpose) {
            correctCell = row * N + offset + i;
            nextPoint = point + 1;
        } else {
            correctCell = (offset + i) * N + row;
            nextPoint = point + N;
        }

        if (point == correctCell && isFirstCellCorrect) {
            i++;
            t.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        } else if (nextPoint != correctCell) {
            i = 0;
            t.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }

//        if entire word was marked
        if (i == currentTranslation.getEngWord().length())
            passData(GameResult.SUCCESS);
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
