package com.justyna.englishsubtitled.menu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.justyna.englishsubtitled.Configuration;
import com.justyna.englishsubtitled.ConnectionUtils;
import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.model.Film;
import com.justyna.englishsubtitled.model.LessonSummary;
import com.justyna.englishsubtitled.model.Progress;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MenuFinishedLessonsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private final List<LessonSummary> finishedLessons = new LinkedList<>();
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_films_list);

        TextView title = findViewById(R.id.title);
        title.setText(R.string.finished_lessons);

        searchBar = findViewById(R.id.searchBar);
        searchBar.addTextChangedListener((TextWatcherSkeleton) this::search);

        recyclerView = findViewById(R.id.films);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        synchronized (finishedLessons) {
            finishedLessons.clear();
            finishedLessons.addAll((List<LessonSummary>) intent.getSerializableExtra("finishedLessons"));
        }
        refreshView();

        new ProgressRetriever().execute();
    }

    private void refreshView() {
        List<Film> films;

        synchronized (finishedLessons) {
            films = packLessonSummariesIntoFilms(finishedLessons);
        }

        // specify an adapter
        RecyclerView.Adapter adapter = new FilmsAdapter(films, MenuFinishedLessonsActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private List<Film> packLessonSummariesIntoFilms(List<LessonSummary> lessonSummaries) {
        Map<String, List<LessonSummary>> lessonsInFilms = new HashMap<>();

        for (LessonSummary lessonSummary : lessonSummaries) {
            String filmTitle = lessonSummary.filmTitle;
            if (lessonsInFilms.containsKey(filmTitle)) {
                lessonsInFilms.get(filmTitle).add(lessonSummary);
            } else {
                List<LessonSummary> list = new LinkedList<>();
                list.add(lessonSummary);
                lessonsInFilms.put(filmTitle, list);
            }
        }

        List<Film> films = new LinkedList<>();

        for (Map.Entry<String, List<LessonSummary>> entry : lessonsInFilms.entrySet()) {
            Film film = new Film();
            film.filmTitle = entry.getKey();
            film.lessons = entry.getValue();
            films.add(film);
        }

        return films;
    }

    private void search(Editable editable) {
        String searchTerm = editable.toString().toLowerCase();
        List<Film> films;
        synchronized (finishedLessons) {
            films = packLessonSummariesIntoFilms(finishedLessons);
        }

        for (Iterator<Film> iterator = films.iterator(); iterator.hasNext(); ) {
            Film film = iterator.next();
            if (!film.getFilmTitle().toLowerCase().contains(searchTerm)) {
                iterator.remove();
            }
        }
        RecyclerView.Adapter adapter = new FilmsAdapter(films, MenuFinishedLessonsActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private class ProgressRetriever extends AsyncTask<Void, Void, Progress> {
        @Override
        protected Progress doInBackground(Void... voids) {
            String baseUrl = Configuration.getInstance().getBackendUrl();
            HttpEntity<String> entity = ConnectionUtils.getInstance().prepareHttpEntity();

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Progress> progress;
            try {
                progress = restTemplate.exchange(baseUrl + "/progress/",
                        HttpMethod.GET, entity, new ParameterizedTypeReference<Progress>() {
                        });
            } catch (Exception e) {
                this.cancel(true);
                return null;
            }

            return progress.getBody();
        }

        @Override
        protected void onPostExecute(Progress progress) {
            super.onPostExecute(progress);
            synchronized (finishedLessons) {
                finishedLessons.clear();
                finishedLessons.addAll(progress.getFinished());
            }
            searchBar.setEnabled(false);
            refreshView();
            search(searchBar.getText());
            searchBar.setEnabled(true);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(MenuFinishedLessonsActivity.this, R.string.data_download_failure, Toast.LENGTH_LONG).show();
        }
    }
}
