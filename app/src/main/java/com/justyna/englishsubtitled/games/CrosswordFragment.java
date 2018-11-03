package com.justyna.englishsubtitled.games;

import android.content.Context;
import android.graphics.Color;
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
import com.justyna.englishsubtitled.model.Translation;
import com.justyna.englishsubtitled.utils.CrosswordAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrosswordFragment extends Fragment implements CrosswordAdapter.customTVListener {

    Translation currentTranslation;
    Random rand = new Random();
    String[][] table;
    String clicked;
    TextView helperTV;
    int i = 0, N = 10, directions = 2, reverse, row, offset, a = 'A', z = 'Z';
    View view;
    boolean startOk = false;


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
        helperTV = view.findViewById(R.id.helperTv);

        Bundle bundle = getArguments();
        if (bundle != null) {
            currentTranslation = (Translation) bundle.getSerializable("translation");
        }

        if (currentTranslation.getEngWord().length() >= N) {
            passData("0");
            return view;
        }

        rand = new Random();
        List<String> gridViewLetters = prepareTable(currentTranslation);
        callGame(gridViewLetters);

        return view;
    }

    private void callGame(List<String> gridViewLetters){

        helperTV.setText(currentTranslation.getPlWord());

        List<TextView> lettersTV = new ArrayList<>();
        for (String letter : gridViewLetters) {
            TextView tv = new TextView(getContext());
            tv.setText(letter);
            lettersTV.add(tv);
        }

        GridView crosswordGrid = (GridView) view.findViewById(R.id.gridview);
        crosswordGrid.setNumColumns(N);
        CrosswordAdapter a = new CrosswordAdapter(getContext(), lettersTV);
        a.setCustomTVListner(CrosswordFragment.this);
        crosswordGrid.setAdapter(a);

        crosswordGrid.setOnTouchListener((v, event) -> {
            float x = event.getX();
            float y = event.getY();
            try {
                int point = crosswordGrid.pointToPosition((int) x, (int) y);
                int action = event.getAction() & MotionEvent.ACTION_MASK;
                clicked = gridViewLetters.get(point);
                if (action == MotionEvent.ACTION_MOVE) {
                    if (clicked.toLowerCase().equals(table[row][offset + i].toLowerCase()) && startOk) {
                        TextView tv = a.getItem(point);
                        tv.setBackgroundColor(Color.parseColor("#ffae19"));
                        i++;
                    } else if (!clicked.toLowerCase().equals(table[row][offset + i - 1].toLowerCase()))
                        i = 0;
                    if (i == currentTranslation.getEngWord().length()) {
                        Toast.makeText(getContext(), "cool", Toast.LENGTH_SHORT).show();
                        passData("1");
                    }
                }
                if (action == MotionEvent.ACTION_DOWN) {
                    if (clicked.toLowerCase().equals((table[row][offset])))
                        startOk = true;
                    else
                        startOk = false;
                }
                return true;

            } catch (IndexOutOfBoundsException e) {
                return false;
            }

        });
    }


    private List<String> prepareTable(Translation translation) {

        table = new String[N][N];
        row = rand.nextInt(N);
        offset = rand.nextInt(N - translation.getEngWord().length());

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                table[i][j] = Character.toString((char) (rand.nextInt(z - a) + a));
            }
        }

        for (int i = offset; i < offset + translation.getEngWord().length(); i++) {
            table[row][i] = Character.toString(translation.getEngWord().charAt(i - offset));
        }

        reverse = rand.nextInt(directions);
        ArrayList<String> tableList = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (reverse == 1)
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

    @Override
    public boolean onTVClickListner(int position, TextView tv, MotionEvent event) {return true;}

    public interface OnDataPass {
        void onDataPass(String data);
    }
}
