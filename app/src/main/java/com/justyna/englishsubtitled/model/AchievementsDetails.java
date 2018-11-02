package com.justyna.englishsubtitled.model;

public enum AchievementsDetails {
    FIRST_STEPS("First Blood", "Ukończ swoją pierwszą lekcję");

    AchievementsDetails(String prettyName, String description){
        this.prettyName = prettyName;
        this.description = description;
    }

    String prettyName;
    String description;

    public String getPrettyName() {
        return prettyName;
    }

    public String getDescription() {
        return description;
    }
}
