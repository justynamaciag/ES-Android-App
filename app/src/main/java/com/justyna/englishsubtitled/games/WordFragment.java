package com.justyna.englishsubtitled.games;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.model.Translation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WordFragment extends Fragment {

    Translation currentTranslation;
    List<String> buttons;
    List<TextView> letters;
    Random rand;
    int checkedIndex;
    View view;


    WordFragment.OnDataPass dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (WordFragment.OnDataPass) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_word, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            currentTranslation = (Translation) bundle.getSerializable("translation");
        }

        rand = new Random();
        buttons = new ArrayList<>();
        letters = new ArrayList<>();
        checkedIndex = 0;

        setButtons(currentTranslation);

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setButtons(Translation translation) {

        buttons = new ArrayList<>();
        letters = new ArrayList<>();

        LinearLayout wordLayout = view.findViewById(R.id.wordLayout);

        for (int i = 0; i < translation.getEngWord().length(); i++) {
            buttons.add(Character.toString(translation.getEngWord().charAt(i)));
        }

        Collections.shuffle(buttons);

        ArrayAdapter<String> buttonsAdapter = new ArrayAdapter<>(getContext(), R.layout.buttons, R.id.buttons, buttons);
        GridView buttonsGrid = view.findViewById(R.id.buttonsGridView);
        buttonsGrid.setNumColumns(5);
        buttonsGrid.setAdapter(buttonsAdapter);

        buttonsGrid.setOnTouchListener((v, event) -> {
            try {
                int index = buttonsGrid.pointToPosition((int) event.getX(), (int) event.getY());
                System.out.println(buttons);

                String clicked = buttons.get(index);
                String correct = Character.toString(currentTranslation.getEngWord().charAt(checkedIndex));
                if (clicked.equals(correct)) {


                    letters.get(checkedIndex).setText(clicked);
                    if (checkedIndex + 1 == currentTranslation.getEngWord().length()) {
                        Toast.makeText(view.getContext(), "Great!", Toast.LENGTH_SHORT).show();
                        passData("1");
                    }
                    checkedIndex++;
                }
            }catch (IndexOutOfBoundsException e){
                System.out.println(e);
            }
            return true;
        });

        for (String b : buttons) {
            TextView letterTV = new TextView(getContext());
            letters.add(letterTV);
            letterTV.setTextSize(20);
            letterTV.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            wordLayout.addView(letterTV);
        }

    }
    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    public void passData(String data) {
        dataPasser.onDataPass(data);
    }

    public interface OnDataPass {
        void onDataPass(String data);
    }

}
