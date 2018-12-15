package com.justyna.englishsubtitled.games.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.games.utilities.GameResult;
import com.justyna.englishsubtitled.games.utilities.WordButtonsAdapter;
import com.justyna.englishsubtitled.model.Translation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class ABCDFragment extends Fragment implements WordButtonsAdapter.customButtonListener {

    OnDataPass dataPasser;
    List<Translation> translations;
    TextView wordTextView;
    Random rand;
    Translation currentTranslation;
    View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rand = new Random();

        view = inflater.inflate(R.layout.fragment_abcd, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            translations = (List<Translation>) bundle.getSerializable("translations");
            currentTranslation = (Translation) bundle.getSerializable("translation");
        }
        List<Button> buttonList = prepareGame();
        callGame(buttonList);

        return view;
    }

    private void callGame(List<Button> buttonList) {

        ListView listView = view.findViewById(R.id.abcdListView);
        WordButtonsAdapter adapter = new WordButtonsAdapter(getContext(), buttonList);
        adapter.setCustomButtonListner(ABCDFragment.this);
        listView.setAdapter(adapter);

        Drawable background = view.getBackground();
        listView.setDivider(background);
    }

    public List<Button> prepareGame() {

        String wordPL = currentTranslation.getPlWord();
        wordTextView = view.findViewById(R.id.wordTextView);
        wordTextView.setText(currentTranslation.getEngWord());

        HashSet<String> buttonLabels = new HashSet<>();
        buttonLabels.add(wordPL);
        while (buttonLabels.size() < 4)
            buttonLabels.add(getRandomTranslation().getPlWord());

        List<Button> buttonList = new ArrayList<>();
        for (String b : buttonLabels) {
            Button btn = new Button(getContext());
            btn.setText(b);
            buttonList.add(btn);
        }

        Collections.shuffle(buttonList, rand);

        return buttonList;

    }

    private Translation getRandomTranslation() {
        int random = rand.nextInt(translations.size());
        return translations.get(random);
    }

    @Override
    public void onButtonClickListner(Button b) {

        String clicked = String.valueOf(b.getText());
        if (clicked.equals(currentTranslation.getPlWord())) {

            b.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);

            passData(GameResult.SUCCESS);

        } else {
            passData(GameResult.FAIL);
            animateIncorrectChoice(b);
        }

    }

    private void animateIncorrectChoice(Button button) {
        button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

        new CountDownTimer(400, 1) {
            @Override
            public void onTick(long arg0) {
            }

            @Override
            public void onFinish() {
                button.getBackground().clearColorFilter();
            }
        }.start();
    }

    public void passData(GameResult data) {
        dataPasser.onDataPass(data);
    }

    public interface OnDataPass {
        void onDataPass(GameResult data);
    }

}
