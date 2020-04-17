package se.mbaeumer.covid19vis.services;

import java.time.LocalDateTime;

public class DailyCase {
    private LocalDateTime dateTime;
    private int number;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getNumber() {
        return number;
    }

    public DailyCase(LocalDateTime dateTime, int number) {
        this.dateTime = dateTime;
        this.number = number;
    }
}
