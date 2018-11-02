package com.justyna.englishsubtitled.model;
import java.util.List;

public class Lesson {

    private int lessonId;
    private String lessonTitle;
    private String filmTitle;
    private boolean finished;
    private int score;

    private List<Translation> translations;

    public int getLessonId(){
        return lessonId;
    }

    public List<Translation> getTranslations(){
        return translations;
    }
}
