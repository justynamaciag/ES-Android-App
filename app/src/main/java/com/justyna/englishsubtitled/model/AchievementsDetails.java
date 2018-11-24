package com.justyna.englishsubtitled.model;

public enum AchievementsDetails {
    FIRST_BLOOD("First Blood", "Ukończ swoją pierwszą lekcję"),
    AROUND_THE_WORLD_IN_80_DAYS("Around the World in 80 Days", "Konto utworzono przynajmniej 80 dni temu"),
    THE_TEMPLE_OF_DOOM("The Temple of Doom", "Zrób łącznie 84 błędy"),
    THE_ORIGINAL_SERIES("The Original Series", "Udziel poprawnej odpowiedzi 20 razy z rzędu w ciągu jednej lekcji"),
    A_BEAUTIFUL_MIND("A Beautiful Mind", "Ukończ 28 lekcji nie popełniając żadnego błędu"),
    AMERICAN_SNIPER("American Sniper", "Łącznie 255 razy udziel poprawnej odpowiedzi bez wcześniejszego popełniania błędu"),
    BOOK_OF_SECRETS("Book of Secrets", "Dodaj 13 słów do Słownika"),
    A_GAME_OF_SHADOWS("A Game of Shadows", "Rozwiąż 21 wykreślanek"),
    SLUMDOG_MILLIONAIRE("Slumdog Millionaire", "Odpowiedz na 100 pytań zamkniętych"),
    THE_ORDER_OF_THE_PHOENIX("The Order of the Phoenix", "Rozwiąż 12 układanek");

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
