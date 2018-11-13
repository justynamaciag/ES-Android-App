package com.justyna.englishsubtitled.model;

import java.io.Serializable;

public class LessonResult implements Serializable {

    private int lessonId;
    private String lessonName;
    private int failures=0;
    private int correctInRow=0;
    private int correctAnswersAsFirst=0;
    private int dictionaryAddings=0;

    private int crosswordGameNum=0;
    private int abcdGameNum=0;
    private int wordGameNum=0;

    public void addCorrectAnswerAsFirst(){
        this.correctAnswersAsFirst++;
    }

    public void addFailure(){
        this.failures++;
    }

    public void addCrosswordGameNum(){
        this.crosswordGameNum++;
    }

    public void addWordGameNum(){
        this.wordGameNum++;
    }

    public void addABCDGameNum(){
        this.abcdGameNum++;
    }

    public void decrementCrosswordNum(){
        this.crosswordGameNum--;
    }

    public void addDictionaryAddings(){
        this.dictionaryAddings++;
    }


    public void setCorrectInRow(int correctInRow){
        this.correctInRow = correctInRow;
    }

    public int getCorrectInRow(){
        return this.correctInRow;
    }
}
