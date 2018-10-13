package com.justyna.englishsubtitled.model;


import java.util.List;

public class Progress {
    public List<LessonSummary> rented;
    public List<LessonSummary> finished;

    public List<LessonSummary> getRented() {
        return rented;
    }

    public List<LessonSummary> getFinished() {
        return finished;
    }
}
