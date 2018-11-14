package com.justyna.englishsubtitled.games.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.model.LessonResult;

public class FinishLessonFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_finish_lesson, container, false);

        Bundle bundle = getArguments();

        if(bundle != null) {
            LessonResult lessonResult = (LessonResult) bundle.getSerializable("lessonResult");

            fillTextViews(lessonResult);
//            LessonResultSender.sendStatistics(lessonResult);

        }

        return view;
    }

    private void fillTextViews(LessonResult lessonResult){
        Resources resources = getResources();

        TextView textView = view.findViewById(R.id.crossword_num_textview);
        textView.setText(resources.getString(R.string.crossword_numbers, lessonResult.getCrosswordGameNum()));

        textView = view.findViewById(R.id.word_num_textview);
        textView.setText(resources.getString(R.string.word_numbers, lessonResult.getWordGameNum()));

        textView = view.findViewById(R.id.abcd_num_textview);
        textView.setText(resources.getString(R.string.abcd_numbers, lessonResult.getAbcdGameNum()));

        textView = view.findViewById(R.id.failures_textview);
        textView.setText((resources.getString(R.string.failure_numbers, lessonResult.getFailures())));
    }

}
