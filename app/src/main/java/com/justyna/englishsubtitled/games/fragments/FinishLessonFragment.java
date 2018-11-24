package com.justyna.englishsubtitled.games.fragments;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.justyna.englishsubtitled.Configuration;
import com.justyna.englishsubtitled.ConnectionUtils;
import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.model.Achievement;
import com.justyna.englishsubtitled.model.LessonResult;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;

public class FinishLessonFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_finish_lesson, container, false);

        Bundle bundle = getArguments();

        if (bundle != null) {
            LessonResult lessonResult = (LessonResult) bundle.getSerializable("lessonResult");

            fillTextViews(lessonResult);
            new SendResult().execute(lessonResult);
        }

        return view;
    }

    private void fillTextViews(LessonResult lessonResult) {
        Resources resources = getResources();

        TextView textView = view.findViewById(R.id.crossword_num_textview);
        textView.setText(resources.getString(R.string.crossword_numbers, lessonResult.getCrosswordGames()));

        textView = view.findViewById(R.id.word_num_textview);
        textView.setText(resources.getString(R.string.word_numbers, lessonResult.getWordGames()));

        textView = view.findViewById(R.id.abcd_num_textview);
        textView.setText(resources.getString(R.string.abcd_numbers, lessonResult.getAbcdGames()));

        textView = view.findViewById(R.id.failures_textview);
        textView.setText((resources.getString(R.string.failure_numbers, lessonResult.getMistakes())));
    }

    private class SendResult extends AsyncTask<LessonResult, Void, List<Achievement>> {
        @Override
        protected List<Achievement> doInBackground(LessonResult... lessonResults) {
            LessonResult lessonResult = lessonResults[0];

            String baseUrl = Configuration.getInstance().getBackendUrl();

            Gson gson = new Gson();
            String json = gson.toJson(lessonResult);

            HttpEntity<String> entity = ConnectionUtils.getInstance().prepareHttpEntity(json);


            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

            ResponseEntity<List<Achievement>> result =
                    restTemplate.exchange(baseUrl + "/progress/" + lessonResult.getLessonId(), HttpMethod.PUT, entity, new ParameterizedTypeReference<List<Achievement>>() {
                    });

            return result.getBody();
        }

        @Override
        protected void onPostExecute(List<Achievement> achievements) {
            super.onPostExecute(achievements);

            if (achievements.size() == 1) {
                Toast.makeText(getContext(), R.string.received_achievement, Toast.LENGTH_LONG).show();
            } else if (achievements.size() > 1) {
                Toast.makeText(getContext(), R.string.received_achievements, Toast.LENGTH_LONG).show();
            }
        }
    }

}
