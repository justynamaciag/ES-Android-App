package com.justyna.englishsubtitled.model;

public class Achievement {
    String achievement;
    long timestamp;

    public Achievement(String achievement, long timestamp){
        this.achievement = achievement;
        this.timestamp = timestamp;
    }

    public String getAchievement() {
        return achievement;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
