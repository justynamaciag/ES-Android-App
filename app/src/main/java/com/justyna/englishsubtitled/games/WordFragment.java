package com.justyna.englishsubtitled.games;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
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
import com.justyna.englishsubtitled.model.Translation;
import com.justyna.englishsubtitled.utils.ButtonAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WordFragment extends Fragment implements ButtonAdapter.customButtonListener {

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

        view = inflater.inflate(R.layout.fragment_word, container, false);
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

        List<Button> btns = new ArrayList<>();
        for (String b : buttons) {
            Button btn = new Button(getContext());
            btn.setText(b);
            btns.add(btn);
            System.out.println(btn.getText());
        }

        GridView gridview = (GridView) view.findViewById(R.id.buttonsLayout);
        gridview.setNumColumns(5);
        ButtonAdapter a = new ButtonAdapter(getContext(), btns);
        a.setCustomButtonListner(WordFragment.this);
        gridview.setAdapter(a);

        for (String b : buttons) {
            TextView letterTV = new TextView(getContext());
            letters.add(letterTV);
            letterTV.setTextSize(20);
            letterTV.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            wordLayout.addView(letterTV);
        }

    }

    public void passData(String data) {
        dataPasser.onDataPass(data);
    }

    @Override
    public void onButtonClickListner(Button b) {

        String clicked = String.valueOf(b.getText());
        String correct = Character.toString(currentTranslation.getEngWord().charAt(checkedIndex));

        if (clicked.equals(correct)) {
            letters.get(checkedIndex).setText(clicked);
            if (checkedIndex + 1 == currentTranslation.getEngWord().length()) {
                Toast.makeText(view.getContext(), "Great!", Toast.LENGTH_SHORT).show();
                passData("1");
            }
            checkedIndex++;
            b.setEnabled(false);
        }
    }

    public interface OnDataPass {
        void onDataPass(String data);
    }

}
