package com.justyna.englishsubtitled.games;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.model.Translation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class ABCDFragment extends Fragment {

    OnDataPass dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }


    List<Translation> translations;
    HashSet<String> buttons;
    TextView wordTextView;
    Random rand = new Random();
    Translation word;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_abcd, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            translations = (List<Translation>) bundle.getSerializable("translations");
            word = (Translation) bundle.getSerializable("translation");
        }
        prepareGame();

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void prepareGame(){

        String wordPL = word.getPlWord();
        wordTextView = view.findViewById(R.id.wordTextView);
        wordTextView.setText(word.getEngWord());

        buttons = new HashSet<>();
        buttons.add(wordPL);

        while(buttons.size() < 4)
            buttons.add(getRandomTranslation().getPlWord());


        ArrayList<String> buttonList = new ArrayList<>();

        buttonList.addAll(buttons);
        Collections.shuffle(buttonList);

        ArrayAdapter<String> buttonsAdapter = new ArrayAdapter<>(getContext(), R.layout.abcd_buttons, R.id.abcdButtons, buttonList);
        ListView buttonsGrid = view.findViewById(R.id.abcdListView);
        buttonsGrid.setAdapter(buttonsAdapter);

        buttonsGrid.setOnTouchListener((v, event) -> {
            try{
                int indexChosen = buttonsGrid.pointToPosition((int)event.getX(), (int)event.getY());
                if(buttonList.get(indexChosen).equals(wordPL)) {
                    Toast.makeText(view.getContext(), "cool", Toast.LENGTH_SHORT).show();
                    passData("1");
                }

            }catch (IndexOutOfBoundsException e){
                System.out.println(e);
            }
            return true;
        });

    }


    private Translation getRandomTranslation() {
        int random = rand.nextInt(translations.size());
        return translations.get(random);
    }

    public void passData(String data) {
        dataPasser.onDataPass(data);
    }

    public interface OnDataPass {
        void onDataPass(String data);
    }

}
