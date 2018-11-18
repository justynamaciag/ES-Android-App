package com.justyna.englishsubtitled.model;

import java.io.Serializable;

public class LessonResult implements Serializable {

    private int lessonId;
    //    number of mistakes in entire lesson
    private int mistakes = 0;

    //    max number of correct answers in a row
    private int correctAnswersInRow = 0;

    //    how many times correct answer occured as first
    private int correctAnswersAsFirst = 0;

    //   number of dictionary additions
    private int dictionaryAdditions = 0;

    //    number of games of any type
    private int crosswordGames = 0;
    private int abcdGames = 0;
    private int wordGames = 0;

    public void incrementCorrectAnswerAsFirst() {
        this.correctAnswersAsFirst++;
    }

    public void incrementMistakes() {
        this.mistakes++;
    }

    public void incrementCrosswordGames() {
        this.crosswordGames++;
    }

    public void incrementWordGames() {
        this.wordGames++;
    }

    public void incrementABCDGames() {
        this.abcdGames++;
    }

    public void incrementDictionaryAdditions() {
        this.dictionaryAdditions++;
    }

    public void decrementCrosswords() {
        this.crosswordGames--;
    }

    public void setCorrectAnswersInRow(int correctAnswersInRow) {
        this.correctAnswersInRow = correctAnswersInRow;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public int getCorrectAnswersInRow() {
        return this.correctAnswersInRow;
    }

    public int getMistakes() {
        return this.mistakes;
    }

    public int getCrosswordGames() {
        return this.crosswordGames;
    }

    public int getAbcdGames() {
        return this.abcdGames;
    }

    public int getWordGames() {
        return this.wordGames;
    }

    public int getLessonId() {
        return this.lessonId;
    }

}
