package com.justyna.englishsubtitled.model;

public class LessonSummary {
    Integer lessonId;
    public String lessonTitle;
    public String filmTitle;

    public Integer getLessonId(){ // For Jackson
        return lessonId;
    }
}
