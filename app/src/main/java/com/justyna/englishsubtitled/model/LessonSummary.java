package com.justyna.englishsubtitled.model;

import java.io.Serializable;

public class LessonSummary implements Serializable {
    Integer lessonId;
    public String lessonTitle;
    public String filmTitle;

    public Integer getLessonId() { // For Jackson
        return lessonId;
    }
}
