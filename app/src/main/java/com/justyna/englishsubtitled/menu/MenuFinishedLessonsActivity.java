package com.justyna.englishsubtitled.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.model.Film;
import com.justyna.englishsubtitled.model.LessonSummary;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MenuFinishedLessonsActivity extends AppCompatActivity {
    private TextView title;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<LessonSummary> finishedLessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_lessons_in_films);

        title = findViewById(R.id.title);
        title.setText(R.string.finished_lessons);

        recyclerView = findViewById(R.id.films);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        finishedLessons = (List<LessonSummary>) intent.getSerializableExtra("finishedLessons");

        List<Film> films = packLessonSummariesIntoFilms(finishedLessons);

        // specify an adapter
        adapter = new FilmsAdapter(films, this);
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
}
