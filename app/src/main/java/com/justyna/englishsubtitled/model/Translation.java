package com.justyna.englishsubtitled.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import static java.lang.System.in;


public class Translation implements Serializable, Parcelable {

    private String engWord;
    private String plWord;

    public Translation(String engWord, String plWord){
        this.engWord = engWord;
        this.plWord = plWord;
    }

    protected Translation(Parcel in) {
        engWord = in.readString();
        plWord = in.readString();
    }


    public String getEngWord() {
        return engWord;
    }

    public String getPlWord(){
        return plWord;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(engWord);
        dest.writeString(plWord);
    }

    public static final Creator<Translation> CREATOR = new Creator<Translation>() {
        @Override
        public Translation createFromParcel(Parcel in) {
            return new Translation(in);
        }

        @Override
        public Translation[] newArray(int size) {
            return new Translation[size];
        }
    };
}
