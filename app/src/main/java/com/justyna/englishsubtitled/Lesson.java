package com.justyna.englishsubtitled;

//TODO: Justyna, dostosuj sobie nazwę (refactor) i wrzuć np. tam, gdzie wrzucisz Translation

import java.util.List;

public class Lesson {
    Integer lessonId;
    public String lessonTitle;
    public String filmTitle;

    List<Translation> translations;

    public Integer getLessonId(){
        return lessonId;
    }

    public List<Translation> getTranslations(){
        return translations;
    }
}
