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

import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.games.utilities.CrosswordAdapter;
import com.justyna.englishsubtitled.games.utilities.GameResult;
import com.justyna.englishsubtitled.model.Translation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrosswordFragment extends Fragment {

    Translation currentTranslation;
    Random rand;
    String[][] table;
    String clicked;
    TextView polishTranslationDisplay;
    int correctlyEnteredSubwordLength = 0, row, offset, englishWordLength;
    View view;
    boolean transpose, firstCellCorrect = false, alreadyMistaken = false;
    List<TextView> crosswordCells;
    List<String> gridViewLetters;
    boolean isFirstCellCorrect;
    // N set to 10 - max word length in crossword
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

        englishWordLength = currentTranslation.getEngWord().length();
        if (englishWordLength > N) {
            passData(GameResult.CANT_EXECUTE);
            return view;
        }

        setBoardSize();

        rand = new Random();
        gridViewLetters = prepareGameTable();
        GridView crosswordGrid = prepareCrosswordGrid(gridViewLetters);
        callGame(crosswordGrid, gridViewLetters);

        return view;
    }

    private void setBoardSize() {
        if (englishWordLength <= 4)
            N = 5;
        else if (englishWordLength <= 7)
            N = 8;
        else
            N = 10;
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
        crosswordGrid.setAdapter(adapter);

        return crosswordGrid;
    }

    // remove green and red colors from board
    private void resetBoard(GridView crosswordGrid) {
        correctlyEnteredSubwordLength = 0;
        firstCellCorrect = false;
        alreadyMistaken = false;
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

                if (action == MotionEvent.ACTION_UP) {
                    if (correctlyEnteredSubwordLength == englishWordLength && !alreadyMistaken)
                        passData(GameResult.SUCCESS);
                    resetBoard(crosswordGrid);
                }

//              ACTION_DOWN - called when first touched cell, touching next cells - ACTION_MOVE
                if (action == MotionEvent.ACTION_DOWN) {
                    firstCellCorrect = checkIfFirstCellCorrect(crosswordGrid, point);
                }

                return true;

            } catch (IndexOutOfBoundsException e) {
                resetBoard(crosswordGrid);
                return false;
            }

        });
    }

//      check if first touched cell is correct,
//      drawing entire row/column containing correct translation wont work, only specific letters
    private boolean checkIfFirstCellCorrect(GridView crosswordGrid, int point) {
        TextView t = (TextView) crosswordGrid.getChildAt(point);

        int correctCell;
        if (!transpose) {
            correctCell = row * N + offset + correctlyEnteredSubwordLength;
        } else {
            correctCell = (offset + correctlyEnteredSubwordLength) * N + row;
        }

        if (point == correctCell) {
            isFirstCellCorrect = true;
            t.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        } else {
            isFirstCellCorrect = false;
            passData(GameResult.FAIL);
            t.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
        return isFirstCellCorrect;
    }

    private void checkIfUserClicksCorrect(GridView crosswordGrid, int point) {
        TextView t = (TextView) crosswordGrid.getChildAt(point);
        int correctCell, nextPoint;

        if (!transpose) {
            correctCell = row * N + offset + correctlyEnteredSubwordLength;
            nextPoint = point + 1;
        } else {
            correctCell = (offset + correctlyEnteredSubwordLength) * N + row;
            nextPoint = point + N;
        }

        if (point == correctCell && isFirstCellCorrect && !alreadyMistaken && correctlyEnteredSubwordLength != englishWordLength) {
            correctlyEnteredSubwordLength++;
            t.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        } else if (nextPoint != correctCell || alreadyMistaken) { // color red only if it's not just the same, already accepted, cell detected again; or if previous cells were wrong already
            alreadyMistaken = true;
            t.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
    }

    private List<String> prepareGameTable() {

        table = new String[N][N];
        row = rand.nextInt(N);
        offset = rand.nextInt(N - englishWordLength + 1);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                table[i][j] = Character.toString((char) (rand.nextInt('Z' - 'A') + 'A'));
            }
        }

//      offset - where correct translation starts
        for (int i = offset; i < offset + englishWordLength; i++) {
            table[row][i] = Character.toString(currentTranslation.getEngWord().charAt(i - offset)).toUpperCase();
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

    public interface OnDataPass {
        void onDataPass(GameResult data);
    }
}
