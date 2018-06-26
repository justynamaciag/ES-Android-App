package com.justyna.englishsubtitled.stuctures;

import java.io.Serializable;

public class Translation implements Serializable {

    private String engWord;
    private String plWord;

    public Translation(String engWord, String plWord){
        this.engWord = engWord;
        this.plWord = plWord;
    }

    public String getEngWord() {
        return engWord;
    }

    public String getPlWord(){
        return plWord;
    }
}
