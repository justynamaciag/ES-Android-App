package com.justyna.englishsubtitled.model;

import java.io.Serializable;

public class LessonResult implements Serializable {

//    ilość błędów, które użytkownik popełnił
    private int mistakes = 0;

//    ile razy użytkownik poprawinie odpowiedział pod rząd
    private int correctAnswersInRow = 0;

//    ile razy uzytkownik odpowiedział poprawnie za pierwszym razem
    private int correctAnswersAsFirst=0;

//    ilośc dodań do słownika
    private int dictionaryAddings=0;

//    ilość wykonanych ćwiczeń z każdego rodzaju
    private int crosswordGames = 0;
    private int abcdGames = 0;
    private int wordGames = 0;

    public void incrementCorrectAnswerAsFirst(){
        this.correctAnswersAsFirst++;
    }

    public void incrementMistakes(){
        this.mistakes++;
    }

    public void incrementCrosswordGames(){
        this.crosswordGames++;
    }

    public void incrementWordGames(){
        this.wordGames++;
    }

    public void incrementABCDGames(){
        this.abcdGames++;
    }

    public void incrementDictionaryAdditions(){
        this.dictionaryAddings++;
    }

    public void decrementCrosswords(){
        this.crosswordGames--;
    }

    public void setCorrectAnswersInRow(int correctAnswersInRow){
        this.correctAnswersInRow = correctAnswersInRow;
    }

    public int getCorrectAnswersInRow(){
        return this.correctAnswersInRow;
    }

    public int getMistakes(){
        return this.mistakes;
    }

    public int getCrosswordGames(){
        return this.crosswordGames;
    }

    public int getAbcdGames(){
        return this.abcdGames;
    }

    public int getWordGames(){
        return this.wordGames;
    }

}
