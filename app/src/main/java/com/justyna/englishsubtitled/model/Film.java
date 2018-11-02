package com.justyna.englishsubtitled.model;

import java.util.List;

public class Film {
    public String filmTitle;
    public List<LessonSummary> lessons;

    public String getFilmTitle() {
        return filmTitle;
    }

    public List<LessonSummary> getLessons() {
        return lessons;
    }
}
