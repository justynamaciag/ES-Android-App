package com.justyna.englishsubtitled.games.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.games.utilities.GameResult;
import com.justyna.englishsubtitled.games.utilities.WordButtonsAdapter;
import com.justyna.englishsubtitled.model.Translation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordFragment extends Fragment implements WordButtonsAdapter.customButtonListener {

    WordFragment.OnDataPass dataPasser;
    Translation currentTranslation;
    List<TextView> letters;
    TextView plWordTextView;
    View view;
    boolean finishGameSuccess = true;
    int colNum = 5, checkedIndex = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (WordFragment.OnDataPass) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_word, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            currentTranslation = (Translation) bundle.getSerializable("translation");
        }

        plWordTextView = view.findViewById(R.id.plWordTv);

        List<Button> buttonList = prepareButtons(currentTranslation);
        callGame(buttonList);

        return view;
    }

    private List<Button> prepareButtons(Translation translation) {

        List<Button> buttonList = new ArrayList<>();
        for (int i = 0; i < translation.getEngWord().length(); i++) {
            Button btn = new Button(getContext());
            btn.setText(Character.toString(translation.getEngWord().charAt(i)));
            buttonList.add(btn);
        }
        Collections.shuffle(buttonList);

        return buttonList;
    }

    private void callGame(List<Button> buttonList) {

        LinearLayout wordLayout = view.findViewById(R.id.wordLayout);
        plWordTextView.setText(currentTranslation.getPlWord());

        GridView gridview = view.findViewById(R.id.buttonsLayout);
        gridview.setNumColumns(colNum);
        WordButtonsAdapter a = new WordButtonsAdapter(getContext(), buttonList);
        a.setCustomButtonListner(WordFragment.this);
        gridview.setAdapter(a);

        int buttonListSize = buttonList.size();
        letters = new ArrayList<>();
        for (int i = 0; i < buttonListSize; i++) {
            TextView letterTV = new TextView(getContext());
            letters.add(letterTV);
            letterTV.setTextSize(24);
            letterTV.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            wordLayout.addView(letterTV);
        }
    }

    @Override
    public void onButtonClickListner(Button b) {

        String clicked = String.valueOf(b.getText());
        String correct = Character.toString(currentTranslation.getEngWord().charAt(checkedIndex));

        if (clicked.equals(correct)) {
            letters.get(checkedIndex).setText(clicked);

            callButtonColorAnimation(Color.GREEN, b, 400);

            if (checkedIndex + 1 == currentTranslation.getEngWord().length()) {
                Handler handler = new Handler();
                handler.postDelayed(() -> passData(GameResult.SUCCESS), Toast.LENGTH_SHORT);

            }
            checkedIndex++;
            b.setEnabled(false);
        } else {
            passData(GameResult.FAIL);
            callButtonColorAnimation(Color.RED, b, 500);
        }

    }


    private void callButtonColorAnimation(int color, Button button, int duration) {

        button.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        new CountDownTimer(duration, 1) {
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
