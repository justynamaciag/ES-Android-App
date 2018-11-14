package com.justyna.englishsubtitled.model;

import java.io.Serializable;

public class LessonResult implements Serializable {

    private int lessonId;
    private String lessonName;

//    ilość błędów, które użytkownik popełnił
    private int failures=0;

//    ile razy użytkownik poprawinie odpowiedział pod rząd
    private int correctInRow=0;

//    ile razy uzytkownik odpowiedział poprawnie za pierwszym razem
    private int correctAnswersAsFirst=0;

//    ilośc dodań do słownika
    private int dictionaryAddings=0;

//    ilość wykonanych ćwiczeń z każdego rodzaju
    private int crosswordGameNum=0;
    private int abcdGameNum=0;
    private int wordGameNum=0;

    public void incrementCorrectAnswerAsFirst(){
        this.correctAnswersAsFirst++;
    }

    public void incrementFailure(){
        this.failures++;
    }

    public void incrementCrosswordGameNum(){
        this.crosswordGameNum++;
    }

    public void incrementWordGameNum(){
        this.wordGameNum++;
    }

    public void incrementABCDGameNum(){
        this.abcdGameNum++;
    }

    public void incrementDictionaryAddings(){
        this.dictionaryAddings++;
    }

    public void decrementCrosswordNum(){
        this.crosswordGameNum--;
    }

    public void setCorrectInRow(int correctInRow){
        this.correctInRow = correctInRow;
    }

    public int getCorrectInRow(){
        return this.correctInRow;
    }

    public int getFailures(){
        return this.failures;
    }

    public int getCorrectAnswersAsFirst(){
        return this.correctAnswersAsFirst;
    }

    public int getDictionaryAddings(){
        return this.dictionaryAddings;
    }

    public int getCrosswordGameNum(){
        return this.crosswordGameNum;
    }

    public int getAbcdGameNum(){
        return this.abcdGameNum;
    }

    public int getWordGameNum(){
        return this.wordGameNum;
    }

    public int getLessonId(){
        return this.lessonId;
    }
}
