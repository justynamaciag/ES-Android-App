package com.justyna.englishsubtitled.games;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.model.Translation;
import com.justyna.englishsubtitled.utils.WordButtonsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class ABCDFragment extends Fragment implements WordButtonsAdapter.customButtonListener {

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

        view = inflater.inflate(R.layout.fragment_abcd, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            translations = (List<Translation>) bundle.getSerializable("translations");
            word = (Translation) bundle.getSerializable("translation");
        }
        prepareGame();

        return view;
    }

    public void prepareGame() {

        String wordPL = word.getPlWord();
        wordTextView = view.findViewById(R.id.wordTextView);
        wordTextView.setText(word.getEngWord());

        buttons = new HashSet<>();
        buttons.add(wordPL);

        while (buttons.size() < 4)
            buttons.add(getRandomTranslation().getPlWord());

        ArrayList<String> buttonList = new ArrayList<>();

        buttonList.addAll(buttons);
        Collections.shuffle(buttonList);


        List<Button> btns = new ArrayList<>();
        for (String b : buttonList) {
            Button btn = new Button(getContext());
            btn.setText(b);
            btns.add(btn);
            System.out.println(btn.getText());
        }

        ListView listView = (ListView) view.findViewById(R.id.abcdListView);
        WordButtonsAdapter a = new WordButtonsAdapter(getContext(), btns);
        a.setCustomButtonListner(ABCDFragment.this);
        listView.setAdapter(a);

        Drawable background = view.getBackground();
        listView.setDivider(background);

    }


    private Translation getRandomTranslation() {
        int random = rand.nextInt(translations.size());
        return translations.get(random);
    }

    public void passData(String data) {
        dataPasser.onDataPass(data);
    }

    @Override
    public void onButtonClickListner(Button b) {

        String clicked = String.valueOf(b.getText());

        if (clicked.equals(word.getPlWord())) {
            Toast.makeText(view.getContext(), "Great!", Toast.LENGTH_SHORT).show();
            passData("1");
            }
    }

    public interface OnDataPass {
        void onDataPass(String data);
    }

}
