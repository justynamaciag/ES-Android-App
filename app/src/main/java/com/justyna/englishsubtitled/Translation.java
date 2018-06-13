package com.justyna.englishsubtitled;

class Translation {

    private String engWord;
    private String plWord;

    Translation(String engWord, String plWord){
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
